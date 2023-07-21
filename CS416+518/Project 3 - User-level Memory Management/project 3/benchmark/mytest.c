#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include "../my_vm.h"

#define SIZE 8192
int main () {
    char data[8193];

    
    for(int i=0;i<SIZE;i++) {
        data[i] = 'A' + (i % 26);
    }
    data[SIZE] = '\0';
    char* data3=(char*) malloc(sizeof(char)*8193);
    strcpy(data3,data);
    char *data2 = t_malloc(SIZE+1);
    put_value((void*)data2,data3,8193);
    char* data4 = (char*) malloc(sizeof(char)*8193);
    get_value((void*)data2,data4,8193);
    printf("%s\n",data4);


}
