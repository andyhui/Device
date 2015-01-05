#define RESERVE 0
#define RESERVE_SIZE 8

#define EPC 1
#define EPC_SIZE 16    //crc-16 2bytes + pc 2bytes + epc 12bytes

#define TID 2
#define TID_SIZE 24

#define USER 3
#define USER_SIZE 64


/** \brief
 * Hardware 版本号          BB 0A 00 0A 0D 0A
 * Firmware 版本号          BB 0B 00 0B 0D 0A
 * Tx Power                 BB 0C 00 0C 0D 0A
 * 射频频率状态              BB 0D 00 0D 0D 0A
 * RF LINK PROFILE 参数     BB 0E 00 0E 0D 0A
 * 当前工作天线              BB 10 00 10 0D 0A
 * 频率区域                 BB 11 00 11 0D 0A
 * 读写器当前温度            BB 12 00 12 0D 0A
 * GPIO 状态                BB 13 01 04 18 0D 0A
 * gen2 参数                BB 14 00 14 0D 0A
 */

typedef struct rfid
{
    char hardware[10];
    char firmware[10];
    char txpower[4];
    int radiostate[20];          //radiostate[0] store hznum
    char rflinkprofile[22];
    char rflprofile;
    char antenna;
    char hz[7];
    char fzone;
    short temperature;
    char gen2[5];
} rfidstate,*prfid;

typedef struct epcbank
{
    char pc[2];
    char rssi[2];
    char epc[13];
    char ant;
} sepc,*pepc;

/** Tag memory(标签内存)
 *  分为Reserved(保留),EPC(电子产品代码),TID(标签识别号)和User(用户)四个独立的存储区块(Bank)
 *
 * Reserve : bank: 0; address: 0~7  bytes  ; access path: 0  ; size: 2  words
 * EPC     : bank: 1; address: 0~15 bytes  ; access path: 2  ; size: 6  words
 * TID     : bank: 2; address: 0~23 bytes ; access path: 8  ; size: 4  words
 * USER    : bank: 3; address: 0~63 bytes ; access path: 12 ; size: 20 words
 *
 */

typedef struct labeldata
{
    char len;
    char data[20];
} slabeldata,*plabeldata;

typedef struct readlabel
{
    int password;
    pepc epc;
    short accesspath;
    short readsize;
    plabeldata data;
    char bank;
} sreadlabel,*preadlabel;
