#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <errno.h>
#include <string.h>

#define MAXPENDING 5

int CreateTCPServerSocket(unsigned short port)
{
    int sock;
    struct sockaddr_in serv_addr;

    if((sock = socket(PF_INET,SOCK_STREAM,IPPROTO_TCP)) < 0)
    {
        printf("create socket error!\n");
    }

    memset(&serv_addr,0,sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(port);

    if(bind(sock,(struct sockaddr*)&serv_addr,sizeof(serv_addr)) < 0)
    {
        printf("bind sock error,Error:%s \t\n",strerror(errno));
    }

    if(listen(sock,MAXPENDING) < 0)
    {
        printf("listen the sock error,Error:%s \t\n",strerror(errno));
    }

    return 0;
}
