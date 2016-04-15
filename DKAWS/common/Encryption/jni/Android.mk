LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    :=encryption
LOCAL_SRC_FILES := encryption.c
include $(BUILD_SHARED_LIBRARY)
