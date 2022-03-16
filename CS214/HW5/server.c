/* 
 * echoservert.c - A concurrent echo server using threads
 */
/* $begin echoservertmain */
#include "csapp.h"

void *thread(void *vargp);


typedef struct game{

TILETYPE grid[GRIDSIZE][GRIDSIZE];
Position playerPosition[4];
int score;
int level;
int numTomatoes;
int plr ;
bool shouldExit ;

}game;

game newgame;
pthread_mutex_t lock;


// get a random value in the range [0, 1]
double rand01()
{
    return (double) rand() / (double) RAND_MAX;
}

void initGrid()
{
    for (int i = 0; i < GRIDSIZE; i++) {
        for (int j = 0; j < GRIDSIZE; j++) {
            double r = rand01();
            if (r < 0.1) {
                newgame.grid[i][j] = TILE_TOMATO;
                newgame.numTomatoes++;
            }
            else
                newgame.grid[i][j] = TILE_GRASS;
        }
    }

    // force player's position to be grass
    for(int i=0;i<4;i++)
    {
    	if (newgame.grid[newgame.playerPosition[i].x][newgame.playerPosition[i].y] == TILE_TOMATO) {
        newgame.grid[newgame.playerPosition[i].x][newgame.playerPosition[i].y] = TILE_GRASS;
        newgame.numTomatoes--;
    }

    }
    
    // ensure grid isn't empty
    while (newgame.numTomatoes == 0)
        initGrid();
}



void moveTo(int x, int y,int this)
{
    // Prevent falling off the grid
    
    if (x < 0 || x >= GRIDSIZE || y < 0 || y >= GRIDSIZE)
        return;
    for(int i=0;i<newgame.plr;i++)
    {
        if(i!=this)
        {
            if(newgame.playerPosition[i].x == x && newgame.playerPosition[i].y == y)
                return;
        }
    }
    
    pthread_mutex_lock(&lock);
    // Sanity check: player can only move to 4 adjacent squares
    if (!(abs(newgame.playerPosition[this].x - x) == 1 && abs(newgame.playerPosition[this].y - y) == 0) &&
        !(abs(newgame.playerPosition[this].x - x) == 0 && abs(newgame.playerPosition[this].y - y) == 1)) {
        fprintf(stderr, "Invalid move attempted from (%d, %d) to (%d, %d)\n", newgame.playerPosition[this].x, newgame.playerPosition[this].y, x, y);
        return;
    }

    newgame.playerPosition[this].x = x;
    newgame.playerPosition[this].y = y;
    
    if (newgame.grid[x][y] == TILE_TOMATO) {
        
        newgame.grid[x][y] = TILE_GRASS;
        newgame.score++;
        newgame.numTomatoes--;
        
        if (newgame.numTomatoes == 0) {
            
            newgame.level++;
            
            initGrid();
        }
    }

    pthread_mutex_unlock(&lock);
   

}




int main(int argc, char **argv) 
{

    srand(time(NULL));

    int listenfd, *connfdp;
    socklen_t clientlen;
    struct sockaddr_storage clientaddr;
    pthread_t tid; 
    
    newgame.score=0;
    newgame.level=1;
    newgame.numTomatoes=0;
    newgame.plr=0;
    newgame.shouldExit=false;
	
    newgame.playerPosition[0].x = 0;
    newgame.playerPosition[0].y = 0;
    
    newgame.playerPosition[1].x = 0;
    newgame.playerPosition[1].y = 9;
    
    newgame.playerPosition[2].x = 9;
    newgame.playerPosition[2].y = 0;
    
    newgame.playerPosition[3].x = 9;
    newgame.playerPosition[3].y = 9;
 
	
    initGrid();


    if (argc != 2) {
	fprintf(stderr, "usage: %s <port>\n", argv[0]);
	exit(0);
    }
    listenfd = Open_listenfd(argv[1]);

    while (1) {
    
    clientlen=sizeof(struct sockaddr_storage);
	connfdp = Malloc(sizeof(int)); //line:conc:echoservert:beginmalloc
	if(newgame.plr<4)
    {
        *connfdp = Accept(listenfd, (SA *) &clientaddr, &clientlen); //line:conc:echoservert:endmalloc
	    Pthread_create(&tid, NULL, thread, connfdp);
    }
    else
    {
        printf("4 player game\n"); 
        newgame.plr--;   
        }
          
    }

}

/* Thread routine */
void *thread(void *vargp) 
{  
    int connfd = *((int *)vargp);
    Pthread_detach(pthread_self()); //line:conc:echoservert:detach
    Free(vargp);                    //line:conc:echoservert:free
    
    pthread_mutex_lock(&lock);
    int this = newgame.plr;
    newgame.plr++;
    pthread_mutex_unlock(&lock);
    
    write(connfd,&newgame,sizeof(game));
    
    
    int buf;
    while(!newgame.shouldExit)
    {
    
        read(connfd,&buf,sizeof(int));
        
        //printf("%d\n",buf);
        if(buf==0)
            newgame.shouldExit=true;
        else if(buf==1)
            moveTo(newgame.playerPosition[this].x,newgame.playerPosition[this].y-1,this);
        else if(buf==2)
            moveTo(newgame.playerPosition[this].x,newgame.playerPosition[this].y+1,this);
        else if(buf==3)
            moveTo(newgame.playerPosition[this].x-1,newgame.playerPosition[this].y,this);
        else if(buf==4)
            moveTo(newgame.playerPosition[this].x+1,newgame.playerPosition[this].y,this);

        write(connfd,&newgame,sizeof(game));
       
    }


    Close(connfd);
    return NULL;
}
/* $end echoservertmain */
