#include <termios.h>
#include <string.h>
#include <stdio.h>
#include <fcntl.h>

static int UART_Set(int fd)
{
    struct termios *options;
    options=(struct termios *)malloc(sizeof(struct termios));
    if(!options)
    {
        perror("malloc");
        return -1;
    }
    memset(options, '\0', sizeof(struct termios));
    if(tcgetattr(fd,options)!=0)
    {
        perror("SetupSerial");
        free(options);
        return -1;
    }

    cfsetispeed(options,B115200);
    cfsetospeed(options,B115200);

    options->c_cflag |= CLOCAL;
    options->c_cflag |= CREAD;
    options->c_cflag &= ~CRTSCTS;
    options->c_cflag &= ~CSIZE;
    options->c_cflag |= CS8;
    options->c_cflag &= ~PARENB;
    options->c_cflag &= ~CSTOPB;
    options->c_oflag &= ~OPOST;
    options->c_lflag &= ~(ICANON|ISIG|ECHO|ECHOE);
    options->c_iflag &= ~(INPCK|BRKINT|ICRNL|ISTRIP|IXON);

    options->c_cc[VTIME] = 0;
    options->c_cc[VMIN] = 0;
    tcflush(fd,TCIOFLUSH);
    if (tcsetattr(fd,TCSANOW,options) != 0)
    {
        perror("com set");
        free(options);
        return -1;
    }
    free(options);
    return 0;
}

int UART_Open(char *path,unsigned short *err)
{
    int fd;
    fd=open(path,O_RDWR|O_NOCTTY|O_NDELAY);
    if(fd<0)
    {
        perror("open tty");
        return -1;
    }

    if(UART_Set(fd)==-1)
    {
        return -1;
    }
    return fd;
}

int UART_Send(int fd,unsigned char *buff,int buff_size)
{
    int ret=0;
    ret=write(fd,buff,buff_size);
    if(ret!=buff_size)
    {
        perror("UART_Send");
        ret=-1;
    }
    return ret;
}

int UART_Recv(int fd,unsigned char *buff,unsigned int blocktime,\
              unsigned short *err)
{
    int ret=0;
    int i=0;
    int recv_size=0;
    unsigned char *p=buff;

    fd_set fs_read;
    struct timeval time;

    while(i<5)
    {
#ifdef DEBUG_UART
        printf("尝试第%d次接收......\n",i+1);
#endif // DEBUG_UART
        FD_ZERO(&fs_read);
        FD_SET(fd,&fs_read);
        time.tv_sec = 0;
        time.tv_usec = blocktime*1000;
        ret = select(fd+1,&fs_read,NULL,NULL,&time);
        if(ret<0)
        {
            perror("select");
            return -1;
        }
        else if(ret==0)
        {
            i++;
            continue;
        }
        else
        {
            ret = read(fd,p,1025);
            if(ret<0)
            {
                perror("read");
                return -1;
            }
            else if(ret==0)
            {
                i++;
                continue;
            }
            else
            {
                recv_size+=ret;
                if(buff[recv_size-2]==0x0D && buff[recv_size-1]==0x0A)
                    break;
                else
                    p=buff+recv_size;
            }
        }
    }
    if(i==5)
        recv_size='s';
    return recv_size;
}
