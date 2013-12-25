#include "rfid.h"

static const char *TAG="SrGetSingleLabel";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

JNIEXPORT jint JNICALL Java_com_norinco_device_GetData_SrGetSingleLabel
  (JNIEnv *env, jobject thiz, jint mfd, jbooleanArray EPC)
{
    int fd = (int)mfd;
    unsigned int szPc;
    double pdbRssi;
    unsigned char szEpc[12] = {'\0'};
    
    unsigned char send[6] = {'\0'};
    send[0] = 0xBB;
    send[1] = send[3] = 0x16;
    send[2] = 0x00;
    send[4] = 0x0D;
    send[5] = 0x0A;

    /* send command(send[]) to device and store reply in recv[] */
    int sendLen = 6, recvLen = 0, i = 0;
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
    if (!CheckError(recv, 0x96))
    {
	return 0;
    }

    szPc = recv[3]*256 + recv[4];
    for (i = 0; i < 12; i++)
    {
        szEpc[i] = recv[5+i];
        LOGD("szEpc[%d] = %.2x", i, szEpc[i]);
    }
    
    if ( recv[17] >= 128 )
    {
	int complement = 255*256 + 255 - recv[17]*256 - recv[18] + 1;
	pdbRssi = (double)complement / 10.0 * (-1);
    }
    else
    {
	pdbRssi = (double) (recv[17]*256+recv[18]) / 10.0;
    }

    jclass rfidClass = (*env)->GetObjectClass(env, thiz);
    //jfieldID EPC_ID = (*env)->GetFieldID(env,rfidClass,"EPC","[Z");
    jfieldID PC_ID = (*env)->GetFieldID(env,rfidClass,"PC","I");
    jfieldID rssi_ID = (*env)->GetFieldID(env,rfidClass,"rssi","D");
    jfieldID fac_ID = (*env)->GetFieldID(env,rfidClass,"factoryId","I");
    jfieldID dev_ID = (*env)->GetFieldID(env,rfidClass,"deviceId","I");
    jfieldID devnum_ID = (*env)->GetFieldID(env,rfidClass,"devnum","I");
    jfieldID devgroup_ID = (*env)->GetFieldID(env,rfidClass,"devgroup","I");
    jfieldID devgid_ID = (*env)->GetFieldID(env,rfidClass,"devgid","I");

    /*if(!EPC_ID)
    {
    	LOGE("not find EPC_ID!");
    	return 0;
    }*/
    if(!PC_ID)
    {
        LOGE("not find PC_ID!");
        return 0;
    }
    if(!rssi_ID)
    {
        LOGE("not find rssi_ID!");
        return 0;
    }
    if(!fac_ID)
    {
        LOGE("not find fac_ID!");
        return 0;
    }
    if(!dev_ID)
    {
        LOGE("not find dev_ID!");
        return 0;
    }
    if(!devnum_ID)
    {
        LOGE("not find devnum_ID!");
        return 0;
    }
    if(!devgroup_ID)
    {
        LOGE("not find devgroup_ID!");
        return 0;
    }
    if(!devgid_ID)
    {
        LOGE("not find devgid_ID!");
        return 0;
    }

    /*jstring js_epc = stoJstring(env, szEpc, 12);
    (*env)->SetObjectField(env, thiz, EPC_ID, js_epc);*/
    jboolean *epcArray = (*env)->GetBooleanArrayElements(env,EPC,NULL);
    if(!epcArray)
    {
    	LOGE("get epc array fail");
    }
    for(i = 0; i < 12; i++)
    {
    	epcArray[i] = szEpc[i];
    	LOGD("epcArray[%d] = %.2x", i, epcArray[i]);
    }
    (*env)->ReleaseBooleanArrayElements(env,EPC,epcArray,0);

    (*env)->SetIntField(env, thiz, PC_ID, szPc);
    (*env)->SetDoubleField(env, thiz, rssi_ID, pdbRssi);

    /* Decode EPC */
    unsigned int fac_index, equip_index;
    int pi,num_in_pi;
    unsigned int number;
    fac_index = szEpc[0]+szEpc[1]*256;
    equip_index = szEpc[2]+szEpc[3]*256;
    pi = (szEpc[5]&0x03);
    pi = (pi)*4+szEpc[4];
    num_in_pi = (int)(((szEpc[5]>>2)&0x3f)+szEpc[6]*64+(szEpc[7]&0x03)*1024*16);
    number = (int)(((szEpc[7]>>2)&0x3f)+64*szEpc[8]+64*256*szEpc[9]+64*256*256*szEpc[10]);
    unsigned char end = (unsigned char)(szEpc[11]);

    if (end == 0xcf)
    {
        (*env)->SetIntField(env, thiz, fac_ID, fac_index);
        (*env)->SetIntField(env, thiz, dev_ID, equip_index);
        (*env)->SetIntField(env, thiz, devnum_ID, number);
        (*env)->SetIntField(env, thiz, devgroup_ID, pi);
        (*env)->SetIntField(env, thiz, devgid_ID, num_in_pi);
        return 1;
    }
    else
    {
        return 0;
    }
}
