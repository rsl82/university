//include headers
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
#include <signal.h>
#include <time.h>
#include <sys/wait.h>

typedef struct job
{
    pid_t pid;  // Actually Group ID
    int state;  //Fore = 0 | Back = 1 | Stop = 2
    char argv[2000]; //Process name
}job;

//Global variables
job* jobs;
pid_t shellId;
int loop = 1;
int jobsize = 200;
static int signal_number = 0;
int signal_pid= 0 ;
int signal_order = 0;
int count=0;


//Prototypes
void shellExit();
void fg(char**);
void bg(char**);
void shellKill(char**);

//initalize jobs
void nullJob(job* job)
{
    job->pid=-1;
    job->state=-1;
    strcpy(job->argv,"");
}
void initJobs()
{
    jobs = malloc(jobsize*sizeof(struct job));
    if(jobs == NULL)
    {
        printf("Malloc Error\n");
        exit(EXIT_FAILURE);
    }
    for(int i=0;i <jobsize; i++)
        nullJob(&jobs[i]);
}
//addJob
void addJob(int state,pid_t pid,char** shellArg)
{
    for(int i=0;i<jobsize;i++)
    {
        if(jobs[i].state == -1)
        {
            jobs[i].state=state;
            jobs[i].pid=pid;
            for(int j=0;shellArg[j]!=NULL;j++)
            {
                strcat(jobs[i].argv,shellArg[j]);
                if(shellArg[j+1]!=NULL)
                    strcat(jobs[i].argv," ");
            }
            break;
        }
        if(i==jobsize-1)
        {
            jobsize=jobsize*2;
            jobs=realloc(jobs,jobsize*sizeof(struct job));
            for(int x=i+1;x<jobsize;x++)
                nullJob(&jobs[x]);
        }
    }
}
//printJob
void printJob()
{
    for(int i=0;i<jobsize;i++)
    {
        if(jobs[i].state!=-1)
        {
            printf("[%d] %d ",i+1,jobs[i].pid);
            if(jobs[i].state == 1 || jobs[i].state == 0)
                printf("Running ");
            else
                printf("Stopped ");
            printf("%s",jobs[i].argv);
            if(jobs[i].state == 1)
                printf(" &");
            printf("\n");
        }
    }
}

//Get input
char *inputS(FILE* fp, int size)
{
    char *input=NULL;
    int len=0;
    input = malloc(sizeof(*input)*size);
    if(input==NULL)
    {
        printf("Malloc Error\n");

        exit(EXIT_FAILURE);
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
                printf("Malloc Error\n");

                exit(EXIT_FAILURE);
            }
        }
    }
    
    if(x==EOF)
        shellExit();
    
    input[len]='\0';
    len++;
    
    return realloc(input,sizeof(*input)*len);
}
//parsing input
char** parse(char* input)
{
    int size = 256;
    char** shellArg = malloc(size*sizeof(char*));
    char* token;
    int x=0;

    if(shellArg == NULL)
    {
        printf("Malloc Error\n");
        exit(EXIT_FAILURE);
    }

    token=strtok(input," ");
    while(token != NULL)
    {
        shellArg[x] = malloc(strlen(token)*sizeof(char)+2);
        strcpy(shellArg[x++],token);
        if(x >= (size-1))
        {
            size = size * 2;
            shellArg = realloc(shellArg,size);
            if(shellArg == NULL)
            {
                printf("Malloc Error\n");
                exit(EXIT_FAILURE);
            }
        
        }
        
        token = strtok(NULL," ");

    }
    
    shellArg[x]=NULL;
    return shellArg;

}
//print shellArg for test
void printArg(char** shellArg)
{
    for(int x=0;shellArg[x]!=NULL;x++)
    {
        printf("%d : %s\n",x,shellArg[x]);
    }
}
//Call ShellExit
void shellExit()
{
    loop = 0;
    for(int i=0;i<jobsize;i++)
    {
        if(jobs[i].state==0 || jobs[i].state==1)
        {
            kill(-(jobs[i].pid), SIGHUP);
            count++;
        }
        else if(jobs[i].state == 2)
        {
            kill(-(jobs[i].pid), SIGHUP);
            kill(-(jobs[i].pid), SIGCONT);
            
            count++;
           
        }
    }
}
//shellCd
void shellcd(char** shellArg)
{
    char path[5000];
    if(shellArg[1] == NULL)
    {

        chdir(getenv("HOME"));
        setenv("PWD",getenv("HOME"),1);
        //printf("%s",getenv("PWD"));
        return;
    }
    if(chdir(shellArg[1]) == 0)
    {
        setenv("PWD",getcwd(path,5000),1);
        //printf("%s",getenv("PWD"));
        return;
    }
    else
    {
        printf("cd: No such file or directory\n");
        return;
    }

}
//Determine if it is Built Function
int ifBuilt(char** shellArg)
{
    if(strcmp(shellArg[0],"exit")==0)
        shellExit();
    else if(strcmp(shellArg[0],"cd")==0)
        shellcd(shellArg);
    else if(strcmp(shellArg[0],"jobs")==0)
        printJob();
    else if(strcmp(shellArg[0],"fg")==0)
        fg(shellArg);
    else if(strcmp(shellArg[0],"bg")==0)
        bg(shellArg);
    else if(strcmp(shellArg[0],"kill")==0)
        shellKill(shellArg);
    else
        return 0;
    

    return 1;
}
//Find job number
int findJn(pid_t pid)
{   
    for(int i=0;i<jobsize;i++)
    {
        if(jobs[i].pid==pid)
        {
            return i;
        }
    }

    return -1;
}
//fg
void fg(char** shellArg)
{
    int i=atoi(&(shellArg[1][1]));
    if(jobs[i-1].state== 1 || jobs[i-1].state == 2)
    {
        kill(-(jobs[i-1].pid),SIGCONT);
        jobs[i-1].state=0;
        pause();
    }
    

}
//bg
void bg(char** shellArg)
{
    int i=atoi(&(shellArg[1][1]));
    if(jobs[i-1].state == 2)
    {
        kill(-(jobs[i-1].pid),SIGCONT);
        jobs[i-1].state=1;
    }
}
//kill
void shellKill(char** shellArg)
{
    int i=atoi(&(shellArg[1][1]));
    if(jobs[i-1].state != -1)
    {
        kill(-(jobs[i-1].pid),SIGTERM);
        printf("[%d] %d terminated by signal %d\n",i,jobs[i-1].pid,SIGTERM);
        nullJob(&(jobs[i-1]));
    }

}
//Execution
void execution(char** shellArg,int isback)
{
    pid_t pid;
    //int status;
    sigset_t mask,prev_mask;
    sigemptyset(&mask);
    sigaddset(&mask,SIGCHLD);
    char* str = malloc(sizeof(char)*strlen(shellArg[0])+20);
    


    sigprocmask(SIG_BLOCK,&mask,&prev_mask);

    if((pid=fork()) < 0)
    {
        printf("Fork Error\n");
        exit(EXIT_FAILURE);
    }

    else if(pid == 0)
    {
        setpgid(0,0);
        sigprocmask(SIG_SETMASK,&prev_mask,NULL);
        if(strchr(shellArg[0],'/')==0)
        {   
            strcpy(str,"/usr/bin/");
            strcat(str,shellArg[0]);
            //printf("%s\n",str);
            if(access(str,X_OK) == 0)
                execve(str,shellArg,NULL);
            strcpy(str,"/bin/");
            strcat(str,shellArg[0]);
            //printf("%s\n",str);
            if(access(str,X_OK) == 0)
                execve(str,shellArg,NULL);
            printf("%s: command not found\n",shellArg[0]);
            free(str);
            exit(EXIT_FAILURE);

        }
        else if(execve(shellArg[0],shellArg,NULL)<0)
        {
            perror(shellArg[0]);
            exit(EXIT_FAILURE);
        }
    }
    
    setpgid(pid,pid);
    if(isback!=1)
    {
        addJob(0,pid,shellArg);
        sigprocmask(SIG_SETMASK,&prev_mask,NULL);
        //waitpid(pid,&status,0);
        pause();
    }
    else
    {
        addJob(1,pid,shellArg);
        sigprocmask(SIG_SETMASK,&prev_mask,NULL);
        printf("[%d] %d\n",findJn(pid)+1,pid);

    }
    
    if(signal_number!=0)
        {
            printf("[%d] %d terminated by signal %d\n",signal_order,signal_pid,signal_number);
            nullJob(&jobs[signal_order-1]);
            signal_number=0;
            signal_order=0;
            signal_pid=0;
        }
    
    free(str);
}
//check &
int checkbg(char** shellArg)
{
    int tmp = 0;

    for(int i=0;shellArg[i+1] != NULL; i++)
        tmp ++ ;
    if(strcmp(shellArg[tmp],"&") == 0)
    {
        free(shellArg[tmp]);
        shellArg[tmp] = NULL;
        return 1;
    }

    else if(strchr(shellArg[tmp],'&') != NULL)
    {
        shellArg[tmp][strlen(shellArg[tmp])-1]='\0';
        return 1;
    }
    

    return 0;
}
//sigInt
void sigint_handler(int sig)
{
    int a = -1;
    for(int i=0;i<jobsize;i++)
    {
        if(jobs[i].state == 0)
        {
            a = i;
            break;
        }      
    }

    if(a!=-1)
    {
        kill(-(jobs[a].pid), SIGINT);
        signal_number=2;
        signal_pid = jobs[a].pid;
        signal_order = a+1;
        write(1,"\n",1);
        //nullJob(&jobs[a]);
        //write(1,"\nTerminated\n",12);

    }

    

}
//sigtstp
void sigtstp_handler(int sig)
{
    int a = -1;
    for(int i=0;i<jobsize;i++)
    {
        if(jobs[i].state == 0)
        {
            a = i;
            break;
        }      
    }

    if(a!=-1)
    {
        kill(-(jobs[a].pid), SIGTSTP);
        kill(-(shellId),SIGCONT);
        jobs[a].state = 2;
        write(1,"\n",1);

    } 
}

//sigchld
void sigchld_handler(int sig)
{
    int status;
    pid_t pid;
    int a=-1;

    while((pid = waitpid(-1,&status,WNOHANG | WUNTRACED)) > 0 )
    {
        for(int i=0;i<jobsize;i++)
        {
            if(jobs[i].pid==pid)
            {
                a=i;
                break;
            }
        }

        if(a!=-1)
        {
            if(WIFSIGNALED(status))
        {
            signal_number=WTERMSIG(status);
            signal_order=a+1;
            signal_pid=jobs[a].pid;
            //nullJob(&jobs[a]);
        }
        else if(WIFEXITED(status))
            nullJob(&jobs[a]);
        else if(WIFSTOPPED(status))
            jobs[a].state = 2; 
        }
        

    }
}

int main(int argc, char** argv)
{
    //local variables
    char* input;
    char** shellArg;
    int bg=0;

    

    signal(SIGINT,sigint_handler);
    signal(SIGTSTP,sigtstp_handler);
    signal(SIGCHLD,sigchld_handler);
    initJobs();
    shellId = getpid(); //Shell ID
    

    while(loop == 1)
    {
        if(signal_number!=0)
        {
            printf("[%d] %d terminated by signal %d\n",signal_order,signal_pid,signal_number);
            nullJob(&jobs[signal_order-1]);
            signal_number=0;
            signal_order=0;
            signal_pid=0;
        }

        
        printf("> ");
        input=inputS(stdin,256);
        if(input==NULL)
        {
            printf("Malloc Error\n");
            exit(EXIT_FAILURE);
        }


        if(strlen(input)!=0)
        {
            shellArg = parse(input);
        
            if(shellArg[0]!=NULL)
            {
                bg = checkbg(shellArg);
                if(ifBuilt(shellArg) == 0)
                {
                    execution(shellArg,bg);
                }
            }
           
            //free shellArg
            if(shellArg[0]!=NULL)
            {
                for(int i=0;shellArg[i]!=NULL;i++)
                free(shellArg[i]);
            }
            free(shellArg);
        }


        if(signal_number!=0)
        {
            printf("[%d] %d terminated by signal %d\n",signal_order,signal_pid,signal_number);
            nullJob(&jobs[signal_order-1]);
            signal_number=0;
            signal_order=0;
            signal_pid=0;
        }
        
        free(input);
    }

    //need to freejob here
    if(count!=0) pause();
    free(jobs);

    exit(EXIT_SUCCESS);
}
