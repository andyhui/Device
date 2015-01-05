LOCAL_PATH := $(call my-dir)

###############################################################################
#                        build libsanray_api.so                               #
############################################################################### 

include $(CLEAR_VARS)
LOCAL_C_INCLUDES := $(LOCAL_PATH)/sanray_api/\
					 $(LOCAL_PATH)/DataBase/
LOCAL_MODULE    := sanray_api
LOCAL_SRC_FILES := sanray_api/main.c sanray_api/rfid.c\
					  sanray_api/uart.c sanray_api/rfid_jni.c\
					  DataBase/sqlite3_db.c
LOCAL_LDLIBS := -llog -landroid -lsqlite
include $(BUILD_SHARED_LIBRARY)

###############################################################################
#                          build libdatabase.so                               #
############################################################################### 

include $(CLEAR_VARS)
LOCAL_C_INCLUDES := $(LOCAL_PATH)/DataBase/
LOCAL_MODULE    := database
LOCAL_SRC_FILES := DataBase/sqlite3_db.c
LOCAL_LDLIBS := -llog -landroid -lsqlite
include $(BUILD_SHARED_LIBRARY)

###############################################################################
#                          build update.so                                    #
############################################################################### 
include $(CLEAR_VARS)
LOCAL_C_INCLUDES := $(LOCAL_PATH)/update/
LOCAL_MODULE    := update
LOCAL_SRC_FILES := update/AcceptTCPConnection.c\
						update/CreateTCPServerSocket.c\
						update/HandleUDPClient.c\
						update/msg_conn.c
LOCAL_LDLIBS := -llog -landroid
include $(BUILD_SHARED_LIBRARY)