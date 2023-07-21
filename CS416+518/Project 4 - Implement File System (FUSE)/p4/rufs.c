/*
 *  Copyright (C) 2023 CS416 Rutgers CS
 *	Tiny File System
 *	File:	rufs.c
 *
 */
 
// List all group member's name: Ryan Lee
// username of iLab: rsl82
// iLab Server: cp.cs.rutgers.edu

#define FUSE_USE_VERSION 26
#define INODES_PER_BLOCK ((int)(BLOCK_SIZE /sizeof(struct inode)))
#define DIRENTS_PER_BLOCK ((int)(BLOCK_SIZE /sizeof(struct dirent)))
#define min(a, b) ((a) < (b) ? (a) : (b))


#include <fuse.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <errno.h>
#include <sys/time.h>
#include <libgen.h>
#include <limits.h>
#include <string.h>

#include "block.h"
#include "rufs.h"

char diskfile_path[PATH_MAX];
struct superblock *super;
bitmap_t inode_bitmap;
bitmap_t data_bitmap;

// Declare your in-memory data structures here
int inodePerBlock;
int direntPerBlock;

/* 
 * Get available inode number from bitmap
 */
 
int calculate_numberOfBlocks() {
    int num = 3;
    int temp = 0;
    for(int i=0;i<MAX_INUM;i++) {
        if(get_bitmap(inode_bitmap,i) == 1)
        	temp++;
    }
    num = num + (int)temp/16;
    
    for(int i =0;i<MAX_DNUM;i++) {
    	if(get_bitmap(data_bitmap,i) == 1)
        	num++;
    }
	
    return num;
}
 
int get_avail_ino() {

	// Step 1: Read inode bitmap from disk
    void* buffer = (void*) malloc(BLOCK_SIZE);
    bio_read(1,buffer);
    memcpy(inode_bitmap,buffer,(MAX_INUM / 8 + 1));
    free(buffer);
	// Step 2: Traverse inode bitmap to find an available slot
    for(int i=0;i<MAX_INUM;i++) {
        if(get_bitmap(inode_bitmap,i) == 0) {
            // Step 3: Update inode bitmap and write to disk
            set_bitmap(inode_bitmap,i);
            bio_write(1,inode_bitmap);
            return i;
        }
    }

	return -1;
}

/* 
 * Get available data block number from bitmap
 */
int get_avail_blkno() {

	// Step 1: Read data block bitmap from disk
    void* buffer = (void*) malloc(BLOCK_SIZE);
    bio_read(2,buffer);
    memcpy(data_bitmap,buffer,(MAX_DNUM / 8 + 1));
    free(buffer);
    // Step 2: Traverse data block bitmap to find an available slot
    for(int i=0;i<MAX_DNUM;i++) {
        if(get_bitmap(data_bitmap,i) == 0) {
            // Step 3: Update data block bitmap and write to disk
            set_bitmap(data_bitmap,i);
            bio_write(2,data_bitmap);
            return i;
        }
    }

    return -1;
}

/* 
 * inode operations
 */
int readi(uint16_t ino, struct inode *inode) {

  // Step 1: Get the inode's on-disk block number
  uint16_t num_block = super->i_start_blk + ((uint16_t)(ino/INODES_PER_BLOCK));
  // Step 2: Get offset of the inode in the inode on-disk block
  unsigned int offset = (ino%INODES_PER_BLOCK) * sizeof(struct inode);

  // Step 3: Read the block from disk and then copy into inode structure
    void* buffer = (void*) malloc(BLOCK_SIZE);
    bio_read(num_block,buffer);

    memcpy(inode,buffer + offset,sizeof(struct inode));
    free(buffer);
    return 0;
}

int writei(uint16_t ino, struct inode *inode) {

	// Step 1: Get the block number where this inode resides on disk
    uint16_t num_block = super->i_start_blk + ((uint16_t)(ino/INODES_PER_BLOCK));

    // Step 2: Get the offset in the block where this inode resides on disk
    unsigned int offset = (ino%INODES_PER_BLOCK) * sizeof(struct inode);
	// Step 3: Write inode to disk 
    void* buffer = (void*) malloc(BLOCK_SIZE);
    bio_read(num_block,buffer);
    memcpy(buffer+offset,inode,sizeof(struct inode));
    bio_write(num_block,buffer);
    free(buffer);
	return 0;
}

int namesame(const char* fname,char name[208],size_t name_len) {

    for(int i=0;i<name_len;i++) {
        if(fname[i] != name[i]) {
            return -1;
        }
    }

    return 0;
}


/* 
 * directory operations
 */
int dir_find(uint16_t ino, const char *fname, size_t name_len, struct dirent *dirent) {

    //printf("%s %ld \n",fname,name_len);
  // Step 1: Call readi() to get the inode using ino (inode number of current directory)
  struct inode* current_inode =(struct inode*) malloc (sizeof(struct inode));
  readi(ino,current_inode);
  //printf("current inode in dir_find: %d\n",current_inode->ino);
  void* buffer = (void*)malloc(BLOCK_SIZE);
  int links = ((int)(current_inode->link/DIRENTS_PER_BLOCK));
  // Step 2: Get data block of current directory from inode
  for(int i=0;i<16;i++) {
      if(current_inode->direct_ptr[i]==0) {
          break;
      }
      else {
          bio_read(current_inode->direct_ptr[i],buffer);
          if(links != 0) {
              for(int j=0;j<DIRENTS_PER_BLOCK;j++) {
                  void* temp = (void*) buffer + j*(sizeof(struct dirent));
                  struct dirent* dirent_ptr = (struct dirent*) temp;
                  //printf("%s\n",dirent_ptr->name);
                  if(dirent_ptr->len != name_len) {
                      continue;
                  }
                  if(namesame(fname,dirent_ptr->name,name_len) == 0) {
                      memcpy(dirent,dirent_ptr,sizeof(struct dirent));
                      free(current_inode);
                      free(buffer);
                      return 0;
                  }
              }
          }
          else {
              for(int j=0;j<current_inode->link%DIRENTS_PER_BLOCK;j++) {
                  void* temp = (void*) buffer + j*(sizeof(struct dirent));
                  struct dirent* dirent_ptr = (struct dirent*) temp;
                  //printf("%s\n",dirent_ptr->name);
                  if(dirent_ptr->len != name_len) {
                      continue;
                  }
                  if(namesame(fname,dirent_ptr->name,name_len) == 0) {
                      memcpy(dirent,dirent_ptr,sizeof(struct dirent));
                      free(current_inode);
                      free(buffer);
                      return 0;
                  }
              }
          }
      }

      links-=1;
  }
  free(current_inode);
  free(buffer);
  // Step 3: Read directory's data block and check each directory entry.
  //If the name matches, then copy directory entry to dirent structure
	return -ENOENT;
}

int dir_add(struct inode dir_inode, uint16_t f_ino, const char *fname, size_t name_len) {

	// Step 1: Read dir_inode's data block and check each directory entry of dir_inode
	struct dirent* newdirent = (struct dirent*) malloc (sizeof(struct dirent));
	// Step 2: Check if fname (directory name) is already used in other entries
    if(dir_find(dir_inode.ino,fname,name_len,newdirent) == 0) {
        //printf("Directory already exists.\n");
        free(newdirent);
        return -EEXIST;
    }

	// Step 3: Add directory entry in dir_inode's data block and write to disk
    newdirent->ino = f_ino;
    strcpy(newdirent->name,fname);
    newdirent->valid = 1;
    newdirent->len=strlen(fname);
	// Allocate a new data block for this directory if it does not exist
    if(dir_inode.link%DIRENTS_PER_BLOCK==0) {
        int newblock = super->d_start_blk + get_avail_blkno();
        for(int i=0;i<16;i++) {
            if(dir_inode.direct_ptr[i] == 0) {
                dir_inode.direct_ptr[i] = newblock;
                bio_write(newblock,newdirent);
                break;
            }

        }
    }
    else {
        uint16_t num_block = ((uint16_t)(dir_inode.link/DIRENTS_PER_BLOCK));
        unsigned int offset = (dir_inode.link%DIRENTS_PER_BLOCK) * sizeof(struct dirent);
        unsigned int num = dir_inode.direct_ptr[num_block];
        void* buffer = (void*) malloc (BLOCK_SIZE);
        bio_read(num,buffer);
        memcpy(buffer+offset,newdirent,sizeof(struct dirent));
        bio_write(num,buffer);
        free(buffer);
    }

	// Update directory inode
    dir_inode.link += 1;
    time(&(dir_inode.vstat.st_mtime));
    writei(dir_inode.ino,&dir_inode);
	// Write directory entry
    free(newdirent);
	return 0;
}

//CAN SKIP
int dir_remove(struct inode dir_inode, const char *fname, size_t name_len) {

	// Step 1: Read dir_inode's data block and checks each directory entry of dir_inode
	
	// Step 2: Check if fname exist

	// Step 3: If exist, then remove it from dir_inode's data block and write to disk

	return 0;
}

/* 
 * namei operation
 */
int get_node_by_path(const char *path, uint16_t ino, struct inode *inode) {
	
	// Step 1: Resolve the path name, walk through path, and finally, find its inode.
	// Note: You could either implement it in a iterative way or recursive way
    // I think there is no need to use ino argument for iterative way.
    
    //printf("Get node by path occured with: %s\n",path);

    const char* token = strtok((char*)path,"/");
    //printf("First token: %s\n",token);
    if(token==NULL) {
        readi(0,inode);
        return 0;
    }
    struct dirent* current_dirent = (struct dirent*) malloc (sizeof(struct dirent));
    while(token != NULL) {
    	//printf("current token: %s\n",token);
        if(dir_find(current_dirent->ino,token,strlen(token),current_dirent) == -ENOENT) {
            free(current_dirent);
            //printf("Cannot find path in get_node_by_path\n");
            return -ENOENT;
        }

        token = strtok(NULL,"/");
    }

    readi(current_dirent->ino,inode);
    free(current_dirent);

	return 0;
}

/* 
 * Make file system
 */
int rufs_mkfs() {

	// Call dev_init() to initialize (Create) Diskfile
    dev_init(diskfile_path);
    dev_open(diskfile_path);
	// write superblock information
    super = (struct superblock*) malloc(sizeof(struct superblock));
    super->magic_num = MAGIC_NUM;
    super->max_inum = MAX_INUM;
    super->max_dnum = MAX_DNUM;
    super->i_bitmap_blk = 1;
    super->d_bitmap_blk = 2;
    super->i_start_blk = 3;
    //inodePerBlock = ((int) (BLOCK_SIZE / sizeof(struct inode)));
    //direntPerBlock = ((int) (BLOCK_SIZE / sizeof(struct dirent)));
    super->d_start_blk = 4 + MAX_INUM/INODES_PER_BLOCK;
    bio_write(0,super);
	// initialize inode bitmap
    inode_bitmap = (bitmap_t)malloc(MAX_INUM / 8 + 1);
    for(int i=0; i<(MAX_INUM/8+1);i++) {
        inode_bitmap[i] = 0;
    }
    set_bitmap(inode_bitmap,0);
	// initialize data block bitmap
    data_bitmap = (bitmap_t)malloc(MAX_DNUM / 8 + 1);
    for(int i=0; i<(MAX_DNUM/8+1);i++) {
        data_bitmap[i] = 0;
    }
    set_bitmap(data_bitmap,0);
	// update bitmap information for root directory
    bio_write(1,inode_bitmap);
    bio_write(2,data_bitmap);
	// update inode for root directory
    struct inode root_inode;
    root_inode.ino = 0;
    root_inode.valid = 1;
    root_inode.size = 0;
    root_inode.type = S_IFDIR | 0755;
    root_inode.link = 2;
    for(int i=0;i<16;i++) {
        root_inode.direct_ptr[i] = 0;
    }
    for(int i=0;i<8;i++) {
        root_inode.indirect_ptr[i] = 0;
    }
    root_inode.direct_ptr[0] = super->d_start_blk;
    time(&(root_inode.vstat.st_mtime));
    bio_write(super->i_start_blk,&root_inode);

    struct dirent arr[2];
    arr[0].ino = 0;
    arr[0].valid = 1;
    strcpy(arr[0].name,".");
    arr[0].len = 1;

    arr[1].ino = 0;
    arr[1].valid = 1;
    strcpy(arr[1].name,"..");
    arr[1].len = 2;
    bio_write(super->d_start_blk,arr);

	return 0;
}


/* 
 * FUSE file operations
 */
static void *rufs_init(struct fuse_conn_info *conn) {

	// Step 1a: If disk file is not found, call mkfs

    if(dev_open(diskfile_path) == -1) {
        rufs_mkfs();
    }
    else {
        super = (struct superblock*) malloc(sizeof(struct superblock));
        inode_bitmap = (bitmap_t)malloc(MAX_INUM / 8 + 1);
        data_bitmap = (bitmap_t)malloc(MAX_DNUM / 8 + 1);
        void* buffer = (void*) malloc(BLOCK_SIZE);
        bio_read(0,buffer);
        memcpy(super,buffer,sizeof(struct superblock));
        bio_read(1,buffer);
        memcpy(inode_bitmap,buffer,(MAX_INUM / 8 + 1));
        bio_read(2,buffer);
        memcpy(data_bitmap,buffer,(MAX_DNUM / 8 + 1));
        free(buffer);
    }

  // Step 1b: If disk file is found, just initialize in-memory data structures
  // and read superblock from disk

	return NULL;
}

static void rufs_destroy(void *userdata) {

	// Step 1: De-allocate in-memory data structures
    free(super);
    free(inode_bitmap);
    free(data_bitmap);

	// Step 2: Close diskfile
    dev_close();
}

static int rufs_getattr(const char *path, struct stat *stbuf) {

	// Step 1: call get_node_by_path() to get inode from path
    //printf("getattr path:%s\n",path);
    struct inode * path_inode = (struct inode*) malloc(sizeof(struct inode));
    if(get_node_by_path(path,0,path_inode) == -ENOENT) {
        //printf("Get attr error.\n");
        free(path_inode);
        return -ENOENT;
    }

	// Step 2: fill attribute of file into stbuf from inode
    stbuf->st_ino = path_inode->ino;
    stbuf->st_mode = path_inode->type;
    stbuf->st_nlink = path_inode->link;
    stbuf->st_size = path_inode->size;
    stbuf->st_blksize = BLOCK_SIZE;
    stbuf->st_atime = path_inode->vstat.st_atime;
    stbuf->st_mtime = path_inode->vstat.st_mtime;

    free(path_inode);
	return 0;
}

static int rufs_opendir(const char *path, struct fuse_file_info *fi) {

	// Step 1: Call get_node_by_path() to get inode from path
    struct inode * path_inode = (struct inode*) malloc(sizeof(struct inode));
    if(get_node_by_path(path,0,path_inode) == -ENOENT) {
        //printf("opendir error.\n");
        return -1;
    }
    if(!S_ISDIR(path_inode->type)) {
        //printf("not a directory\n");
        return -1;
    }

    time(&path_inode->vstat.st_atime);
    writei(path_inode->ino,path_inode);

    free(path_inode);
    printf("Blocks used: %d\n",calculate_numberOfBlocks());
	// Step 2: If not find, return -1
    return 0;
}

static int rufs_readdir(const char *path, void *buffer, fuse_fill_dir_t filler, off_t offset, struct fuse_file_info *fi) {

	// Step 1: Call get_node_by_path() to get inode from path
    //printf("READ DIR PATH: %s\n",path);
    struct inode* path_inode = (struct inode *) malloc (sizeof(struct inode));
    if(get_node_by_path(path,0,path_inode) == -ENOENT) {
        //printf("rufs_readdir error.\n");
        free(path_inode);
        return -ENOENT;
    }
	// Step 2: Read directory entries from its data blocks, and copy them to filler
    //struct dirent* path_dirent = (struct dirent *) malloc (sizeof(struct dirent));


    void* temp_buffer = (void*)malloc(BLOCK_SIZE);
    int links = ((int)(path_inode->link/DIRENTS_PER_BLOCK));
    // Step 2: Get data block of current directory from inode
   struct dirent* dirent_ptr = (struct dirent*) malloc (sizeof(struct dirent));  
    for(int i=0;i<16;i++) {
        if(path_inode->direct_ptr[i]==0) {
            break;
        }
        else {
            bio_read(path_inode->direct_ptr[i],temp_buffer);
            if(links != 0) {
                for(int j=0;j<DIRENTS_PER_BLOCK;j++) {
                    void* temp = temp_buffer + j*(sizeof(struct dirent));
                    memcpy(dirent_ptr,temp,sizeof(struct dirent));
                    //printf("%s ",dirent_ptr->name);
                    
                    
                    
                    filler(buffer,(const char*)dirent_ptr->name,NULL,0);
                    

                }
            }
            else {
                for(int j=0;j<path_inode->link%DIRENTS_PER_BLOCK;j++) {
                    void* temp = temp_buffer + j*(sizeof(struct dirent));
                    memcpy(dirent_ptr,temp,sizeof(struct dirent));
                    //printf("%s ",dirent_ptr->name);
                    filler(buffer,(const char*)dirent_ptr->name,NULL,0);

                }
            }
        }
	
        links-=1;
    }
    //printf("\n");
    
    free(dirent_ptr);
    free(temp_buffer);
    free(path_inode);
	return 0;
}


static int rufs_mkdir(const char *path, mode_t mode) {

	// Step 1: Use dirname() and basename() to separate parent directory path and target directory name
    char* temp1 = (char *) malloc (strlen(path)+1);
    char* temp2 = (char *) malloc (strlen(path)+1);
    strcpy(temp1,path);
    strcpy(temp2,path);
    char* dir_name = (char *) malloc (strlen(path) +1);
    char* base_name = (char *) malloc (strlen(path) +1);
    strcpy(dir_name,dirname(temp1));
    strcpy(base_name,basename(temp2));
    free(temp1);
    free(temp2);
    
    //printf("parent directordy: %s , direc: %s\n",dir_name,base_name);
	// Step 2: Call get_node_by_path() to get inode of parent directory
    struct inode* dir_inode = (struct inode *) malloc (sizeof(struct inode));
    if(get_node_by_path(dir_name,0,dir_inode) == -ENOENT ) {
        //printf("rufs_mkdir path not found\n");
        free(dir_name);
        free(base_name);
        free(dir_inode);
        return -ENOENT;
    }
	// Step 3: Call get_avail_ino() to get an available inode number
    int newino = get_avail_ino();
	// Step 4: Call dir_add() to add directory entry of target directory to parent directory
    dir_add(*dir_inode,newino,base_name,strlen(base_name));
	// Step 5: Update inode for target directory
    struct inode* new_inode = (struct inode *) malloc (sizeof(struct inode));
    new_inode->ino = newino;
    new_inode->valid = 1;
    new_inode->size = 0;
    new_inode->type = S_IFDIR | mode;
    new_inode->link = 2;
    for(int i=0;i<16;i++) {
        new_inode->direct_ptr[i] = 0;
    }
    for(int i=0;i<8;i++) {
        new_inode->indirect_ptr[i] = 0;
    }

    int newdirent = super->d_start_blk + get_avail_blkno();
    new_inode->direct_ptr[0] = newdirent;
    time(&(new_inode->vstat.st_mtime));

    struct dirent arr[2];
    arr[0].ino = dir_inode->ino;
    arr[0].valid = 1;
    strcpy(arr[0].name,".");
    arr[0].len = 1;

    arr[1].ino = newino;
    arr[1].valid = 1;
    strcpy(arr[1].name,"..");
    arr[1].len = 2;
    bio_write(newdirent,arr);
    // Step 6: Call writei() to write inode to disk
    writei(newino,new_inode);

    free(new_inode);
    free(dir_name);
    free(base_name);
    free(dir_inode);
	return 0;
}


//CAN SKIP
static int rufs_rmdir(const char *path) {

	// Step 1: Use dirname() and basename() to separate parent directory path and target directory name

	// Step 2: Call get_node_by_path() to get inode of target directory


	// Step 3: Clear data block bitmap of target directory

	// Step 4: Clear inode bitmap and its data block

	// Step 5: Call get_node_by_path() to get inode of parent directory

	// Step 6: Call dir_remove() to remove directory entry of target directory in its parent directory


	return 0;
}

static int rufs_releasedir(const char *path, struct fuse_file_info *fi) {
	// For this project, you don't need to fill this function
	// But DO NOT DELETE IT!
    return 0;
}


static int rufs_create(const char *path, mode_t mode, struct fuse_file_info *fi) {

	// Step 1: Use dirname() and basename() to separate parent directory path and target file name
	
	char* temp1 =(char *) malloc (strlen(path)+1);
	strcpy(temp1,path);
	char* temp2 = (char *) malloc (strlen(path)+1);
	strcpy(temp2,path);
    char* dir_name = (char *) malloc (strlen(path)+1);
    char* base_name = (char *) malloc (strlen(path)+1);

    strcpy(dir_name,dirname(temp1));
    strcpy(base_name,basename(temp2));
    free(temp1);
    free(temp2);
    //printf("current path: %s, dirname: %s, base_name: %s\n",path,dir_name,base_name);
	// Step 2: Call get_node_by_path() to get inode of parent directory
    struct inode* dir_inode = (struct inode *) malloc (sizeof(struct inode));
    if(get_node_by_path(dir_name,0,dir_inode) == -ENOENT ) {
        //printf("rufs_create path not found\n");
        free(dir_name);
        free(base_name);
        free(dir_inode);
        return -ENOENT;
    }
    
    
    
    struct dirent* checker = (struct dirent *) malloc (sizeof(struct dirent));
    if(dir_find(dir_inode->ino,base_name,strlen(base_name),checker) == 0) {
        //printf("File already exists.\n");
        free(dir_name);
        free(base_name);
        free(dir_inode);
        free(checker);
        return -EEXIST;

    }
    free(checker);

	// Step 3: Call get_avail_ino() to get an available inode number
    int newino = get_avail_ino();
	// Step 4: Call dir_add() to add directory entry of target file to parent directory
    dir_add(*dir_inode,newino,base_name,strlen(base_name));
    // Step 5: Update inode for target file
    struct inode* new_inode = (struct inode *) malloc (sizeof(struct inode));
    new_inode->ino = newino;
    new_inode->valid = 1;
    new_inode->size = 0;
    new_inode->type = S_IFREG | mode;
    new_inode->link = 1;
    for(int i=0;i<16;i++) {
        new_inode->direct_ptr[i] = 0;
    }
    for(int i=0;i<8;i++) {
        new_inode->indirect_ptr[i] = 0;
    }

	// Step 6: Call writei() to write inode to disk
    writei(newino,new_inode);
    //printf("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFf\n");
    free(dir_name);
    free(base_name);
    free(dir_inode);
	return 0;
}

static int rufs_open(const char *path, struct fuse_file_info *fi) {

	// Step 1: Call get_node_by_path() to get inode from path

	// Step 2: If not find, return -1

    struct inode * path_inode = (struct inode*) malloc(sizeof(struct inode));
    if(get_node_by_path(path,0,path_inode) == -ENOENT) {
        //printf("open error.\n");
        return -1;
    }
    if(!S_ISREG(path_inode->type)) {
        //printf("not a file.\n");
        return -1;
    }

    time(&path_inode->vstat.st_atime);
    writei(path_inode->ino,path_inode);
    free(path_inode);
	return 0;
}

static int rufs_read(const char *path, char *buffer, size_t size, off_t offset, struct fuse_file_info *fi) {

	// Step 1: You could call get_node_by_path() to get inode from path
    struct inode* path_inode = (struct inode *) malloc (sizeof(struct inode));
    if(get_node_by_path(path,0,path_inode) == -ENOENT) {
        //printf("rufs_read error.\n");
        free(path_inode);
        return -ENOENT;
    }

    // Step 2: Based on size and offset, read its data blocks from disk
    // Step 3: copy the correct amount of data from offset to buffer

    int start_blk = ((int) offset / BLOCK_SIZE);
    int offset_new = offset % BLOCK_SIZE;
    int copied_size = 0;
    int left_size = size;
    int copy_byte = 0;
    if(size + offset > path_inode->size) {
        left_size = path_inode->size - offset;
    }
    void * temp_block = (void *) malloc (BLOCK_SIZE);

    for(int i=start_blk;i<16;i++) {
        if(path_inode->direct_ptr[i] == 0 || left_size == 0) {
            break;
        }
        bio_read(path_inode->direct_ptr[i],temp_block);

        if(i==start_blk) {
            copy_byte = min(left_size,BLOCK_SIZE-offset_new);
            memcpy(buffer,(void *)(temp_block+offset_new),copy_byte);
            left_size -= copy_byte;
            copied_size += copy_byte;

        }
        else {
            copy_byte = min(left_size,BLOCK_SIZE);
            memcpy(buffer+copied_size,temp_block,copy_byte);
            left_size -= copy_byte;
            copied_size += copy_byte;
        }


    }

	// Note: this function should return the amount of bytes you copied to buffer
    free(temp_block);
    free(path_inode);
	return copied_size;
}

static int rufs_write(const char *path, const char *buffer, size_t size, off_t offset, struct fuse_file_info *fi) {
	// Step 1: You could call get_node_by_path() to get inode from path
    struct inode* path_inode = (struct inode *) malloc (sizeof(struct inode));
    if(get_node_by_path(path,0,path_inode) == -ENOENT) {
        //printf("rufs_write error.\n");
        free(path_inode);
        return -ENOENT;
    }

    int start_blk = ((int) offset / BLOCK_SIZE);
    int offset_new = offset % BLOCK_SIZE;
    int copied_size = 0;
    int left_size = size;
    void * temp_block = (void *) malloc (BLOCK_SIZE);
    int copy_byte = 0;

    // Step 2: Based on size and offset, read its data blocks from disk
    for(int i = start_blk;i<16;i++) {
        if(left_size <= 0) {
            break;
        }
        if(path_inode->direct_ptr[i] == 0) {
            int newblock = get_avail_blkno();
            path_inode->direct_ptr[i] = newblock+super->d_start_blk;
        }

        if(i == start_blk) {
            copy_byte = min(left_size,BLOCK_SIZE-offset_new);
            memcpy(temp_block+offset_new,buffer,copy_byte);
            bio_write(path_inode->direct_ptr[i],temp_block);
            copied_size += BLOCK_SIZE-offset_new;
            left_size -= copy_byte;
            //printf("Writing first block with copybyte : %d in %d th (%d block) \n",copy_byte,i,path_inode->direct_ptr[i]);
        }
        else {
            copy_byte = min(left_size,BLOCK_SIZE);
            memcpy(temp_block,buffer+copied_size,copy_byte);
            bio_write(path_inode->direct_ptr[i],temp_block);
            copied_size += BLOCK_SIZE-offset_new;
            left_size -= copy_byte;
            //printf("Writing block with copybyte : %d\n",copy_byte);
        }

    }
	// Step 3: Write the correct amount of data from offset to disk

	// Step 4: Update the inode info and write it to disk
	path_inode->size += copied_size;
    time(&path_inode->vstat.st_mtime);
    writei(path_inode->ino,path_inode);
	// Note: this function should return the amount of bytes you write to disk

    free(temp_block);
    free(path_inode);
	return copied_size;
}


//CAN SKIP
static int rufs_unlink(const char *path) {

	// Step 1: Use dirname() and basename() to separate parent directory path and target file name

	// Step 2: Call get_node_by_path() to get inode of target file

	// Step 3: Clear data block bitmap of target file

	// Step 4: Clear inode bitmap and its data block

	// Step 5: Call get_node_by_path() to get inode of parent directory

	// Step 6: Call dir_remove() to remove directory entry of target file in its parent directory

	return 0;
}

static int rufs_truncate(const char *path, off_t size) {
	// For this project, you don't need to fill this function
	// But DO NOT DELETE IT!
    return 0;
}

static int rufs_release(const char *path, struct fuse_file_info *fi) {
	// For this project, you don't need to fill this function
	// But DO NOT DELETE IT!
	return 0;
}

static int rufs_flush(const char * path, struct fuse_file_info * fi) {
	// For this project, you don't need to fill this function
	// But DO NOT DELETE IT!
    return 0;
}

static int rufs_utimens(const char *path, const struct timespec tv[2]) {
	// For this project, you don't need to fill this function
	// But DO NOT DELETE IT!
    return 0;
}


static struct fuse_operations rufs_ope = {
	.init		= rufs_init,
	.destroy	= rufs_destroy,

	.getattr	= rufs_getattr,
	.readdir	= rufs_readdir,
	.opendir	= rufs_opendir,
	.releasedir	= rufs_releasedir,
	.mkdir		= rufs_mkdir,
	.rmdir		= rufs_rmdir,

	.create		= rufs_create,
	.open		= rufs_open,
	.read 		= rufs_read,
	.write		= rufs_write,
	.unlink		= rufs_unlink,

	.truncate   = rufs_truncate,
	.flush      = rufs_flush,
	.utimens    = rufs_utimens,
	.release	= rufs_release
};


int main(int argc, char *argv[]) {
	int fuse_stat;

	getcwd(diskfile_path, PATH_MAX);
	strcat(diskfile_path, "/DISKFILE");

	fuse_stat = fuse_main(argc, argv, &rufs_ope, NULL);

	return fuse_stat;
}

