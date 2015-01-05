int UART_Open(char *path,unsigned int *err);
int UART_Send(int fd,unsigned char *buff,int buff_size);
int UART_Recv(int fd,unsigned char *buff,unsigned int blocktime,\
              unsigned int *err);
