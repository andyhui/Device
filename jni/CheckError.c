#include "rfid.h"

#include "android/log.h"

static const char *TAG="CheckError";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

int CheckError(unsigned char *recv, unsigned char type)
{

    if (recv[0] != 0xBB)
    {
	LOGE("head error!\n");
	return 0;
    }

    if (recv[1] != type)
    {
    	LOGD("recv[1] = %.2x", recv[1]);
    	LOGD("type = %.2x", type);
	LOGE("type error!\n");
	return 0;
    }

    unsigned int i, crc, sum = 0, length = recv[2];
    crc = recv[3+length];

    if (recv[4+length] != 0x0D)
    {
	LOGE("tail1 error!\n");
	return 0;
    }

    if (recv[5+length] != 0x0A)
    {
	LOGE("tail2 error!\n");
	return 0;
    }

    for (i = 1; i < 3+length; i++)
    {
	sum += recv[i];
    }
    if ( sum % 256 != crc )
    {
	LOGE("CRC error!\n");
	return 0;
    }
    return 1;
}
