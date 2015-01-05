#ifndef __MSG_TYPE_H
#define __MSG_TYPE_H

#include <stdio.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>


#include <arpa/inet.h>
#include <net/if.h>
#include <sys/ioctl.h>
#include <string.h>
#include <errno.h>

#define UDPMSGSIZE 10000
#define SERVERIP "192.168.114.118"
#define BUFFSIZE 1024

#define FILEBUFFSIZE 8187
#define PACKETLEN 4
#define PACKETTYPE 1
#define SENDBUFFSIZE 8192

#define BROADCAST 0x0 /*广播寻址*/
#define IPMSG 0x1 /*ip消息*/
#define DOWNLOADREQUEST 0x2 /*下载数据库申请*/
#define UPLOADBASEINFO 0X3 /*基本信息上传*/
#define UPLOADMTINFO 0x4 /*维修信息上传*/
#define DOWNLOADBASEINFO 0x5 /*设备基本信息下载*/
#define DOWNLOADMTINFO 0x6 /*维修方案下载*/
#define DOWNLOADUINFO 0x7 /*用户信息下载*/

#define FILE_1 "equipMaintain.xml"
#define FILE_2 "equipBasicInfo.xml"

typedef struct MsgBroadCastPacket
{
    int packet_len;
    char msg_type;
}MsgBCPacket;

typedef struct MsgSyncPacket
{
    char msg_type;
    int msg_len;
    int  sync_time;
}MsgSyncPacket;

typedef struct MsgTotalPacket
{
    int packet_len;
    int packet_num;
}MsgTotalPacket;

typedef struct MsgSync
{
    int packet_len;
    char msg_type;
}MsgSync;

//void request_ip();/*广播寻址*/
//void send_to_server(int socket,char *Data);/*send to server*/
//void recv_msg(int socket,char *Data);/*接收消息*/

#endif //__MSG_TYPE_H
