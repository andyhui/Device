#include "rfid.h"

static const char *TAG="SrSetDataLabel";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

JNIEXPORT jint JNICALL Java_com_norinco_device_GetData_SrSetDataLabel
  (JNIEnv *env, jobject thiz, jint mfd)
{
	int fd=(int)mfd;
    jclass rfidClass = (*env)->GetObjectClass(env, thiz);

    jfieldID fac_ID = (*env)->GetFieldID(env,rfidClass,"factoryId","I");
    jfieldID dev_ID = (*env)->GetFieldID(env,rfidClass,"deviceId","I");
    jfieldID devnum_ID = (*env)->GetFieldID(env,rfidClass,"devnum","I");
    jfieldID devgroup_ID = (*env)->GetFieldID(env,rfidClass,"devgroup","I");
    jfieldID devgid_ID = (*env)->GetFieldID(env,rfidClass,"devgid","I");
    jfieldID y1_ID = (*env)->GetFieldID(env,rfidClass,"y1","I");
    jfieldID y2_ID = (*env)->GetFieldID(env,rfidClass,"y2","I");
    jfieldID y3_ID = (*env)->GetFieldID(env,rfidClass,"y3","I");
    jfieldID m1_ID = (*env)->GetFieldID(env,rfidClass,"m1","I");
    jfieldID m2_ID = (*env)->GetFieldID(env,rfidClass,"m2","I");
    jfieldID m3_ID = (*env)->GetFieldID(env,rfidClass,"m3","I");
    jfieldID d1_ID = (*env)->GetFieldID(env,rfidClass,"d1","I");
    jfieldID d2_ID = (*env)->GetFieldID(env,rfidClass,"d2","I");
    jfieldID d3_ID = (*env)->GetFieldID(env,rfidClass,"d3","I");

/* Get Single Label */
    unsigned char send1[6] = {'\0'};
    send1[0] = 0xBB;
    send1[1] = send1[3] = 0x16;
    send1[2] = 0x00;
    send1[4] = 0x0D;
    send1[5] = 0x0A;
    if (!CheckError(send1, 0x16))
    {
    	return 0;
	}
    int send1Len = 6, recv1Len = 0, i = 0;
    unsigned char recv1[512] = {'\0'};
    int ret = SrSendRecv(fd, send1, send1Len, recv1, &recv1Len);
    if (ret == 0)
    {
        LOGE("SrSendRecv error!\n");
        return 0;
    }

    /* Debug */
    LOGD("single_label recv1Len = %d",recv1Len);
    for(i = 0; i < recv1Len; i++)
    {
    	LOGD("single_label recv1[%d] = %.2x", i, recv1[i]);
    }

    /* decode reply */
    if (!CheckError(recv1, 0x96))
    {
	return 0;
    }

/* Prepare Set Usr Data */
    int cBlock = 3, shStartAddr = 0, shDataLenWord = 5, iPw = 0;
    int y1 = (*env)->GetIntField(env, thiz, y1_ID);
    int y2 = (*env)->GetIntField(env, thiz, y2_ID);
    int y3 = (*env)->GetIntField(env, thiz, y3_ID);
    int m1 = (*env)->GetIntField(env, thiz, m1_ID);
    int m2 = (*env)->GetIntField(env, thiz, m2_ID);
    int m3 = (*env)->GetIntField(env, thiz, m3_ID);
    int d1 = (*env)->GetIntField(env, thiz, d1_ID);
    int d2 = (*env)->GetIntField(env, thiz, d2_ID);
    int d3 = (*env)->GetIntField(env, thiz, d3_ID);
    unsigned char usrData[10] = {'\0'};
    encodeUSR(usrData, y1, m1, d1, y2, m2, d2, y3, m3, d3);

    unsigned int sum = 0;
    unsigned char send2[39] = {'\0'};
    send2[0] = 0xBB;
    send2[1] = 0x1A;
    send2[2] = 4+2+12+1+2+2+shDataLenWord*2;
    sum += send2[1] + send2[2];

    for (i = 0; i < 4; i++)
    {
	send2[i+3] = (iPw >> (8*(3-i))) % 256;
	sum += send2[i+3];
    }
    memcpy(send2+7, recv1+3, 14);
    for (i = 0; i < 14; i++)
    {
    	sum += send2[i+7];
    }
    send2[21] = cBlock;
    send2[22] = shStartAddr / 256;
    send2[23] = shStartAddr % 256;
    send2[24] = shDataLenWord / 256;
    send2[25] = shDataLenWord % 256;
    for (i = 0; i < shDataLenWord*2; i++)
    {
	send2[i+26] = usrData[i];
	sum += send2[i+26];
    }
    sum += send2[21] + send2[22] + send2[23] + send2[24] + send2[25];
    send2[26+shDataLenWord*2] = sum;
    send2[27+shDataLenWord*2] = 0x0D;
    send2[28+shDataLenWord*2] = 0x0A;
    if (!CheckError(send2, 0x1A))
    {
      	return 0;
    }
    /* send command(send[]) to device and store reply in recv[] */
    int send2Len = 39, recv2Len = 0;
    unsigned char recv2[512] = {'\0'};
    ret = SrSendRecv(fd, send2, send2Len, recv2, &recv2Len);
    LOGD("ret = %d", ret);
    for (i = 0; i < send2Len; i++)
    	LOGD("send2[%d] = %.2x", i, send2[i]);
    if (ret == 0)
    {
        LOGD("SrSendRecv error!\n");
        return 0;
    }

    /* Debug */
        LOGD("single_label recv2Len = %d",recv2Len);
        for(i = 0; i < recv2Len; i++)
        {
        	LOGD("single_label recv2[%d] = %.2x", i, recv2[i]);
        }

    /* decode reply */
    if (!CheckError(recv2, 0x9A))
    {
	return 0;
    }

    if (recv2[3] != 1)
    {
        LOGD("Set Usr error");
        return 0;
    }

/* Prepare Set Epc Data */
    cBlock = 1, shStartAddr = 2, shDataLenWord = 6, iPw = 0;
    int fac_index = (*env)->GetIntField(env, thiz, fac_ID);
    int dev_index = (*env)->GetIntField(env, thiz, dev_ID);
    int devnum = (*env)->GetIntField(env, thiz, devnum_ID);
    int devgroup = (*env)->GetIntField(env, thiz, devgroup_ID);
    int devgid = (*env)->GetIntField(env, thiz, devgid_ID);
    unsigned char epcData[12] = {'\0'};
    memcpy((void*)epcData,(void*)&fac_index,2*sizeof(char));
    memcpy((void*)(epcData+2),(void*)&dev_index,2*sizeof(char));
    epcData[4] = (char)devgroup;
    epcData[5] = (char)(devgroup/256) + 4*(char)(devgid&0x3f);
    epcData[6] = (char)(devgid/64);
    epcData[7] = (char)(devgid/(256*64)) + 4*(char)(devnum&0x3f);
    epcData[8] = (char)(devnum/64);
    epcData[9] = (char)(devnum/(256*64));
    epcData[10] = (char)(devnum/(256*64*256));
    epcData[11] = 0xcf;

    sum = 0;
    unsigned char send3[41] = {'\0'};
    send3[0] = 0xBB;
    send3[1] = 0x1A;
    send3[2] = 4+2+12+1+2+2+shDataLenWord*2;
    sum += send3[1] + send3[2];
    for (i = 0; i < 4; i++)
    {
	send3[i+3] = (iPw >> (8*(3-i))) % 256;
	sum += send3[i+3];
    }
    memcpy(send3+7, recv1+3, 14);
    for (i = 0; i < 14; i++)
        {
        	sum += send3[i+7];
        }
    send3[21] = cBlock;
    send3[22] = shStartAddr / 256;
    send3[23] = shStartAddr % 256;
    send3[24] = shDataLenWord / 256;
    send3[25] = shDataLenWord % 256;
    for (i = 0; i < shDataLenWord*2; i++)
    {
	send3[i+26] = epcData[i];
	sum += send3[i+26];
    }
    sum += send3[21] + send3[22] + send3[23] + send3[24] + send3[25];
    send3[26+shDataLenWord*2] = sum;
    send3[27+shDataLenWord*2] = 0x0D;
    send3[28+shDataLenWord*2] = 0x0A;

    if (!CheckError(send3, 0x1A))
        {
    	return 0;
        }
    /* send command(send[]) to device and store reply in recv[] */
    int send3Len = 41, recv3Len = 0;
    unsigned char recv3[512] = {'\0'};
    ret = SrSendRecv(fd, send3, send3Len, recv3, &recv3Len);
    if (ret == 0)
    {
        LOGD("SrSendRecv error!\n");
        return 0;
    }

    /* Debug */
        LOGD("single_label recv3Len = %d",recv3Len);
        for(i = 0; i < recv3Len; i++)
        {
        	LOGD("single_label recv3[%d] = %.2x", i, recv3[i]);
        }

    /* decode reply */
    if (!CheckError(recv3, 0x9A))
    {
	return 0;
    }

    if (recv3[3] != 1)
    {
        LOGD("Set Epc error");
        return 0;
    }

    return 1;
}

