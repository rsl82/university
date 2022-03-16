
// INCLUDING

#include "mymalloc.h"

// GLOBAL VARIABLES

static int fit_number = -1; //AllocAlg
const int heap_size = 1024*1024;
void* heapStart = NULL;
void* heapList = NULL;
void* allocList = NULL;
void* nextfit = NULL;
static int free_number = 0;
static int alloc_number = 0;


// STRUCTS
/*
typedef struct header{
    size_t size;
    size_t realsize;
    int alloced;
    struct header* next;
    struct header* prev;
}header; //Size = 40

typedef struct footer{
    size_t size;
}footer; //Size = 8
*/

int getFreenumber()
{
    int i = 0;
    void* tmp = heapList;
    while(tmp!= NULL)
    {
        i++;
        tmp = HEADER(tmp)->next;
        
    }

    return i;
}
int getAllocnumber()
{
    int i = 0;
    void* tmp = allocList;
    while(tmp!= NULL)
    {
        i++;
        tmp = HEADER(tmp)->next;
        
    }

    return i;
}

void* getFreePointer(int a) 
{
    return allocList+HSIZE;
    /*
    void* ptr= allocList;
    if(allocList == NULL)
        return NULL;
    for(int i=0;i<a;i++)
        ptr = HEADER(ptr)->next;
    
    return ptr+HSIZE; */
}

void* getHeapList()
{
    //printf("%p\n",heapList);
    return heapList;
}

void traverseHeapList()
{
    void* tmp = heapList;
    while(tmp != NULL)
    {
        printf("(%p, %zu, %zu, %zu) -> ",tmp,HEADER(tmp)->size,HEADER(tmp)->realsize, FOOTER(GETFOOT(tmp))->size);
        tmp = HEADER(tmp)->next;
    }

    printf("END\n");
}

void traverseReverseList()
{
    void* tmp = heapList;
    if(tmp == NULL)
    {
        printf("NULL List\n");
        return;
    }
        
    while(HEADER(tmp)->next != NULL)
    {
        tmp= HEADER(tmp)->next;
    }

    while(tmp != NULL)
    {
        printf("%p -> ",tmp);
        tmp = HEADER(tmp)->prev;

    }
    printf("END\n");
}

void traverseReverseAlloc()
{
    void* tmp = allocList;
    if(tmp == NULL)
    {   
        printf("NULL List\n");
        return;
    }
       
    
    while(HEADER(tmp)->next != NULL)
    {
        tmp= HEADER(tmp)->next;
    }

    while(tmp != NULL)
    {
        printf("%p -> ",tmp);
        tmp = HEADER(tmp)->prev;

    }
    printf("END\n");
}

void traverseAllocList()
{
    void* tmp = allocList;
    while(tmp != NULL)
    {
        printf("(%p, %zu, %zu) -> ",tmp,HEADER(tmp)->size,FOOTER(GETFOOT(tmp))->size);//FOOTER(GETFOOT(tmp))->size);
        tmp = HEADER(tmp)->next;
    }

    printf("END\n");
}

void myinit(int AllocAlg)
{
    heapStart = malloc(heap_size);
    heapList = heapStart;
    fit_number = AllocAlg;
    
    HEADER(heapList)->size = heap_size - HSIZE -  FSIZE;
    HEADER(heapList)->next = NULL;
    HEADER(heapList)->prev = NULL;
    HEADER(heapList)->realsize = HEADER(heapList)->size;
    HEADER(heapList)->alloced = 0;

    void* ptr = GETFOOT(heapList);
    FOOTER(ptr)->size = HEADER(heapList)->size;

} 

void* findSpace(size_t size)
{
    void* tmp = heapList;
    
    if(fit_number == 0) // First fit
    {
        while( tmp != NULL)
        {
            if(HEADER(tmp)->size >= size)
                return tmp;
        
            tmp = HEADER(tmp)->next;
        }

        return NULL;

    }

    else if(fit_number == 1) // Next fit
    {
        void* ptr2 = nextfit;

        if(nextfit == NULL)
        {
            while( tmp != NULL)
            {
                if(HEADER(tmp)->size >= size)
                {
                    nextfit = HEADER(tmp)->next;
                    return tmp;
                }

                tmp = HEADER(tmp)->next;
            }
        }

        else
        {
            while(ptr2 != NULL)
            {
                if(HEADER(ptr2)->size >= size)
                {
                    nextfit = HEADER(ptr2)->next;
                    return ptr2;
                }

                ptr2 = HEADER(ptr2)->next;
            }

            while(tmp != nextfit)
            {
                if(HEADER(tmp)->size >= size)
                {
                    nextfit = HEADER(tmp)->next;
                    return tmp;
                }

                tmp = HEADER(tmp)->next;
            }

        }

        return NULL;
    }

    else if(fit_number == 2) // Best fit
    {
        void* smallest = NULL;

        while( tmp != NULL)
        {
            if(HEADER(tmp)->size >= size)
            {
                if(smallest == NULL)
                    smallest = tmp;
                else
                {
                    if(HEADER(smallest)->size > HEADER(tmp)->size)
                        smallest = tmp;
                }
            }

            tmp = HEADER(tmp)->next;
        }

        return smallest;
    }


    return NULL;

}

// Delete in free list in mymalloc
void deleteList(header* find)
{
    if(find == heapList)
        heapList = (void*) (HEADER(heapList)->next);
    if(find->next != NULL)
        find->next->prev = find -> prev;
    if(find->prev != NULL)
        find->prev->next = find -> next;

    find->next= NULL;
    find->prev= NULL;

}


void deleteAlloc(header* find)
{
    if(find == allocList)
        allocList = (void*) (HEADER(allocList)->next);
    
    if(find->next != NULL)
        find->next->prev = find -> prev;
    if(find->prev != NULL)
        find->prev->next = find -> next;

    find->next=NULL;
    find->prev=NULL;
}


void* mymalloc(size_t size)
{
    if(size == 0)
        return NULL;
    
    size_t align_size = ALIGN(size);
    
    void* find = findSpace(align_size);
    
    if(find==NULL)
        return NULL;
    
    void* findFoot = GETFOOT(find);
    size_t newsize = HEADER(find)->size - HSIZE - FSIZE - align_size; // For splitting

    if(HEADER(find)->size == align_size || HEADER(find)->size <= HSIZE + FSIZE + align_size)
    {   
        HEADER(find)->size = ALLOCATE(HEADER(find)->size);
        HEADER(find)->realsize = size;
        FOOTER(findFoot)->size = HEADER(find)->size;    
        HEADER(find)->alloced = 1;
        deleteList(HEADER(find));
        addAlloc(find);
        
        alloc_number++;
        free_number--;
         
    }
    else    
    {

        HEADER(find)->size = ALLOCATE(align_size);
        HEADER(find)->realsize = size;
        HEADER(find)->alloced = 1;
        void* newfoot = GETFOOT(find);
        FOOTER(newfoot)->size = HEADER(find)->size;
        
        deleteList(HEADER(find));
        addAlloc(find);

        void* newhead = newfoot + FSIZE;
        FOOTER(findFoot)->size = newsize;
        HEADER(newhead)->alloced = 0;
        HEADER(newhead)->size = newsize;
        HEADER(newhead)->realsize = newsize;
        HEADER(newhead)->next =NULL;
        HEADER(newhead)->prev = NULL;
        addList(newhead);
        
       

    }
    return find+HSIZE;

}


void addList(void* ptr)
{
    if(heapList == NULL)
        heapList = ptr;
    
    else
    {
        HEADER(ptr)->prev=NULL;
        HEADER(ptr)->next = heapList;
        HEADER(heapList)->prev =ptr;
        heapList= ptr;
    }
}


void addAlloc(void* ptr)
{
    if(allocList == NULL)
        allocList = ptr;
    
    else
    {
        HEADER(ptr)->prev=NULL;
        HEADER(ptr)->next = allocList;
        HEADER(allocList)->prev =ptr;
        allocList= ptr;
    }
}


void* coalesce(void* ptr)
{
    size_t prevAlloc;
    size_t nextAlloc;
    size_t newsize;

    if(ptr-HSIZE-256-FSIZE < heapStart)
        prevAlloc = 1;
    else
        prevAlloc = CHECK(FOOTER(ptr-8)->size);

    if(GETFOOT(ptr)+HSIZE+256+FSIZE>heapStart+heap_size)
        nextAlloc = 1;
    else
        nextAlloc = CHECK(HEADER(GETFOOT(ptr)+8)->size);



    if(prevAlloc == 1 && nextAlloc == 1)
    {
        HEADER(ptr)->size = SIZE(HEADER(ptr)->size);
        HEADER(ptr)->realsize = HEADER(ptr)->size;
        FOOTER(GETFOOT(ptr))->size = HEADER(ptr)->size;
        free_number++;

        return ptr;
    }
    
    if(prevAlloc == 0 && nextAlloc == 1)
    {
        newsize = FOOTER(ptr-8)->size + SIZE(HEADER(ptr)->size) + HSIZE + FSIZE;

        void* tmp = ptr - FSIZE - FOOTER(ptr-8)->size - HSIZE;

        HEADER(tmp) -> size = newsize;
        HEADER(tmp) -> realsize = newsize;
        deleteList(tmp);
        FOOTER(GETFOOT(ptr)) -> size = newsize;
        
        return tmp;

    }

    if(prevAlloc == 1 && nextAlloc == 0)
    {
        void* nextptr = GETFOOT(ptr) + 8 ;
        void* nextfoot = GETFOOT(nextptr);
        newsize = SIZE(HEADER(ptr)->size) + HSIZE + FSIZE + HEADER(nextptr)->size;

        HEADER(ptr)->size = newsize;
        HEADER(ptr)->realsize = newsize;
        FOOTER(nextfoot)->size = newsize;
        deleteList(nextptr);

        if(nextfit == nextptr)
            nextfit = ptr;
        
        return ptr;
    }

    if(prevAlloc == 0 && nextAlloc == 0)
    {
        void* prevhead = ptr - FSIZE - FOOTER(ptr-8)->size - HSIZE;
        void* nexthead = GETFOOT(ptr) + 8;
        void* nextfoot = GETFOOT(nexthead);

        newsize = SIZE(HEADER(ptr)->size) + HSIZE + HSIZE + FSIZE + FSIZE + HEADER(nexthead)->size + HEADER(prevhead)->size;

        HEADER(prevhead) -> size = newsize;
        HEADER(prevhead) -> realsize = newsize;
        deleteList(prevhead);

        
        FOOTER(nextfoot)->size = newsize;
        deleteList(nexthead);

        if(nextfit == nexthead)
            nextfit = prevhead;
        free_number--;
        return prevhead;
    }

    return NULL;

}


int findAlloc(void* ptr)
{
    void* tmp = allocList;

    while(tmp!= NULL)
    {
        if(tmp == ptr)
            return 1;
        tmp = HEADER(tmp)->next;
    }

    return 0;
}


void myfree(void* ptr)
{
    if(ptr==NULL)
        return;
    
    //ptr is outside of heap
    if( ptr < heapStart || ptr > (heapStart + heap_size))
    {
        printf("error: not a heap pointer\n");
        return;
    }

    //ptr is not 8-aligned pointer
    uintptr_t x = (uintptr_t) ptr;
    if(x % 8 != 0)
    {
        printf("error: not a malloced address1\n");
        return;
    }

    void* head = ptr - HSIZE;
    //metadata of ptr is outside of heap so it is impossible
    if( head < heapStart || GETFOOT(head) > (heapStart + heap_size) -8 )
    {
        printf("error: not a malloced address2\n");
        return;
    }
    //header and footer are not matching so not a block
    if(HEADER(head)->size != FOOTER(GETFOOT(head))->size)
    {
        printf("error: not a malloced address3\n");
        return;
    }
    // if ptr is not in allocated list, then check if it was malloced before
    if (findAlloc(head) == 0)
    {
        if(HEADER(head)->alloced == 0)
        {
            printf("error: not a malloced address4\n");
            return;
        }
        if(HEADER(head)->alloced == 1)
        {
            printf("error: double free\n");
            return;
        }
    }
    
    deleteAlloc(HEADER(head));
    alloc_number--;
    void * tmp = coalesce(head);
    
    addList(tmp);
    return;
}


void* myrealloc(void* ptr,size_t size)
{

    
    if(ptr==NULL && size == 0)
        return NULL;
    if(ptr==NULL)
        return mymalloc(size);
    if(size==0)
    {
        myfree(ptr);
        return NULL;
    }

    //ptr is outside of heap
    if( ptr < heapStart || ptr > (heapStart + heap_size))
    {
        printf("error: not a heap pointer\n");
        return NULL;
    }

    //ptr is not 8-aligned pointer
    uintptr_t x = (uintptr_t) ptr;
    if(x % 8 != 0)
    {
        printf("error: not a malloced address1\n");
        return NULL;
    }

    void* head = ptr - HSIZE;
    //metadata of ptr is outside of heap so it is impossible
    if( head < heapStart || GETFOOT(head) > (heapStart + heap_size) -8 )
    {
        printf("error: not a malloced address2\n");
        return NULL;
    }
    //header and footer are not matching so not a block
    if(HEADER(head)->size != FOOTER(GETFOOT(head))->size)
    {
        printf("error: not a malloced address3\n");
        return NULL;
    }


    void* next = GETFOOT(head)+8;
    if(SIZE(HEADER(head)->size) == size)
        return ptr;

    //printf("%zu %zu\n",HEADER(head)->size,size);

    if(SIZE(HEADER(head)->size) < ALIGN(size)) //move to bigger room
    {

        if(next>=heapStart + heap_size )
        {
            void* new = mymalloc(size);
            if(new== NULL)
                return NULL;
            memmove(new,ptr,SIZE(HEADER(head)->size));
            myfree(ptr);

            return new;
        }
        size_t ifnextAlloc = CHECK(HEADER(next)->size);
        if(ifnextAlloc==1 || ((ifnextAlloc == 0) && (SIZE(HEADER(next)->size) <= (ALIGN(size) - SIZE(HEADER(head)->size)))) )
        {
            
            void* new = mymalloc(size);
            if(new== NULL)
                return NULL;
            memmove(new,ptr,SIZE(HEADER(head)->size));
            myfree(ptr);

            return new;
        }
        else
        {   //printf("@@@@@@@@@@\n");
            deleteList(next);
            size_t z = (ALIGN(size) - SIZE(HEADER(head)->size));   
            //printf("Size : %zu\n",z);
            FOOTER(GETFOOT(next))->size = FOOTER(GETFOOT(next))->size - z;
            //printf("FOOTSIZE: %zu\n",FOOTER(GETFOOT(next))->size);
            HEADER(next)->size = FOOTER(GETFOOT(next))->size;
            //printf("NEXTSIZE: %zu\n",HEADER(next)->size);
            void* newnext = next + z;
            memmove(newnext,next,sizeof(header));
            addList(newnext);


            if(nextfit == next)
                nextfit = newnext;
            
            

            void* foot = GETFOOT(head);
            FOOTER(foot)->size += z;
            HEADER(head)->size = FOOTER(GETFOOT(head))->size;
            HEADER(head)->realsize = size;
            memmove(GETFOOT(head),foot,sizeof(footer));
        
            return ptr;
        }
    }

    else 
    {
        if(SIZE(HEADER(head)->size) - ALIGN(size) <= HSIZE + FSIZE)
        {
            HEADER(head)->realsize = size;
            return ptr;
        }
        else
        {
            size_t newsize = SIZE(HEADER(head)->size) - ALIGN(size)- HSIZE - FSIZE;

            HEADER(head)->size = ALLOCATE(ALIGN(size));
            HEADER(head)->realsize = size;
            void* newfoot = GETFOOT(head);
            FOOTER(newfoot)->size = HEADER(head)->size;
            
            void* newhead = newfoot + FSIZE;
            HEADER(newhead)->alloced = 0;
            HEADER(newhead)->size = newsize;
            HEADER(newhead)->realsize = newsize;
            HEADER(newhead)->next =NULL;
            HEADER(newhead)->prev = NULL;
            FOOTER(GETFOOT(newhead))->size = newsize;
            addList(newhead);
        }
    }
    
    

    return ptr;
    

}

void mycleanup()
{
    free(heapStart);
    heapStart=NULL;
    heapList=NULL;
    allocList = NULL;
    nextfit = NULL;
    free_number=0;
    alloc_number=0;
}


double utilization()
{
    void* ptr = allocList;
    double memory_size = 0;
    double denominator = 0;

    if(ptr == NULL)
    {
        return 0;
    }
    while(ptr!= NULL)
    {
        memory_size = memory_size + HEADER(ptr)->realsize;
        denominator = denominator + SIZE(HEADER(ptr)->size) + HSIZE + FSIZE;

        ptr = HEADER(ptr)->next;
    }

    return (memory_size / denominator);
}
