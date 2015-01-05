#include "ErrCodeEx.h"
#include "ParMacro.h"
#include "main.h"


/*******************************************************************************
函数名   : SrInit
功能     : 初始化 
备注     : 在进程初始化的时候调用该函数且只调用一次
=================================================================================
The function name: SrInit 
Function: Initialization
Note: Call this function when initialization process and the call only once
*******************************************************************************/
void SrInit();


/*******************************************************************************
函数名   : SrUnInit
功能     : 反初始化
备注     : 在进程退出的时候调用该函数且只调用一次
=================================================================================
The function name: SrInit 
Function: Reverse initialization
Note: Call this function when close process and the call only once
*******************************************************************************/
void SrUnInit();


/*******************************************************************************
函数名   : SrOpen
功能     : 打开串口
参数     : [IN] hCom : 串口的句柄请确保非空
           [IN] strPort : 串口名称 例如"\\\\.\\COM1"
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrOpen 
Function: open the serial port 
Parameters: [IN] hCom: please make sure that the handle to the serial port is not null 
            [IN] strPort: serial port name such as "\\\\.\\COM1" 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrOpen(HANDLE *phCom, char *strPort, unsigned int *dwErr);


/*******************************************************************************
函数名   : SrClose
功能     : 关闭串口
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrClose 
Function: closing the serial port 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrClose(HANDLE *phCom, unsigned int *dwErr);


/*******************************************************************************
函数名   : SrGetHardWareVer
功能     : 获取读卡器硬件版本
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pcVer1st : 版本号1
           [OUT] pcVer2st : 版本号2
           [OUT] pcVer3st : 版本号3
           [OUT] bufFormat : 将版本号格式化为形如1.0.0的字符串
           [IN] lenBuf : bufFormat的长度
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetHardWareVer 
Function: acquisition card reader hardware version 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pcVer1st: the version number 
            [OUT] pcVer2nd: the version number 
            [OUT] pcVer3rd: the version number 
            [OUT] bufFormat: will the version number format for form such as 1.0.0
            [IN] lenBuf: bufFormat length 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetHardWareVer(HANDLE hCom, unsigned char *pcVer1st, unsigned char *pcVer2nd, unsigned char *pcVer3rd, char *bufFormat, unsigned int lenBuf, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetFirmWareVer
功能     : 获取读卡器固件版本
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pcVer1st : 版本号1
           [OUT] pcVer2st : 版本号2
           [OUT] pcVer3st : 版本号3
           [OUT] bufFormat : 将版本号格式化为形如1.0.0的字符串
           [IN] lenBuf : bufFormat的长度
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetFirmWareVer 
Function: acquisition card reader firmware version 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pcVer1st: the version number 
            [OUT] pcVer2nd: the version number 
            [OUT] pcVer3rd: the version number 
            [OUT] bufFormat: will the version number format for form such as 1.0.0 string 
            [IN] lenBuf: bufFormat length 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetFirmWareVer(HANDLE hCom, unsigned char *pcVer1st, unsigned char *pcVer2nd, unsigned char *pcVer3rd, char *bufFormat, unsigned int lenBuf, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetTxPower
功能     : 获取功率
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pcData : 开闭环参数详见ParMacro.h
           [OUT] pcRead : 读功率(5-30)db
           [OUT] pcWrite : 写功率(5-30)db
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetTxPower 
Function: get power 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pcData: open/close loop parameters (check ParMacro.h) 
            [OUT] pcRead: read power (5-30)db 
            [OUT] pcWrite: write power (5-30)db 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetTxPower(HANDLE hCom, unsigned char *pcData, unsigned char *pcRead, unsigned char *pcWrite, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrSetTxPower
功能     : 设置功率
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] pcData : 开闭环参数详见ParMacro.h
           [IN] pcRead : 读功率(5-30)db
           [IN] pcWrite : 写功率(5-30)db
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrSetTxPower 
Function: set power 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] pcData: open/close loop parameters (check ParMacro.h) 
            [IN] pcRead: read power (5-30)db 
            [IN] pcWrite: write power (5-30)db 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrSetTxPower(HANDLE hCom, unsigned char cData, unsigned char cRead, unsigned char cWrite, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetFreq
功能     : 获取频率状态
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] dbszFreq : 频率状态点数组(单位KHz)
           [IN] iszLen : dbszFreq的长度
           [OUT] piFreqCount : 频率状态点个数
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetFreq 
Function: get frequency statet 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] dbszFreq: frequency array (unit KHz) 
            [IN] iszLen: dbszFreq length 
            [OUT] piFreqCount: frequency number 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetFreq(HANDLE hCom, double *dbszFreq, unsigned int iszLen, unsigned int *piFreqCount, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrSetFreq
功能     : 设置频率状态
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] dbszFreq : 频率状态点数组(单位KHz)
           [IN] iszLen : dbszFreq的长度
           [IN] piFreqCount : 频率状态点个数
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrSetFreq 
Function: set frequency statet 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] dbszFreq: frequency array (unit KHz) 
            [IN] iszLen: dbszFreq length 
            [IN] piFreqCount: frequency number 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrSetFreq(HANDLE hCom, double *dbszFreq, unsigned int iszLen, unsigned int iFreqCount, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetRfLink
功能     : 获取RF Link
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pcData : RF Link值详见ParMacro.h
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetRfLink 
Function: get RF link 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pcData: RF link value (check ParMacro.h) 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetRfLink(HANDLE hCom, unsigned char *pcData, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrSetRfLink
功能     : 设置RF Link
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] pcData : RF Link值详见ParMacro.h
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrSetRfLink 
Function: set RF link 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] pcData: RF link value (check ParMacro.h) 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrSetRfLink(HANDLE hCom, unsigned char cData, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetAnt
功能     : 获取天线
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pcAntMask : 天线掩码 例如0x0A(即00001010)设置第2号与第4号天线
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetAnt 
Function: get antenna 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pcAntMask: antenna mask for example x0A set 2 and 4 antenna 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetAnt(HANDLE hCom, unsigned char *pcAntMask, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrSetAnt
功能     : 设置天线
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] pcAntMask : 天线掩码 例如0x0A(即00001010)设置第2号与第4号天线
           [OUT] pdwErr : 返回false时请查看错误码
================================================================================= 
The function name: SrSetAnt 
Function: set antenna 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] pcAntMask: antenna mask for example x0A set 2 and 4 antenna 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrSetAnt(HANDLE hCom, unsigned char cAntMask, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetTemporary
功能     : 获取温度
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pdbTemp : 摄氏温度
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetTemporary 
Function: get temperature 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pdbTemp: Celsius temperature 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetTemporary(HANDLE hCom, double *pdbTemp, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetArea
功能     : 获取区域
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pcData : 区域值详见ParMacro.h
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetArea 
Function: get area 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pcData: area value (check ParMacro.h) 
            [OUT] pdwErr:  returns false: please check error code 
*******************************************************************************/
bool SrGetArea(HANDLE hCom, unsigned char *pcData, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrSetArea
功能     : 设置区域
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] pcData : 区域值详见ParMacro.h
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrSetArea 
Function: set area 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] pcData: area value (check ParMacro.h) 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrSetArea(HANDLE hCom, bool bSave, unsigned char cData, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetCarrierWaveSW
功能     : 获取载波开关状态
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pbSW : 载波开关状态
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetCarrierWaveSW 
Function: get carrier wave switch state 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pbSW: carrier switch state 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetCarrierWaveSW(HANDLE hCom, bool *pbSW, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrSetCarrierWaveSW
功能     : 设置载波开关状态
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] pbSW : 载波开关状态
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrSetCarrierWaveSW 
Function: set carrier switch state 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] pbSW: carrier switch state 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrSetCarrierWaveSW(HANDLE hCom, bool bSW, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetReg
功能     : 获取寄存器值
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] cRegGroup : 寄存器组号详见ParMacro.h
           [IN] iAddr : 地址
           [OUT] piData : 数据值
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetReg 
Function: get register value 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] cRegGroup: register type (check ParMacro.h) 
            [IN] iAddr: address 
            [OUT] piData: value 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetReg(HANDLE hCom, unsigned char cRegGroup, unsigned int iAddr, unsigned int *piData, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrSetReg
功能     : 设置寄存器值
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] cRegGroup : 寄存器组号详见ParMacro.h
           [IN] iAddr : 地址
           [IN] piData : 数据值
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrSetReg 
Function: set register value 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] cRegGroup: register type (check ParMacro.h) 
            [IN] iAddr: address 
            [IN] piData: value 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrSetReg(HANDLE hCom, unsigned char cRegGroup, unsigned int iAddr, unsigned int iData, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetGpio
功能     : 获取GPIO
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] cMask : GPIO掩码 例如0x0A(即00001010)获取GPIO2与GPIO4的状态
           [OUT] pcStata : 状态 例如0x02(即00000010)即GPIO2为高电平GPIO4低电平
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetGpio 
Function: get GPIO 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] cMask: GPIO mask for example x0A for GPIO2 and GPIO4 state 
            [OUT] pcStata: state such as x02 is GPIO2 for high level GPIO4 low level
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetGpio(HANDLE hCom, unsigned char cMask, unsigned char *pcStata, unsigned int *pdwErr);

/*******************************************************************************
函数名   : SrSetGpio
功能     : 设置GPIO
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] cMask : GPIO掩码 例如0x0A(即00001010)获取GPIO2与GPIO4的状态
           [IN] pcStata : 状态 例如0x02(即00000010)即GPIO2为高电平GPIO4低电平
           [OUT] pdwErr : 返回false时请查看错误码
================================================================================= 
The function name: SrSetGpio 
Function: set GPIO 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] cMask: GPIO mask for example x0A for GPIO2 and GPIO4 state 
            [IN] pcStata: state such as x02 is GPIO2 for high level GPIO4 low level 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrSetGpio(HANDLE hCom, unsigned char cMask, unsigned char cStata, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetGen2
功能     : 获取Gen2
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] piQFlag : 0表示固定Q算法 1表示动态Q算法 详见ParMacro.h
           [OUT] piQStart : [0,15]
		   [OUT] piQMin : [0,15]
		   [OUT] piQMax : [0,15]
		   [OUT] piReserved : 保留参数 请传入有效地址
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetGen2 
Function: get Gen2 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] piQFlag: 0 is fixed Q algorithm, 1 is dynamic Q algorithm, check ParMacro.h 
            [OUT] piQStart: [0,15] 
            [OUT] piQMin: [0,15] 
            [OUT] piQMax: [0,15] 
            [OUT] piReserved: reserved parameters to effectively address 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetGen2(HANDLE hCom, unsigned int *piQFlag, unsigned int *piQStart, unsigned int *piQMin, unsigned int *piQMax, unsigned int *piReserved, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrSetGen2
功能     : 设置Gen2
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] piQFlag : 0表示固定Q算法 1表示动态Q算法 详见ParMacro.h
           [IN] piQStart : [0,15]
		   [IN] piQMin : [0,15]
		   [IN] piQMax : [0,15]
		   [IN] piReserved : 保留参数 请显示初始化为0
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrSetGen2 
Function: set Gen2 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] piQFlag: 0 said fixed Q algorithm, 1 said dynamic Q algorithm, check ParMacro.h 
            [IN] piQStart: [0,15] 
            [IN] piQMin: [0,15] 
            [IN] piQMax: [0,15] 
            [IN] piReserved: reserved parameter please initialized to 0 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrSetGen2(HANDLE hCom, unsigned int iQFlag, unsigned int iQStart, unsigned int iQMin, unsigned int iQMax, unsigned int iReserved, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetSingleLabel
功能     : 单次寻标签
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] szPc : PC(两个字节)
           [OUT] pdbRssi : RSSI
		   [OUT] szEpc : EPC
		   [IN] iszLen : szEpc的有效长度
		   [OUT] plenEpc : EPC返回的实际长度
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetSingleLabel
Function: single found labels
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] szPc: PC (2 char) 
            [OUT] pdbRssi: RSSI 
            [OUT] szEpc: EPC
            [IN] iszLen: szEpc effective length 
            [OUT] plenEpc: EPC return actual length 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetSingleLabel(HANDLE hCom, unsigned short *pshPc, double *pdbRssi, unsigned char *szEpc, unsigned int iszLen, unsigned int *plenEpc, unsigned int *pdwErr);


/******************************************************************************* 
函数名   : SrStartGetMultiLabel
功能     : 开始连续寻标签,请务必与SrStopGetMultiLabel配对使用,除SrGetMultiLabel
           读取标签之外,连续寻标签停止之前请不要进行其他串口操作.
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] shNum : 连续寻标签次数,为0时永久寻标签.
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrStartGetMultiLabel 
Function: start for continuous label, please be sure to use with SrStopGetMultiLabel pairing, 
in addition  to SrGetMultiLabel Read the label, but continuous label stop not  stop don't  other 
serial port operation. 
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] shNum: parameters 0 for permanent find labels. 
            [OUT] dwErr: if return false please check error 
*******************************************************************************/
bool SrStartGetMultiLabel(HANDLE hCom, unsigned short shNum, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetMultiLabel
功能     : 连续寻标签可以重复调用，但是如果并发的话必须确保互斥
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] szPc : PC(两个字节)
           [OUT] pdbRssi : RSSI
		   [OUT] szEpc : EPC
		   [IN] iszLen : szEpc的有效长度
		   [OUT] iepcLen : 返回EPC的实际长度
		   [IN] dwMilliseconds : 阻塞时间(类似于WaitForSingleObject)
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetMultiLabel
Function: continuous find label can be repeated calls,but must be mutually exclusive if concurrent
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] szPc: PC (2 char) 
            [OUT] pdbRssi: RSSI 
            [OUT] szEpc: EPC 
            [IN] iszLen: szEpc effective length 
            [OUT] piepcLen: return EPC actual length 
            [IN] dwMilliseconds: blocking time (similar to the WaitForSingleObject) 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrGetMultiLabel(HANDLE hCom, unsigned short *pshPc, double *pdbRssi, unsigned char *szEpc, unsigned int iszLen, unsigned int *piepcLen, unsigned int dwMilliseconds, unsigned int  *pdwErr);


/*******************************************************************************
函数名   : SrStopGetMultiLabel
功能     : 停止连续寻标签,请务必与SrStartGetMultiLabel配对使用,除SrGetMultiLabel
           读取标签之外,连续寻标签停止之前请不要进行其他串口操作.
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrStopGetMultiLabel 
Function: stop for continuous label, please be sure to use with SrStartGetMultiLabel pairing, 
in addition  to SrGetMultiLabel Read the label, but continuous label stop not  stop don't  other 
serial port operation. 
Parameters: [IN] hCom: serial handle please ensure effective 
            [OUT] pdwErr: if return false please check error 
*******************************************************************************/
bool SrStopGetMultiLabel(HANDLE hCom, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetDataLabel
功能     : 读标签数据(请确保地址有效并可安全访问)
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] iPw : 密码(4个字节形如0x01234567)
           [IN] shPc : PC(两个字节)
           [IN] szEpc : EPC
		   [IN] iszLen : EPC长
	       [IN] cBlock : 内存类型(详见ParMacro.h)
	       [IN] shStartAddr : 起始位置(注意以字为单位)
	       [IN] shDataLenWord : 设置数据的长度(注意以字为单位)
	       [OUT] szOut : 返回的数据(注意长度为shDataLenWord*2的字节)
           [OUT] pdwErr : 返回false时请查看错误码
注意	:  PC与EPC针对特定标签的锁操作时需要明确输入,若不过滤则全部置零
=================================================================================
The function name: SrGetDataLabel
Function: read the label data (please ensure that the address effective and safe access)
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] iPw: password (4 char such as 0x01234567) 
            [IN] shPc: PC (2 char) 
            [IN] szEpc: EPC
			[IN] iszLen : EPC length
            [IN] cBlock: memory type (see ParMacro.h) 
            [IN] shStartAddr: initial position (attention to the word for the unit) 
            [IN] shDataLenWord:  set the length of the data (attention to the word for the  unit) 
            [OUT] szOut: return data (note length is shDataLenWord*2 char) 
            [OUT] pdwErr: if return false please check error 
Note: PC and EPC specific label lock operation need to the input, if not filter are all 0 
*******************************************************************************/
bool SrGetDataLabel(HANDLE hCom, unsigned int iPw, unsigned short shPc, unsigned char *szEpc, unsigned int iszLen, unsigned char cBlock, unsigned short shStartAddr, unsigned short shDataLenWord, unsigned char *szOut, unsigned int *pdwErr);

/*******************************************************************************
函数名   : SrSetDataLabel
功能     : 写标签数据(请确保地址有效并可安全访问)
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] iPw : 密码(4个字节形如0x01234567)
           [IN] shPc : PC(两个字节)
           [IN] szEpc : EPC
		   [IN] iszLen : EPC长
	       [IN] cBlock : 内存类型(详见ParMacro.h)
	       [IN] shStartAddr : 起始位置(注意以字为单位)
	       [IN] shDataLenWord : 设置数据的长度(注意以字为单位)
	       [IN] szIN : 输入的数据(注意长度为shDataLenWord*2的字节)
           [OUT] pdwErr : 返回false时请查看错误码
注意	:  PC与EPC针对特定标签的锁操作时需要明确输入,若不过滤则全部置零
=================================================================================
The function name: SrSetDataLabel
Function: write label data (please ensure that the address effective and safe access)
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] iPw: password (4 char such as 0x01234567) 
            [IN] shPc: PC (2 char) 
            [IN] szEpc: EPC
			[IN] iszLen : EPC length
            [IN] cBlock: memory type (see ParMacro.h) 
            [IN] shStartAddr: initial position (attention to the word for the unit) 
            [IN] shDataLenWord:  set the length of the data (attention to the word for the  unit) 
            [IN] szIN: input data (note length is shDataLenWord*2 char) 
            [OUT] pdwErr returns false: please check error code 
Note: PC and EPC specific label lock operation need to the input, if not filter are all 0 
*******************************************************************************/
bool SrSetDataLabel(HANDLE hCom, unsigned int iPw, unsigned short shPc, unsigned char *szEpc, unsigned int iszLen, unsigned char cBlock, unsigned short shStartAddr, unsigned short shDataLenWord, unsigned char *szIn, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrLockLabel
功能     : 锁标签
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] iPw : 密码(4个字节形如0x01234567)
           [IN] shPc : PC(两个字节)
           [IN] szEpc : EPC
		   [IN] iszLen : EPC长
	       [IN] shMask : 掩码
		   (10bit: 01 Kill掩码; 23 访问掩码; 45 EPC掩码; 67 TID掩码; 89 用户掩码)
	       [IN] shOper : 操作
		   (10bit: 0 读/写口令;  1 永久锁定;  2 读/写口令;  3 永久锁定; 4 写口令;  
		   5 永久锁定;  6 写口令;  7 永久锁定;  8 写口令;  9 永久锁定; )
           [OUT] pdwErr : 返回false时请查看错误码
注意	:  PC与EPC针对特定标签的锁操作时需要明确输入,若不过滤则全部置零
=================================================================================
The function name: SrLockLabel
Function: lock label
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] iPw: password (4 char such as 0x01234567) 
            [IN] shPc: PC (2 char) 
            [IN] szEpc: EPC
			[IN] iszLen : EPC length
            [IN] shMask: mask 
            (10 bit: 01 Kill the mask, 23 access mask; 45 EPC mask; 67 TID mask; 89 user mask) 
            [IN] shOper: operation 
            (10 bit: 0 read/write; 1 permanent lock; 2 read/write; 3 permanent lock; 4 write; 
            5 permanent lock; 6 write; 7 permanent lock; 8 write; 9 permanent lock; ) 
            [OUT] pdwErr: if return false please check error 
Note: PC and EPC specific label lock operation need to the input, if not filter are all 0 
*******************************************************************************/
bool SrLockLabel(HANDLE hCom, unsigned int iPw, unsigned short shPc, unsigned char *szEpc, unsigned int iszLen, unsigned short shMask, unsigned short shOper, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrKillLabel
功能     : Kill标签
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] iPw : 密码(4个字节形如0x01234567)
           [IN] shPc : PC(两个字节)
           [IN] szEpc : EPC
		   [IN] iszLen : EPC长
           [OUT] pdwErr : 返回false时请查看错误码
注意	:  PC与EPC针对特定标签的锁操作时需要明确输入,若不过滤则全部置零
=================================================================================
The function name: SrKillLabel
Function: Kill label
Parameters: [IN] hCom: serial handle please ensure effective 
            [IN] iPw: not all 0 password (4 char such as 0x01234567) 
            [IN] shPc: PC (2 char) 
            [IN] szEpc: EPC
			[IN] iszLen : EPC length
            [OUT] pdwErr: if return false please check error 
Note: PC and EPC specific label lock operation need to the input, if not filter are all 0 
*******************************************************************************/
bool SrKillLabel(HANDLE hCom, unsigned int iPw, unsigned short shPc, unsigned char *szEpc, unsigned int iszLen, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetMultiLabelLag
功能     : 获取循环查询标签周期时间间隔
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] pshLag : 时间间隔
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function name: SrGetMultiLabelLag 
Function: get multi find label time lag 
Parameters: [IN] hCom : serial handle please ensure effective 
            [OUT] pshLag : time lag 
            [OUT] pdwErr : if return false please check error 
*******************************************************************************/
bool SrGetMultiLabelLag(HANDLE hCom, unsigned short *pshLag, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrSetMultiLabelLag
功能     : 设置循环查询标签周期时间间隔
参数     : [IN] hCom : 串口的句柄请确保有效
           [IN] pshLag : 时间间隔
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function nanme: SrSetMultiLabelLag 
Function: set multi find label time lag 
Parameter: [IN] hCom : serial handle please ensure effective 
           [IN] pshLag : time lag 
           [OUT] pdwErr : if return false please check error 
*******************************************************************************/
bool SrSetMultiLabelLag(HANDLE hCom, unsigned short shLag, unsigned int *pdwErr);


/*******************************************************************************
函数名   : SrGetRfidState
功能     : 查询RFID设备参数
参数     : [IN] hCom : 串口的句柄请确保有效
           [OUT] hardware : 硬件版本
           [OUT] firmware : 固件版本
           [OUT] txpower : 发送功率
           .
           .
           .
           [OUT] pdwErr : 返回false时请查看错误码
=================================================================================
The function nanme: SrSetMultiLabelLag
Function: set multi find label time lag
Parameter: [IN] hCom : serial handle please ensure effective
           [IN] pshLag : time lag
           [OUT] pdwErr : if return false please check error
*******************************************************************************/
bool SrGetRfidState(HANDLE hCom, unsigned char *hardware,
		unsigned char *firmware, unsigned char *txpower,
		unsigned int *radiostate, unsigned char *rflinkprofile,
		unsigned char *antenna, unsigned char *hz, unsigned short *temperature,
		unsigned char *gen2, unsigned int *pdwErr);
