#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <malloc.h>
#include <termios.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/select.h>

#include "android/log.h"

char* jstringTostring(JNIEnv* env, jstring jstr);
jstring stoJstring(JNIEnv* env, unsigned char* pat, int dataLen);
int SrSendRecv(int fd, unsigned char *send, unsigned int sendLen, unsigned char *recv, unsigned int *recvLen);
int CheckError(unsigned char *recv, unsigned char type);
