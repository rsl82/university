
//
//  uniq.c
//  hw1
//
//  Created by Lee Ryan on 2021/09/14.
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

struct node{
    char* string;
    int count;
    struct node* next;
    
};

void addNode(struct node** head,char** data,int count,int* check)
{
    struct node* new=malloc(sizeof(struct node));
    if(new==NULL)
    {
        (*check)=1;
        return;
    }
    struct node* finder=*head;
    
    new->string=malloc(sizeof(char)*strlen(*data)+1);
    if((new->string)==NULL)
    {
        (*check)=1;
        return;
    }
    
    strcpy(new->string,*data);
    new->count=count;
    new->next=NULL;
    
    if(*head==NULL)
    {
        (*head)=new;
        return;
    }
    
    while(finder->next !=NULL)
        finder=finder->next;
    
    finder->next=new;

}

void traverse(struct node* head)
{
    while (head!=NULL)
    {
        printf("%d %s\n",head->count,head->string);
        head=head->next;
    }
}




int main(int argc,char * argv[])
{
    int flag=0;
    char* input;
    char* previous=malloc(sizeof(char));
    int check=0;
    if(previous==NULL)
    {
        printf("Malloc Error\n");
        exit(EXIT_FAILURE);
    }
    int count=1;
    int start=0;
    struct node* head=NULL;
    
    while(1)
    {
        input=inputS(stdin,8,&flag);
        if(flag==2 || input==NULL)
        {
            printf("Malloc Error\n");
            goto ONE;
        }
        
        if(start!=0)
        {
            if(strcmp(previous, input)==0)
            {
                count++;
            }
            else
            {
                addNode(&head, &previous, count,&check);
                if(check==1)
                {
                    printf("Malloc Error\n");
                    free(input);
                    goto ONE;
                }
                count=1;
                free(previous);
                previous=malloc(sizeof(char)*strlen(input)+1);
                if(previous==NULL)
                {   
                    printf("Malloc Error\n");
                    free(input);
                    goto ONE;
                }
                strcpy(previous,input);
            }
        }
        else
        {
            previous=realloc(previous,sizeof(char)*strlen(input)+1);
            if(previous==NULL)
            {   
                    printf("Malloc Error\n");
                    free(input);
                    goto ONE;
            }
            strcpy(previous,input);
            start=1;
        }
            
                
        if(flag==1)
        {
            free(input);
            break;
        }
            
        
        free(input);
    }
    
    traverse(head);
    
    struct node* next;

    ONE:

    while(head !=NULL)
    {
        next=head->next;
        free(head->string);
        free(head);
        head=next;
    }
    
    if(previous)
        free(previous);
    return 0;
    
}
