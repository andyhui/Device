#include "main.h"
#include <android/log.h>
static const char *TAG = "main.c";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

void SrInit() {
	RFID_Init();
}

void SrUnInit() {
	RFID_Close();
}

bool SrOpen(HANDLE *phCom, char *strPort, unsigned int *pdwErr) {
	*phCom = RFID_UART_Open(strPort, pdwErr);
	if (phCom < 0) {
		*pdwErr = ERROR_SYSTEM_DEVICE_NOT_FOUND;
		return false;
	}
	return true;
}

bool SrClose(HANDLE *phCom, unsigned int *pdwErr) {
	close(*phCom);
	return true;
}

bool SrGetHardWareVer(HANDLE hCom, unsigned char *pcVer1st,
		unsigned char *pcVer2nd, unsigned char *pcVer3rd, char *bufFormat,
		unsigned int lenBuf, unsigned int *pdwErr) {
	int ret;
	if (lenBuf < 5) {
		*pdwErr = ERR_CODE_IN_PAR;
		return false;
	}
	rfidstate rfid;
	bzero(&rfid, sizeof(rfidstate));
	ret = InquiryRfidState(hCom, 0x0A, &rfid, pdwErr);
	if (ret < 0) {
		return false;
	}
	strncpy(bufFormat, rfid.hardware, 5);
	*pcVer1st = rfid.hardware[5];
	*pcVer2nd = rfid.hardware[6];
	*pcVer3rd = rfid.hardware[7];

	return true;
}

bool SrGetFirmWareVer(HANDLE hCom, unsigned char *pcVer1st,
		unsigned char *pcVer2nd, unsigned char *pcVer3rd, char *bufFormat,
		unsigned int lenBuf, unsigned int *pdwErr) {
	int ret;
	if (lenBuf < 5) {
		*pdwErr = ERR_CODE_IN_PAR;
		return false;
	}
	rfidstate rfid;
	bzero(&rfid, sizeof(rfidstate));
	ret = InquiryRfidState(hCom, 0x0B, &rfid, pdwErr);
	if (ret < 0) {
		return false;
	}
	strncpy(bufFormat, rfid.firmware, 5);
	*pcVer1st = rfid.firmware[5];
	*pcVer2nd = rfid.firmware[6];
	*pcVer3rd = rfid.firmware[7];

	return true;
}

bool SrGetTxPower(HANDLE hCom, unsigned char *pcData, unsigned char *pcRead,
		unsigned char *pcWrite, unsigned int *pdwErr) {
	int ret;
	rfidstate rfid;
	bzero(&rfid, sizeof(rfidstate));
	ret = InquiryRfidState(hCom, 0x0C, &rfid, pdwErr);
	if (ret < 0) {
		return false;
	}
	*pcData = rfid.txpower[0];
	*pcRead = rfid.txpower[1];
	*pcWrite = rfid.txpower[2];
	return true;
}

bool SrSetTxPower(HANDLE hCom, unsigned char cData, unsigned char cRead,
		unsigned char cWrite, unsigned int *pdwErr) {
	unsigned char txpower[4] = { 0 };
	txpower[0] = cData & 0x01;
	txpower[1] = cRead;
	txpower[2] = cWrite;
	int ret = SetTxPower(hCom, txpower, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_SET_TX_POWER;
		return false;
	}
	return true;
}

bool SrGetFreq(HANDLE hCom, double *dbszFreq, unsigned int iszLen,
		unsigned int *piFreqCount, unsigned int *pdwErr) {
	int ret = 0;
	rfidstate rfid;
	bzero(&rfid, sizeof(rfidstate));
	ret = InquiryRfidState(hCom, 0x0D, &rfid, pdwErr);
	if (ret < 0) {
		return false;
	}
	*piFreqCount = rfid.radiostate[0];
	if (iszLen < rfid.radiostate[0])
		memcpy(dbszFreq, rfid.radiostate + 1, iszLen);
	else
		memcpy(dbszFreq, rfid.radiostate + 1, rfid.radiostate[0]);
	return true;
}

bool SrSetFreq(HANDLE hCom, double *dbszFreq, unsigned int iszLen,
		unsigned int iFreqCount, unsigned int *pdwErr) {
	unsigned int freq[100];
	bzero(freq, 100);
	freq[0] = iFreqCount;
	int i;
	for (i = 0; i < iszLen; i++)
		freq[i + 1] = (unsigned int) dbszFreq[i];
	int ret = SetFreq(hCom, freq, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_SET_FREQ;
		return false;
	}
	return true;
}

bool SrGetRfLink(HANDLE hCom, unsigned char *pcData, unsigned int *pdwErr) {
	int ret = 0;
	rfidstate rfid;
	bzero(&rfid, sizeof(rfidstate));
	ret = InquiryRfidState(hCom, 0x0E, &rfid, pdwErr);
	if (ret < 0 || (rfid.rflprofile == -1)) {
		*pdwErr = ERR_CODE_GET_RF_LINK;
		return false;
	}
	*pcData = rfid.rflprofile;
	return true;
}

bool SrSetRfLink(HANDLE hCom, unsigned char cData, unsigned int *pdwErr) {
	int ret = SetRfLink(hCom, cData, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_SET_RF_LINK;
		return false;
	}
	return true;
}

bool SrGetAnt(HANDLE hCom, unsigned char *pcAntMask, unsigned int *pdwErr) {
	int ret = 0;
	rfidstate rfid;
	bzero(&rfid, sizeof(rfidstate));
	ret = InquiryRfidState(hCom, 0x10, &rfid, pdwErr);
	if (ret < 0) {
		return false;
	}
	*pcAntMask = 0x01 << (rfid.antenna - 1);
	return true;
}

bool SrSetAnt(HANDLE hCom, unsigned char cAntMask, unsigned int *pdwErr) {
	int ret = SetAnt(hCom, cAntMask, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_SET_ANT;
		return false;
	}
	return true;
}

bool SrGetTemporary(HANDLE hCom, double *pdbTemp, unsigned int *pdwErr) {
	int ret = 0;
	rfidstate rfid;
	bzero(&rfid, sizeof(rfidstate));
	ret = InquiryRfidState(hCom, 0x12, &rfid, pdwErr);
	if (ret < 0 || rfid.temperature == -10000) {
		*pdwErr = ERR_CODE_GET_TEMPORARY;
		return false;
	}
	*pdbTemp = (double) (rfid.temperature);
	return true;
}

bool SrGetArea(HANDLE hCom, unsigned char *pcData, unsigned int *pdwErr) {
	int ret = 0;
	rfidstate rfid;
	bzero(&rfid, sizeof(rfidstate));
	ret = InquiryRfidState(hCom, 0x11, &rfid, pdwErr);
	if (ret < 0 || rfid.fzone == -1) {
		*pdwErr = ERR_CODE_GET_AREA;
		return false;
	}
	*pcData = rfid.fzone;
	return true;
}

bool SrSetArea(HANDLE hCom, bool bSave, unsigned char cData,
		unsigned int *pdwErr) {
	int ret = SetArea(hCom, (unsigned char) bSave, cData, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_SET_AREA;
		return false;
	}
	return true;
}

bool SrGetCarrierWaveSW(HANDLE hCom, bool *pbSW, unsigned int *pdwErr) {
	return false;
}

bool SrSetCarrierWaveSW(HANDLE hCom, bool bSW, unsigned int *pdwErr) {
	return false;
}

bool SrGetReg(HANDLE hCom, unsigned char cRegGroup, unsigned int iAddr,
		unsigned int *piData, unsigned int *pdwErr) {
	return false;
}

bool SrSetReg(HANDLE hCom, unsigned char cRegGroup, unsigned int iAddr,
		unsigned int iData, unsigned int *pdwErr) {
	return true;
}

bool SrGetGpio(HANDLE hCom, unsigned char cMask, unsigned char *pcStata,
		unsigned int *pdwErr) {
	int ret = 0;
	unsigned char data[2];
	bzero(data, 2);
	data[0] = cMask;
	ret = sendcommand(hCom, 0x13, 0x01, data, pcStata, pdwErr);
	if (ret < 0) {
		*pdwErr = ERR_CODE_GET_GPIO;
		return false;
	}
	return true;
}

bool SrSetGpio(HANDLE hCom, unsigned char cMask, unsigned char cStata,
		unsigned int *pdwErr) {
	int ret = SetGpio(hCom, cMask, cStata, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_SET_GPIO;
		return false;
	}
	return true;
}

bool SrGetGen2(HANDLE hCom, unsigned int *piQFlag, unsigned int *piQStart,
		unsigned int *piQMin, unsigned int *piQMax, unsigned int *piReserved,
		unsigned int *pdwErr) {
	int ret = 0;
	rfidstate rfid;
	bzero(&rfid, sizeof(rfidstate));
	ret = InquiryRfidState(hCom, 0x14, &rfid, pdwErr);
	if (ret < 0) {
		return false;
	}
	*piQFlag = rfid.gen2[0];
	*piQStart = rfid.gen2[1];
	*piQMin = rfid.gen2[2];
	*piQMax = rfid.gen2[3];
	return true;
}

bool SrSetGen2(HANDLE hCom, unsigned int iQFlag, unsigned int iQStart,
		unsigned int iQMin, unsigned int iQMax, unsigned int iReserved,
		unsigned int *pdwErr) {
	int ret = SetGen2(hCom, iQFlag, iQStart, iQMin, iQMax, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_SET_GEN2;
		return false;
	}
	return true;
}

bool SrGetRfidState(HANDLE hCom, unsigned char *hardware,
		unsigned char *firmware, unsigned char *txpower,
		unsigned int *radiostate, unsigned char *rflinkprofile,
		unsigned char *antenna, unsigned char *hz, unsigned short *temperature,
		unsigned char *gen2, unsigned int *pdwErr) {
	int ret = 0;
	rfidstate rfid;
	memset(&rfid, '\0', sizeof(rfidstate));
	ret = InquiryRfid(hCom, &rfid, pdwErr);
	if (ret == -1) {
		return false;
	}

	memcpy(hardware, rfid.hardware, 5);
	memcpy(firmware, rfid.firmware, 5);
	memcpy(txpower, rfid.txpower, 3);
	memcpy(radiostate, rfid.radiostate, (rfid.radiostate[0] + 1) * sizeof(int));
	memcpy(rflinkprofile, rfid.rflinkprofile, strlen(rfid.rflinkprofile));
	*antenna = rfid.antenna;
	memcpy(hz, rfid.hz, strlen(rfid.hz));
	*temperature = rfid.temperature;
	memcpy(gen2, rfid.gen2, 5);

	return true;
}

bool SrGetSingleLabel(HANDLE hCom, unsigned short *pshPc, double *pdbRssi,
		unsigned char *szEpc, unsigned int iszLen, unsigned int *plenEpc,
		unsigned int *pdwErr) {
	sepc epc;
	memset(&epc, '\0', sizeof(sepc));
	if (InquiryLabelEPC(hCom, (void*) &epc, pdwErr) < 0) {
		return false;
	}
	*pshPc = epc.pc[0] << 8 | epc.pc[1];
	*pdbRssi = epc.rssi[0] << 8 | epc.rssi[1];
	memcpy(szEpc, epc.epc, iszLen);
	*plenEpc = 12;
	return true;
}

bool SrStartGetMultiLabel(HANDLE hCom, unsigned short shNum,
		unsigned int *pdwErr) {
	int ret = StartInquiryMultiLaber(hCom, shNum, pdwErr);LOGD("%d",ret);
	if (ret == -1) {
		return false;
	}
	return true;
}

bool SrGetMultiLabel(HANDLE hCom, unsigned short *pshPc, double *pdbRssi,
		unsigned char *szEpc, unsigned int iszLen, unsigned int *piepcLen,
		unsigned int dwMilliseconds, unsigned int *pdwErr) {
	sepc epc;
	bzero(&epc, sizeof(sepc));
	int ret = GetMultiLabel(hCom, &epc, dwMilliseconds, pdwErr);
	if (ret == -8 || ret == -1 || ret == 's') {
		return false;
	}
	*pshPc = epc.pc[0] << 8 | epc.pc[1];
	*pdbRssi = epc.rssi[0] << 8 | epc.rssi[1];
	memcpy(szEpc, epc.epc, iszLen);
	*piepcLen = 12;
	return true;
}

bool SrStopGetMultiLabel(HANDLE hCom, unsigned int *pdwErr) {
	int ret = StopInquiryMultiLaber(hCom, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_STOP_GET_MULTI_LABEL;
		return false;
	}
	return true;
}

bool SrGetDataLabel(HANDLE hCom, unsigned int iPw, unsigned short shPc,
		unsigned char *szEpc, unsigned int iszLen, unsigned char cBlock,
		unsigned short shStartAddr, unsigned short shDataLenWord,
		unsigned char *szOut, unsigned int *pdwErr) {
	sreadlabel commandstruct;
	bzero(&commandstruct, sizeof(sreadlabel));
	sepc epc;
	bzero(&epc, sizeof(sepc));
	commandstruct.epc = &epc;

	commandstruct.password = iPw;
	commandstruct.epc->pc[0] = shPc >> 8 & 0xff;
	commandstruct.epc->pc[1] = shPc >> 0 & 0xff;
	memcpy(commandstruct.epc->epc, szEpc, iszLen);
	commandstruct.bank = cBlock;
	commandstruct.accesspath = shStartAddr * 2;
	commandstruct.readsize = shDataLenWord * 2;

	slabeldata data;
	bzero(&data, sizeof(slabeldata));

	LabelData(hCom, 0x19, &commandstruct, (void*) &data, pdwErr);
	if (!data.data) {
		*pdwErr = ERR_CODE_GET_LABEL_DATA;
		return false;
	}
	memcpy(szOut, data.data, data.len);
	return true;
}

bool SrSetDataLabel(HANDLE hCom, unsigned int iPw, unsigned short shPc,
		unsigned char *szEpc, unsigned int iszLen, unsigned char cBlock,
		unsigned short shStartAddr, unsigned short shDataLenWord,
		unsigned char *szIn, unsigned int *pdwErr) {
	sreadlabel commandstruct;
	bzero(&commandstruct, sizeof(sreadlabel));

	sepc epc;
	bzero(&epc, sizeof(sepc));
	commandstruct.epc = &epc;

	commandstruct.password = iPw;
	commandstruct.epc->pc[0] = shPc >> 8 & 0xff;
	commandstruct.epc->pc[1] = shPc >> 0 & 0xff;
	memcpy(commandstruct.epc->epc, szEpc, iszLen);
	commandstruct.bank = cBlock;
	commandstruct.accesspath = shStartAddr * 2;

	commandstruct.readsize = 0;

	slabeldata data;
	bzero(&data, sizeof(slabeldata));
	commandstruct.data = &data;
	commandstruct.data->len = shDataLenWord * 2;
	memcpy(commandstruct.data->data, szIn, commandstruct.data->len);

	char ret;
	LabelData(hCom, 0x1A, &commandstruct, (void*) &ret, pdwErr);
	if (ret == 0x00) {
		*pdwErr = ERR_CODE_SET_LABEL_DATA;
		return false;
	}
	return true;
}

bool SrLockLabel(HANDLE hCom, unsigned int iPw, unsigned short shPc,
		unsigned char *szEpc, unsigned int iszLen, unsigned short shMask,
		unsigned short shOper, unsigned int *pdwErr) {
	pepc epc = NULL;
	if (szEpc && iszLen != 0) {
		epc = (pepc) malloc(sizeof(sepc));
		if (!epc) {
			*pdwErr = ERR_CODE_LOCK_LABEL;
			return false;
		}
		bzero(epc, sizeof(sepc));
		epc->pc[0] |= shPc >> 8 & 0xff;
		epc->pc[1] |= shPc & 0xff;
		memcpy(epc->epc, szEpc, iszLen);
	}

	int ret = LockLabel(hCom, iPw, epc, shMask, shOper, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_LOCK_LABEL;
		return false;
	}
	return true;
}

bool SrKillLabel(HANDLE hCom, unsigned int iPw, unsigned short shPc,
		unsigned char *szEpc, unsigned int iszLen, unsigned int *pdwErr) {
	pepc epc = NULL;
	if (szEpc && iszLen != 0) {
		epc = (pepc) malloc(sizeof(sepc));
		if (!epc) {
			*pdwErr = ERR_CODE_KILL_LABEL;
			return false;
		}
		bzero(epc, sizeof(sepc));
		epc->pc[0] |= shPc >> 8 & 0xff;
		epc->pc[1] |= shPc & 0xff;
		memcpy(epc->epc, szEpc, iszLen);
	}
	int ret = KillLabel(hCom, iPw, epc, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_KILL_LABEL;
		return false;
	}
	return true;
}

bool SrSetMultiLabelLag(HANDLE hCom, unsigned short shLag, unsigned int *pdwErr) {
	int ret = SetMultiLabelTimer(hCom, shLag, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_SET_MUL_LABEL_LAG;
		return false;
	}
	return true;
}

bool SrGetMultiLabelLag(HANDLE hCom, unsigned short *pshLag,
		unsigned int *pdwErr) {
	int ret = InquiryMultiLaberTimer(hCom, pshLag, pdwErr);
	if (ret == -1) {
		*pdwErr = ERR_CODE_GET_MUL_LABEL_LAG;
		return false;
	}
	return true;
}

bool srSetAntTime(HANDLE hCom, unsigned short ant1, unsigned short ant2,
		unsigned short ant3, unsigned short ant4, unsigned short waittime,
		unsigned int *pdwErr) {
	unsigned short time[6];
	time[0] = ant1;
	time[1] = ant2;
	time[2] = ant3;
	time[3] = ant4;
	time[4] = waittime;
	int ret = SetAntTime(hCom, time, pdwErr);
	if (ret == -1)
		return false;
	return true;
}

bool SrGetAntTime(HANDLE hCom, unsigned short ant1, unsigned short ant2,
		unsigned short ant3, unsigned short ant4, unsigned short waittime,
		unsigned int *pdwErr) {
	unsigned short time[6];
	bzero(time, 6);
	int ret = GetAntTime(hCom, time, pdwErr);
	if (ret == -1)
		return false;
	ant1 = time[0];
	ant2 = time[1];
	ant3 = time[2];
	ant4 = time[3];
	waittime = time[4];
	return true;
}

/*int main()
 {
 unsigned int pdwErr=0;

 SrInit();

 HANDLE hCom;

 if(!SrOpen(&hCom, NULL, &pdwErr))
 {
 return -1;
 }

 pid_t pid=fork();
 if(pid<0)
 {
 perror("fork");
 return 0;
 }
 else if(pid==0)
 {
 sleep(30);
 SrStopGetMultiLabel(hCom,&pdwErr);
 exit(0);
 }
 else
 {
 SrStartGetMultiLabel(hCom, 0, &pdwErr);

 bool i=true;
 unsigned short pshPc;
 double pdbRssi;
 unsigned char szEpc[13]= {'\0'};
 unsigned int iszLen=12;
 unsigned int piepcLen;
 while(i)
 {
 i=SrGetMultiLabel(hCom, &pshPc,&pdbRssi,szEpc,iszLen,\
                              &piepcLen,5000,&pdwErr);
 }
 }

 SrClose(&hCom,&pdwErr);

 SrUnInit();

 return 0;
 }*/
