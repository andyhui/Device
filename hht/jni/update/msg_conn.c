#include <jni.h>
//#include <linux/if.h>
#include <linux/in.h>
#include <linux/sockios.h>
#include <msg_conn.h>
#include <stdint.h>
#include <sys/endian.h>
#include <arpa/inet.h>

#define DEBUG 1

static struct in_addr *
getbroadcastaddr(char *inter, int sock, char *buf){
    struct ifconf ifc;
    struct ifreq ifreq,*ifr;
    static struct in_addr addrs;
    struct sockaddr_in *sin;

    //memset(&addrs,0,sizeof(addrs));
    bzero(&addrs,sizeof(addrs));
    ifc.ifc_len = UDPMSGSIZE;
    ifc.ifc_buf = buf;
    if (ioctl(sock, SIOCGIFCONF, (char *)&ifc) < 0) {
        perror("broadcast: ioctl (get interface configuration)");
        return(&addrs);
    }
    ifr = ifc.ifc_req;
    for (ifr=ifc.ifc_req;ifr->ifr_name[0];ifr++) {
        if (strcmp(ifr->ifr_name,inter)) continue;
        ifreq = *ifr;
        if (ioctl(sock, SIOCGIFFLAGS, (char *)&ifreq) < 0) {
            perror("broadcast: ioctl (get interface flags)");
            continue;
        }
        if ((ifreq.ifr_flags & IFF_BROADCAST) &&
                (ifreq.ifr_flags & IFF_UP) &&
                (ifr->ifr_addr.sa_family == AF_INET)) {
            sin = (struct sockaddr_in *)&ifr->ifr_addr;
            if (ioctl(sock, SIOCGIFBRDADDR, (char *)&ifreq) < 0) {
                addrs = inet_makeaddr(inet_netof(sin->sin_addr), INADDR_ANY);
            } else {
                addrs = ((struct sockaddr_in *)
                        (&ifreq.ifr_broadaddr))->sin_addr;
            }
            printf("Interface : %s\n",ifr->ifr_name);
            return(&addrs);
        }
    }
    return(NULL);
}


//int create_socket(in_addr_t serv_ip,uint16_t serv_port,struct sockaddr_in *addr)
int create_socket(struct in_addr serv_ip,uint16_t serv_port,struct sockaddr_in *addr)
{
    int sock;
    if((sock = socket(PF_INET,SOCK_STREAM,IPPROTO_TCP)) < 0)
    {
        printf("cannot create a socket!\n");
        exit(-1);
    }
    //memset(addr,0,sizeof(struct sockaddr_in));
    bzero(addr,sizeof(struct sockaddr_in));

    addr->sin_family = AF_INET;
    //addr->sin_addr.s_addr = serv_ip;
    addr->sin_addr = serv_ip;
    addr->sin_port = htons(serv_port);
    return sock;
}

int create_bcsocket(char* serv_ip,uint16_t serv_port,struct sockaddr_in *server_addr)
{
    char inbuf[UDPMSGSIZE];
    int sock;
    struct in_addr *broadcast_addr;
    int i = 1;
    if((sock = socket(PF_INET,SOCK_DGRAM,IPPROTO_UDP)) < 0)
    {
        printf("cannot create a socket!\n");
        exit(-1);
    }
    uint32_t len = sizeof(i);
    setsockopt(sock,SOL_SOCKET,SO_BROADCAST,&i,len);
    //memset(server_addr,0,sizeof(struct sockaddr_in));
    bzero(server_addr,sizeof(struct sockaddr_in));

    if((broadcast_addr = getbroadcastaddr(serv_ip,sock,inbuf)) == NULL)
    {
        printf("No such %s interface\r\n",serv_ip);
        exit(-1);
    }

    server_addr->sin_family = AF_INET;
    server_addr->sin_addr = *broadcast_addr;
    server_addr->sin_port = htons(serv_port);
    return sock;
}


//int send_file(char *serv_ip,unsigned short serv_port)
static int send_file(struct in_addr serv_ip,unsigned short serv_port)
{

    //int32_t ip = inet_addr(SERVERIP);
    int sock;
    //uint32_t ip = inet_addr(serv_ip);
    //in_addr_t ip = inet_addr(serv_ip);
    struct in_addr ip = serv_ip;

#ifdef DEBUG
    //printf("server ip is: %d.\n",ip);
#endif
    uint16_t server_port = serv_port;
    struct sockaddr_in server_addr;

    int filesize = 0;
    int filesize2 = 0;
    int total_file_size = 0;


    FILE *fp;
    //if((fp = fopen("equipBasicInfo.xml","r")) == NULL)
    if((fp = fopen(FILE_1,"r")) == NULL)
    {
        printf("read file equipBasicInfo.xml failed! Error:%s \t\n",strerror(errno));

    }
    else
    {
        fseek(fp,0,SEEK_END);
        filesize = ftell(fp);
        fseek(fp,0,SEEK_SET);
    }

    FILE *fp2;
    if((fp2 = fopen(FILE_2,"r")) == NULL)
    {
        printf("read file equipMaintain.xml failed! Error:%s \t\n",strerror(errno));
    }
    else
    {
        fseek(fp2,0,SEEK_END);
        filesize2 = ftell(fp2);
        fseek(fp2,0,SEEK_SET);
    }

    if((filesize != 0)&&(filesize2 != 0))
    {
        total_file_size = filesize + filesize2;
    }

#ifdef DEBUG
    printf("total packet size is :%d.\n",total_file_size);
#endif

    sock = create_socket(ip,server_port,&server_addr);
    if(sock < 0)
    {
        fprintf(stderr,"create socket error.\n");
    }

#ifdef DEBUG
    printf("create socket success.ip is:%d ,port is:%d \n",ip.s_addr ,server_port);
#endif

    MsgTotalPacket message;
    message.packet_len = total_file_size;
    message.packet_num = 2;

    char buff[BUFFSIZE];
    memcpy(buff,&message,sizeof(MsgTotalPacket));

    MsgSyncPacket msg_1;/*同步的文件一*/
    MsgSyncPacket msg_2;/*同步的文件二*/

    msg_1.msg_type = UPLOADBASEINFO;
    msg_1.msg_len = filesize;
    msg_1.sync_time = 20140104;
    printf("msg_1's msg type is %0x,msg_1's msg len is %d\n",msg_1.msg_type,msg_1.msg_len);
    memcpy(buff+sizeof(MsgTotalPacket),&msg_1,sizeof(msg_1));

    msg_2.msg_type = UPLOADMTINFO;
    msg_2.msg_len = filesize2;
    msg_2.sync_time = 20140807;
    memcpy(buff+sizeof(MsgTotalPacket)+sizeof(MsgSyncPacket),&msg_2,sizeof(msg_2));

#ifdef DEBUG
    printf("packet file ok.\n");
#endif
    /*与服务器建立连接*/
    if(connect(sock,(struct sockaddr *)&server_addr,sizeof(server_addr)) < 0)
    {
        fprintf(stderr,"connect failed!");
        exit(1);
    }

#ifdef DEBUG
    printf("connect server success.\n");
#endif

    /*发送数据格式信息*/
    int send_buff_size = sizeof(MsgTotalPacket)+sizeof(msg_1)+sizeof(msg_2);

    if(send(sock,buff,send_buff_size,0) != send_buff_size)
    {
        fprintf(stderr,"send error! \n");
        exit(1);
    }

#ifdef DEBUG
    printf("send file information success.\n");
#endif
#if 0
    char recv_buffer[1024];
    int recvMsgSize;

    if((recvMsgSize = recv(sock,recv_buffer,1024,0)) < 0)
    {
        fprintf(stderr,"recv message error.\n");
        exit(1);
    }
#endif

    /*发送文件*/
    char length_buff[PACKETLEN];
    char file_buff[FILEBUFFSIZE];
    char packet_type[PACKETTYPE];
    char send_buff[SENDBUFFSIZE];
    int count = 0;

    /*发送第一个文件*/

#ifdef DEBUG
    printf("start to send the frist file.\n");
#endif

    while((count = fread(file_buff,1,FILEBUFFSIZE,fp)) <= FILEBUFFSIZE)
    {
        MsgSync msg_file1;
        msg_file1.packet_len = count;
        msg_file1.msg_type = UPLOADBASEINFO;
        //memset(send_buff,0,SENDBUFFSIZE);
        bzero(send_buff,SENDBUFFSIZE);
        bcopy(&msg_file1,send_buff,sizeof(MsgSync));
#if 0
        sprintf(length_buff,"%d",count);
        //memcpy(send_buff,length_buff,PACKETLEN);
        bcopy(length_buff,send_buff,PACKETLEN);

        packet_type[0] = UPLOADBASEINFO;
        //memcpy(send_buff+sizeof(length_buff),packet_type,PACKETTYPE);
        bcopy(packet_type,send_buff+sizeof(length_buff),PACKETTYPE);
#endif
        if(count == FILEBUFFSIZE)
        {
            bcopy(file_buff,send_buff+sizeof(MsgSync),FILEBUFFSIZE);
            //memcpy(send_buff+sizeof(length_buff)+PACKETTYPE,file_buff,FILEBUFFSIZE);
            int send_len = send(sock,send_buff,SENDBUFFSIZE,0);
            if(send_len != SENDBUFFSIZE)
            {
                fprintf(stderr,"1 step send file error \n");
            }
        }
        else
        {
            bcopy(file_buff,send_buff+sizeof(MsgSync),count);
            //memcpy(send_buff+sizeof(length_buff)+PACKETTYPE,file_buff,count);
            int send_len = send(sock,send_buff,sizeof(MsgSync)+count,0);
            if(send_len != sizeof(MsgSync)+count)
            {
                fprintf(stderr,"2 step send file error \n");
                exit(1);
            }
            break;
        }
    }
#ifdef DEBUG
    printf("send the frist file ok.\n");
#endif

    /*发送第二个文件*/
    while((count = fread(file_buff,1,FILEBUFFSIZE,fp2)) <= FILEBUFFSIZE)
    {
        MsgSync msg_file2;
        msg_file2.packet_len = count;
        msg_file2.msg_type = UPLOADMTINFO;
        bzero(send_buff,SENDBUFFSIZE);
        bcopy(&msg_file2,send_buff,sizeof(MsgSync));
#if 0
        //memset(send_buff,0,SENDBUFFSIZE);
        sprintf(length_buff,"%d",count);
        memcpy(send_buff,length_buff,PACKETLEN);

        packet_type[0] = UPLOADMTINFO;
        memcpy(send_buff+sizeof(length_buff),packet_type,PACKETTYPE);
#endif
        if(count == FILEBUFFSIZE)
        {
            //memcpy(send_buff+sizeof(length_buff)+PACKETTYPE,file_buff,FILEBUFFSIZE);
            bcopy(file_buff,send_buff+sizeof(MsgSync),count);
            int send_len = send(sock,send_buff,SENDBUFFSIZE,0);
            if(send_len != SENDBUFFSIZE)
            {
                fprintf(stderr,"send file error \n");
                exit(1);
            }
        }
        else
        {
            bcopy(file_buff,send_buff+sizeof(MsgSync),count);
            //memcpy(send_buff+sizeof(length_buff)+PACKETTYPE,file_buff,count);
            int send_len = send(sock,send_buff,sizeof(MsgSync)+count,0);
            if(send_len != sizeof(MsgSync)+count)
            {
                fprintf(stderr,"send file error \n");
                exit(1);
            }
            break;
        }
    }
    printf("send file succeed!\t\n");
    return 0;
}

//int main(int argc,char *argv[])
JNIEXPORT jint JNICALL Java_com_norinco_upload_Upload_sendrequst
  (JNIEnv *env, jobject obj)
{
    char *serv_ip = (char *)malloc(sizeof(char)*16);
    /*if(1 != argc)
    {
        fprintf(stderr,"Usage: <Server Port>\n");
        exit(1);
    }*/
    int sock;
    /*send_to_server(sock,)*/

    //uint16_t serv_udp_port = atoi(argv[1]);
    uint16_t serv_udp_port = 8899;
    //uint16_t serv_tcp_port = atoi(argv[3]);
    //unsigned short serv_tcp_port = atoi(argv[2]);

    int flag = 0;
#if 0
    if(0 == memcmp(argv[1],"search",6))
    {
        flag = 0;
    }
    else if(0 == memcmp(argv[1],"send_xml_number",15))
    {
        flag = 1;
    }
    else if(0 == memcmp(argv[1],"send packet",100))
    {
        flag = 2;
    }
    else
    {
        fprintf(stderr,"the parameter is error please check again.\n");
        exit(1);
    }
#endif
    switch(flag)
    {
        case 0:
            {
                char recv_buff[1024];
                char *interface = NULL;
                interface = (char *)malloc(sizeof(char)*15);
                memcpy(interface,"eth0",15);
                uint16_t server_port = serv_udp_port;
                struct sockaddr_in server_addr;
                struct sockaddr_in from_addr;
                unsigned int from_size;
                sock = create_bcsocket(interface,server_port,&server_addr);

                int i = 1;
                setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&i,sizeof(i));

                MsgBCPacket bcPacket;
                bcPacket.packet_len = 5;
                bcPacket.msg_type = BROADCAST;
                /*广播寻址ip消息*/
                sendto(sock,(void *)&bcPacket,sizeof(bcPacket),0,(struct sockaddr
                            *)&server_addr,sizeof(server_addr));

                from_size = sizeof(from_addr);
                recvfrom(sock,recv_buff,sizeof(recv_buff),0,(struct sockaddr
                            *)&from_addr,&from_size);

#ifdef DEBUG
                printf("from server ip is: %s\n",inet_ntoa(from_addr.sin_addr));
                printf("server ip is: %s\n",inet_ntoa(server_addr.sin_addr));
#if 0
                printf("recv the server 's ip:%s\n",recv_buff);
#endif

#endif

                serv_ip = inet_ntoa(from_addr.sin_addr);
                printf("the server ip is :%s\n",serv_ip);


                struct in_addr server_ip = from_addr.sin_addr;

                //send_file(server_ip,serv_tcp_port);

                send_file(server_ip,server_port);
                /*memcpy(serv_ip,inet_ntoa(server_addr.sin_addr),sizeof());*/
                /*if(server_addr.sin_addr.s_addr != from_addr.sin_addr.s_addr)*/
                /*{*/
                /*printf("Error :received a packet from unknown source.\n");*/
                /*exit(1);*/
                /*}*/


                //close(sock);
                break;
            }
#if 0
        case 1:
            {
                int32_t ip = inet_addr(SERVERIP);
                //uint16_t ip = *serv_ip;
                uint16_t server_port = serv_tcp_port;
                struct sockaddr_in server_addr;

                int filesize = 0;
                int filesize2 = 0;
                int total_file_size = 0;


                FILE *fp;
                if((fp = fopen("equipBasicInfo.xml","r")) == NULL)
                {
                    printf("read file equipBasicInfo.xml failed! Error:%s \t\n",strerror(errno));

                }
                else
                {
                    fseek(fp,0,SEEK_END);
                    filesize = ftell(fp);
                    fseek(fp,0,SEEK_SET);
                }

                FILE *fp2;
                if((fp2 = fopen("equipMaintain.xml","r")) == NULL)
                {
                    printf("read file equipMaintain.xml failed! Error:%s \t\n",strerror(errno));
                }
                else
                {
                    fseek(fp2,0,SEEK_END);
                    filesize2 = ftell(fp2);
                    fseek(fp2,0,SEEK_SET);
                }

                if((filesize != 0)&&(filesize2 != 0))
                {
                    total_file_size = filesize + filesize2;
                }

                sock = create_socket(ip,server_port,&server_addr);
                if(sock < 0)
                {
                    fprintf(stderr,"create socket error.\n");
                }

#ifdef DEBUG
                printf("create socket success.ip is:%d ,port is:%d \n",ip,server_port);
#endif

                MsgTotalPacket message;
                message.packet_len = total_file_size;
                message.packet_num = 2;

                char buff[BUFFSIZE];
                memcpy(buff,&message,sizeof(MsgTotalPacket));

                MsgSyncPacket msg_1;/*同步的文件一*/
                MsgSyncPacket msg_2;/*同步的文件二*/

                msg_1.msg_type = UPLOADBASEINFO;
                msg_1.msg_len = filesize;
                memcpy(buff+sizeof(MsgTotalPacket),&msg_1,sizeof(msg_1));

                msg_2.msg_type = UPLOADMTINFO;
                msg_2.msg_len = filesize2;
                memcpy(buff+sizeof(MsgTotalPacket)+sizeof(MsgSyncPacket),&msg_2,sizeof(msg_2));

#ifdef DEBUG
                printf("packet file ok.\n");
#endif
                /*与服务器建立连接*/
                if(connect(sock,(struct sockaddr *)&server_addr,sizeof(server_addr)) < 0)
                {
                    fprintf(stderr,"connect failed!");
                    exit(1);
                }

#ifdef DEBUG
                printf("connect server success.\n");
#endif

                /*发送数据格式信息*/
                int send_buff_size = sizeof(MsgTotalPacket)+sizeof(msg_1)+sizeof(msg_2);
                if(send(sock,buff,send_buff_size,0) != send_buff_size)
                {
                    fprintf(stderr,"send error! \n");
                    exit(1);
                }

#ifdef DEBUG
                printf("send file information success.\n");
#endif
#if 0
                char recv_buffer[1024];
                int recvMsgSize;

                if((recvMsgSize = recv(sock,recv_buffer,1024,0)) < 0)
                {
                    fprintf(stderr,"recv message error.\n");
                    exit(1);
                }
#endif

                /*发送文件*/
                char length_buff[PACKETLEN];
                char file_buff[FILEBUFFSIZE];
                char packet_type[PACKETTYPE];
                char send_buff[SENDBUFFSIZE];
                int count = 0;
                /*发送第一个文件*/

#ifdef DEBUG
                printf("start to send the frist file.\n");
#endif

                while((count = fread(file_buff,1,FILEBUFFSIZE,fp)) <= FILEBUFFSIZE)
                {
                    //memset(send_buff,0,SENDBUFFSIZE);
                    bzero(send_buff,SENDBUFFSIZE);
                    sprintf(length_buff,"%d",count);
                    //memcpy(send_buff,length_buff,PACKETLEN);
                    bcopy(length_buff,send_buff,PACKETLEN);

                    packet_type[0] = UPLOADBASEINFO;
                    //memcpy(send_buff+sizeof(length_buff),packet_type,PACKETTYPE);
                    bcopy(packet_type,send_buff+sizeof(length_buff),PACKETTYPE);
                    if(count == FILEBUFFSIZE)
                    {
                        memcpy(send_buff+sizeof(length_buff)+PACKETTYPE,file_buff,FILEBUFFSIZE);
                        int send_len = send(sock,send_buff,SENDBUFFSIZE,0);
                        if(send_len != SENDBUFFSIZE)
                        {
                            fprintf(stderr,"send file error \n");
                        }
                    }
                    else
                    {
                        memcpy(send_buff+sizeof(length_buff)+PACKETTYPE,file_buff,count);
                        int send_len = send(sock,send_buff,PACKETLEN+PACKETTYPE+count,0);
                        if(send_len != PACKETLEN+PACKETTYPE+count)
                        {
                            fprintf(stderr,"send file error \n");
                            exit(1);
                        }
                        break;
                    }
                }
#ifdef DEBUG
                printf("send the frist file ok.\n");
#endif

                /*发送第二个文件*/
                while((count = fread(file_buff,1,FILEBUFFSIZE,fp2)) <= FILEBUFFSIZE)
                {
                    memset(send_buff,0,SENDBUFFSIZE);
                    sprintf(length_buff,"%d",count);
                    memcpy(send_buff,length_buff,PACKETLEN);

                    packet_type[0] = UPLOADMTINFO;
                    memcpy(send_buff+sizeof(length_buff),packet_type,PACKETTYPE);
                    if(count == FILEBUFFSIZE)
                    {
                        memcpy(send_buff+sizeof(length_buff)+PACKETTYPE,file_buff,FILEBUFFSIZE);
                        int send_len = send(sock,send_buff,SENDBUFFSIZE,0);
                        if(send_len != SENDBUFFSIZE)
                        {
                            fprintf(stderr,"send file error \n");
                            exit(1);
                        }
                    }
                    else
                    {
                        memcpy(send_buff+sizeof(length_buff)+PACKETTYPE,file_buff,count);
                        int send_len = send(sock,send_buff,PACKETLEN+PACKETTYPE+count,0);
                        if(send_len != PACKETLEN+PACKETTYPE+count)
                        {
                            fprintf(stderr,"send file error \n");
                            exit(1);
                        }
                        break;
                    }
                }
                printf("send file succeed!\t\n");
                break;
            }
            /*
               case 3:
               {
               }
               */
#endif
        default:
            {
                fprintf(stderr,"You input the error parameters,please input the \
                        correct parameters.\n");
                break;
            }
    }
    exit(0);
}

/**
  void send_to_server(int socket,char *Data)
  {
  int len = 0;
  send(socket,Data,len,0);
  }
 **/

/**
  void recv_msg(int socket,char *Data)
  {
  char *buf;
  int buf_size = 2048;
  buf = (char *)malloc(sizeof(char)*buf_size);
  int recv_size = 2048;
  memset(buf,'\0',buf_size);
  recv(socket,buf,recv_size,0);
  Data = buf;
  }
 **/
