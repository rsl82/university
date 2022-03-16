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
/*
struct node
{
    int space;
    struct node* next;
    struct node* child;
    char* string;
};

int insert(struct node** head,int space,char* string,int check)
{
    struct node* new=malloc(sizeof(struct node));
    if(new==NULL)
    {
        printf("Malloc Error\n");
        return 1;
    }

    new->space=space;
    new->child=NULL;
    new->next=NULL;
    new->string=malloc(sizeof(char)*strlen(string)+1);
    strcpy(new->string,string);


    if(check==0)
    {
        (*head)->child=new;
    }
    else
    {
        struct node* find=(*head)->child;
        struct node* finde  r=find;

         if(find==NULL || strcasecmp(find->string, new->string) >0)
        {
            new->next=find;
            find=new;
            return 0;
        }
    
        while(finder->next !=NULL && strcasecmp(finder->next->string, new->string) <=0)
            finder=finder->next;
    
        new->next=finder->next;
        finder->next=new;

    }

    return 0;

}



void tree(char* path,int space,struct node** head)
{
    DIR* dirp= opendir(path);
    struct dirent* direc;
    int wild=0;
    char* repath=malloc(sizeof(char)*strlen(path)+1);
    if(repath==NULL)
    {
        printf("MALLOC ERROR\n");
        return;
    }

    struct stat pathS;
    errno=0;

    if(!dirp)
    {
        return;
    }

    while((direc = readdir(dirp)) != NULL )
    {
        

        stat(direc->d_name,&pathS);

        if(strcmp(direc->d_name,".") != 0 && strcmp(direc->d_name,"..") != 0)
        {
            if(wild==0)
            {
                int x=insert(head,space,direc->d_name,wild);
                if(x==1)
                {
                     free(repath);
                     closedir(dirp); 
                     return;
                     wild++;
                }
            }
            else{
                int x=insert(head,space,direc->d_name,1);
                if(x==1)
                {
                     free(repath);
                     closedir(dirp); 
                     return;
                     wild++;
                }
            }
            if(S_ISDIR(pathS.st_mode))
                {
                    repath=realloc(repath,sizeof(char)*(strlen(path)+strlen(direc->d_name))+3);
                    if(repath==NULL)
                    {
                        printf("MALLOC ERROR\n");
                        return;
                    }
                    strcpy(repath,path);
                    strcat(repath,"/");
                    strcat(repath,direc->d_name);
                    tree(repath,space+2,head);
                }
        }

        

        if(errno)
        {
        //printf("%d %s ERROR\n",errno,direc->d_name);

        }

    }
    
    free(repath);
    closedir(dirp);
}

void traverseTree(struct node * head) 
{ 
    if (head == NULL) 
        return; 

    while (head) 
    { 
        if((head->string))
        {
            for(int i=1;i<head->space;i++)
                printf(" ");
            printf("- %s\n",head->string);
        }
        if (head->child) 
            traverseTree(head->child); 
        head= head->next; 
    } 
} 
*/

void tree(char* path,int count)
{
    struct dirent **namelist;
   int n;
   int i=0;
   n= scandir(path, &namelist,NULL,alphasort);
    char* repath=malloc(sizeof(char)*strlen(path)+3);
    
   
    
    struct stat pathS;



   if(n<0)
        perror("scandir");
    else{
        while(i<n)
        {
            if(namelist[i]->d_name[0]!='.')
            {
                char* path1=malloc(sizeof(char)*(strlen(path))+1000);
                strcpy(path1,path);
                strcat(path1,"/");
                strcat(path1,namelist[i]->d_name);
                stat(path1,&pathS);
                for(int i=0;i<count;i++)
                    printf(" ");
                printf("- %s\n",namelist[i]->d_name);
                    //printf("direc: %s\n",path);
                if(S_ISDIR(pathS.st_mode))
                {
                    repath=realloc(repath,sizeof(char)*(strlen(path)+strlen(namelist[i]->d_name))+3);
                    strcpy(repath,path);
                    strcat(repath,"/");
                    strcat(repath,namelist[i]->d_name);
                    tree(repath,count+2);
                
                
                }
                
                free(path1);
            }
            
            
            free(namelist[i]);
            i++;
        }
        free(namelist);
    }
    free(repath);
}


int main(int argc, char* argv[])
{
    /*
    char* path=".";
    printf("%s\n",path);
    struct node* head;
    head->space=1;
    head->next=NULL;
    head->child=NULL;
    head->string=".";

    tree(path,1,&head);
    traverseTree(head->child);
    */
   printf(".\n");
    tree(".",0);
   


    exit(EXIT_SUCCESS);
}
