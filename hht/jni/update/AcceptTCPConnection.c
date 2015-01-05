#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>

int AcceptTCPConnection(int servSock)
{
    int clntSock;
    struct sockaddr_in echoClntAddr;
    unsigned int clntLen;

    clntLen = sizeof(echoClntAddr);

    if((clntSock = accept(servSock,(struct sockaddr *)&echoClntAddr,&clntLen)) < 0)
    {
        printf("accept sock error! \t\n");
    }

    return clntSock;
}
