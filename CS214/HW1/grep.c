//
//  grep.c
//  hw1
//
//  Created by Lee Ryan on 2021/09/17.
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

char *inputS(FILE* fp, int size,int *flag)
{
    char *input=NULL;
    int len=0;
    input = malloc(sizeof(*input)*size);
    if(input==NULL)
    {
        (*flag)=2;
        return "";
    }

    
    int x;
    while((x=fgetc(fp))!=EOF && x != '\n')
    {
        input[len]=x;
        
        len++;
        if(len==size)
        {
            size=size*2;
            input=realloc(input,sizeof(*input)*size);
            if(input==NULL)
            {
                (*flag)=2;
                return "";
            }
        }
    }
    
    if(x==EOF)
        (*flag)=1;
    
    input[len]='\0';
    len++;
    
    return realloc(input,sizeof(*input)*len);
}




int main(int argc, char * argv[]){
   
    char*c = "";
    int check = 0;
    if(strcmp(argv[1], "-i") == 0){
        c = (char*)malloc(sizeof(char) * strlen(argv[2]) + 1);
        if(c == NULL)
        {
            printf("Malloc Error\n");
            exit(EXIT_FAILURE);
        }
        strcpy(c, argv[2]);
        check = 1;
    }
    else{
        c = (char*)malloc(sizeof(char) * strlen(argv[1]) + 1);
        if(c == NULL)
        {
            printf("Malloc Error\n");
            exit(EXIT_FAILURE);
        }
        strcpy(c, argv[1]);
    }
    
    
    if(check==1)
    {
        for(int i=0;i<strlen(c);i++)
            c[i]=tolower(c[i]);
    }
    int flag=0;
    
    while(1)
    {
        char* input;
        input=inputS(stdin,8,&flag);
        
        if(flag==1)
        {
        	free(input);
        	break;
        }

        if(flag==2 || input == NULL)
        {
            printf("Malloc Error\n");
            break;      
        }
        
        char *lower=malloc(sizeof(char)*strlen(input)+1);
        if(lower==NULL)
        {
            free(input);
            printf("Malloc Error\n");
            break;
        }
        strcpy(lower,input);
        if(check==1)
        {
            for(int i=0;i<strlen(lower);i++)
                lower[i]=tolower(lower[i]);
        }
        
        //compare
        if(strlen(c)>strlen(lower))
            goto ONE;
        char *find=strstr(lower,c);
        if(find!=NULL)
            printf("%s\n",input);

        
    ONE:
        free(lower);
        free(input);
    }
    
    free(c);
    return 0;
}
