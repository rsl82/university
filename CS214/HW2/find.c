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

void find(char* path,char* findWord)
{
    DIR* dirp= opendir(path);
    struct dirent* direc;
    
    struct stat pathS;
    
    if(!dirp)
    {
        return;
    }
        
    
    while((direc = readdir(dirp)) != NULL )
    {

	char* repath=malloc(sizeof(char)*strlen(path)+1);
    	if(repath==NULL)
    	{
        	printf("MALLOC ERROR\n");
        	return;
    	}
        stat(direc->d_name,&pathS);

        if(direc->d_name[0]!='.')
        {
            if(strlen(direc->d_name)>=strlen(findWord))
            {
                if(strstr(direc->d_name,findWord)!= NULL)
                    printf("%s/%s\n",path,direc->d_name);                 
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
                    find(repath,findWord);
                }
        }
        
      
	free(repath);
    }

    
    closedir(dirp);
}

int main(int argc, char* argv[])
{
    char* path=".";
    char* target=malloc(sizeof(char)*strlen(argv[1])+1);
    strcpy(target,argv[1]);

    find(path,target);

    free(target);
    exit(EXIT_SUCCESS);
}
