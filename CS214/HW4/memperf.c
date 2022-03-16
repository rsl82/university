#include "mymalloc.h"



int main(int argc, char* argv[])
{
    int whattodo;
    int size;
    time_t t;
    int freenumber;
    struct timeval startTime;
    struct timeval endTime;
    double diffTime;
    double throughput;
    int trial = 1000000;
    //void* ptr;

    
    srand((unsigned) time(&t));

    for(int j=0;j<3;j++)
    {
        myinit(j);

        gettimeofday(&startTime,NULL);

        for(int i= 0 ; i < trial ; i ++)
        {   
            
            whattodo = rand()% 3; // 0 = malloc , 1 = free , 2= realloc
            
            if(whattodo == 0)
            {
                
                size = 1+(rand()%256);
                
                mymalloc(size);
            }

            else if(whattodo == 1)
            {
                //printf("FREE ->");
                if(getAllocnumber()<=0)
                    continue;

                freenumber = rand()% (getAllocnumber());

                myfree(getFreePointer(freenumber));

            }

            else if(whattodo == 2)
            {
                size = rand()%257;


                void* ptr;
                freenumber = (rand() % (getAllocnumber()+1)) -1;
                if(freenumber == -1)
                    ptr = NULL;
                    
                else
                    ptr = getFreePointer(freenumber);
                
                myrealloc(ptr,size);
            }
           
        }
        
        gettimeofday(&endTime,NULL);
        //traverseHeapList();
        //traverseAllocList();
        diffTime = (endTime.tv_sec - startTime.tv_sec) + ((endTime.tv_usec - startTime.tv_usec) * 1e-6);
        throughput = trial/diffTime;

        if(j==0)
            printf("First fit ");
        else if(j==1)
            printf("Next fit ");
        else if(j==2)
            printf("Best fit ");

        printf("throughput: %.1f ops/sec\n", throughput);
        if(j==0)
            printf("First fit ");
        else if(j==1)
            printf("Next fit ");
        else if(j==2)
            printf("Best fit ");
        printf("utilization: %.2f\n",utilization());
       // printf("%d %d\n",getFreenumber(),getAllocnumber());
        mycleanup();

        
    }
    
   
    
    return 0;
}
/*

 printf("FREELIST: ");
            traverseHeapList();
            printf("ALLOCLIST: ");
            traverseAllocList(); 
int main(int argc, char* argv[]) 
{

    myinit(0);

    void* ptr1= mymalloc(100);
    void* ptr2= mymalloc(100);
    //void* ptr3 =mymalloc(10);

 

     printf("FREELIST: ");
    traverseHeapList();
    printf("ALLOCLIST: ");
    traverseAllocList();

    ptr1= myrealloc(ptr1,30);



    printf("FREELIST: ");
    traverseHeapList();
    printf("ALLOCLIST: ");
    traverseAllocList();

    mycleanup();

    return 0;
}*/