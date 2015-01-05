#include <stdio.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include "msg_server.h"

#define RECVBUFFSIZE 8192

#define DEBUG 1

static void write_file(FILE* fd,char* buffer,int recvMsgSize)
{

    char *ptr = buffer;
    int bytes_write = 0;
    while(bytes_write = fwrite(ptr,sizeof(char),recvMsgSize,fd))
    {
        printf("write bytes is %d\n",bytes_write);
        if((-1 == bytes_write)&&(errno != EINTR))
        {
            break;
        }
        else if(bytes_write == recvMsgSize)
        {
            printf("write bytes is %d\n",bytes_write);
            break;
        }
        else if(bytes_write > 0)
        {
            ptr += bytes_write;
            recvMsgSize -= bytes_write;
        }

    }

}

void HandleTCPClient(int clntSocket)
{
    printf("recv client data.\n");
    char recv_buffer[RECVBUFFSIZE];
    int recvMsgSize;

    int sum_recv = 0;

    MsgTotalPacket total_packet;

    MsgSyncPacket msg_1;
    MsgSyncPacket msg_2;

    int file1_len = 0;
    int file2_len = 0;

    int sync_time1 = 0;
    int sync_time2 = 0;

    MsgSync msg_file;

    FILE *file1;
    file1 = fopen(RFILE_1,"w");
    if(file1 == NULL)
    {
        fprintf(stderr,"open file1 failed.\n");
        exit(1);
    }

    FILE *file2;
    file2 = fopen(RFILE_2,"w");
    if(file2 == NULL)
    {
        fprintf(stderr,"open file2 failed.\n");
        exit(1);
    }

    bzero(&total_packet,sizeof(MsgTotalPacket));
    if((recvMsgSize = recv(clntSocket,&total_packet,sizeof(MsgTotalPacket),0)) < 0)
    {
        fprintf(stderr,"recv total packet failed! \n");
        exit(1);
    }

#ifdef DEBUG
    printf("packet len is :%d packet num is %d.\
            \n",total_packet.packet_len,total_packet.packet_num);
#endif

    bzero(&msg_1,sizeof(msg_1));
    if((recvMsgSize = recv(clntSocket,&msg_1,sizeof(MsgSyncPacket),0)) < 0)
    {
        fprintf(stderr,"recv msg_1 failed! \n");
        exit(1);
    }

    file1_len = msg_1.msg_len;
    printf("file1_len is %d.msg_1's type is %0x.\n",file1_len,msg_1.msg_type);
    sync_time1 = msg_1.sync_time;

    bzero(&msg_2,sizeof(MsgSyncPacket));
    if((recvMsgSize = recv(clntSocket,&msg_2,sizeof(MsgSyncPacket),0)) < 0)
    {
        fprintf(stderr,"recv msg_2 failed! \n");
        exit(1);
    }

    file2_len = msg_2.msg_len;
    printf("file2_len is %d.msg_2's type is %0x.\n",file2_len,msg_2.msg_type);
    sync_time2 = msg_2.sync_time;

    int i;
    for(i=0;i<total_packet.packet_num;i++)
    {
        bzero(&msg_file,sizeof(msg_file));
        if((recvMsgSize = recv(clntSocket,&msg_file,sizeof(MsgSync),0)) < 0)
        {
            fprintf(stderr,"recv msgfile_1 failed! \n");
            exit(1);
        }

        /*recv file1 begin*/
        if(msg_file.msg_type == UPLOADBASEINFO)
        {
            int recv_size = 0;
            while(sum_recv < file1_len)
            {
                recv_size = file1_len - sum_recv;
                if(recv_size < RECVBUFFSIZE)
                {
                    bzero(&recv_buffer,sizeof(recv_size));
                    if((recvMsgSize = recv(clntSocket,&recv_buffer,recv_size,0)) < 0)
                    {
                        fprintf(stderr,"recv msgfile_1 data failed! \n");
                        exit(1);
                    }
                    printf("the remain recv_size is %d\n",recv_size);
                    printf("the remain recvMsgSize is %d\n",recvMsgSize);
                    write_file(file1,recv_buffer,recvMsgSize);
                    break;
                }
                else
                {
                    printf("the recv_size is %d\n",recv_size);
                    bzero(&recv_buffer,sizeof(recv_buffer));
                    if((recvMsgSize = recv(clntSocket,&recv_buffer,sizeof(recv_buffer),0)) < 0)
                    {
                        fprintf(stderr,"recv msgfile_1 data failed! \n");
                        exit(1);
                    }
                    sum_recv += recvMsgSize;
                    printf("the remain recvMsgSize is %d\n",recvMsgSize);
                    write_file(file1,recv_buffer,recvMsgSize);
                }
            }
        }
        else if(msg_file.msg_type == UPLOADMTINFO)
        {
            int recv_size = 0;
            while(sum_recv < file2_len)
            {
                recv_size = file2_len - sum_recv;
                if(recv_size < RECVBUFFSIZE)
                {
                    bzero(&recv_buffer,sizeof(recv_size));
                    if((recvMsgSize = recv(clntSocket,&recv_buffer,recv_size,0)) < 0)
                    {
                        fprintf(stderr,"recv msgfile_1 data failed! \n");
                        exit(1);
                    }
                    write_file(file2,recv_buffer,recvMsgSize);
                    break;
                }
                else
                {
                    bzero(&recv_buffer,sizeof(recv_buffer));
                    if((recvMsgSize = recv(clntSocket,&recv_buffer,sizeof(recv_buffer),0)) < 0)
                    {
                        fprintf(stderr,"recv msgfile_1 data failed! \n");
                        exit(1);
                    }
                    sum_recv += recvMsgSize;
                    write_file(file2,recv_buffer,recvMsgSize);
                }
            }
        }
    }
    fclose(file1);
    fclose(file2);
    close(clntSocket);
}


#if 0
            if(file1_len <= RECVBUFFSIZE)
            {
                recv_size = file1_len;
                printf("recv size is %d.\n",recv_size);
                bzero(&recv_buffer,sizeof(recv_size));
                if((recvMsgSize = recv(clntSocket,&recv_buffer,recv_size,0)) < 0)
                {
                    fprintf(stderr,"recv msgfile_1 data failed! \n");
                    exit(1);
                }
                write_file(file1,recv_buffer,recvMsgSize);
                continue;
            }
            else
            {
                recv_size = sizeof(recv_buffer);
                bzero(&recv_buffer,sizeof(recv_size));
                if((recvMsgSize = recv(clntSocket,&recv_buffer,recv_size,0)) < 0)
                {
                    fprintf(stderr,"recv msgfile_1 data failed! \n");
                    exit(1);
                }
                write_file(file1,recv_buffer,recvMsgSize);
                sum_recv += recvMsgSize;
                while(sum_recv < file1_len)
                {
                    recv_size = file1_len - sum_recv;
                    if(recv_size < RECVBUFFSIZE)
                    {
                        bzero(&recv_buffer,sizeof(recv_size));
                        if((recvMsgSize = recv(clntSocket,&recv_buffer,recv_size,0)) < 0)
                        {
                            fprintf(stderr,"recv msgfile_1 data failed! \n");
                            exit(1);
                        }
                        write_file(file1,recv_buffer,recvMsgSize);
                        break;
                    }
                    else
                    {
                        bzero(&recv_buffer,sizeof(recv_buffer));
                        if((recvMsgSize = recv(clntSocket,&recv_buffer,sizeof(recv_buffer),0)) < 0)
                        {
                            fprintf(stderr,"recv msgfile_1 data failed! \n");
                            exit(1);
                        }
                        sum_recv += recvMsgSize;
                        write_file(file1,recv_buffer,recvMsgSize);
                    }
                }
            }
        }
        else if(msg_file.msg_type == UPLOADMTINFO)
        {
            int recv_size = 0;
            if(file2_len <= RECVBUFFSIZE)
            {
                recv_size = file2_len;
                printf("recv size is %d.\n",recv_size);
                bzero(&recv_buffer,recv_size);
                if((recvMsgSize = recv(clntSocket,&recv_buffer,recv_size,0)) < 0)
                {
                    fprintf(stderr,"recv msgfile_1 data failed! \n");
                    exit(1);
                }
                write_file(file2,recv_buffer,recvMsgSize);
                continue;
            }
            else
            {
                recv_size = sizeof(recv_buffer);
                bzero(&recv_buffer,recv_size);
                if((recvMsgSize = recv(clntSocket,&recv_buffer,recv_size,0)) < 0)
                {
                    fprintf(stderr,"recv msgfile_1 data failed! \n");
                    exit(1);
                }
                write_file(file2,recv_buffer,recvMsgSize);
                sum_recv += recvMsgSize;
                while(sum_recv < file2_len)
                {
                    recv_size = file2_len - sum_recv;
                    if(recv_size < RECVBUFFSIZE)
                    {
                        bzero(&recv_buffer,recv_size);
                        if((recvMsgSize = recv(clntSocket,&recv_buffer,recv_size,0)) < 0)
                        {
                            fprintf(stderr,"recv msgfile_1 data failed! \n");
                            exit(1);
                        }
                        write_file(file2,recv_buffer,recvMsgSize);
                        break;
                    }
                    else
                    {
                        bzero(&recv_buffer,sizeof(recv_buffer));
                        if((recvMsgSize = recv(clntSocket,&recv_buffer,sizeof(recv_buffer),0)) < 0)
                        {
                            fprintf(stderr,"recv msgfile_1 data failed! \n");
                            exit(1);
                        }
                        sum_recv += recvMsgSize;
                        write_file(file2,recv_buffer,recvMsgSize);
                    }
                }
            }
        }
    }
    fclose(file1);
    fclose(file2);
    close(clntSocket);
}


#if 0

#ifdef DEBUG
printf("recv msg size is %d.\n",recvMsgSize);
#endif



while(recvMsgSize > 0)
{
#if 0
    if(send(clntSocket,recvBuffer,recvMsgSize,0) != recvMsgSize)
    {
        fprintf(stderr,"send failed! \n");
        exit(1);
    }
#endif

    /*did'not in the end of the file1*/
    if(recvMsgSize == RECVBUFFSIZE)
    {
#ifdef DEBUG
        printf("full recv msg size is %d.\n",recvMsgSize);
#endif
        bzero(&msg_file,sizeof(MsgSync));
        if((recvMsgSize = recv(clntSocket,&msg_file,sizeof(MsgSync),0)) < 0)
        {
            fprintf(stderr,"recv msgfile_1 failed! \n");
            exit(1);
        }

#ifdef DEBUG
        printf("the msg type is: %0x.,msg len is: %d.\n",msg_file.msg_type,msg_file.packet_len);
#endif
        /*recv file1 begin*/
        if(msg_file.msg_type == UPLOADBASEINFO)
        {
            bzero(recv_buffer,sizeof(recv_buffer));
            if((recvMsgSize = recv(clntSocket,&recv_buffer,sizeof(recv_buffer),0)) < 0)
            {
                fprintf(stderr,"recv msgfile_1 data failed! \n");
                exit(1);
            }

            printf("recv msg size is %d.\n",recvMsgSize);
            write_file(file1,recv_buffer,recvMsgSize);
        }

    }
    /*in the end of the file1*/
    else
    {
        printf("recv the end of the file.\n");
        bzero(&msg_file,sizeof(MsgSync));
        if((recvMsgSize = recv(clntSocket,&msg_file,sizeof(MsgSync),0)) < 0)
        {
            fprintf(stderr,"recv msgfile_1 failed! \n");
            exit(1);
        }
        /*recv file1 begin*/
        if(msg_file.msg_type == UPLOADBASEINFO)
        {
            bzero(recv_buffer,sizeof(recv_buffer));
            if((recvMsgSize = recv(clntSocket,&recv_buffer,sizeof(recv_buffer),0)) < 0)
            {
                fprintf(stderr,"recv msgfile_1 data failed! \n");
                exit(1);
            }

            write_file(file1,recv_buffer,recvMsgSize);
        }
        else if(msg_file.msg_type == UPLOADMTINFO)
        {
            bzero(recv_buffer,sizeof(recv_buffer));
            if((recvMsgSize = recv(clntSocket,&recv_buffer,sizeof(recv_buffer),0)) < 0)
            {
                fprintf(stderr,"recv msgfile_1 data failed! \n");
                exit(1);
            }
            write_file(file2,recv_buffer,recvMsgSize);
        }
        break;
    }

}
fclose(file1);
fclose(file2);
close(clntSocket);
}
}

#if 0
if((recvMsgSize = recv(clntSocket,recvBuffer,RECVBUFFSIZE,0)) < 0)
{
    fprintf(stderr,"frist recv failed! \n");
    exit(1);
}

if((recvMsgSize = recv(clntSocket,recv_total,1024,0)) < 0)
{
    fprintf(stderr,"second recv failed! \n");
    exit(1);
}

while(recvMsgSize > 0)
{
    if(send(clntSocket,recvBuffer,recvMsgSize,0) != recvMsgSize)
    {
        fprintf(stderr,"send failed! \n");
        exit(1);
    }

    if((recvMsgSize = recv(clntSocket,recv_total,1024,0)) < 0)
    {
        fprintf(stderr,"second recv failed! \n");
        exit(1);
    }
}
#endif

#endif
#endif
