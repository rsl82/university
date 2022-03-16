#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <dirent.h>
#include <unistd.h>
#include <errno.h>
#include <pwd.h>
#include <grp.h>
#include <time.h>

struct node
{
    char* string;
    struct node* next;
    char perm[15];
    char* usrid;
    char* grpid;
    size_t size;
    char modtime[50];
};

void addString(struct node** head,struct dirent* direc,int* flag)
{
    struct stat path;
    stat(direc->d_name,&path);


    struct node* new=malloc(sizeof(struct node));
    if(new==NULL)
    {
        (*flag)=2;
        return;
    }
    struct node* finder=*head;
    
    
    new->string=malloc(sizeof(char)*strlen(direc->d_name)+1);
    if((new->string)==NULL)
    {
        (*flag)=2;
        return;
    }
    strcpy(new->string,direc->d_name);
    new->next=NULL;

    //permission && directory
    for(int i=0;i<10;i++)
    {
        new->perm[i]='-';
    }
    new->perm[10]='\0';

    if(S_ISDIR(path.st_mode))
        new->perm[0]='d';
    if(path.st_mode & S_IRUSR)
        new->perm[1]='r';
    if(path.st_mode & S_IWUSR)
        new->perm[2]='w';
    if(path.st_mode & S_IXUSR)
        new->perm[3]='x';
    if(path.st_mode & S_IRGRP)
        new->perm[4]='r';
    if(path.st_mode & S_IWGRP)
        new->perm[5]='w';
    if(path.st_mode & S_IXGRP)
        new->perm[6]='x';
    if(path.st_mode & S_IROTH)
        new->perm[7]='r';
    if(path.st_mode & S_IWOTH)
        new->perm[8]='w';
    if(path.st_mode & S_IXOTH)
        new->perm[9]='x';

    //usrid
    new->usrid=malloc(sizeof(char)*strlen((getpwuid(path.st_uid))->pw_name)+1);
    if((new->usrid)==NULL)
    {
        (*flag)=2;
        return;
    }
    strcpy(new->usrid,(getpwuid(path.st_uid))->pw_name);

    //grpip
    new->grpid=malloc(sizeof(char)*strlen((getgrgid(path.st_gid))->gr_name)+1);
    if((new->grpid)==NULL)
    {
        (*flag)=2;
        return;
    }
    strcpy(new->grpid,(getgrgid(path.st_gid))->gr_name);
    
    //size
    new->size=path.st_size;


    //mod time
    
    strftime(new->modtime,50,"%b %d %H:%M",localtime(&path.st_mtime));

    //implement && sort
    
    if(*head==NULL || strcasecmp((*head)->string, new->string) >0)
    {
        new->next=*head;
        (*head)=new;
        return;
    }
    
    while(finder->next !=NULL && strcasecmp(finder->next->string, new->string) <=0)
        finder=finder->next;
    
    new->next=finder->next;
    finder->next=new;

}

void traverse(struct node* head,int a)
{
    while (head!=NULL)
    {
        if(a==1)
        {
            printf("%s %s %s %zu %s ",head->perm,head->usrid,head->grpid,head->size,head->modtime);
        }

        printf("%s\n",head->string);
        head=head->next;
    }
}


int main(int argc, char* argv[])
{
    
    struct node* next=NULL;
    struct node* head = NULL;  
    int flag=0;
    int checker=0;
    struct dirent* direc;
    DIR* dirp= opendir(".");
    if(!dirp)
    {
        perror("Open directory: ");
        exit(EXIT_FAILURE);
    }
    errno=0;

    while (( direc=readdir(dirp)) != NULL)
    {   
        if(direc->d_name[0]!='.')
            addString(&head,direc,&flag);

        if(flag==2) //Malloc Error
        {
            printf("Malloc Error\n");
            while(head !=NULL)
            {
                next=head->next;
                free(head->usrid);
                free(head->grpid);
                free(head->string);
                free(head);
                head=next;
            }
            exit(EXIT_FAILURE);
        }
    }
    if(errno)
    {
        printf("Error!");
        exit(EXIT_FAILURE);
    }

    if(argc==2 && strcmp(argv[1],"-l")==0)
    {
        checker=1;
    }
    

    traverse(head,checker);

    while(head !=NULL)
    {
        next=head->next;
        if(head->usrid!=NULL)
            free(head->usrid);
        if(head->grpid!=NULL)    
            free(head->grpid);
        if(head->string!=NULL)    
            free(head->string);
        free(head);
        head=next;
    }

    closedir(dirp);

   exit(EXIT_SUCCESS);
}