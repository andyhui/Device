#include <stdio.h>     
#include <termios.h>
#include <unistd.h>    
#include <sys/types.h>
#include <sys/stat.h>  
#include <fcntl.h>    
#include <string.h>
#include <jni.h>

#include "android/log.h"

static const char *TAG="SrSendRecv";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

/****************************************************************************
函数名 : SrSendRecv
功能 : 串口单次收发数据
参数 : [IN] fd : 串口的句柄请确保有效
[IN] send : 需要往串口发送的数据
[IN] sendLen : 发送数据的长度
[OUT] recv : 需要从串口接收的数据
[OUT] recvLen : 接收数据的长度
****************************************************************************/
int SrSendRecv(int fd, unsigned char *send, unsigned int sendLen, \
               unsigned char *recv, unsigned int *recvLen)
{
    tcflush(fd, TCIFLUSH);
    if (write(fd, send, sendLen) != sendLen)
    {
        LOGE("send fail!\n");
        return 0;
    }

    fd_set rfds;
    struct timeval tv;
    tv.tv_sec = 1;
    tv.tv_usec = 0;
    int nread = 0;
    unsigned char buff[512] = {'\0'};
    int index = 0;
    memset(recv,'\0',sizeof(recv));
    while (1)
    {
        FD_ZERO(&rfds);
        FD_SET(fd, &rfds);
        if (select(1+fd, &rfds, NULL, NULL, &tv)>0)
        {
            //LOGD("read serialport");
            if (FD_ISSET(fd, &rfds))
            {
                nread=read(fd, buff, 512);
                memcpy(recv+index, buff, nread);
	        index += nread;
            }
        }else break;/*
       else if(ret==0)
       {
    	   if(index==0)
    	   {
            LOGE("timeout");
    	   }
    	   else
    	   {
    		   LOGD("have read all data");
    	   }
    	   break;
       }*/
    }
    *recvLen = index;
    if (index > 0)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}
