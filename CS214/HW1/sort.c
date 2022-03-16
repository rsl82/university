//
//  sort.c
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

struct node
{
    char* string;
    int num;
    struct node* next;
};



void addString(struct node** head,char* data,int* flag)
{
    struct node* new=malloc(sizeof(struct node));
    if(new==NULL)
    {
        (*flag)=2;
        return;
    }
    struct node* finder=*head;
    
    
    new->string=malloc(sizeof(char)*strlen(data)+1);
    if((new->string)==NULL)
    {
        (*flag)=2;
        return;
    }
    new->num=0;
    strcpy(new->string,data);
    new->next=NULL;
    
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

void addNum(struct node** head,int data,int* flag)
{
    struct node* new=malloc(sizeof(struct node));
    if(new==NULL)
    {
        (*flag)=2;
        return;
    }
    struct node* finder=*head;
    
    new->string=NULL;
    new->num=data;
    new->next=NULL;
    
    if(*head==NULL || (*head)->num > new->num)
    {
        new->next=*head;
        (*head)=new;
        return;
    }
    
    while(finder->next !=NULL && finder->next->num <= new->num)
        finder=finder->next;
    
    new->next=finder->next;
    finder->next=new;
    
}



int main(int argc, char* argv[])
{
    struct node* head=NULL;
    char* input;
    int flag=0;
    int check=0;
    int flag2=0;
    struct node* next;
    
    if(argc==2 && strcmp(argv[1], "-n") == 0)
    {
        check=1;
    }
    
    while(1)
    {
        input=inputS(stdin,8,&flag);
        if(flag==1)
        {
            free(input);
            break;
        }
        if(flag==2 || input==NULL)
        {
            printf("Malloc Error\n");
            goto ONE;
        }
        
        if(check==0)
        {
            addString(&head, input,&flag2);
        }
        else if(check==1)
        {
            addNum(&head, atoi(input),&flag2);
        }
        if(flag2==2)
        {
            printf("Malloc Error\n");
            free(input);
            goto ONE;

        }
        
        free(input);
        
    }
    
    struct node* traverse=head;
    
    if(check==0)
    {
        while (traverse!=NULL)
        {
            printf("%s\n",traverse->string);
            traverse=traverse->next;
        }
        
    }
    else if(check==1)
    {
        while (traverse!=NULL)
        {
            printf("%d\n",traverse->num);
            traverse=traverse->next;
        }
    }

    ONE:
    
    while(head !=NULL)
    {
        next=head->next;
        if(check==0)
            free(head->string);
        free(head);
        head=next;
    }
    
    return 0;
    
}
