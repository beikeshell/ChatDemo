LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

PF_ROOT_DIR := $(LOCAL_PATH)/../../../../../../tap4fun_pfsdk/pfsdk
CHAT_DIR := $(PF_ROOT_DIR)/chat/trunk/export
BASE_DIR := $(PF_ROOT_DIR)/base/trunk/export


LOCAL_MODULE := chat_jni

LOCAL_CPP_EXTENSION := .cpp .c

LOCAL_SRC_FILES := ChatJNI.cpp
LOCAL_SRC_FILES += $(CHAT_DIR)/lua

LOCAL_C_INCLUDES := $(BASE_DIR)/include \
                    $(CHAT_DIR)/include \
                    $(CHAT_DIR)/lua \

LOCAL_STATIC_LIBRARIES := base_static \
                          tfchat_static \


LOCAL_LDLIBS := -llog
LOCAL_ALLOW_UNDEFINED_SYMBOLS := true

include $(BUILD_SHARED_LIBRARY)

LOCAL_CFLAGS += -fPIC
LOCAL_CFLAGS += -O3
LOCAL_CFLAGS += -W
LOCAL_CFLAGS += -Wall
LOCAL_CFLAGS += -Wno-unused-parameter
LOCAL_CFLAGS += -Wno-empty-body
LOCAL_CFLAGS += -DPIC
LOCAL_CFLAGS += -D_LIB
LOCAL_CFLAGS += -D_MBCS
LOCAL_CFLAGS += -DPLATFORM_ANDROID
LOCAL_CFLAGS += -DHAVE_MEMMOVE

LOCAL_CPPFLAGS += -frtti
LOCAL_CPPFLAGS += -fexceptions

#LOCAL_LDLIBS += -llog

$(call import-add-path, $(LOCAL_PATH))
$(call import-add-path, $(PF_ROOT_DIR))

#$(call import-module, base/trunk/export/prebuilt/android)
$(call import-module, chat/trunk/export/prebuilt/android)

