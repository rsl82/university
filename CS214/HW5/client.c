#include "csapp.h"


TTF_Font* font;

typedef struct game{
TILETYPE grid[GRIDSIZE][GRIDSIZE];
Position playerPosition[4];
int score;
int level;
int numTomatoes;
int plr;
bool shouldExit ;

}game;

game newgame;

int hit=-1;

void initSDL()
{
    if (SDL_Init(SDL_INIT_VIDEO) < 0) {
        fprintf(stderr, "Error initializing SDL: %s\n", SDL_GetError());
        exit(EXIT_FAILURE);
    }

    int rv = IMG_Init(IMG_INIT_PNG);
    if ((rv & IMG_INIT_PNG) != IMG_INIT_PNG) {
        fprintf(stderr, "Error initializing IMG: %s\n", IMG_GetError());
        exit(EXIT_FAILURE);
    }

    if (TTF_Init() == -1) {
        fprintf(stderr, "Error initializing TTF: %s\n", TTF_GetError());
        exit(EXIT_FAILURE);
    }
}



void handleKeyDown(SDL_KeyboardEvent* event,int clientfd)
{
    // ignore repeat events if key is held down
    if (event->repeat)
        return;
    
    int hi;
   

    if (event->keysym.scancode == SDL_SCANCODE_Q || event->keysym.scancode == SDL_SCANCODE_ESCAPE)
    {
    	hi=0;
    	write(clientfd,&hi,sizeof(int));
    }
        
    else if (event->keysym.scancode == SDL_SCANCODE_UP || event->keysym.scancode == SDL_SCANCODE_W)
    {
    	hi=1;
    	write(clientfd,&hi,sizeof(int));
    }

    else if (event->keysym.scancode == SDL_SCANCODE_DOWN || event->keysym.scancode == SDL_SCANCODE_S)
    {
    	hi=2;
    	write(clientfd,&hi,sizeof(int));
    }

    else if (event->keysym.scancode == SDL_SCANCODE_LEFT || event->keysym.scancode == SDL_SCANCODE_A)
    {
    	hi=3;
    	write(clientfd,&hi,sizeof(int));
    }

    else if (event->keysym.scancode == SDL_SCANCODE_RIGHT || event->keysym.scancode == SDL_SCANCODE_D)
    {
    	hi=4;
    	write(clientfd,&hi,sizeof(int));
    }	 
   
}


void processInputs(int clientfd)
{
	SDL_Event event;
	
	while (SDL_PollEvent(&event)) {
	
		switch (event.type) {
			case SDL_QUIT:
				newgame.shouldExit = true;
				break;

            case SDL_KEYDOWN:
                handleKeyDown(&event.key,clientfd);
				break;

			default:
				break;
		}
	}
}


void drawGrid(SDL_Renderer* renderer, SDL_Texture* grassTexture, SDL_Texture* tomatoTexture, SDL_Texture* playerTexture)
{
    SDL_Rect dest;
    
    for (int i = 0; i < GRIDSIZE; i++) {
        for (int j = 0; j < GRIDSIZE; j++) {
            dest.x = 64 * i;
            dest.y = 64 * j + HEADER_HEIGHT;
            SDL_Texture* texture = (newgame.grid[i][j] == TILE_GRASS) ? grassTexture : tomatoTexture;
            SDL_QueryTexture(texture, NULL, NULL, &dest.w, &dest.h);
            SDL_RenderCopy(renderer, texture, NULL, &dest);
        }
    }

    for(int i = 0; i<newgame.plr;i++)
    {
        dest.x = 64 * newgame.playerPosition[i].x;
        dest.y = 64 * newgame.playerPosition[i].y + HEADER_HEIGHT;
        SDL_QueryTexture(playerTexture, NULL, NULL, &dest.w, &dest.h);
        SDL_RenderCopy(renderer, playerTexture, NULL, &dest);
    }
    
}

void drawUI(SDL_Renderer* renderer)
{
    // largest score/level supported is 2147483647
    char scoreStr[18];
    char levelStr[18];
    sprintf(scoreStr, "Score: %d", newgame.score);
    sprintf(levelStr, "Level: %d", newgame.level);

    SDL_Color white = {255, 255, 255};
    SDL_Surface* scoreSurface = TTF_RenderText_Solid(font, scoreStr, white);
    SDL_Texture* scoreTexture = SDL_CreateTextureFromSurface(renderer, scoreSurface);

    SDL_Surface* levelSurface = TTF_RenderText_Solid(font, levelStr, white);
    SDL_Texture* levelTexture = SDL_CreateTextureFromSurface(renderer, levelSurface);

    SDL_Rect scoreDest;
    TTF_SizeText(font, scoreStr, &scoreDest.w, &scoreDest.h);
    scoreDest.x = 0;
    scoreDest.y = 0;

    SDL_Rect levelDest;
    TTF_SizeText(font, levelStr, &levelDest.w, &levelDest.h);
    levelDest.x = GRID_DRAW_WIDTH - levelDest.w;
    levelDest.y = 0;

    SDL_RenderCopy(renderer, scoreTexture, NULL, &scoreDest);
    SDL_RenderCopy(renderer, levelTexture, NULL, &levelDest);

    SDL_FreeSurface(scoreSurface);
    SDL_DestroyTexture(scoreTexture);

    SDL_FreeSurface(levelSurface);
    SDL_DestroyTexture(levelTexture);
}

int main(int argc, char* argv[])
{
    
    initSDL();

    int clientfd;
    char *host, *port;
    
    if (argc != 3) {
	fprintf(stderr, "usage: %s <host> <port>\n", argv[0]);
	exit(0);
    }
    host = argv[1];
    port = argv[2];

    clientfd = Open_clientfd(host, port);

    read(clientfd,&newgame,sizeof(game));
    
    //printf("%d\n",plr);

    font = TTF_OpenFont("resources/Burbank-Big-Condensed-Bold-Font.otf", HEADER_HEIGHT);
    if (font == NULL) {
        fprintf(stderr, "Error loading font: %s\n", TTF_GetError());
        exit(EXIT_FAILURE);
    }

    SDL_Window* window = SDL_CreateWindow("Client", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, WINDOW_WIDTH, WINDOW_HEIGHT, 0);

    if (window == NULL) {
        fprintf(stderr, "Error creating app window: %s\n", SDL_GetError());
        exit(EXIT_FAILURE);
    }

    SDL_Renderer* renderer = SDL_CreateRenderer(window, -1, 0);

	if (renderer == NULL)
	{
		fprintf(stderr, "Error creating renderer: %s\n", SDL_GetError());
        exit(EXIT_FAILURE);
	}

    SDL_Texture *grassTexture = IMG_LoadTexture(renderer, "resources/grass.png");
    SDL_Texture *tomatoTexture = IMG_LoadTexture(renderer, "resources/tomato.png");
    SDL_Texture *playerTexture = IMG_LoadTexture(renderer, "resources/player.png");

    // main game loop
    while (!newgame.shouldExit) {
        SDL_SetRenderDrawColor(renderer, 0, 105, 6, 255);
        SDL_RenderClear(renderer);    

        drawGrid(renderer, grassTexture, tomatoTexture, playerTexture);
        drawUI(renderer);

        processInputs(clientfd);
        SDL_RenderPresent(renderer);
        
        write(clientfd,&hit,sizeof(int));
       
        read(clientfd,&newgame,sizeof(game));
	
        SDL_Delay(16); // 16 ms delay to limit display to 60 fps
    }

    Close(clientfd);
    // clean up everything
    SDL_DestroyTexture(grassTexture);
    SDL_DestroyTexture(tomatoTexture);
    SDL_DestroyTexture(playerTexture);

    TTF_CloseFont(font);
    TTF_Quit();

    IMG_Quit();

    SDL_DestroyRenderer(renderer);
    SDL_DestroyWindow(window);
    SDL_Quit();
}
