#include <jni.h>
#include <stdio.h>
#include <android/log.h>

#include "chat_client.h"
#include "chat_client_handler.h"
#include "base/logging.h"
#include "base/android/jni_helper.h"

using namespace std;
using namespace base::android;

static ChatClient *TestChatClient = nullptr;
static jclass chat_client_handler_class = nullptr;
static jclass muc_room_item_class = nullptr;
static jclass friend_item_class = nullptr;

struct TestChatClientHandler : ChatClientHandler {

    void OnLogin(ChatErrorCode error_code) override {
        //下面的逻辑需要独立出来
        JNIEnv *env = JniHelper::getEnv();
        if (nullptr == env) {
            LOG(ERROR) << "the JNIEnv cannot is nullptr.";
            return;
        }
        if (nullptr == chat_client_handler_class) {
            jclass tmp = env->FindClass("com/tap4fun/chatdemo/ChatClientHandler");
            chat_client_handler_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == chat_client_handler_class) {
            LOG(ERROR) << "Find class [com/tap4fun/chatdemo/ChatClientHandler] failed.";
            return;
        }

        static jmethodID on_login_method = nullptr;
        if (nullptr == on_login_method) {
            on_login_method = env->GetStaticMethodID(chat_client_handler_class, "onLogin", "(I)V");
        }
        if (nullptr == on_login_method) {
            LOG(ERROR) << "Find method [" << "onLogin" << "] failed!";
            return;
        }
        jint success = (error_code == CHAT_ERR_NO_ERROR) ? 1 : 0;
        env->CallStaticVoidMethod(chat_client_handler_class, on_login_method, success);

        if (error_code == CHAT_ERR_NO_ERROR) {
            LOG(INFO) << "Login success";
            TestChatClient->GetJoinedMucRooms();
            TestChatClient->FetchFriendsList();
        } else {
            LOG(INFO) << "Login failed";
        }
    }
    void OnLogout() override {
        LOG(INFO) << "OnLogout";
    }
    void OnDisconnected() override {
        LOG(INFO) << "OnDisconnected";
    }

    void OnMessageReceived(const ChatMessage &message) override {
        LOG(INFO) << "OnMessageReceived";
        LOG(INFO) << message.get_body()
        << message.get_chat_type() << std::endl
        << message.get_from() << std::endl
        << message.get_to() << std::endl
        << message.get_subject() << std::endl
        << message.get_session_id() << std::endl
        << message.get_user_data() << std::endl
        << message.get_body_type();
        //下面的逻辑需要独立出来
        JNIEnv *env = JniHelper::getEnv();
        if (nullptr == env) {
            LOG(ERROR) << "the JNIEnv cannot is nullptr.";
            return;
        }
        if (nullptr == chat_client_handler_class) {
            jclass tmp = env->FindClass("com/tap4fun/chatdemo/ChatClientHandler");
            chat_client_handler_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == chat_client_handler_class) {
            LOG(ERROR) << "Find class [com/tap4fun/chatdemo/ChatClientHandler] failed.";
            return;
        }

        static jmethodID on_message_received_method = nullptr;
        if (nullptr == on_message_received_method) {
            on_message_received_method = env->GetStaticMethodID(chat_client_handler_class, "onMessageReceived", 
                "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
        }
        if (nullptr == on_message_received_method) {
            LOG(ERROR) << "Find method [" << "onMessageReceived" << "] failed!";
            return;
        }
        jstring from = env->NewStringUTF(message.get_from().c_str());
        jstring to = env->NewStringUTF(message.get_to().c_str());
        std::string chatType = message.get_chat_type() == ChatMessage::ChatType::P2P_CHAT ? "P2P_CHAT" : "P2G_CHAT";
        jstring chat_type = env->NewStringUTF(chatType.c_str());
        std::string bodyType = "MESSAGE_BODY_UNKNOWN";
        if (message.get_body_type() == ChatMessage::MessageBodyType::MESSAGE_BODY_TEXT) {
            bodyType = "MESSAGE_BODY_TEXT";
        } else if (message.get_body_type() == ChatMessage::MessageBodyType::MESSAGE_BODY_PICTURE) {
            bodyType = "MESSAGE_BODY_PICTURE";
        } else if (message.get_body_type() == ChatMessage::MessageBodyType::MESSAGE_BODY_AUDIO) {
            bodyType = "MESSAGE_BODY_AUDIO";
        }
        jstring body_type = env->NewStringUTF(bodyType.c_str());
        jstring body = env->NewStringUTF(message.get_body().c_str());

        env->CallStaticVoidMethod(chat_client_handler_class, on_message_received_method, 
            from, to, chat_type, body_type, body);

    }
    void OnSendMessageFailed(const ChatMessage &message) override {
        LOG(INFO) << "OnSendMessageFailed";
    }

    void OnHistoryMessage(const string &session_id, const ChatMessageList &history_mesage) override {
        LOG(INFO) << "OnHistoryMessage";
    }

    void OnFriendsList(const UserList &friends) override {
        LOG(INFO) << "OnFriendsList";
        int i = 0;
        for(auto &item : friends) {
            LOG(INFO) << i << " character_id : [" << item.character_uid << "], jid : [" <<item.jid << "]";
        }
        LOG(INFO) << "---1---";
        //下面的逻辑需要独立出来
        JNIEnv *env = JniHelper::getEnv();
        if (nullptr == env) {
            LOG(ERROR) << "the JNIEnv cannot is nullptr.";
            return;
        }
        if (nullptr == chat_client_handler_class) {
            jclass tmp = env->FindClass("com/tap4fun/chatdemo/ChatClientHandler");
            chat_client_handler_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == chat_client_handler_class) {
            LOG(ERROR) << "Find class [com/tap4fun/chatdemo/ChatClientHandler] failed.";
            return;
        }

        LOG(INFO) << "---2---";
        static jmethodID on_get_friend_list_method = nullptr;
        if (nullptr == on_get_friend_list_method) {
            LOG(INFO) << "---3---";
            on_get_friend_list_method = env->GetStaticMethodID(chat_client_handler_class, "onGetFriendList", "(Ljava/util/List;)V");
            LOG(INFO) << "---4---";
        }
        if (nullptr == on_get_friend_list_method) {
            LOG(ERROR) << "Find method [" << "onGetFriendList" << "] failed!";
            return;
        }

        LOG(INFO) << "---5---";
        static jclass friend_list_class = nullptr;
        if (nullptr == friend_list_class) {
            jclass tmp = env->FindClass("java/util/ArrayList");
            friend_list_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == friend_list_class) {
            LOG(ERROR) << "Find class [java/util/ArrayList] failed.";
            return;
        }
        
        LOG(INFO) << "---6---";
        static jmethodID friend_list_constructor = nullptr;
        if (nullptr == friend_list_constructor) {
            friend_list_constructor = env->GetMethodID(friend_list_class, "<init>", "()V");
        }
        if (nullptr == friend_list_constructor) {
            LOG(ERROR) << "Find [friend list constructor] failed.";
            return;
        }

        LOG(INFO) << "---7---";
        static jmethodID friend_list_add_method = nullptr;
        if (nullptr == friend_list_add_method) {
            friend_list_add_method = env->GetMethodID(friend_list_class, "add", "(Ljava/lang/Object;)Z");
        }
        if (nullptr == friend_list_add_method) {
            LOG(ERROR) << "Find [friend_list_add_method] failed.";
            return;
        }

        LOG(INFO) << "---8---";
        jobject friend_list = env->NewObject(friend_list_class, friend_list_constructor);
        if (nullptr ==  friend_list) {
            LOG(ERROR) << "new ArrayList<>() failed.";
            return;
        }
        
        LOG(INFO) << "---9---";
        for (auto item : friends) {
            if (nullptr == friend_item_class) {
                jclass tmp = env->FindClass("com/tap4fun/chatdemo/FriendItem");
                friend_item_class = (jclass)env->NewGlobalRef(tmp);
                env->DeleteLocalRef(tmp);
            }
            if (nullptr == friend_item_class) {
                LOG(ERROR) << "Find class [" << "FriendItem" << "] failed!";
                return;
            }

            static jmethodID friend_item_constructor = nullptr;
            if (nullptr == friend_item_constructor) {
                friend_item_constructor = env->GetMethodID(friend_item_class, "<init>", "(Ljava/lang/String;Ljava/lang/String;I)V");
            }
            if (nullptr == friend_item_constructor) {
                LOG(ERROR) << "Find method [" << "init" << "] failed!";
                return;
            }

            jstring obj_friend_id = env->NewStringUTF(item.jid.c_str());
            jstring obj_friend_nick_name = env->NewStringUTF(item.character_uid.c_str());
            jint    obj_friend_image_id = 0;
            jobject newObj = env->NewObject(friend_item_class, friend_item_constructor, obj_friend_id, obj_friend_nick_name, obj_friend_image_id);

            if (nullptr != newObj) {
                env->CallBooleanMethod(friend_list, friend_list_add_method, newObj);
                env->DeleteLocalRef(newObj);
            } else {
                LOG(ERROR) << "new object failed!";
                return;
            }
        }

        LOG(INFO) << "---10---";
        env->CallStaticVoidMethod(chat_client_handler_class, on_get_friend_list_method, friend_list);
        env->DeleteLocalRef(friend_list);
    }
    
    void OnFollowRequest(const string &other, const string &extra_msg) override {
        LOG(INFO) << "OnFollowRequest other is [" << other << "], extra_msg is [" << extra_msg;
         //下面的逻辑需要独立出来
        JNIEnv *env = JniHelper::getEnv();
        if (nullptr == env) {
            LOG(ERROR) << "the JNIEnv cannot is nullptr.";
            return;
        }
        if (nullptr == chat_client_handler_class) {
            jclass tmp = env->FindClass("com/tap4fun/chatdemo/ChatClientHandler");
            chat_client_handler_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == chat_client_handler_class) {
            LOG(ERROR) << "Find class [com/tap4fun/chatdemo/ChatClientHandler] failed.";
            return;
        }
        static jmethodID on_follow_request_method = nullptr;
        if (nullptr == on_follow_request_method) {
            on_follow_request_method = env->GetStaticMethodID(chat_client_handler_class,
                "onFollowRequest", "(Ljava/lang/String;Ljava/lang/String;)V");
        }
        if (nullptr == on_follow_request_method) {
            LOG(ERROR) << "Find method [" << "onFollowRequest" << "] failed.";
            return;
        }
        jstring j_from = env->NewStringUTF(other.c_str());
        jstring j_msg = env->NewStringUTF(extra_msg.c_str());
        env->CallStaticVoidMethod(chat_client_handler_class, on_follow_request_method, j_from, 
            j_msg);

    }
    void OnFollowResult(const string &other, bool accept) override {
        LOG(INFO) << "OnFollowResult. other is [" << other;
         //下面的逻辑需要独立出来
        JNIEnv *env = JniHelper::getEnv();
        if (nullptr == env) {
            LOG(ERROR) << "the JNIEnv cannot is nullptr.";
            return;
        }
        if (nullptr == chat_client_handler_class) {
            jclass tmp = env->FindClass("com/tap4fun/chatdemo/ChatClientHandler");
            chat_client_handler_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == chat_client_handler_class) {
            LOG(ERROR) << "Find class [com/tap4fun/chatdemo/ChatClientHandler] failed.";
            return;
        }
        static jmethodID on_follow_result_method = nullptr;
        if (nullptr == on_follow_result_method) {
            on_follow_result_method = env->GetStaticMethodID(chat_client_handler_class,
                "onFollowResult", "(Ljava/lang/String;I)V");
        }
        if (nullptr == on_follow_result_method) {
            LOG(ERROR) << "Find method [" << "onFollowResult" << "] failed.";
            return;
        }
        jstring j_from = env->NewStringUTF(other.c_str());
        jint accepted = accept ? 1 : 0;
        env->CallStaticVoidMethod(chat_client_handler_class, on_follow_result_method, j_from, 
            accepted);
        if (accept) {
            if (nullptr != TestChatClient) {
                TestChatClient->FetchFriendsList();
            }
        }
    }
    void OnUnfollowRequest(const string &from) override {
        LOG(INFO) << "OnUnfollowRequest";
       //下面的逻辑需要独立出来
        JNIEnv *env = JniHelper::getEnv();
        if (nullptr == env) {
            LOG(ERROR) << "the JNIEnv cannot is nullptr.";
            return;
        }
        if (nullptr == chat_client_handler_class) {
            jclass tmp = env->FindClass("com/tap4fun/chatdemo/ChatClientHandler");
            chat_client_handler_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == chat_client_handler_class) {
            LOG(ERROR) << "Find class [com/tap4fun/chatdemo/ChatClientHandler] failed.";
            return;
        }
        static jmethodID on_unfollow_method = nullptr;
        if (nullptr == on_unfollow_method) {
            on_unfollow_method = env->GetStaticMethodID(chat_client_handler_class,
                "onUnFollow", "(Ljava/lang/String;)V");
        }
        if (nullptr == on_unfollow_method) {
            LOG(ERROR) << "Find method [" << "onUnFollow" << "] failed.";
            return;
        }
        jstring j_from = env->NewStringUTF(from.c_str());
        env->CallStaticVoidMethod(chat_client_handler_class, on_unfollow_method, j_from);

        if (nullptr != TestChatClient) {
            TestChatClient->FetchFriendsList();
        }
    }
    void OnStatusChange(const string &other, int status) override {
        LOG(INFO) << "OnStatusChange";
    }

    void OnBlackList(const UserList &black_list) override {
        LOG(INFO) << "OnBlackList";
    }
    void OnBlackListChanged() override {
        LOG(INFO) << "OnBlackListChanged";
    }

    void OnSearchUserResult(const UserList &search_result) override {
        LOG(INFO) << "OnSearchUserResult";
    }

    void OnEnterGroup(const string &session_id, const string &title, bool success) override {
        if (success) {
            LOG(INFO) << "OnEnterGroup success : session_id is ["
            << session_id << "], title is ["
            << title << "]";
        }
    }

    void OnCreateMucRoom(const string &room_cid, const string &room_title, bool success) override {
        if (success) {
            LOG(INFO) << "OnCreateMucRoom success : room_cid is ["
            << room_cid << "] room_title is ["
            << room_title << "]";

        }
        else {
            LOG(INFO) << "OnCreateMucRoom failed : room_cid is ["
            << room_cid << "] room_title is ["
            << room_title << "]";
        }
        //下面的逻辑需要独立出来
        JNIEnv *env = JniHelper::getEnv();
        if (nullptr == env) {
            LOG(ERROR) << "the JNIEnv cannot is nullptr.";
            return;
        }
        if (nullptr == chat_client_handler_class) {
            jclass tmp = env->FindClass("com/tap4fun/chatdemo/ChatClientHandler");
            chat_client_handler_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == chat_client_handler_class) {
            LOG(ERROR) << "Find class [com/tap4fun/chatdemo/ChatClientHandler] failed.";
            return;
        }
        static jmethodID on_create_muc_room_method = nullptr;
        if (nullptr == on_create_muc_room_method) {
            on_create_muc_room_method = env->GetStaticMethodID(chat_client_handler_class,
                "onCreateMucRoom", "(Ljava/lang/String;Ljava/lang/String;I)V");
        }
        if (nullptr == on_create_muc_room_method) {
            LOG(ERROR) << "Find method [" << "onCreateMucRoom" << "] failed.";
            return;
        }
        //TODO
        jstring j_room_id = env->NewStringUTF(room_cid.c_str());
        jstring j_room_title = env->NewStringUTF(room_title.c_str());
        jint succ = success ? 1 : 0; 
        env->CallStaticVoidMethod(chat_client_handler_class, on_create_muc_room_method, j_room_id, 
            j_room_title, succ);

        if (success) {
            TestChatClient->GetJoinedMucRooms();
        }

    }

    void OnGetMucRoomList(const MucRoomInfoList &room_list) override {
        LOG(INFO) << "OnGetMucRoomList";
        int i = 0;
        for(auto item : room_list) {
            LOG(INFO) << i++ << " " << item.room_cid << " " << item.room_title;
        }
        //下面的逻辑需要独立出来
        JNIEnv *env = JniHelper::getEnv();
        if (nullptr == env) {
            LOG(ERROR) << "the JNIEnv cannot is nullptr.";
            return;
        }
        if (nullptr == chat_client_handler_class) {
            jclass tmp = env->FindClass("com/tap4fun/chatdemo/ChatClientHandler");
            chat_client_handler_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == chat_client_handler_class) {
            LOG(ERROR) << "Find class [com/tap4fun/chatdemo/ChatClientHandler] failed.";
            return;
        }
        static jmethodID on_get_muc_room_list_method = nullptr;
        if (nullptr == on_get_muc_room_list_method) {
            LOG(INFO) << "---111---";
            on_get_muc_room_list_method = env->GetStaticMethodID(chat_client_handler_class, "onGetMucRoomList", "(Ljava/util/List;)V");
            LOG(INFO) << "---222---";
        }
        if (nullptr == on_get_muc_room_list_method) {
            LOG(ERROR) << "Find static interface [onGetMucRoomList] failed.";
            return;
        }

        static jclass muc_room_list_class = nullptr;
        if (nullptr == muc_room_list_class) {
            jclass tmp = env->FindClass("java/util/ArrayList");
            muc_room_list_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == muc_room_list_class) {
            LOG(ERROR) << "Find class [java/util/ArrayList] failed.";
            return;
        }

        static jmethodID muc_room_list_constructor = nullptr;
        if (nullptr == muc_room_list_constructor) {
            muc_room_list_constructor = env->GetMethodID(muc_room_list_class, "<init>", "()V");
        }
        if (nullptr == muc_room_list_constructor) {
            LOG(ERROR) << "Find [mucRoom list constructor] failed.";
            return;
        }

        static jmethodID muc_room_list_add_method = nullptr;
        if (nullptr == muc_room_list_add_method) {
            muc_room_list_add_method = env->GetMethodID(muc_room_list_class, "add", "(Ljava/lang/Object;)Z");
        }
        if (nullptr == muc_room_list_add_method) {
            LOG(ERROR) << "Find [muc_room_list_add_method] failed.";
            return;
        }

        jobject mucRoomList = env->NewObject(muc_room_list_class, muc_room_list_constructor);
        if (nullptr ==  mucRoomList) {
            LOG(ERROR) << "new List<String> failed.";
            return;
        }

        for (auto item : room_list) {
            /*
            jstring obj_room_title = env->NewStringUTF(item.room_cid.c_str());
            if (nullptr == obj_room_title) {
                LOG(ERROR) << "new jstring [" << item.room_cid.c_str() << "] failed.";
                continue;
            } else {
                env->CallBooleanMethod(mucRoomList, muc_room_list_add_method, obj_room_title);
                env->DeleteLocalRef(obj_room_title);
            }
            */

            //TODO
            if (nullptr == muc_room_item_class) {
                jclass tmp = env->FindClass("com/tap4fun/chatdemo/MucRoomItem");
                muc_room_item_class = (jclass)env->NewGlobalRef(tmp);
                env->DeleteLocalRef(tmp);
            }
            if (nullptr == muc_room_item_class) {
                LOG(ERROR) << "Find class [" << "MucRoomItem" << "] failed!";
                return;
            }

            static jmethodID muc_room_item_constructor = nullptr;
            if (nullptr == muc_room_item_constructor) {
                muc_room_item_constructor = env->GetMethodID(muc_room_item_class, "<init>", "(Ljava/lang/String;Ljava/lang/String;I)V");
            }
            if (nullptr == muc_room_item_constructor) {
                LOG(ERROR) << "Find method [" << "init" << "] failed!";
                return;
            }

            jstring obj_room_id = env->NewStringUTF(item.room_cid.c_str());
            jstring obj_room_title = env->NewStringUTF(item.room_title.c_str());
            jint    obj_room_image_id = 0;
            jobject newObj = env->NewObject(muc_room_item_class, muc_room_item_constructor, obj_room_id, obj_room_title, obj_room_image_id);

            if (nullptr != newObj) {
                env->CallBooleanMethod(mucRoomList, muc_room_list_add_method, newObj);
                env->DeleteLocalRef(newObj);
            } else {
                LOG(ERROR) << "new object failed!";
                return;
            }
        }

        env->CallStaticVoidMethod(chat_client_handler_class, on_get_muc_room_list_method, mucRoomList);
        env->DeleteLocalRef(mucRoomList);
    }

    void OnInviteToMucRoom(const string &room_cid, const string &room_title, const string &sender_jid) override {
        LOG(INFO) << "OnInviteToMucRoom : room_cid is [" << room_cid << "], room_title is [" << room_title
        << "user id is [" << sender_jid << "]";
    }

    void OnGetMemberOfMucRoom(const string &room_cid, vector<string> &member_list) override {
        LOG(INFO) << "OnGetMemberOfMucRoom";
        LOG(INFO) << "Get mucroom [" << room_cid << "] members.";
        for (auto &member : member_list) {
            LOG(INFO) << member;
        }

        //下面的逻辑需要独立出来
        JNIEnv *env = JniHelper::getEnv();
        if (nullptr == env) {
            LOG(ERROR) << "the JNIEnv cannot is nullptr.";
            return;
        }
        if (nullptr == chat_client_handler_class) {
            jclass tmp = env->FindClass("com/tap4fun/chatdemo/ChatClientHandler");
            chat_client_handler_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == chat_client_handler_class) {
            LOG(ERROR) << "Find class [com/tap4fun/chatdemo/ChatClientHandler] failed.";
            return;
        }

        static jmethodID on_get_muc_room_members_method = nullptr;
        if (nullptr == on_get_muc_room_members_method) {
            on_get_muc_room_members_method = env->GetStaticMethodID(chat_client_handler_class, "onGetMucRoomMembers", 
                "(Ljava/lang/String;Ljava/util/List;)V");
        }
        if (nullptr == on_get_muc_room_members_method) {
            LOG(ERROR) << "Find method [" << "onGetMucRoomMembers" << "] failed!";
            return;
        }

        static jclass muc_room_member_list_class = nullptr;
        if (nullptr == muc_room_member_list_class) {
            jclass tmp = env->FindClass("java/util/ArrayList");
            muc_room_member_list_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == muc_room_member_list_class) {
            LOG(ERROR) << "Find class [java/util/ArrayList] failed.";
            return;
        }

        static jmethodID muc_room_member_list_constructor = nullptr;
        if (nullptr == muc_room_member_list_constructor) {
            muc_room_member_list_constructor = env->GetMethodID(muc_room_member_list_class, "<init>", "()V");
        }
        if (nullptr == muc_room_member_list_constructor) {
            LOG(ERROR) << "Find [mucRoomMember list constructor] failed.";
            return;
        }

        static jmethodID muc_room_member_list_add_method = nullptr;
        if (nullptr == muc_room_member_list_add_method) {
            muc_room_member_list_add_method = env->GetMethodID(muc_room_member_list_class, "add", "(Ljava/lang/Object;)Z");
        }
        if (nullptr == muc_room_member_list_add_method) {
            LOG(ERROR) << "Find [muc_room_member_list_add_method] failed.";
            return;
        }

        jobject mucRoomMemberList = env->NewObject(muc_room_member_list_class, muc_room_member_list_constructor);
        if (nullptr ==  mucRoomMemberList) {
            LOG(ERROR) << "new List<String> failed.";
            return;
        }

        for (const auto &item : member_list) {
            jstring j_member = env->NewStringUTF(item.c_str());
            if (nullptr == j_member) {
                LOG(ERROR) << "new jstring [" << item << "] failed!";
                continue;
            } else {
                env->CallBooleanMethod(mucRoomMemberList, muc_room_member_list_add_method, j_member);
                env->DeleteLocalRef(j_member);
            }
        }
        jstring j_roomId = env->NewStringUTF(room_cid.c_str());

        env->CallStaticVoidMethod(chat_client_handler_class, on_get_muc_room_members_method, j_roomId, mucRoomMemberList);
        env->DeleteLocalRef(j_roomId);
        env->DeleteLocalRef(mucRoomMemberList);

    }
    void OnJoinMucRoom(const string &room_cid, bool success) override {
        if (success) {
            LOG(INFO) << "OnJoinMucRoom [" << room_cid << "] success";
        }
        else {
            LOG(INFO) << "OnJoinMucRoom [" << room_cid << "] failed";
        }
    }
    void OnLeaveMucRoom(const string &room_cid) override {
        LOG(INFO) << "OnLeaveMucRoom";
        LOG(INFO) << "OnLeaveMucRoom : room_cid is [" << room_cid << "]";
        if (nullptr != TestChatClient) {
            TestChatClient->GetJoinedMucRooms();
        }
    }
    void OnChangeMucRoomTitle(const string &room_cid, const string &new_title) override {
        LOG(INFO) << "OnChangeMucRoomTitle : room_cid is [" << room_cid << "], new title is [" << new_title << "]";
    }
    void OnOtherLeaveMucRoom(const string &room_cid, const string &jid) override {
        LOG(INFO) << "OnOtherLeaveMucRoom : room_cid is [" << room_cid << "], user id is [" << jid << "]";
        if (nullptr != TestChatClient) {
            TestChatClient->GetMemberOfMucRoom(room_cid);
        }
    }
    void OnOtherJoinMucRoom(const string &room_cid, const string &jid) override {
        LOG(INFO) << "OnOtherJoinMucRoom : room_cid is [" << room_cid << "], user id is [" << jid <<"]";
        if (nullptr != TestChatClient) {
            TestChatClient->GetMemberOfMucRoom(room_cid);
        }
    }
};



extern "C" {

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_init(JNIEnv *env, jobject obj) {
        if (nullptr == TestChatClient) {
            TestChatClient = new ChatClient();
            TestChatClient->set_handler(new TestChatClientHandler());
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_setCachePath(JNIEnv *env, jobject obj, jstring path) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_setCachePath";
        const char *c_cache_path = env->GetStringUTFChars(path, nullptr);
        if (nullptr == c_cache_path) { return; }
        std::string cache_path_str = c_cache_path;
        env->ReleaseStringUTFChars(path, c_cache_path);
        if (nullptr != TestChatClient) {
            TestChatClient->SetWritePath(cache_path_str);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_login(JNIEnv *env, jobject obj,
        jstring user_name, jstring password, jstring nick_name, jstring IP, jstring port) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_login";
        const char *c_user_name = env->GetStringUTFChars(user_name, nullptr);
        const char *c_nick_name = env->GetStringUTFChars(nick_name, nullptr);
        const char *c_password = env->GetStringUTFChars(password, nullptr);
        const char *c_ip = env->GetStringUTFChars(IP, nullptr);
        const char *c_port = env->GetStringUTFChars(port, nullptr);
        if (nullptr == c_user_name || nullptr == c_nick_name || nullptr == c_password || nullptr == c_ip || nullptr == c_port) {
            return;
        }
        std::string user_name_str = c_user_name;
        std::string nick_name_str = c_nick_name;
        std::string password_str = c_password;
        std::string ip_str = c_ip;
        std::string port_str = c_port;

        env->ReleaseStringUTFChars(user_name, c_user_name);
        env->ReleaseStringUTFChars(nick_name, c_nick_name);
        env->ReleaseStringUTFChars(password, c_password);
        env->ReleaseStringUTFChars(IP, c_ip);
        env->ReleaseStringUTFChars(port, c_port);

        if (nullptr != TestChatClient) {
            /*TestChatClient->Login("local|2|g303|9001|65502569326393@chat.pf.tap4fun.com",
                                                  "6mr48DVnMJ77",
                                                  "ABC",
                                                  "e.portal-platform.t4f.cn",
                                                    30065); */
            TestChatClient->Login(user_name_str, password_str, nick_name_str, ip_str, std::stoi(port_str));
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_setGameSpecInfo(JNIEnv *env, jobject obj,
        jstring gameId, jstring gameKey, jstring gameServerId) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_setGameSpecInfo";
        //TODO
        const char *c_game_id = env->GetStringUTFChars(gameId, nullptr);
        const char *c_game_key = env->GetStringUTFChars(gameKey, nullptr);
        const char *c_game_server_id = env->GetStringUTFChars(gameServerId, nullptr);
        if (nullptr == c_game_id || nullptr == c_game_key || nullptr == c_game_server_id) {
            return;
        }
        std::string game_id_str = c_game_id;
        std::string game_key_str = c_game_key;
        std::string game_server_id_str = c_game_server_id;

        env->ReleaseStringUTFChars(gameId, c_game_id);
        env->ReleaseStringUTFChars(gameKey, c_game_key);
        env->ReleaseStringUTFChars(gameServerId, c_game_server_id);

        if (nullptr != TestChatClient) {
            //TestChatClient->SetGameSpecInfo("G303", "yhRhrEHjIb", "1");
            TestChatClient->SetGameSpecInfo(game_id_str, game_key_str, game_server_id_str);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_update(JNIEnv *env, jobject obj) {
        //LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_update";
        if (nullptr != TestChatClient) {
            TestChatClient->Update();
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_createMucRoom(JNIEnv *env, jobject obj,
        jstring roomId, jstring roomTitle) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_createMucRoom";
        std::string room_id_str = JniHelper::jstring2string(roomId);
        std::string room_title_str = JniHelper::jstring2string(roomTitle);
        if (nullptr != TestChatClient) {
            TestChatClient->CreateMucRoom(room_id_str, room_title_str);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_sendMsg(JNIEnv *env, jobject obj, jstring toUserId,
        jstring chatType, jstring contentType, jstring content, jstring subject, jstring userData) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_sendMsg";
        const char *c_to_user_id = env->GetStringUTFChars(toUserId, nullptr);
        const char *c_chat_type = env->GetStringUTFChars(chatType, nullptr);
        const char *c_content_type = env->GetStringUTFChars(contentType, nullptr);
        const char *c_content = env->GetStringUTFChars(content, nullptr);
        const char *c_subject = env->GetStringUTFChars(subject, nullptr);
        const char *c_user_data = env->GetStringUTFChars(userData, nullptr);
        if (nullptr == c_to_user_id || nullptr == c_chat_type || nullptr == c_content_type ||
            nullptr == c_content || nullptr == c_subject || nullptr == c_user_data) {
            return;
        }
        std::string to_user_id_str = c_to_user_id;
        std::string chat_type_str = c_chat_type;
        std::string content_type_str = c_content_type;
        std::string content_str = c_content;
        std::string subject_str = c_subject;
        std::string user_data_str = c_user_data;

        env->ReleaseStringUTFChars(toUserId, c_to_user_id);
        env->ReleaseStringUTFChars(chatType, c_chat_type);
        env->ReleaseStringUTFChars(contentType, c_content_type);
        env->ReleaseStringUTFChars(content, c_content);
        env->ReleaseStringUTFChars(subject, c_subject);
        env->ReleaseStringUTFChars(userData, c_user_data);
        if (nullptr != TestChatClient) {
            ChatMessage msg;
            msg.set_to(XmppJid(to_user_id_str));
            if (chat_type_str == "P2P_CHAT") {
                msg.set_chat_type(ChatMessage::P2P_CHAT);
            } else {
                msg.set_chat_type(ChatMessage::P2G_CHAT);
            }
            if (content_type_str == "TEXT") {
                msg.set_body_type(ChatMessage::MESSAGE_BODY_TEXT);
            } else if (content_type_str == "IMAGE") {
                msg.set_body_type(ChatMessage::MESSAGE_BODY_PICTURE);
            } else if (content_type_str == "AUDIO") {
                msg.set_body_type(ChatMessage::MESSAGE_BODY_AUDIO);
            }
            msg.set_body(content_str);
            msg.set_subject(subject_str);
            msg.set_user_data(user_data_str);
            TestChatClient->SendChatMessage(msg);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_getMucRoomList(JNIEnv *env, jobject obj) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_getMucRoomList";
        if (nullptr != TestChatClient) {
            TestChatClient->GetJoinedMucRooms();
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_quitMucRoom(JNIEnv *env, jobject obj, 
        jstring roomId) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_quitMucRoom";
        std::string room_id_str = JniHelper::jstring2string(roomId);
        if (nullptr != TestChatClient) {
            TestChatClient->LeaveMucRoom(room_id_str);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_getMucRoomMembers(JNIEnv *env, jobject obj, 
        jstring roomId) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_getMucRoomMembers";
        std::string room_id_str = JniHelper::jstring2string(roomId);

        if (nullptr != TestChatClient) {
            TestChatClient->GetMemberOfMucRoom(room_id_str);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_inviteFriend(JNIEnv *env, jobject obj, 
        jstring roomId, jobject ids) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_inviteFriend";
        std::string room_id_str = JniHelper::jstring2string(roomId);
        

        static jclass id_list_class = nullptr;
        if (nullptr == id_list_class) {
            jclass tmp = env->FindClass("java/util/List");
            id_list_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == id_list_class) {
            LOG(ERROR) << "find class [" << "java/util/List" << "] failed.";
            return;
        }
        if (!env->IsInstanceOf(ids, id_list_class)) {
            LOG(ERROR) << "ids type error!";
            return;
        }
        static jmethodID size_method = nullptr;
        if (nullptr == size_method) {
            size_method = env->GetMethodID(id_list_class, "size", "()I");
        }
        if (nullptr == size_method) {
            LOG(ERROR) << "find method size() failed!";
            return;
        }
        static jmethodID get_method = nullptr;
        if (nullptr == get_method) {
            get_method = env->GetMethodID(id_list_class, "get", "(I)Ljava/lang/Object;");
        }
        if (nullptr == get_method) {
            LOG(ERROR) << "find method get() failed!";
            return;
        }
        std::vector<std::string> friendIds;
        jint id_list_size = env->CallIntMethod(ids, size_method);
        for (int i = 0; i < id_list_size; ++i) {
            jstring j_id = (jstring)env->CallObjectMethod(ids, get_method, i);
            if (nullptr == j_id) {
                LOG(ERROR) << "id is nullptr at position " << i;
                continue;
            }
            std::string id_str = JniHelper::jstring2string(j_id);
            friendIds.push_back(id_str);
            env->DeleteLocalRef(j_id);
        }
        if (nullptr != TestChatClient) {
            TestChatClient->InviteToMucRoom(room_id_str, friendIds, true);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_kickFromMucRoom(JNIEnv *env, jobject obj, 
        jstring roomId, jobject ids) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_kickFromMucRoom";
        std::string room_id_str = JniHelper::jstring2string(roomId);
        

        static jclass id_list_class = nullptr;
        if (nullptr == id_list_class) {
            jclass tmp = env->FindClass("java/util/List");
            id_list_class = (jclass)env->NewGlobalRef(tmp);
            env->DeleteLocalRef(tmp);
        }
        if (nullptr == id_list_class) {
            LOG(ERROR) << "find class [" << "java/util/List" << "] failed.";
            return;
        }
        if (!env->IsInstanceOf(ids, id_list_class)) {
            LOG(ERROR) << "ids type error!";
            return;
        }
        static jmethodID size_method = nullptr;
        if (nullptr == size_method) {
            size_method = env->GetMethodID(id_list_class, "size", "()I");
        }
        if (nullptr == size_method) {
            LOG(ERROR) << "find method size() failed!";
            return;
        }
        static jmethodID get_method = nullptr;
        if (nullptr == get_method) {
            get_method = env->GetMethodID(id_list_class, "get", "(I)Ljava/lang/Object;");
        }
        if (nullptr == get_method) {
            LOG(ERROR) << "find method get() failed!";
            return;
        }
        std::vector<std::string> friendIds;
        jint id_list_size = env->CallIntMethod(ids, size_method);
        for (int i = 0; i < id_list_size; ++i) {
            jstring j_id = (jstring)env->CallObjectMethod(ids, get_method, i);
            if (nullptr == j_id) {
                LOG(ERROR) << "id is nullptr at position " << i;
                continue;
            }
            std::string id_str = JniHelper::jstring2string(j_id);
            friendIds.push_back(id_str);
            env->DeleteLocalRef(j_id);
        }
        if (nullptr != TestChatClient) {
            TestChatClient->KickFromMucRoom(room_id_str, friendIds);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_sendFollowRequest(JNIEnv *env, jobject obj, 
        jstring userId, jstring msg) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_sendFollowRequest";
        std::string user_id_str = JniHelper::jstring2string(userId);
        std::string msg_str = JniHelper::jstring2string(msg);
        if (nullptr != TestChatClient) {
            TestChatClient->SendFollowRequest(user_id_str, msg_str);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_ackFollowRequest(JNIEnv *env, jobject obj, 
        jstring userId, jstring msg, jint agree) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_ackFollowRequest";
        std::string user_id_str = JniHelper::jstring2string(userId);
        std::string msg_str = JniHelper::jstring2string(msg);
        bool agreed = (agree == 1) ? true : false;
        if (nullptr != TestChatClient) {
            TestChatClient->AckFollowRequest(user_id_str, msg_str, agreed);
        }
    }

    JNIEXPORT void JNICALL Java_com_tap4fun_chatdemo_ChatJniInterface_unFollowRequest(JNIEnv *env, jobject obj, 
        jstring userId) {
        LOG(INFO) << "Java_com_tap4fun_chatdemo_ChatJniInterface_unFollowRequest";
        std::string user_id_str = JniHelper::jstring2string(userId);
        if (nullptr != TestChatClient) {
            TestChatClient->Unfollow(user_id_str);
            TestChatClient->FetchFriendsList();
        }
    }

    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
        LOG(INFO) << "JNI_OnLoad";
        JniHelper::setJavaVM(vm);

        JNIEnv *env = nullptr;
        vm->GetEnv((void **)&env, JNI_VERSION_1_4);        

        JniHelper::InitJni(env);
        return JNI_VERSION_1_4;
    }
}