#include "sr_api.h"
#include "sqlite3_db.h"
#include "android/log.h"
static const char *TAG = "RFID";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

static jstring CStr2Jstring(JNIEnv* env, const char* pat) {
	jclass strClass = (*env)->FindClass(env, "java/lang/String");
	jmethodID ctorID = (*env)->GetMethodID(env, strClass, "<init>",
			"([BLjava/lang/String;)V");
	jbyteArray bytes = (*env)->NewByteArray(env, (jsize) strlen(pat));
	(*env)->SetByteArrayRegion(env, bytes, 0, (jsize) strlen(pat),
			(jbyte*) pat);
	jstring encoding = (*env)->NewStringUTF(env, "UTF-8");
	return (jstring) (*env)->NewObject(env, strClass, ctorID, bytes, encoding);
}

static void CChar2JcharArray(JNIEnv *env, const char *pat, jint len,
		jcharArray dest) {
	jchar temp[100] = { 0 };
	jint i = 0;
	for (; i < len; i++) {
		//LOGI("%X",pat[i]);
		temp[i] |= pat[i] & 0x00ff;
	}
	(*env)->SetCharArrayRegion(env, dest, 0, len, temp);
}

static void CChar2JintArray(JNIEnv *env, const int *pat, jintArray dest) {
	(*env)->SetIntArrayRegion(env, dest, 0, pat[0] + 1, pat);
}

static void getStringTime(unsigned int time, unsigned char *time_array) {
	unsigned int year = 0;
	year = time >> 12 & 0x0fff;
	//LOGD("year:%d",year);

	unsigned char month = 0;
	month = time >> 8 & 0x0f;
	//LOGD("month:%d",month);

	unsigned char day = 0;
	day = time & 0xff;
	//LOGD("day:%d",day);

	int ret = 0;
	sprintf(time_array, "%d.", year);
	if (year > 999)
		ret = 5;
	else if (year > 99 && year < 1000)
		ret = 4;
	else if (year > 9 && year < 100)
		ret = 3;
	else if (year > 0 && year < 10)
		ret = 2;
	sprintf(time_array + ret, "%d.", month);
	if (month > 10)
		ret += 3;
	else
		ret += 2;
	sprintf(time_array + ret, "%d", day);

	//LOGD("time:%s",time_array);
}

JNIEXPORT void JNICALL Java_com_cngc_hht_RfidPower_Init(JNIEnv *env,
		jobject obj) {
	SrInit();
}

JNIEXPORT void JNICALL Java_com_cngc_hht_RfidPower_UnInit(JNIEnv *env,
		jobject obj) {
	SrUnInit();
}

JNIEXPORT jint JNICALL Java_com_cngc_hht_RfidPower_OpenUart(JNIEnv *env,
		jobject obj) {
	int fd;
	unsigned int err;
	if (access("/dev/ttyMSM2", 0) < 0) {
		return -2;
	}
	int ret = SrOpen(&fd, "/dev/ttyMSM2", &err);
	if (ret == 0)
		return -1;
	return fd;
}

JNIEXPORT void JNICALL Java_com_cngc_hht_RfidPower_CloseUart(JNIEnv *env,
		jobject obj, jint fd) {
	close(fd);
}

JNIEXPORT int JNICALL Java_com_cngc_hht_RfidPower_GetRfidState(JNIEnv *env,
		jobject obj, jint fd) {
	unsigned char hardware[6] = { '\0' };
	jstring jhardware;
	unsigned char firmware[6] = { '\0' };
	jstring jfirmware;
	unsigned char txpower[4] = { '\0' };
	jcharArray jtxpower = (*env)->NewCharArray(env, 4);
	unsigned int radiostate[20] = { '\0' };
	jintArray jradiostate = (*env)->NewIntArray(env, 20);
	unsigned char rflinkprofile[22] = { '\0' };
	jstring jrflinkprofile;
	unsigned char antenna;
	jchar jantenna = 0;
	unsigned char hz[8] = { '\0' };
	jcharArray jhz = (*env)->NewCharArray(env, 8);
	unsigned short temperature;
	jshort jtemperature = 0;
	unsigned char gen2[5] = { '\0' };
	jcharArray jgen2 = (*env)->NewCharArray(env, 5);
	unsigned int err;

	int ret = SrGetRfidState(fd, hardware, firmware, txpower, radiostate,
			rflinkprofile, &antenna, hz, &temperature, gen2, &err);
	if (ret == 0) {
		LOGE("get rfid error!");
		return -1;
	}

	jhardware = CStr2Jstring(env, hardware);
	jfirmware = CStr2Jstring(env, firmware);
	CChar2JcharArray(env, txpower, 4, jtxpower);
	CChar2JintArray(env, radiostate, jradiostate);
	jrflinkprofile = CStr2Jstring(env, rflinkprofile);
	jantenna = antenna & 0x00ff;
	jhz = CStr2Jstring(env, hz);
	jtemperature = temperature & 0xffff;
	CChar2JcharArray(env, gen2, 5, jgen2);

	jclass cls = (*env)->GetObjectClass(env, obj);
	jmethodID mid =
			(*env)->GetMethodID(env, cls, "setRfidState",
					"(Ljava/lang/String;Ljava/lang/String;[C[ILjava/lang/String;CLjava/lang/String;S[C)V");
	if (mid == NULL) {
		return -1;
	}
	(*env)->CallVoidMethod(env, obj, mid, jhardware, jfirmware, jtxpower,
			jradiostate, jrflinkprofile, jantenna, jhz, jtemperature, jgen2);

	return 0;
}

JNIEXPORT int JNICALL Java_com_cngc_hht_RfidPower_WriteLabel(JNIEnv *env,
		jobject obj, jint fd) {
	unsigned short pshPc;
	double pdbRssi;
	unsigned char szEpc[13];
	unsigned int iszLen = 12;
	unsigned int piepcLen = 0;
	unsigned int dwMilliseconds = 5000;
	unsigned char szOut[13];
	unsigned int err;

	jstring factory = NULL;
	jstring devname = NULL;
	jstring manudata = NULL;
	jstring recedata = NULL;

	unsigned int sn = 0;
	unsigned int factoryid = 0;
	unsigned int devnameid = 0;
	unsigned int group = 0;
	unsigned int manutime = 0;
	unsigned int devnum = 0;
	unsigned int recetime = 0;

	jclass cls = (*env)->GetObjectClass(env, obj);
	jmethodID mid = (*env)->GetMethodID(env, cls, "getEquipmentSN", "()I");
	if (mid == NULL) {
		return -1;
	}
	sn = (*env)->CallIntMethod(env, obj, mid);
	LOGI("sn:%d\n", sn);

	mid = (*env)->GetMethodID(env, cls, "getEquipAssetNo", "()I");
	if (mid == NULL) {
		return -1;
	}
	devnum = (*env)->CallIntMethod(env, obj, mid);
	LOGI("devnum:%d\n", devnum);

	mid = (*env)->GetMethodID(env, cls, "getEquipmentName",
			"()Ljava/lang/String;");
	if (mid == NULL) {
		return -1;
	}
	devname = (jstring) (*env)->CallObjectMethod(env, obj, mid);

	mid = (*env)->GetMethodID(env, cls, "getEquipManuClass", "()I");
	if (mid == NULL) {
		return -1;
	}
	group = (*env)->CallIntMethod(env, obj, mid);
	LOGI("group:%d\n", group);

	mid = (*env)->GetMethodID(env, cls, "getEquipFactory",
			"()Ljava/lang/String;");
	if (mid == NULL) {
		return -1;
	}
	factory = (jstring) (*env)->CallObjectMethod(env, obj, mid);

	mid = (*env)->GetMethodID(env, cls, "getEquipManuTime",
			"()Ljava/lang/String;");
	if (mid == NULL) {
		return -1;
	}
	manudata = (jstring) (*env)->CallObjectMethod(env, obj, mid);

	mid = (*env)->GetMethodID(env, cls, "getEquipFitoutTime",
			"()Ljava/lang/String;");
	if (mid == NULL) {
		return -1;
	}
	recedata = (jstring) (*env)->CallObjectMethod(env, obj, mid);

	factoryid = Java_com_cngc_hht_DataBase_getFactoryId(env, obj, factory);
	devnameid = Java_com_cngc_hht_DataBase_getDeviceId(env, obj, devname);
	LOGI("devnameid:%d", devnameid);

	unsigned int year = 0;
	unsigned int month = 0;
	unsigned int day = 0;
	char data[5];
	char *p, *q;

	const char *str1 = (*env)->GetStringUTFChars(env, manudata, 0);

	memset(data, '\0', 5);
	p = strstr(str1, "-");
	memcpy(data, str1, p - str1);
	year = atoi(data);
	//LOGI("year:%d", year);

	memset(data, '\0', 5);
	p += 1;
	q = strstr(p, "-");
	memcpy(data, p, q - p);
	month = atoi(data);
	//LOGI("month:%d", month);

	memset(data, '\0', 5);
	q += 1;
	p = strstr(q, "-");
	memcpy(data, q, p - q);
	day = atoi(data);
	//LOGI("day:%d", day);

	manutime = (year << 12) | ((month & 0x0f) << 8) | (day & 0xff);

	(*env)->ReleaseStringUTFChars(env, manudata, str1);

	const char *str2 = (*env)->GetStringUTFChars(env, recedata, 0);

	memset(data, '\0', 5);
	p = strstr(str2, "-");
	//LOGI("year:%s", p);
	memcpy(data, str2, p - str2);
	year = atoi(data);
	//LOGI("year:%d", year);

	memset(data, '\0', 5);
	p += 1;
	q = strstr(p, "-");
	memcpy(data, p, q - p);
	month = atoi(data);
	//LOGI("month:%d", month);

	memset(data, '\0', 5);
	q += 1;
	p = strstr(q, "-");
	memcpy(data, q, p - q);
	day = atoi(data);
	//LOGI("day:%d", day);

	manutime = (year << 12) | ((month & 0x0f) << 8) | (day & 0xff);

	(*env)->ReleaseStringUTFChars(env, recedata, str2);

	/*int ret = SrGetSingleLabel(fd, &pshPc, &pdbRssi, szEpc, iszLen, &piepcLen,
	 &err);
	 if (ret == 0) {
	 LOGE("start single-read epc fail!");
	 return -1;
	 }*/

	szOut[0] = sn >> 8 & 0xff;
	szOut[1] = sn & 0xff;
	szOut[2] = factoryid >> 8 & 0xff;
	szOut[3] = factoryid & 0xff;
	szOut[4] = devnameid >> 8 & 0xff;
	szOut[5] = devnameid & 0xff;
	szOut[6] = group >> 8 & 0xff;
	szOut[7] = group & 0xff;
	szOut[8] = manutime >> 16 & 0xff;
	szOut[9] = manutime >> 8 & 0xff;
	szOut[10] = manutime & 0xff;
	szOut[11] = 0;
	memset(szEpc, 0, sizeof(szEpc));
	pshPc=0;
	int ret = SrSetDataLabel(fd, 0, pshPc, szEpc, 12, PAR_LABEL_MEM_EPC, 2, 6,
			szOut, &err);
	if (ret == 0) {
		LOGE("write epc fail!");
		return -1;
	}

	memset(szOut, '\0', sizeof(szOut));
	memset(szEpc, 0, sizeof(szEpc));
	pshPc=0;
	szOut[0] = devnum >> 24 & 0xff;
	szOut[1] = devnum >> 16 & 0xff;
	szOut[2] = devnum >> 8 & 0xff;
	szOut[3] = devnum & 0xff;
	szOut[4] = recetime >> 16 & 0xff;
	szOut[5] = recetime >> 8 & 0xff;
	szOut[6] = recetime & 0xff;
	szOut[7] = 0;
	szOut[8] = 0;
	szOut[9] = 0;
	szOut[10] = 0;
	szOut[11] = 0;
	ret = SrSetDataLabel(fd, 0, pshPc, szEpc, 12, PAR_LABEL_MEM_USER, 0, 6,
			szOut, &err);
	if (ret == 0) {
		LOGE("write user fail!");
		return -1;
	}
	return 0;
}

JNIEXPORT int JNICALL Java_com_cngc_hht_RfidPower_ReadLabelSingle(JNIEnv *env,
		jobject obj, jint fd) {
	unsigned short pshPc;
	double pdbRssi;
	unsigned char szEpc[13];
	unsigned int iszLen = 12;
	unsigned int piepcLen = 0;
	unsigned int dwMilliseconds = 5000;
	unsigned char szOut[13];
	unsigned int err;
	int ret = SrGetSingleLabel(fd, &pshPc, &pdbRssi, szEpc, iszLen, &piepcLen,
			&err);
	if (ret == 0) {
		LOGE("start single-read epc fail!");
		return -1;
	}
	ret = SrGetDataLabel(fd, 0, pshPc, szEpc, 12, 3, 0, 5, szOut, &err);
	if (ret == 0) {
		LOGE("single-read data fail!");
		return -1;
	}

	unsigned int sn = 0;
	unsigned int factoryid = 0;
	unsigned int devnameid = 0;
	unsigned int group = 0;
	unsigned int manutime = 0;
	unsigned int devnum = 0;
	unsigned int recetime = 0;

	jstring factory = NULL;
	jstring devname = NULL;
	jstring manudata = NULL;
	jstring recedata = NULL;

	sn = szEpc[0] << 8 | szEpc[1];
	factoryid = szEpc[2] << 8 | szEpc[3];
	devnameid = szEpc[4] << 8 | szEpc[5];
	group = szEpc[6] << 8 | szEpc[7];
	manutime = szEpc[8] << 16 | szEpc[9] << 8 | szEpc[10];
	devnum = szOut[0] << 24 | szOut[1] << 16 | szOut[2] << 8 | szOut[3];
	recetime = szOut[4] << 16 | szOut[5] << 8 | szOut[6];

	factory = Java_com_cngc_hht_DataBase_getFactory(env, obj, factoryid);
	devname = Java_com_cngc_hht_DataBase_getDeviceName(env, obj, devnameid);

	unsigned char time[15] = { '\0' };
	getStringTime(manutime, time);
	manudata = CStr2Jstring(env, time);

	memset(time, '\0', 11);
	getStringTime(recetime, time);
	recedata = CStr2Jstring(env, time);

	jclass cls = (*env)->GetObjectClass(env, obj);
	jmethodID mid =
			(*env)->GetMethodID(env, cls, "setDeviceInfo",
					"(IILjava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	if (mid == NULL) {
		return -1;
	}
	(*env)->CallVoidMethod(env, obj, mid, sn, devnum, devname, group, factory,
			manudata, recedata);

	return 0;
}

JNIEXPORT int JNICALL Java_com_cngc_hht_RfidPower_ReadLabelMulti(JNIEnv *env,
		jobject obj, jint fd) {
	unsigned short pshPc;
	double pdbRssi;
	unsigned char szEpc[13];
	unsigned int iszLen = 12;
	unsigned int piepcLen = 0;
	unsigned int dwMilliseconds = 5000;
	unsigned char szOut[11];
	unsigned int err;

	unsigned char epc[100][13] = { '\0' };
	char i, j;
	char flag = 0;

	LOGD("start scan!");

	int ret = SrStartGetMultiLabel(fd, 0, &err);
	LOGD("%d", ret);
	if (ret == 0) {
		LOGE("start multi-read label data fail!");
		return -1;
	}

	LOGD("start to get label epc");

	int scannum = 100;
	while (scannum > 0) {
		ret = SrGetMultiLabel(fd, &pshPc, &pdbRssi, szEpc, iszLen, &piepcLen,
				dwMilliseconds, &err);
		LOGD("cycle_ret:%d", ret);
		if (ret == 0) {
			break;
		}

		for (i = 0; epc[i][12] != '\0'; i++) {
			for (j = 0; j < 13; j++) {
				if (epc[i][j] != szEpc[j])
					break;
			}
			if (j != 13) {
				break;
			}
		}
		if (epc[i][12] == '\0') {
			memcpy(epc[i], szEpc, 12);
			epc[i][12] = i;
			flag = 1;
		}

		if (flag == 1) {
			ret = SrGetDataLabel(fd, 0, pshPc, szEpc, 12, 3, 0, 5, szOut, &err);
			if (ret == 0) {
				LOGE("multi-read data fail!");
				return -1;
			}

			flag = 0;
		}
		scannum--;
	}
	return 0;
}

JNIEXPORT jint JNICALL Java_com_cngc_hht_RfidPower_StopLabelMulti(JNIEnv *env,
		jobject obj, jint fd) {
	unsigned int err;
	int ret;
	ret = SrStopGetMultiLabel(fd, &err);
	if (ret == 0) {
		LOGE("start multi-read label data fail!");
		return -1;
	}
	return 0;
}
