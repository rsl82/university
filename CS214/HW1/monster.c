//
//  monster.c
//  hw1
//
//  Created by Lee Ryan on 2021/09/19.
//


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>


struct node
{
    char a;
    int x;
    int y;
    int check;
    struct node* next;
};


void addNode(struct node** head,int x,int y,int* check)
{
    struct node* new=malloc(sizeof(struct node));
    if(new==NULL)
    {
        (*check)=1;
        return;
    }
    struct node* finder=*head;
    
    
    new->a='.';
    new->x=x;
    new->y=y;
    new->check=0;
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


void printboard(struct node* head,int a,int b)
{
    for(int x=0;x<b;x++)
    {
        for(int y=0;y<a;y++)
        {
            if(head->x==a-1)
                printf("%c",head->a);
            else
                printf("%c ",head->a);
            head=head->next;
        }
        
        printf("\n");
    }
}

/*
 check 1 -> monster and goal
 2 -> monster and player
 3 -> player and goal
 */

int plrMove(struct node** head,char a)
{
    struct node* cur=*head;
    int x,y;
    while(cur->a!='P')
        cur=cur->next;
    x=cur->x;
    y=cur->y;
    
    cur->a='.';
    if(a=='E')
        x++;
    else if(a=='W')
        x--;
    else if(a=='S')
        y--;
    else if(a=='N')
        y++;
    
    cur=*head;
    
    while(cur->x!=x || cur->y!=y)
    {
        cur=cur->next;
    }
    
    if(cur->a=='M')
        return 2;
    else if(cur->a=='G')
        return 3;
    else
        cur->a='P';
    
    return 0;
}

int monMove(struct node** head)
{
    struct node* plr=*head;
    while(plr->a!='P')
        plr=plr->next;
    struct node* mon=*head;
    while(mon->a!='M')
        mon=mon->next;
    
    int pathx=mon->x - plr->x;
    int pathy=mon->y - plr->y;
    int monx=mon->x;
    int mony=mon->y;
    
    if(abs(pathx)>abs(pathy))
    {
        if(pathx<0)
        {
            monx++;
            printf("monster moves E\n");
        }
        else{
            monx--;
            printf("monster moves W\n");
        }
            
    }
    else
    {
        if(pathy<0)
        {
            mony++;
            printf("monster moves N\n");
        }
            
        else
        {
            mony--;
            printf("monster moves S\n");
        }
            
    }
    
    if(mon->check==1)
    {
        mon->a='G';
        mon->check=0;
    }
    else
        mon->a='.';
    
    mon=*head;
    
    while(mon->x!=monx || mon->y!=mony)
    {
        mon=mon->next;
    }
    
    if(mon->a=='G')
    {
        mon->check=1;
        mon->a='M';
    }
    else if(mon->a=='P')
        return 2;
    else
        mon->a='M';
    
    return 0;
}

int main(int argc,char* argv[])
{
    struct node* head =NULL;
    int boardx=atoi(argv[1]);
    int boardy=atoi(argv[2]);
    int malloccheck=0;
    struct node* next;
    
    for(int j=boardy-1;j>-1;j--)
    {
        for(int i=0;i<boardx;i++)
        {
            addNode(&head, i, j,&malloccheck);
            if(malloccheck==1)
            {
                printf("Malloc Error\n");
                goto ONE;
            }
        }
        
    }
    
    //plrset
    int plrx=atoi(argv[3]);
    int plry=atoi(argv[4]);
    
    struct node* cur=head;
    while(cur->x!=plrx || cur->y!=plry)
    {
        cur=cur->next;
    }
    cur->a='P';
    
    //goalset
    int goalx=atoi(argv[5]);
    int goaly=atoi(argv[6]);
    
    cur=head;
    while(cur->x!=goalx || cur->y!=goaly)
    {
        cur=cur->next;
    }
    cur->a='G';
    
    //monsset
    int monx=atoi(argv[7]);
    int mony=atoi(argv[8]);
    cur=head;
    while(cur->x!=monx || cur->y!=mony)
    {
        cur=cur->next;
    }
    if(cur->a=='G')
        cur->check=1;
    cur->a='M';
    
    printboard(head,boardx,boardy);
    
    int checker=0;
    char move;
    

    while (checker!=2 && checker!=3) {
        cur=head;
        while(cur->a!='P')
            cur=cur->next;
        
        while(1)
        {
            scanf(" %c",&move);
            
            if(move!='E' && move!='W' && move!='S' && move!= 'N')
            {
                printf("invalid move\n");
            }
            else if((cur->x==0 && move=='W') || (cur->x==boardx-1 && move=='E') || (cur->y==0 && move=='S') || (cur->y==boardy-1 && move=='N'))
                printf("invalid move\n");
            
            else
                break;
        }
        
        checker=plrMove(&head,move);
        if(checker==2)
        {
            printf("monster wins!\n");
            break;
        }
        else if(checker==3)
        {
            printf("player wins!\n");
            break;
        }
        
        checker=monMove(&head);
        if(checker==2)
        {
            printf("monster wins!\n");
            break;
        }
        
        
        printboard(head,boardx,boardy);
    }
    
    ONE:

    
    while(head !=NULL)
    {
        next=head->next;
        free(head);
        head=next;
    }
    
    
    
    
    return 0;
}
