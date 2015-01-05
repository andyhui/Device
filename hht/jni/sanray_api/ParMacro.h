#ifndef PARMACRO_H_INCLUDED
#define PARMACRO_H_INCLUDED

//SetTxPower函数cData参数的可选项
//SetTxPower function cData parameter is optional
#define PAR_OPEN_LOOP 0x0
#define PAR_CLOSED_LOOP 0x01

#define PAR_MIN_READ_POWER 5
#define PAR_MIN_WRITE_POWER 5

#define PAR_MAX_READ_POWER 30
#define PAR_MAX_WRITE_POWER 30


//SetRfLink函数cData参数的可选项
//SetRfLink function cData parameter is optional
#define PAR_DSP_ASK_PMO_40KHZ 0x0
#define PAR_PR_ASK_MILLER4_250KHZ 0x01
#define PAR_PR_ASK_MILLER4_300KHZ 0x02
#define PAR_DSB_ASK_FMO_400KHZ 0x03


//SetArea函数cData参数的可选项
//SetArea function cData parameter is optional
#define PAR_CHINA1 0x01
#define PAR_CHINA2 0x02
#define PAR_EUROPE 0x03
#define PAR_USA 0x04
#define PAR_KOREA 0x05
#define PAR_JAPAN 0x06


//SetReg函数cRegGroup参数的可选项
//SetReg function cRegGroup parameter is optional
#define PAR_REG_MAC 0x0
#define PAR_REG_OEM 0x01
#define PAR_REG_BYPASS 0x02


//SetGen2函数iQFlag参数的可选项
//SetGen2 function iQFlag parameter is optional
#define PAR_FIXED_Q 0x0    //固定Q算法
#define PAR_DYNAMIC 0x1    //动态Q算法

#define PAR_SECTION_STARTQ 15
#define PAR_SECTION_MAXQ 15
#define PAR_SECTION_MINQ 15

//SrGetDataLabel96与SrSetDataLabel96函数cBlock参数的可选项
//SrGetDataLabel96 and SrSetDataLabel96 function cBlock parameter is optional
#define PAR_LABEL_MEM_RESERVE 0    //保留内存
#define PAR_LABEL_MEM_EPC 1    //EPC内存
#define PAR_LABEL_MEM_TID 2    //TID内存
#define PAR_LABEL_MEM_USER 3   //用户内存

#endif // PARMACRO_H_INCLUDED
