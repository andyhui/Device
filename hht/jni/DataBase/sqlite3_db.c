#include <stdio.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <unistd.h>
#include <assert.h>
#include <sqlite3.h>

#include <android/asset_manager_jni.h>

#include <android/log.h>
static const char *TAG = "DataBase";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#define DBPATH "/data/data/com.cngc.hht/DeviceBase.db"
#define DBPATH_MODIFY "/data/data/com.cngc.hht/databases/method.db"

static int copydb(JNIEnv *env, jobject assetManager) {
	LOGI("copy start");
	jstring filename = (*env)->NewStringUTF(env, "DeviceBase.db");
	const char *utf8 = (*env)->GetStringUTFChars(env, filename, NULL);
	if (!utf8) {
		LOGE("filename swith to jstring");
		return JNI_FALSE;
	}

	AAssetManager* mgr = AAssetManager_fromJava(env, assetManager);
	if (!mgr) {
		LOGE("AAssetManager_fromJava");
		return JNI_FALSE;
	}
	AAsset* asset = AAssetManager_open(mgr, utf8, AASSET_MODE_UNKNOWN);
	(*env)->ReleaseStringUTFChars(env, filename, utf8);
	if (!asset) {
		LOGE("AAssetManager_open");
		return JNI_FALSE;
	}

	off_t bufferSize = AAsset_getLength(asset);
	char *buffer = (char *) malloc(bufferSize + 1);
	if (!buffer) {
		LOGE("malloc buffer");
		return JNI_FALSE;
	}
	buffer[bufferSize] = '\0';
	int numBytesRead = AAsset_read(asset, buffer, bufferSize);
	if (0 > numBytesRead) {
		LOGE("AAsset_read");
		return JNI_FALSE;
	}

	AAsset_close(asset);

	int fd = open(DBPATH, O_RDWR | O_CREAT | O_TRUNC, S_IRUSR | S_IWUSR);
	if (0 > fd) {
		LOGE("open db");
		return JNI_FALSE;
	}

	int ret = write(fd, buffer, bufferSize);
	if (ret != bufferSize) {
		LOGE("write db");
		return JNI_FALSE;
	}

	close(fd);
	free(buffer);

	return JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_com_cngc_hht_DataBase_DatabaseInit(JNIEnv *env,
		jobject obj, jobject assetManager) {

	jint ret = 0;
	if ((ret = access(DBPATH, 0)) == 0)
		ret = access(DBPATH, 6);

	if (ret < 0) {
		if (copydb(env, assetManager) < 0)
			return JNI_FALSE;
	}
	return JNI_TRUE;
}

JNIEXPORT jint JNICALL Java_com_cngc_hht_DataBase_isUser(JNIEnv *env,
		jobject obj, jstring username, jstring userpasswd) {
	char name[21] = { '\0' };
	char passwd[51] = { '\0' };
	(*env)->GetStringUTFRegion(env, username, 0,
			(*env)->GetStringLength(env, username), name);
	(*env)->GetStringUTFRegion(env, userpasswd, 0,
			(*env)->GetStringLength(env, userpasswd), passwd);

	const char *zSql = "select * from userInfo_tbl";

	sqlite3 *db;
	sqlite3_stmt *ppStmt = NULL;
	jint level = -1;
	if (sqlite3_open_v2(DBPATH, &db, SQLITE_OPEN_READONLY, NULL) != SQLITE_OK) {
		LOGE("数据库打开:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
		return level;
	}
	if (sqlite3_prepare_v2(db, zSql, -1, &ppStmt, NULL) != SQLITE_OK) {
		LOGE("sqlite3_prepare_v2:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
		return level;
	}

	while (sqlite3_step(ppStmt) == SQLITE_ROW) {
		if (strcmp(name, sqlite3_column_text(ppStmt, 2)) == 0) {
			if (strcmp(passwd, sqlite3_column_text(ppStmt, 3)) == 0) {
				level = sqlite3_column_int(ppStmt, 4);
			} else
				level = -1;
			break;
		}
	}
	sqlite3_finalize(ppStmt);
	sqlite3_close(db);
	return level;
}

JNIEXPORT jint JNICALL Java_com_cngc_hht_DataBase_getFactoryId(JNIEnv *env,
		jobject obj, jstring factoryname) {
	jint ret = 0;
	char *name;
	name = (*env)->GetStringUTFChars(env, factoryname, 0);
	LOGI("factoryname:%s\n", name);

	const char *zSql = "select * from factoryCodeInfo_tbl";
	sqlite3 *db;
	sqlite3_stmt *ppStmt = NULL;
	if (sqlite3_open_v2(DBPATH, &db, SQLITE_OPEN_READONLY, NULL) != SQLITE_OK) {
		LOGE("数据库打开:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}
	if (sqlite3_prepare_v2(db, zSql, -1, &ppStmt, NULL) != SQLITE_OK) {
		LOGE("sqlite3_prepare_v2:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}

	while (sqlite3_step(ppStmt) == SQLITE_ROW) {
		if (strcmp(name, (const char *) sqlite3_column_text(ppStmt, 1)) == 0) {
			ret = sqlite3_column_int(ppStmt, 0);
		}
	}
	(*env)->ReleaseStringUTFChars(env, factoryname, name);
	sqlite3_finalize(ppStmt);
	sqlite3_close(db);
	return ret;
}

JNIEXPORT jstring JNICALL Java_com_cngc_hht_DataBase_getFactory(JNIEnv *env,
		jobject obj, jint factoryId) {
	jstring ret = NULL;
	const char *zSql = "select * from factoryCodeInfo_tbl";
	sqlite3 *db;
	sqlite3_stmt *ppStmt = NULL;
	if (sqlite3_open_v2(DBPATH, &db, SQLITE_OPEN_READONLY, NULL) != SQLITE_OK) {
		LOGE("数据库打开:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}
	if (sqlite3_prepare_v2(db, zSql, -1, &ppStmt, NULL) != SQLITE_OK) {
		LOGE("sqlite3_prepare_v2:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}

	while (sqlite3_step(ppStmt) == SQLITE_ROW) {
		if (factoryId == sqlite3_column_int(ppStmt, 0)) {
			ret = (*env)->NewStringUTF(env, sqlite3_column_text(ppStmt, 1));
		}
	}
	sqlite3_finalize(ppStmt);
	sqlite3_close(db);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_cngc_hht_DataBase_getDeviceId(JNIEnv *env,
		jobject obj, jstring devicename) {
	jint ret = 0;
	char *name;
	name = (*env)->GetStringUTFChars(env, devicename, 0);
	LOGI("devicename:%s\n", name);

	const char *zSql = "select * from equipCodeInfo_tbl";
	sqlite3 *db;
	sqlite3_stmt *ppStmt = NULL;
	if (sqlite3_open_v2(DBPATH, &db, SQLITE_OPEN_READONLY, NULL) != SQLITE_OK) {
		LOGE("数据库打开:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}
	if (sqlite3_prepare_v2(db, zSql, -1, &ppStmt, NULL) != SQLITE_OK) {
		LOGE("sqlite3_prepare_v2:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}

	while (sqlite3_step(ppStmt) == SQLITE_ROW) {
		if (strcmp(name, (const char *) sqlite3_column_text(ppStmt, 1)) == 0) {
			ret = sqlite3_column_int(ppStmt, 0);
		}
	}
	(*env)->ReleaseStringUTFChars(env, devicename, name);
	sqlite3_finalize(ppStmt);
	sqlite3_close(db);
	return ret;
}

JNIEXPORT jstring JNICALL Java_com_cngc_hht_DataBase_getDeviceName(JNIEnv *env,
		jobject obj, jint nameId) {
	jstring ret = NULL;
	const char *zSql = "select * from equipCodeInfo_tbl";
	sqlite3 *db;
	sqlite3_stmt *ppStmt = NULL;
	if (sqlite3_open_v2(DBPATH, &db, SQLITE_OPEN_READONLY, NULL) != SQLITE_OK) {
		LOGE("数据库打开:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}
	if (sqlite3_prepare_v2(db, zSql, -1, &ppStmt, NULL) != SQLITE_OK) {
		LOGE("sqlite3_prepare_v2:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}

	while (sqlite3_step(ppStmt) == SQLITE_ROW) {
		if (nameId == sqlite3_column_int(ppStmt, 0)) {
			ret = (*env)->NewStringUTF(env, sqlite3_column_text(ppStmt, 1));
		}
	}
	sqlite3_finalize(ppStmt);
	sqlite3_close(db);
	return ret;
}

JNIEXPORT jint JNICALL Java_com_cngc_hht_DataBase_getModifyInfo(JNIEnv *env,
		jobject obj, jint devnum) {
	jstring modifydata = NULL;
	jstring faultdes = NULL;
	jstring faultsolution = NULL;
	const char *zSql = "select * from methodstwo";
	sqlite3 *db;
	sqlite3_stmt *ppStmt = NULL;
	if (sqlite3_open_v2(DBPATH_MODIFY, &db, SQLITE_OPEN_READONLY,
			NULL) != SQLITE_OK) {
		LOGE("数据库打开:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}
	if (sqlite3_prepare_v2(db, zSql, -1, &ppStmt, NULL) != SQLITE_OK) {
		LOGE("sqlite3_prepare_v2:%s", sqlite3_errmsg(db));
		sqlite3_close(db);
	}

	jclass cls = (*env)->GetObjectClass(env, obj);
	jmethodID mid = (*env)->GetMethodID(env, cls, "setFaultInfo",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	if (mid == NULL) {
		return -1;
	}

	while (sqlite3_step(ppStmt) == SQLITE_ROW) {
		if (devnum == sqlite3_column_int(ppStmt, 1)) {
			modifydata = (*env)->NewStringUTF(env,
					sqlite3_column_text(ppStmt, 8));
			faultdes = (*env)->NewStringUTF(env,
					sqlite3_column_text(ppStmt, 9));
			faultsolution = (*env)->NewStringUTF(env,
					sqlite3_column_text(ppStmt, 10));

			(*env)->CallVoidMethod(env, obj, mid, modifydata, faultdes,
					faultsolution);
		}
	}
	return 0;
}
