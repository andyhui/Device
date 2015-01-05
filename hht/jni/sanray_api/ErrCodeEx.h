#ifndef ERRCODEEX_H_INCLUDED
#define ERRCODEEX_H_INCLUDED

#define ERROR_SYSTEM_DEVICE_NOT_FOUND 528

//[0,15299]即[0x0,0x3BC3]同GetLastError()返回值
//[0,15299] that [0x0,0x3BC3] with GetLastError() return value

#define CUSTOM_ERR_CODE (ERROR_SYSTEM_DEVICE_NOT_FOUND+100)

//串口读取到的数据帧错误
//Serial port to read the data frame head error
#define ERR_CODE_FRAME_HEAD (CUSTOM_ERR_CODE+1)
#define ERR_CODE_FRAME_CMD (CUSTOM_ERR_CODE+2)
#define ERR_CODE_FRAME_TAIL_1ST (CUSTOM_ERR_CODE+3)
#define ERR_CODE_FRAME_TAIL_2ND (CUSTOM_ERR_CODE+4)
#define ERR_CODE_FRAME_CRC (CUSTOM_ERR_CODE+5)

//操作失败应答帧
//The operation failure response frame
#define CMD_OPER_FAIL 0xFF

//温度过高
//Temperature is too high
#define ERR_CODE_FRAME_RET_T (CUSTOM_ERR_CODE+6)

//反射过大
//Reflection too big
#define ERR_CODE_FRAME_RET_REFLECT (CUSTOM_ERR_CODE+7)

//没有从串口读到完整的数据
//Not from the serial port to read the complete data
#define ERR_CODE_READ_DATA_FAIL (CUSTOM_ERR_CODE+8)

//GetGPIO串口返回失败
//GetGPIO serial return fail
#define ERR_CODE_GET_GPIO (CUSTOM_ERR_CODE+9)

//SetGPIO串口返回失败
//SetGPIO serial return fail
#define ERR_CODE_SET_GPIO (CUSTOM_ERR_CODE+10)

//SetGen2串口返回失败
//SetGen2 serial return fail
#define ERR_CODE_SET_GEN2 (CUSTOM_ERR_CODE+11)

//SetArea串口返回失败
//SetArea serial return fail
#define ERR_CODE_SET_AREA (CUSTOM_ERR_CODE+12)

//GetArea串口返回失败
//GetArea serial return fail
#define ERR_CODE_GET_AREA (CUSTOM_ERR_CODE+13)

//SetCWSW串口返回失败
//SetCWSW serial return fail
#define ERR_CODE_SET_CW_SW (CUSTOM_ERR_CODE+14)

//SetFreq串口返回失败
//SetFreq serial return fail
#define ERR_CODE_SET_FREQ (CUSTOM_ERR_CODE+15)

//GetReg串口返回失败
//GetReg serial return fail
#define ERR_CODE_GET_REG (CUSTOM_ERR_CODE+16)

//SetReg串口返回失败
//SetReg serial return fail
#define ERR_CODE_SET_REG (CUSTOM_ERR_CODE+17)

//GetRfLink串口返回失败
//GetRfLink serial return fail
#define ERR_CODE_GET_RF_LINK (CUSTOM_ERR_CODE+18)

//SetRfLink串口返回失败
//SetRfLink serial return fail
#define ERR_CODE_SET_RF_LINK (CUSTOM_ERR_CODE+19)

//GetTemporary串口返回失败
//GetTemporary serial return fail
#define ERR_CODE_GET_TEMPORARY (CUSTOM_ERR_CODE+20)

//SetTxPower串口返回失败
//SetTxPower serial return fail
#define ERR_CODE_SET_TX_POWER (CUSTOM_ERR_CODE+21)

//SetUpdate串口返回失败
//SetUpdate serial return fail
#define ERR_CODE_SET_FM_UPDATE (CUSTOM_ERR_CODE+22)

//SetAnt串口返回失败
//SetAnt serial return fail
#define ERR_CODE_SET_ANT (CUSTOM_ERR_CODE+23)

//传入参数错误
//Input parameter error
#define ERR_CODE_IN_PAR (CUSTOM_ERR_CODE+24)

//操作失败返回0xFF命令 未定义错误标志
//Operation failed to return to 0xFF Undefined error mark
#define ERR_CODE_FAIL_UNDEFINED (CUSTOM_ERR_CODE+25)

//StopGetMultiLabel串口返回失败
//StopGetMultiLabe serial return fail
#define ERR_CODE_STOP_GET_MULTI_LABEL (CUSTOM_ERR_CODE+26)

//GetDataLabel串口返回失败
//GetDataLabel serial return fail
#define ERR_CODE_GET_LABEL_DATA (CUSTOM_ERR_CODE+27)

//SetDataLabel串口返回失败
//SetDataLabel serial return fail
#define ERR_CODE_SET_LABEL_DATA (CUSTOM_ERR_CODE+28)

//LockLabel串口返回失败
//LockLabel serial return fail
#define ERR_CODE_LOCK_LABEL (CUSTOM_ERR_CODE+29)

//KillLabel串口返回失败
//KillLabel serial return fail
#define ERR_CODE_KILL_LABEL (CUSTOM_ERR_CODE+30)

//操作失败返回0xFF命令 未定义错误标志0005 没有寻到标签 可略移动标签重试
//Operation failed to return to 0xF Undefined error mark 0005 No found labels can be slightly mobile label try again
#define ERR_CODE_FAIL_UNDEFINED_FIVE (CUSTOM_ERR_CODE+31)

//GetMulLabelLag串口返回失败
//GetMulLabelLag serial return fail
#define ERR_CODE_GET_MUL_LABEL_LAG (CUSTOM_ERR_CODE+32)

//SetMulLabelLag串口返回失败
//SetMulLabelLag serial return fail
#define ERR_CODE_SET_MUL_LABEL_LAG (CUSTOM_ERR_CODE+33)



#endif // ERRCODEEX_H_INCLUDED
