#include "rfid.h"

static const char *TAG="SrGetDataLabel";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

JNIEXPORT jint JNICALL Java_com_norinco_device_GetData_SrGetDataLabel
  (JNIEnv *env, jobject thiz, jint mfd, jint iPw, jint shPc, jbooleanArray szEpc, jint cBlock, jint shStartAddr, jint shDataLenWord, jbooleanArray szOut)
{
    int i;
    int fd = (int)mfd;
    int pw = (int)iPw;
    int pc = (int)shPc;
    int block = (int)cBlock;
    int startAddr = (int)shStartAddr;
    int lenWord = (int)shDataLenWord;
    unsigned char epc[12] = {'\0'};
    jboolean *epcArray = (*env)->GetBooleanArrayElements(env,szEpc,NULL);
    for(i = 0; i < 12; i++)
    {
    	epc[i] = epcArray[i];
    }
    (*env)->ReleaseBooleanArrayElements(env,szEpc,epcArray,0);

    /* Debug */
    for (i = 0; i < 12; i++)
    {
        LOGD("epc[%d] = %.2x", i, epc[i]);
    }

    unsigned int sum = 0;
    unsigned char send[29] = {'\0'};
    send[0] = 0xBB;
    send[1] = 0x19;
    send[2] = 0x17;
    sum += send[1] + send[2];
    for (i = 0; i < 4; i++)
    {
	send[i+3] = (pw >> (8*(3-i))) % 256;
	sum += send[i+3];
    }
    send[7] = pc / 256;
    send[8] = pc % 256;
    sum += send[7] + send[8];
    for (i = 0; i < 12; i++)
    {
	send[i+9] = epc[i];
	sum += send[i+9];
    }

    send[21] = block;
    send[22] = startAddr / 256;
    send[23] = startAddr % 256;
    send[24] = lenWord / 256;
    send[25] = lenWord % 256;
    sum += send[21] + send[22] + send[23] + send[24] + send[25];
    send[26] = sum;
    send[27] = 0x0D;
    send[28] = 0x0A;

    /* send command(send[]) to device and store reply in recv[] */
    int sendLen = 29, recvLen = 0;
    unsigned char recv[512] = {'\0'};
    int ret = SrSendRecv(fd, send, sendLen, recv, &recvLen);
    if (ret == 0)
    {
        LOGE("SrSendRecv error!\n");
        return 0;
    }

    /* Debug */
    LOGD("recvLen = %d",recvLen);
    for(i = 0; i < recvLen; i++)
    {
    	LOGD("recv[%d] = %.2x", i, recv[i]);
    }

    /* decode reply */
    if (!CheckError(recv, 0x99))
    {
    	return 0;
    }

    if(recv[3] == 0)
    {
        LOGE("Get Data Fail!");
        return 0;
    }

    if(recv[3] != 1)
    {
        LOGE("Unknown Error!");
        return 0;
    }

    unsigned char data[512] = {'\0'};
    unsigned int dataLen = recv[4]*512 + recv[5]*2;
    memcpy(data, recv+6, dataLen);

    jboolean *dataArray = (*env)->GetBooleanArrayElements(env,szOut,NULL);
    if(!dataArray)
    {
    	LOGE("get data array fail");
    }
    for(i = 0; i < dataLen; i++)
    {
    	dataArray[i] = data[i];
    	LOGD("dataArray[%d] = %.2x", i, dataArray[i]);
    }
    (*env)->ReleaseBooleanArrayElements(env,szOut,dataArray,0);

    jclass rfidClass = (*env)->GetObjectClass(env, thiz);
    jfieldID y1_ID = (*env)->GetFieldID(env,rfidClass,"y1","I");
    jfieldID y2_ID = (*env)->GetFieldID(env,rfidClass,"y2","I");
    jfieldID y3_ID = (*env)->GetFieldID(env,rfidClass,"y3","I");
    jfieldID m1_ID = (*env)->GetFieldID(env,rfidClass,"m1","I");
    jfieldID m2_ID = (*env)->GetFieldID(env,rfidClass,"m2","I");
    jfieldID m3_ID = (*env)->GetFieldID(env,rfidClass,"m3","I");
    jfieldID d1_ID = (*env)->GetFieldID(env,rfidClass,"d1","I");
    jfieldID d2_ID = (*env)->GetFieldID(env,rfidClass,"d2","I");
    jfieldID d3_ID = (*env)->GetFieldID(env,rfidClass,"d3","I");

    if(!y1_ID)
    {
        LOGE("not find y1_ID!");
        return 0;
    }
    if(!y2_ID)
    {
        LOGE("not find y2_ID!");
        return 0;
    }
    if(!y3_ID)
    {
        LOGE("not find y3_ID!");
        return 0;
    }
    if(!m1_ID)
    {
        LOGE("not find m1_ID!");
        return 0;
    }
    if(!m2_ID)
    {
        LOGE("not find m2_ID!");
        return 0;
    }
    if(!m3_ID)
    {
        LOGE("not find m3_ID!");
        return 0;
    }
    if(!d1_ID)
    {
        LOGE("not find d1_ID!");
        return 0;
    }
    if(!d2_ID)
    {
        LOGE("not find d2_ID!");
        return 0;
    }
    if(!d3_ID)
    {
        LOGE("not find d3_ID!");
        return 0;
    }

    /* Decode User */
    int y1 = 0, y2 = 0, y3 = 0;
    int m1 = 0, m2 = 0, m3 = 0;
    int d1 = 0, d2 = 0, d3 = 0;

    if (block == 3 && startAddr == 0 && lenWord == 5)
    {
        char end = decodeUSR(data, &y1, &m1, &d1, &y2, &m2, &d2, &y3, &m3, &d3);
        if (end == 0xcf)
        {
            (*env)->SetIntField(env, thiz, y1_ID, y1);
            (*env)->SetIntField(env, thiz, y2_ID, y2);
            (*env)->SetIntField(env, thiz, y3_ID, y3);
            (*env)->SetIntField(env, thiz, m1_ID, m1);
            (*env)->SetIntField(env, thiz, m2_ID, m2);
            (*env)->SetIntField(env, thiz, m3_ID, m3);
            (*env)->SetIntField(env, thiz, d1_ID, d1);
            (*env)->SetIntField(env, thiz, d2_ID, d2);
            (*env)->SetIntField(env, thiz, d3_ID, d3);
        }
    }

    return 1;
}
