#ifndef SQLITE3_DB_H_INCLUDED
#define SQLITE3_DB_H_INCLUDED

#include <jni.h>
#include <sqlite3.h>

jint Java_com_cngc_hht_DataBase_DatabaseInit(JNIEnv *env, jobject obj,
		jobject assetManager);

jint Java_com_cngc_hht_DataBase_isUser(JNIEnv *env, jobject obj,
		jstring username, jstring userpasswd);

jint Java_com_cngc_hht_DataBase_getFactoryId(JNIEnv *env, jobject obj,
		jstring factoryname);

jstring Java_com_cngc_hht_DataBase_getFactory(JNIEnv *env, jobject obj,
		jint factoryId);

jint Java_com_cngc_hht_DataBase_getDeviceId(JNIEnv *env, jobject obj,
		jstring devicename);

jstring Java_com_cngc_hht_DataBase_getDeviceName(JNIEnv *env, jobject obj,
		jint nameId);

#endif //SQLITE3_DB_H_INCLUDED
