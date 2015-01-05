#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>

#include "rfid.h"
#include "ParMacro.h"
#include "ErrCodeEx.h"

#define HANDLE int
#define bool unsigned int
#define false 0
#define true 1

void RFID_Init();

void RFID_Close();

int RFID_UART_Open(char *path,unsigned int *err);

int sendcommand(int fd,unsigned char type,char datalen,\
                unsigned char *data,void *infostruct,\
                unsigned int *err);
int LabelData(int fd,char type,preadlabel readlabel,void *infostruct,
              unsigned int *err);

int InquiryRfid(int fd,void *infostruct,unsigned int *err);
int InquiryRfidState(int fd,unsigned char type,void *infostruct,\
                     unsigned int *err);
int InquiryLabelEPC(int fd,void *infostruct,unsigned int *err);
int WriteLabelEPC(int fd,preadlabel commandstruct,void *infostruct,\
                  unsigned int *err);
int LockLabel(int fd,unsigned int lockpw,pepc epc,\
              unsigned short shMask, unsigned short shOper,\
              unsigned int *err);
int KillLabel(int fd,unsigned int killpw,pepc epc,unsigned int *err);
int SetMultiLabelTimer(int fd,unsigned short timer,unsigned int *err);
int InquiryMultiLaberTimer(int fd,unsigned short *timer,\
                           unsigned int *err);
int SetTxPower(int fd,unsigned char *txpower,unsigned int *err);
int SetGpio(int fd, unsigned char cMask, unsigned char cStata,\
            unsigned int *err);
int SetFreq(int fd, unsigned int *freq,unsigned int *err);
int SetRfLink(int fd, unsigned char cData,unsigned int *err);
int SetGen2(int fd, unsigned int QFlag, unsigned int QStart,\
            unsigned int QMin,unsigned int QMax,unsigned int *err);
int SetAnt(int fd, unsigned char cAntMask,unsigned int *err);
int SetArea(int fd, unsigned char bSave, unsigned char cData,\
            unsigned int *err);

int StartInquiryMultiLaber(int fd,unsigned short SNum,\
                           unsigned int *err);
int GetMultiLabel(int fd, pepc epc, unsigned int blocktime,\
                  unsigned int *err);
int StopInquiryMultiLaber(int fd,unsigned int *err);

int SetAntTime(int fd,unsigned short *time,unsigned int *err);
int GetAntTime(int fd,unsigned short *time,unsigned int *err);




