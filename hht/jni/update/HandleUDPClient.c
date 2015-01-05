#include <stdio.h>
#include <stdio.h>
#include "msg_server.h"
#include <stdio.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
//#include <wait.h>
#include <errno.h>
#include "msg_server.h"


void HandleUDPClient(int udp_sock)
{
    struct sockaddr_in cliaddr;
    uint32_t len;
    int recvMsgSize = 0;

    MsgBCPacket bc_packet;

    len = sizeof(cliaddr);
    if((recvMsgSize = recvfrom(udp_sock,&bc_packet,sizeof(MsgBCPacket),0,(struct sockaddr
                        *)&cliaddr,&len)) != sizeof(MsgBCPacket))
    {
        fprintf(stderr,"recv msg error! \n");
        exit(1);
    }


#ifdef DEBUG
    printf("broadcast length is %d\n",bc_packet.packet_len);
    printf("broadcast type is %0x\n",bc_packet.msg_type);
#endif

    printf("Handling UDP client %s\n",inet_ntoa(cliaddr.sin_addr));

    if(!bc_packet.msg_type&0xf)
    {
        bc_packet.msg_type = ACK;

#ifdef DEBUG
        printf("bc_packet's msg_type is: %0x\n",bc_packet.msg_type);
#endif
        if(sendto(udp_sock,&bc_packet,sizeof(bc_packet),0,(struct sockaddr
                        *)&cliaddr,sizeof(cliaddr)) != sizeof(bc_packet))
        {
            fprintf(stderr,"sendto() sent a different number of bytes than \
                    expected\n");
            exit(1);
        }
    }

}
