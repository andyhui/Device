#include "uart.h"
#include "rfid.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/wait.h>

#include "ErrCodeEx.h"

#include <android/log.h>

static const char *TAG = "rfid.c";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#ifdef DEBUG_RESULT
static void printRfidState(prfid rfid)
{
	int i;

	printf("\nHardware版本号：%s\n",rfid->hardware);
	printf("Firmware版本号：%s\n",rfid->firmware);
	printf("TxPower:\n    状态：%s\n    读功率：%ddBm\n    写功率：%ddBm\n",
			((rfid->txpower[0]==0x01)?"闭环":"开环"),
			rfid->txpower[1],rfid->txpower[2]);
	printf("射频频率（单位:KHZ）:\n    状态: 跳频，共有%d个跳频频点\n    频率分别为:",rfid->radiostate[0]);
	for(i=0; i<(rfid->radiostate[0]); i++)
	{
		if(i!=0 && i%4==0)
		{
			printf("\n               ");
		}
		printf(" %d",rfid->radiostate[i+1]);
	}

	printf("\nRF LINK PROFILE: %s\n",rfid->rflinkprofile);
	printf("当前工作的天线：%d\n",rfid->antenna);
	printf("当前工作的频率区域：%s\n",rfid->hz);
	printf("当前工作的温度：%d摄氏度\n",rfid->temperature);
	printf("gen2参数:\n    Q算法:%s\n    StartQ:%d\n",
			(rfid->gen2[0]==1?"动态":"固定"),rfid->gen2[1]);
	if(rfid->gen2[0]==1)
	printf("    MinQ:%d\n    MaxQ:%d\n",rfid->gen2[2],rfid->gen2[3]);
	else
	printf("固定Q算法，忽略MinQ及MaxQ！\n");
}

static void printLabelEPC(pepc epc)
{
	int i;
	short rssi=epc->rssi[0]<<8|epc->rssi[1];
	printf("标签PC=%.2X\nEPC=0x",epc->pc[0]<<8 | epc->pc[1]);
	for(i=0; i<12; i++)
	printf("%.2X",epc->epc[i]);
	printf("\nRSSI=%.1fdBm\n",((float)rssi)/10);
}

static void printLabelData(plabeldata data)
{
	printf("接收到%d个字节的数据，有效数据为：",data->len);
	int i;
	for(i=0; i<data->len; i++)
	{
		printf("%c",data->data[i]);
		if(data->data[i+2]==0x0D && data->data[i+3]==0x0A)
		break;
	}
	printf("\n");
}

static void printSetLabel(char c)
{
	if(c==0x01)
	{
		printf("数据写入成功!\n");
	}
	else
	{
		printf("数据写入不成功!\n");
	}
}

static void printLockLabel(char c)
{
	if(c==0x01)
	{
		printf("成功锁定标签！\n");
	}
	else
	{
		printf("锁定标签失败！\n");
	}
}

static void printKillLabel(char c)
{
	if(c==0x01)
	{
		printf("成功杀死标签！\n");
	}
	else
	{
		printf("锁定杀死失败！\n");
	}
}

static void printsetmlabeltimer(char c)
{
	if(c==0x01)
	{
		printf("设置循环查询时间成功！\n");
	}
	else
	{
		printf("设置循环查询时间失败！\n");
	}
}

static void printmlabeltimer(unsigned short timer)
{
	if(timer!=-1)
	printf("循环查询时间为%d\n",timer);
	else
	printf("循环查询时间查询失败！\n");
}

static void printerror(unsigned int err)
{
	unsigned char buff[3];
	bzero(buff,3);
	buff[0] = err >> 8 & 0xff;
	buff[1] = err & 0xff;

	printf("\n错误码:%X%X\n",buff[0],buff[1]);
	switch(buff[1])
	{
		case 0x01:
		printf("     校验码错误！\n");
		break;
		case 0x02:
		printf("     温度过高！\n");
		break;
		case 0x03:
		printf("     反射过大！\n");
		break;
		default:
		printf("     错误码无法识别！\n");
		break;
	}
}
#endif // DEBUG_RESULT
/**
 *
 *
 */
static void gethardware(prfid rfid, unsigned char *buff) {
	char version[6] = { '\0' };
	char *p = version;
	int i = 0;
	for (; i < 3; i++) {
		*p++ = buff[i + 3] + 48;
		if ((i + 1) != 3)
			*p++ = '.';
	}
	strncpy(rfid->hardware, (const char*) version, 5);
	strncpy(rfid->hardware + 5, (const char*) buff + 3, 3);
}

static void getfirmware(prfid rfid, unsigned char *buff) {
	char version[6] = { '\0' };
	char *p = version;
	int i = 0;
	for (; i < 3; i++) {
		*p++ = buff[i + 3] + 48;
		if ((i + 1) != 3)
			*p++ = '.';
	}
	strncpy(rfid->firmware, (const char*) version, 5);
	strncpy(rfid->firmware + 5, (const char*) buff + 3, 3);
}

static void gettxpower(prfid rfid, unsigned char *buff) {
	memcpy(rfid->txpower, (const char*) (buff + 3), 3);
}

static void getradiostate(prfid rfid, unsigned char *buff) {
	unsigned char hznum = buff[3];
	bzero(rfid->radiostate, 20);
	rfid->radiostate[0] |= buff[3];
	unsigned char i;
	unsigned char hz[4] = { '\0' };
	for (i = 0; i < hznum; i++) {
		memcpy(hz, buff + 4 + 3 * i, 3);
		rfid->radiostate[i + 1] |= hz[0] << 16 | hz[1] << 8 | hz[2] << 0;
	}
}

static void getrflinkprofile(prfid rfid, unsigned char *buff) {
	if (buff[3] == 0x01) {
		rfid->rflprofile = buff[4];
		switch (buff[4]) {
		case 0x00:
			strncpy(rfid->rflinkprofile, "DSB_ASK/FM0/40KHz", 17);
			break;
		case 0x01:
			strncpy(rfid->rflinkprofile, "PR_ASK/Miller4/250KHz", 21);
			break;
		case 0x02:
			strncpy(rfid->rflinkprofile, "PR_ASK/Miller4/300KHz", 21);
			break;
		case 0x03:
			strncpy(rfid->rflinkprofile, "DSB_ASK/FM0/400KHz", 18);
			break;
		default:
			strncpy(rfid->rflinkprofile, "error", 5);
			break;
		}
	} else {
		rfid->rflprofile = -1;
		strncpy(rfid->rflinkprofile, "error", 5);
	}
}

static void getcurrentantenna(prfid rfid, unsigned char *buff) {
	int i;
	for (i = 0; (buff[3] >> i) != 1; i++)
		;
	rfid->antenna = 0;
	rfid->antenna = i + 1;
}

static void gethzzone(prfid rfid, unsigned char *buff) {
	if (buff[3] == 0x01) {
		rfid->fzone = buff[4];
		switch (buff[4]) {
		case 0x01:
			strncpy(rfid->hz, "China1", 6);
			break;
		case 0x02:
			strncpy(rfid->hz, "China2", 6);
			break;
		case 0x03:
			strncpy(rfid->hz, "Europe", 6);
			break;
		case 0x04:
			strncpy(rfid->hz, "USA", 3);
			break;
		case 0x05:
			strncpy(rfid->hz, "Korea", 5);
			break;
		case 0x06:
			strncpy(rfid->hz, "Japan", 5);
			break;
		default:
			strncpy(rfid->hz, "error", 5);
			break;
		}
	} else {
		rfid->fzone = -1;
		strncpy(rfid->hz, "error", 5);
	}
}

static void getcurrenttemperature(prfid rfid, unsigned char *buff) {
	rfid->temperature = 0;
	if (buff[3] == 0x01) {
		rfid->temperature = buff[4] << 8 | buff[5] << 0;
		rfid->temperature /= 100;
	} else
		rfid->temperature = -10000;
}

static void getgpiostate(char *pcstate, unsigned char *buff) {
	*pcstate = 0;
	*pcstate |= buff[3];
}

static void getgen2(prfid rfid, unsigned char *buff) {

	if ((buff[3] & 0x01) == 0x01)
		rfid->gen2[0] = 1;
	else
		rfid->gen2[0] = 0;
	rfid->gen2[1] |= buff[4] >> 4;
	rfid->gen2[2] |= buff[4] & 0x0f;
	rfid->gen2[3] |= buff[5] >> 4;
}

static void getepcsingle(pepc epc, unsigned char *buff) {
	if (epc) {
		memcpy(epc->pc, buff + 3, 2);
		memcpy(epc->epc, buff + 5, 12);
		memcpy(epc->rssi, buff + 17, 2);

		epc->ant &= 0;
		epc->ant |= buff[19];
	}
}

static void getepcmulti(pepc epc, unsigned char *buff) {
	if (epc) {
		memcpy(epc->pc, buff + 3, 2);
		memcpy(epc->epc, buff + 5, 12);
		memcpy(epc->rssi, buff + 17, 2);

		epc->ant &= 0;
		epc->ant |= buff[19];
	}
}

static void getlabeldata(plabeldata data, unsigned char *buff) {
	if (buff[3] == 0x01) {
		data->len = buff[4] << 8 | buff[5];
		memcpy(data->data, buff + 6, data->len);
	} else {
		data = NULL;
	}
}

static void getmlabertimer(unsigned short *timer, unsigned char *buff) {
	*timer = 0;
	if (buff[3] == 0x01) {
		*timer |= buff[4] << 8 | buff[5];
	} else
		*timer = -1;
}

static void my_setstate(unsigned char *s, unsigned char *buff) {
	*s = buff[3];
	LOGI("result:%s",buff[3]==0x01 ? "成功" : "失败");
}

static void getantandwaittime(unsigned short *time, unsigned char *buff) {
	int i;
	if (buff[3] == 0x01) {
		for (i = 0; i < 5; i++) {
			time[i] = buff[4 + 2 * i] << 8 | buff[5 + 2 * i];
		}
	}
}

static void geterrorinfo(unsigned int *error, unsigned char *buff) {
	if (buff[3] == 0x00) {
		if (buff[4] == 0x01)
			*error = ERR_CODE_FRAME_CRC;
		else if (buff[4] == 0x02)
			*error = ERR_CODE_FRAME_RET_T;
		else if (buff[4] == 0x03)
			*error = ERR_CODE_FRAME_RET_REFLECT;
		else if (buff[4] == 0x05)
			*error = ERR_CODE_FAIL_UNDEFINED_FIVE;
		else
			*error = ERR_CODE_FAIL_UNDEFINED;
	} else
		*error = ERR_CODE_FAIL_UNDEFINED;
#ifdef DEBUG_RESULT
	printerror(error);
#endif // DEBUG_RESULT
}

static int RFID_Response(int fd, void *infostruct, unsigned int blocktime,
		unsigned int *err) {
	int len = 0;
	unsigned char rcv_buf[1024];
	len = UART_Recv(fd, rcv_buf, blocktime, err);
	if (len == -1 || len == 's') {
		goto end;
	}
	if (rcv_buf[0] != 0xBB) {
		*err = ERR_CODE_FRAME_HEAD;
		len = -1;
		goto end;
	} else if (rcv_buf[len - 1] != 0x0A) {
		*err = ERR_CODE_FRAME_TAIL_2ND;
		len = -1;
		goto end;
	} else if (rcv_buf[len - 2] != 0x0D) {
		*err = ERR_CODE_FRAME_TAIL_1ST;
		len = -1;
		goto end;
	}
#ifdef DEBUG
	int i=0;
	printf("recv %d:",len);
	for(i=0; i<len; i++)
	{
		printf("%.2X ",rcv_buf[i]);
	}
	printf("\n");
#endif // DEBUG
	LOGD("type:%x", rcv_buf[1]);
	switch (rcv_buf[1]) {
	case 0x80:
		my_setstate((unsigned char*) infostruct, rcv_buf);
		break;
	case 0x81:
		my_setstate((unsigned char*) infostruct, rcv_buf);
		break;
	case 0x82:
		my_setstate((unsigned char*) infostruct, rcv_buf);
		break;
	case 0x83:
		my_setstate((unsigned char*) infostruct, rcv_buf);
		break;
	case 0x87:
		my_setstate((unsigned char*) infostruct, rcv_buf);
		break;
	case 0x88:
		my_setstate((unsigned char*) infostruct, rcv_buf);
		break;
	case 0x89:
		my_setstate((unsigned char*) infostruct, rcv_buf);
		break;
	case 0x8A:
		gethardware((prfid) infostruct, rcv_buf);
		break;
	case 0x8B:
		getfirmware((prfid) infostruct, rcv_buf);
		break;
	case 0x8C:
		gettxpower((prfid) infostruct, rcv_buf);
		break;
	case 0x8D:
		getradiostate((prfid) infostruct, rcv_buf);
		break;
	case 0x8E:
		getrflinkprofile((prfid) infostruct, rcv_buf);
		break;
	case 0x90:
		getcurrentantenna((prfid) infostruct, rcv_buf);
		break;
	case 0x91:
		gethzzone((prfid) infostruct, rcv_buf);
		break;
	case 0x92:
		getcurrenttemperature((prfid) infostruct, rcv_buf);
		break;
	case 0x93:
		getgpiostate((char *) infostruct, rcv_buf);
		break;
	case 0x94:
		getgen2((prfid) infostruct, rcv_buf);
		break;
	case 0x96:
		getepcsingle((pepc) infostruct, rcv_buf);
		break;
	case 0x97:
		getepcmulti((pepc) infostruct, rcv_buf);
		break;
	case 0x98:
		//my_setstate((unsigned char *) infostruct, rcv_buf);
		len = -8;
		break;
	case 0x99:
		getlabeldata((plabeldata) infostruct, rcv_buf);
		break;
	case 0x9A:
		my_setstate((unsigned char *) infostruct, rcv_buf);
		break;
	case 0x9B:
		my_setstate((unsigned char *) infostruct, rcv_buf);
		break;
	case 0x9C:
		my_setstate((unsigned char *) infostruct, rcv_buf);
		break;
	case 0x9D:
		my_setstate((unsigned char *) infostruct, rcv_buf);
		break;
	case 0x9E:
		getmlabertimer((unsigned short*) infostruct, rcv_buf);
		break;
	case 0x9F:
		my_setstate((unsigned char *) infostruct, rcv_buf);
		break;
	case 0xA0:
		getantandwaittime((unsigned short *) infostruct, rcv_buf);
		break;
	case 0xFF:
		geterrorinfo(err, rcv_buf);
		len = -1;
		break;
	default:
		*err = ERR_CODE_FRAME_CMD;
		len = -1;
		break;
	}
	end: return len;
}

/** \brief
 *
 * \param
 * \param
 * \return
 *
 */

static int buildcommand(unsigned char *command, char type, char datalen,
		unsigned char *data) {
	int i;
	unsigned short crc = 0;
	command[0] |= 0xBB;
	command[1] |= type;
	command[2] |= datalen;
	if (data)
		memcpy(command + 3, data, datalen);
	for (i = 1; i < datalen + 3; i++) {
#ifdef DEBUG_CRC
		printf("crc: 0x%X+0x%X\n",crc,command[i]);
#endif // DEBUG_CRC
		crc += command[i];
	}
	command[datalen + 3] |= crc & 0xff;
	command[datalen + 4] |= 0x0D;
	command[datalen + 5] |= 0x0A;
	command[datalen + 6] |= '\0';
	return datalen + 6;
}

static void buildlabeldata(preadlabel readlabel, unsigned short datalen,
		unsigned char *data) {
	int i;
	if (readlabel->bank == EPC && readlabel->readsize == 0) {
		for (i = 0; i < 18; i++)
			data[i] = 0;
	} else {
		data[0] |= ((readlabel->password) >> 24) & 0xff;
		data[1] |= ((readlabel->password) >> 16) & 0xff;
		data[2] |= ((readlabel->password) >> 8) & 0xff;
		data[3] |= ((readlabel->password) >> 0) & 0xff;

		memcpy(data + 4, readlabel->epc->pc, 2);
		memcpy(data + 6, readlabel->epc->epc, 12);
	}

	data[18] |= readlabel->bank & 0xff;
	data[19] |= ((readlabel->accesspath >> 1) >> 8) & 0xff;
	data[20] |= ((readlabel->accesspath >> 1) >> 0) & 0xff;

	if (readlabel->readsize == 0) {
		data[21] |= ((datalen / 2) >> 8) & 0xff;
		data[22] |= ((datalen / 2) >> 0) & 0xff;
		memcpy(data + 23, readlabel->data->data, readlabel->data->len);
	} else if (readlabel->readsize > 0) {
		data[21] |= (((readlabel->readsize) / 2) >> 8) & 0xff;
		data[22] |= (((readlabel->readsize) / 2) >> 0) & 0xff;
	}
}

int sendcommand(int fd, unsigned char type, char datalen, unsigned char *data,
		void *infostruct, unsigned int *err) {
	int ret = 0;
	int command_size = 0;
	unsigned char command[513] = { '\0' };
	command_size = buildcommand(command, type, datalen, data);
#ifdef DEBUG
	int i=0;
	printf("sendcommand:   %d:",command_size);
	for(i=0; i<command_size; i++)
	{
		printf("%.2X ",command[i]);
	}
	printf("\n");
#endif // DEBUG
	ret = UART_Send(fd, command, command_size);
	LOGD("SEND SUCCESS");
	if (type != 0x18)
		ret = RFID_Response(fd, infostruct, 1000, err);
	LOGD("Receive :%d", ret);
	return ret;
}

int SetTxPower(int fd, unsigned char *txpower, unsigned int *err) {
	unsigned char data[4];
	bzero(data, 4);
	memcpy(data, txpower, 3);
	unsigned char s;
	int ret = sendcommand(fd, 0x00, 0x03, data, &s, err);
	if (ret == -1 || s == 0x00)
		ret = -1;
	return ret;
}

int SetGpio(int fd, unsigned char cMask, unsigned char cStata,
		unsigned int *err) {
	unsigned char data[3];
	bzero(data, 3);
	data[0] |= cMask & 0xff;
	data[1] |= cStata & 0xff;
	unsigned char s;
	int ret = sendcommand(fd, 0x01, 0x02, data, &s, err);
	if (ret == -1 || s == 0x00)
		ret = -1;
	return ret;
}

int SetFreq(int fd, unsigned int *freq, unsigned int *err) {
	unsigned char data[100];
	bzero(data, 100);
	data[0] |= freq[0] & 0xff;
	int i, j;
	for (i = 1; i < freq[0] + 1; i++) {
		for (j = 0; j < 3; j++)
			data[j + 3 * i - 2] = (freq[i] >> (16 - 8 * j)) & 0xff;
	}
	unsigned char s;
	int ret = sendcommand(fd, 0x02, 3 * freq[0] + 1, data, &s, err);
	if (ret == -1 || s == 0x00)
		ret = -1;
	return ret;
}

int SetRfLink(int fd, unsigned char cData, unsigned int *err) {
	unsigned char data[2];
	bzero(data, 2);
	data[0] = cData;
	unsigned char s;
	int ret = sendcommand(fd, 0x03, 0x01, data, &s, err);
	if (ret == -1 || s == 0x00)
		ret = -1;
	return ret;
}

int SetGen2(int fd, unsigned int QFlag, unsigned int QStart, unsigned int QMin,
		unsigned int QMax, unsigned int *err) {
	unsigned char data[5];
	bzero(data, 5);
	data[0] |= QFlag & 0x01;
	data[1] |= QStart << 4 & 0xf0;
	data[1] |= QMin & 0x0f;
	data[2] |= QMax << 4 & 0xf0;

	unsigned char s;
	int ret = sendcommand(fd, 0x07, 0x04, data, &s, err);
	if (ret == -1 || s == 0x00)
		ret = -1;
	return ret;

}

int SetAnt(int fd, unsigned char cAntMask, unsigned int *err) {
	unsigned char data[2];
	bzero(data, 2);
	data[0] = cAntMask;

	unsigned char s;
	int ret = sendcommand(fd, 0x08, 0x01, data, &s, err);
	if (ret == -1 || s == 0x00)
		ret = -1;
	return ret;
}

int SetArea(int fd, unsigned char bSave, unsigned char cData, unsigned int *err) {
	unsigned char data[3];
	bzero(data, 3);
	data[0] = bSave;
	data[1] = cData;

	unsigned char s;
	int ret = sendcommand(fd, 0x09, 0x02, data, &s, err);
	if (ret == -1 || s == 0x00)
		ret = -1;
	return ret;
}

int InquiryRfidState(int fd, unsigned char type, void *infostruct,
		unsigned int *err) {
	int ret = 0;
	ret = sendcommand(fd, type, 0, NULL, infostruct, err);
	return ret;
}

int InquiryRfid(int fd, void *infostruct, unsigned int *err) {
	int ret = 0;
	unsigned char i;

	for (i = 0x0A; i < 0x15; i++) {
		if (i == 0x0F || i == 0x13)
			continue;
		ret = sendcommand(fd, i, 0, NULL, infostruct, err);
	}

#ifdef DEBUG_RESULT
	printRfidState((prfid)infostruct);
#endif // DEBUG
	return ret;
}

int InquiryLabelEPC(int fd, void *infostruct, unsigned int *err) {
	int ret = 0;
	ret = sendcommand(fd, 0x16, 0, NULL, infostruct, err);
#ifdef DEBUG_RESULT
	printLabelEPC((pepc)infostruct);
#endif // DEBUG
	return ret;
}

int StartInquiryMultiLaber(int fd, unsigned short SNum, unsigned int *err) {
	unsigned char data[3];
	bzero(data, 3);
	data[0] = SNum >> 8 & 0xff;
	data[1] = SNum & 0xff;

	unsigned char s = 0;
	int ret = sendcommand(fd, 0x17, 0x02, data, &s, err);
	LOGD("%d", ret);
	return ret;
}

int GetMultiLabel(int fd, pepc epc, unsigned int blocktime, unsigned int *err) {
	int ret = RFID_Response(fd, (void *) epc, blocktime, err);
	return ret;
}

int StopInquiryMultiLaber(int fd, unsigned int *err) {
	unsigned char s = 0;
	int ret = sendcommand(fd, 0x18, 0x00, NULL, &s, err);
	return ret;
}

int LabelData(int fd, char type, preadlabel readlabel, void *infostruct,
		unsigned int *err) {
	int ret = 0;
	int flag = 0;

	pepc epc;

	if (!readlabel->epc) {
		epc = (pepc) malloc(sizeof(sepc));
		if (!epc) {
			ret = -1;
			goto end;
		}
		memset(epc, '\0', sizeof(sepc));

		flag = 1;

		ret = InquiryLabelEPC(fd, epc, err);
		if (ret == -1) {
			goto end;
		}
		readlabel->epc = epc;
	}

	unsigned short len = 0;
	unsigned char *data;
	if (readlabel->readsize == 0) {
		if (readlabel->bank == RESERVE)
			len = RESERVE_SIZE;
		else if (readlabel->bank == EPC)
			len = EPC_SIZE;
		else if (readlabel->bank == TID)
			len = TID_SIZE;
		else if (readlabel->bank == USER)
			len = USER_SIZE;
		else {
			printf("bank error!\n");
			ret = -1;
			goto end;
		}
		len -= readlabel->accesspath;
	} else if (readlabel->readsize < 0) {
		printf("length error!\n");
		ret = -1;
		goto end;
	}

	data = (unsigned char *) malloc((len + 0x18) * sizeof(unsigned char));
	if (!data) {
		ret = -1;
		goto end;
	}
	bzero(data, len + 0x18);

	buildlabeldata(readlabel, len, data);

	ret = sendcommand(fd, type, len + 0x17, data, infostruct, err);
#ifdef DEBUG_RESULT
	if(ret!=-1)
	{
		if(readlabel->readsize==0)
		{
			printSetLabel(*((char*)infostruct));
		}
		else if(readlabel->readsize > 0)
		printLabelData((plabeldata)infostruct);
	}
#endif // DEBUG_RESULT
	free(data);
	end: if (flag == 1)
		free(epc);
	return ret;
}

int WriteLabelEPC(int fd, preadlabel commandstruct, void *infostruct,
		unsigned int *err) {
	LabelData(fd, 0x1A, commandstruct, infostruct, err);
	return 0;
}

int LockLabel(int fd, unsigned int lockpw, pepc epc, unsigned short shMask,
		unsigned short shOper, unsigned int *err) {
	int flag = 0;
	int ret = 0;
	if (!epc) {
		epc = (pepc) malloc(sizeof(sepc));
		if (!epc) {
			perror("malloc epc");
			ret = -1;
			goto end;
		}
		memset(epc, '\0', sizeof(sepc));
		flag = 1;
		ret = InquiryLabelEPC(fd, (void*) epc, err);
		if (ret == -1) {
			goto end;
		}
	}

	unsigned char data[22];
	bzero(data, 22);

	int i = 0;
	for (; i < 4; i++)
		data[i] = (lockpw >> (24 - i * 8)) & 0xff;
	memcpy(data + 4, epc->pc, 2);
	memcpy(data + 6, epc->epc, 12);
	data[18] |= shMask >> 6 & 0x0f;
	data[19] |= shMask << 2 & 0xfc;
	data[19] |= shOper >> 8 & 0x03;
	data[20] |= shOper & 0xff;

	char s = 0;
	ret = sendcommand(fd, 0x1B, 0x15, data, &s, err);
	if (s == 0x00)
		ret = -1;
#ifdef DEBUG_RESULT
	printLockLabel(s);
#endif // DEBUG_RESULT
	end: if (flag == 1)
		free(epc);
	return ret;
}

int KillLabel(int fd, unsigned int killpw, pepc epc, unsigned int *err) {
	int flag = 0;
	int ret = 0;
	if (!epc) {
		epc = (pepc) malloc(sizeof(sepc));
		if (!epc) {
			perror("malloc epc");
			ret = -1;
			goto end;
		}
		memset(epc, '\0', sizeof(sepc));
		flag = 1;
		ret = InquiryLabelEPC(fd, (void*) epc, err);
		if (ret == -1) {
			goto end;
		}
	}

	unsigned char data[18];
	bzero(data, 18);

	int i = 0;
	for (; i < 4; i++)
		data[i] = (killpw >> (24 - i * 8)) & 0xff;
	memcpy(data + 4, epc->pc, 2);
	memcpy(data + 6, epc->epc, 12);

	char s = 0;
	ret = sendcommand(fd, 0x1C, 0x12, data, &s, err);
	if (s == 0x00)
		ret = -1;
#ifdef DEBUG_RESULT
	printKillLabel(s);
#endif // DEBUG_RESULT
	end: if (flag == 1)
		free(epc);
	return ret;
}

int SetMultiLabelTimer(int fd, unsigned short timer, unsigned int *err) {
	int ret = 0;
	char s;
	unsigned char data[2] = { '\0' };
	data[0] |= timer >> 8 & 0xff;
	data[1] |= timer & 0xff;
	ret = sendcommand(fd, 0x1D, 0x02, data, &s, err);
	if (s == 0x00)
		ret = -1;
#ifdef DEBUG_RESULT
	printsetmlabeltimer(s);
#endif // DEBUG_RESULT
	return ret;
}

int InquiryMultiLaberTimer(int fd, unsigned short *timer, unsigned int *err) {
	int ret = sendcommand(fd, 0x1E, 0x00, NULL, timer, err);
#ifdef DEBUG_RESULT
	printmlabeltimer(*timer);
#endif // DEBUG_RESULT
	return ret;
}

int SetAntTime(int fd, unsigned short *time, unsigned int *err) {
	unsigned char data[10];
	bzero(data, 10);
	int i;
	for (i = 0; i < 5; i++) {
		data[i * 2] |= time[i] >> 8 & 0xff;
		data[i * 2 + 1] |= time[i] & 0xff;
	}
	unsigned char s = 0;
	int ret = sendcommand(fd, 0x1F, 0x0A, data, &s, err);
	if (s == 0x00)
		ret = -1;
	return ret;
}

int GetAntTime(int fd, unsigned short *time, unsigned int *err) {
	int ret = sendcommand(fd, 0x20, 0x00, NULL, time, err);
	return ret;
}

int RFID_UART_Open(char *path, unsigned int *err) {
	int fd = UART_Open(path, err);
	if (fd < 0) {
		return -1;
	}
	return fd;
}

/** \brief
 *
 * \param
 * \param
 * \return
 *
 */

static int poweron() {
	system("echo on > /proc/vbat_en");
	system("echo on > /proc/usb_dc_en");
	return 0;
}

static int poweroff() {
	system("echo off > /proc/vbat_en");
	system("echo off > /proc/usb_dc_en");
	return 0;
}

void RFID_Init() {
	poweron();
#ifdef DEBUG
	printf("正在初始化，请稍后...\n");
#endif // DEBUG
	//sleep(1);
}

void RFID_Close() {
	poweroff();
}
