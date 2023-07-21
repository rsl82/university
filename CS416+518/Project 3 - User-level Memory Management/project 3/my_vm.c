
//File: my_vm.c
// List all group member's name: Ryan Lee, Mario Barsoum
// username of iLab: rsl82, mwb96
// iLab Server: cp.cs.rutgers.edu


#include "my_vm.h"

/*
Function responsible for allocating and setting your physical memory 
*/

unsigned char* mem;
unsigned char* bitmap_physical;
unsigned char* bitmap_virtual;
int physical_page_number;
int virtual_page_number;
int offset=12;
int outer=10;
int inner=10;
pde_t * page_directory;
pthread_mutex_t mutex;
struct tlb *storeTLB[TLB_ENTRIES];
void* myNull = UINTPTR_MAX;

void set_physical_mem() {
    //Allocate physical memory using mmap or malloc; this is the total size of
    //your memory you are simulating

    pthread_mutex_init(&mutex,NULL);
    pthread_mutex_lock(&mutex);
    mem = mmap(NULL,MEMSIZE,PROT_READ|PROT_WRITE|PROT_EXEC, MAP_ANONYMOUS|MAP_PRIVATE, 0, 0);
    if(mem == MAP_FAILED) {
        perror("Failed to assign memory");
        exit(0);
    }

    physical_page_number = MEMSIZE / PGSIZE;
    virtual_page_number = MAX_MEMSIZE / PGSIZE;

    bitmap_physical = (unsigned char *) malloc (physical_page_number / 8 + 1);
    bitmap_virtual = (unsigned char *) malloc (virtual_page_number / 8 + 1);
    for(int i=0; i<physical_page_number/8+1;i++) {
        bitmap_physical[i] = 0;
    }
    for(int i=0; i<virtual_page_number/8+1;i++) {
        bitmap_virtual[i] = 0;
    }

    page_directory = (pde_t *) calloc(1024,sizeof(pde_t));
    //printf("malloc initialization success\n");
    //HINT: Also calculate the number of physical and virtual pages and allocate
    //virtual and physical bitmaps and initialize them
    numChecks = 0;
    numMisses = 0;
    bitmapTLB = (unsigned char *) malloc(TLB_ENTRIES / 8);
    for(int i=0;i<TLB_ENTRIES / 8 ;i++) {
        bitmapTLB[i] = 0;
        
    }
    for(int i=0;i<TLB_ENTRIES;i++) {
    	storeTLB[i] = (struct tlb*) malloc(sizeof(struct tlb));
    }
 
    
    pthread_mutex_unlock(&mutex);
}


/*
 * Part 2: Add a virtual to physical page translation to the TLB.
 * Feel free to extend the function arguments or return type.
 */
int
add_TLB(void *va, void *pa) {

    /*Part 2 HINT: Add a virtual to physical page translation to the TLB */

    for (int x = 0; x < TLB_ENTRIES; x++) {
        if (!getBit(bitmapTLB, x)) {
        	//printf("bitmap: %d\n",x);
            storeTLB[x]->virTLB = va;
            storeTLB[x]->physTLB = pa;
            setBit(bitmapTLB,x);
            return 1;
        }
    }

    storeTLB[0]->virTLB = va;
    storeTLB[0]->physTLB = pa;
    return 1;
}

void delete_TLB(void *va) {
    for (int x = 0; x < TLB_ENTRIES; x++) {
        if (!getBit(bitmapTLB, x) && storeTLB[x]->virTLB == va) {
            clearBit(bitmapTLB,x);
            return;
        }
    }

    return;
}



/*
 * Part 2: Check TLB for a valid translation.
 * Returns the physical page address.
 * Feel free to extend this function and change the return type.
 */
pte_t *
check_TLB(void *va) {

    /* Part 2: TLB lookup code here */

	numChecks++;
	for(int x = 0; x<TLB_ENTRIES; x++) {
		if(getBit(bitmapTLB, x) && storeTLB[x]->virTLB == va) {
			return storeTLB[x]->physTLB;
		}
	}
	numMisses++;
	return NULL;

   /*This function should return a pte_t pointer*/
}


/*
 * Part 2: Print TLB miss rate.
 * Feel free to extend the function arguments or return type.
 */
void
print_TLB_missrate()
{
    /*Part 2 Code here to calculate and print the TLB miss rate*/
    double miss_rate = numMisses / numChecks;
    fprintf(stderr, "TLB miss rate %lf \n", miss_rate);
}

//just to see
void print_TLB(){
	for(int x = 0; x < TLB_ENTRIES; x++) {
		if(getBit(bitmapTLB, x)){
			printf("TLB %d: (%p,%p)\t",x, storeTLB[x]->virTLB, storeTLB[x]->physTLB);
		}
        else {
			printf("NULL\t");
		}
	}
	printf("\n");
	
}

/*
The function takes a virtual address and page directories starting address and
performs translation to return the physical address
*/
pte_t *translate(pde_t *pgdir, void *va) {
    /* Part 1 HINT: Get the Page directory index (1st level) Then get the
    * 2nd-level-page table index using the virtual address.  Using the page
    * directory index and page table index get the physical address.
    *
    * Part 2 HINT: Check the TLB before performing the translation. If
    * translation exists, then you can return physical address from the TLB.
    */
    
    unsigned long outer_bit = ((unsigned int)va >> (inner + offset));
    unsigned long inner_bit = ((unsigned int)va >> offset) & 0x3FF;
    unsigned long offset_bit = (unsigned int)va & 0xFFF;

    pte_t * checkTLB = check_TLB(va);
    if(checkTLB != NULL) {
        return checkTLB;
    }
    


    if((pte_t *)pgdir[outer_bit] == NULL) {
        perror("Cannot perform page translation(1)");
        pthread_mutex_unlock(&mutex);
        return NULL;
    }

    void * address = (void*)((pte_t *)pgdir[outer_bit])[inner_bit];
    if(address == NULL) {
        perror("Cannot perform page translation(2)");
        pthread_mutex_unlock(&mutex);
        return NULL;
    }

    add_TLB(va,address);
    //printf("HA\n");
   
    //If translation not successful, then return NULL
    return address;
}


/*
The function takes a page directory address, virtual address, physical address
as an argument, and sets a page table entry. This function will walk the page
directory to see if there is an existing mapping for a virtual address. If the
virtual address is not present, then a new entry will be added
*/
int page_map(pde_t *pgdir, void *va, void *pa)
{
    
    /*HINT: Similar to translate(), find the page directory (1st level)
    and page table (2nd-level) indices. If no mapping exists, set the
    virtual to physical mapping */

    unsigned long outer_bit = ((unsigned int)va >> (inner + offset));
    unsigned long inner_bit = ((unsigned int)va >> offset) & 0x3FF;
    unsigned long offset_bit = (unsigned int)va & 0xFFF;

   
    if((pte_t*)pgdir[outer_bit] == NULL) {
        pgdir[outer_bit] =(pte_t*) calloc(1024, sizeof(pte_t));
    }

    if(((pte_t*)pgdir[outer_bit])[inner_bit] == NULL) {
        ((pte_t*)pgdir[outer_bit])[inner_bit] = pa;
        
        return 1;
    }


    //If translation not successful, then return NULL null is not good for int return so i put it 0
   
	
    return 0;
}


/*Function that gets the next available page
*/
void *get_next_avail(int num_pages) {
 
    //Use virtual address bitmap to find the next free page

    int foundPages = 0;
    for(int i=0;i<virtual_page_number;i++) {
        if(getBit(bitmap_virtual,i) == 0) {
            foundPages ++;

            if(foundPages == num_pages) {
                for(int j = i - num_pages + 1;j<=i;j++) {
                    setBit(bitmap_virtual,j);
                    //printf("setbit\n");
                }
		
                int value = (i-foundPages+1);
                //printf("virtual bitmap:%d",value);
                
                return (void *)((intptr_t) value);
            }
        }
        else {
            foundPages = 0;
        }

    }
    perror("No page found in get_next_avail");
    return myNull;



}


void *get_next_avail2() {

    //Use physical address bitmap to find the next free page

    for(int i=0;i<physical_page_number;i++) {
        if(getBit(bitmap_physical,i) == 0) {

            setBit(bitmap_physical,i);
            //printf("physical bitmap:%d\n",i);
            unsigned long value = i * PGSIZE;
            return (void *) (mem+value);
        }
    }
    perror("No page found in get_next_avail2");
    return myNull;

}

bool getBit(unsigned char* bitmap, unsigned int i) {
    int byte = i/8;
    int bit = i%8;
    return (bitmap[byte] >> bit) & 1;


}


void setBit(unsigned char* bitmap, unsigned int i) {
    unsigned int byte = i/8;
    unsigned int bit = i%8;
    bitmap[byte] |= (1 << bit);

}

void clearBit(unsigned char* bitmap, unsigned int i) {
    unsigned int byte = i/8;
    unsigned int bit = i*8;
    bitmap[byte] &= ~(1 << bit);

}


/* Function responsible for allocating pages
and used by the benchmark
*/
void *t_malloc(unsigned int num_bytes) {
   
    /* 
     * HINT: If the physical memory is not yet initialized, then allocate and initialize.
     */
    if(mem == NULL) {
        set_physical_mem();
    }
    pthread_mutex_lock(&mutex);

    
    int num_page = ((int)((float)num_bytes / (float)PGSIZE)) + 1;
    //printf("num pages: %d\n",num_page);
    void* allocated =  get_next_avail(num_page);
    if(allocated== myNull) {
        printf("malloc error.\n");
        return NULL;
    }
	
    int allocated_page = (int) allocated;
    //printf("allocated_page: %d\n",allocated_page);
    void * address = NULL;



    for(int i = 0; i<num_page;i++) {

        unsigned long outer = allocated_page / 1024;
        unsigned long inner = allocated_page % 1024;
        //printf("%ld %ld \n",outer,inner);
        void * va = (void*)((outer << 22) | (inner << 12));
	//printf("%p\n",va);
        void * pa = get_next_avail2();
        if(pa == myNull) {
            printf("malloc error.\n");
            return NULL;
        }


        if(i == 0) {
            address = va + ((unsigned int)pa & 0xFFF);
        }
       
        
        page_map(page_directory,va,pa);



        allocated_page = allocated_page + 1;
    }


    pthread_mutex_unlock(&mutex);

   /* 
    * HINT: If the page directory is not initialized, then initialize the
    * page directory. Next, using get_next_avail(), check if there are free pages. If
    * free pages are available, set the bitmaps and map a new page. Note, you will 
    * have to mark which physical pages are used. 
    */

    return (void *) address;
}

/* Responsible for releasing one or more memory pages using virtual address (va)
*/
void t_free(void *va, int size) {

    /* Part 1: Free the page table entries starting from this virtual address
     * (va). Also mark the pages free in the bitmap. Perform free only if the 
     * memory from "va" to va+size is valid.
     *
     * Part 2: Also, remove the translation from the TLB
     */
      pthread_mutex_lock(&mutex);
    unsigned long outer_bit = ((unsigned int)va >> (inner + offset));
    unsigned long inner_bit = ((unsigned int)va >> offset) & 0x3FF;

   

    int page_num =((int)((float) size /(float)PGSIZE)) + 1;
    delete_TLB(va);
    for(int i=0;i<page_num;i++) {
        int temp = ((float)(outer_bit * PGSIZE + inner_bit) / (float)PGSIZE);
        void* pa = ((pte_t*)page_directory[outer_bit])[inner_bit];
        ((pte_t*)page_directory[outer_bit])[inner_bit] == NULL;
        int phy = (int)((float)((unsigned long)pa-(unsigned long)mem) / (float)PGSIZE);
        //printf("freeing %d %d %p\n",phy,inner_bit,va);
        clearBit(bitmap_physical,phy);
        clearBit(bitmap_virtual,inner_bit);


        if(inner_bit >= 1024) {
            inner_bit = 0;
            outer_bit += 1;
        }
        else {
            inner_bit += 1;
        }

    }

    pthread_mutex_unlock(&mutex);

}


/* The function copies data pointed by "val" to physical
 * memory pages using virtual address (va)
 * The function returns 0 if the put is successfull and -1 otherwise.
*/
int put_value(void *va, void *val, int size) {

    /* HINT: Using the virtual address and translate(), find the physical page. Copy
     * the contents of "val" to a physical page. NOTE: The "size" value can be larger 
     * than one page. Therefore, you may have to find multiple pages using translate()
     * function.
     */

	//printf("%ld %ld \n",outer_bit,inner_bit);

    pthread_mutex_lock(&mutex);
    if(size <= PGSIZE) {
        void * pa = translate(page_directory, va);
        if(pa == NULL) {
            printf("translate failed.\n");
            return -1;
	}
        memcpy(pa,val,size);
        pthread_mutex_unlock(&mutex);
        return 0;
    }
    else {
        int num_page = ((int)((float)size / (float)PGSIZE)) +1;
        for(int i = 0; i<num_page;i++) {
            
            void * pa = translate(page_directory, va);
            if(pa == NULL) {
            printf("translate failed.\n");
		return -1;
	    }
            //printf("pointer: %p\n",pa);
            if(i == num_page -1) {
                memcpy(pa,val+i*PGSIZE,size%PGSIZE);
            }
            else {
                memcpy(pa,val+ i* PGSIZE,PGSIZE);
                
            }
            
	    va = (void*) va + PGSIZE;
            
        }
        pthread_mutex_unlock(&mutex);

        return 0;
    }

    /*return -1 if put_value failed and 0 if put is successfull*/

}


/*Given a virtual address, this function copies the contents of the page to val*/
void get_value(void *va, void *val, int size) {

    /* HINT: put the values pointed to by "va" inside the physical memory at given
    * "val" address. Assume you can access "val" directly by derefencing them.
    */
    
    unsigned long outer_bit = ((unsigned int)va >> (inner + offset));
    unsigned long inner_bit = ((unsigned int)va >> offset) & 0x3FF;
    unsigned long offset_bit = (unsigned int)va & 0xFFF;

    pthread_mutex_lock(&mutex);
    if(size <= PGSIZE) {
        void * pa = translate(page_directory, va);
        if(pa == NULL) {
            printf("translate failed.\n");
            return;
	}
        memcpy(val,pa,size);
        pthread_mutex_unlock(&mutex);
        return;
    }
    else {
        int num_page = ((int)((float)size / (float)PGSIZE)) +1;
        for(int i = 0; i<num_page;i++) {
            void * pa = (void*)((pte_t*)page_directory[outer_bit])[inner_bit];
            if(i == num_page -1) {
                memcpy(val+i*PGSIZE,pa,size%PGSIZE);
            }
            else {
                memcpy(val+i*PGSIZE,pa,PGSIZE);
            }

            if(inner_bit >= 1024) {
                inner_bit = 0;
                outer_bit += 1;
            }
            else {
                inner_bit += 1;
            }
        }
        pthread_mutex_unlock(&mutex);

        return;
    }



}



/*
This function receives two matrices mat1 and mat2 as an argument with size
argument representing the number of rows and columns. After performing matrix
multiplication, copy the result to answer.
*/
void mat_mult(void *mat1, void *mat2, int size, void *answer) {
    
    /* Hint: You will index as [i * size + j] where  "i, j" are the indices of the
     * matrix accessed. Similar to the code in test.c, you will use get_value() to
     * load each element and perform multiplication. Take a look at test.c! In addition to 
     * getting the values from two matrices, you will perform multiplication and 
     * store the result to the "answer array"
     */

    for(int i=0;i < size;i ++) {
        for(int j=0;j<size;j++) {
            int sum = 0;
            for(int k=0;k<size;k++) {
                int val1,val2;

                get_value(mat1 + i*size*sizeof(int) + k*sizeof(int), &val1,sizeof(int));
                get_value(mat2 + k*size*sizeof(int) + j*sizeof(int),&val2,sizeof(int));
                sum += val1*val2;
            }
            put_value(answer + i *size * sizeof(int) + j*sizeof(int),&sum,sizeof(int));
        }
    }

    
}



