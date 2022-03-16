//
//  main.c
//  hw1
//
//  Created by Lee Ryan on 2021/09/14.
//

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char * argv[]) {
    
    
    int oriNum = atoi(argv[1]);
    int count=0;
    
    while(oriNum!=1)
    {
        for(int x=2;x<=oriNum;x++)
        {
            if(oriNum%x == 0)
            {
                if(count>0)
                    printf(" ");
                
                printf("%d",x);
                oriNum=oriNum/x;
                count++;
                break;
            }
        }
        
      
    }

	printf("\n");
    return 0;
}
