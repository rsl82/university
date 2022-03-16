#ifndef _MYMALLOC_H_
#define _MYMALLOC_H_

// INCLUDING

#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <stdio.h>
#include <stdint.h>
#include <time.h>
#include <sys/time.h>


// MACROS

#define ALLOCATE(x) ((x | (1UL)))  // Mark as allocated size
#define CHECK(x) ((x >> 0) & 1U)   // check if it is allocated   
#define SIZE(x) ((x & ~(1U)))    // Mark off alocated flag from size
#define HSIZE 40 // Header size
#define FSIZE 8  // Footer size
#define HEADER(x) ((header*)(x)) // void* -> header*
#define FOOTER(x) ((footer*)(x)) // void* -> footer*
#define ALIGN(x) (((x) + (7)) & ~0x7) // Size alignment
#define GETFOOT(x) (x + HSIZE + SIZE((HEADER(x)->size)))  // footer address from header address



// STRUCTS

typedef struct header{
    size_t size;
    size_t realsize;
    size_t alloced;
    struct header* next;
    struct header* prev;
}header; //Size = 40

typedef struct footer{
    size_t size;
}footer; //Size = 8

void myinit(int AllocAlg);
void* findSpace(size_t size);
void* mymalloc(size_t size);
int findAlloc(void* ptr);
void* coalesce(void* ptr);
void deleteList(header* find);
void myfree(void* ptr);
void* getHeapList();
void traverseHeapList();
void traverseAllocList();
void addAlloc(void* ptr);
void addList(void* ptr);
void traverseReverseAlloc();
void traverseReverseList();
void* myrealloc(void* ptr,size_t size);
void mycleanup();
double utilization();
void* getFreePointer(int a);
int getFreenumber();

int getAllocnumber();

#endif