package com.tap4fun.chatdemo;

import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatManager {



    interface MucRoomListListener {
        void onMucRoomListChanged();
    }

    interface FriendListListener {
        void onFriendListChanged();
    }

    interface OnLoginResultListener {
        void onLoginResult(boolean result);
    }

    interface P2PMessageReceivedListener {
        void onMessageReceived();
    }

    interface P2GMessageReceivedListener {
        void onMessageReceived();
    }

    interface OnGetMucRoomMemberListListener {
        void onGetMucRoomMembersResult(String mucRoomId, List<String> members);
    }

    private static String mUserId = "UNKNOWN";
    private static String mUserNickName = "UNKNOWN";

    private static final String TAG = "ChatManager";

    private static ChatManager sInstance;
    private List<MucRoomItem> mMucRoomList = new ArrayList<>();
    private List<FriendItem> mFriendList = new ArrayList<>();
    private MucRoomListListener mMucRoomListListener;
    private FriendListListener mFriendListListener;
    private OnGetMucRoomMemberListListener mOnGetMucRoomMembersListener;
    private boolean mMucRoomListCached = false;
    private boolean mFriendListCached = false;
    private boolean mLoginResult;
    private OnLoginResultListener mOnLoginResultListener;
    private boolean mOnLoginResultCached = false;

    private HashMap<String, List<ChatMessage>> friendMsgCache = new HashMap<>();
    private HashMap<String, List<ChatMessage>> mucMsgCache = new HashMap<>();
    private HashMap<String, List<String>> mMucRoomMembersCache = new HashMap<>();

    private List<P2PMessageReceivedListener> mP2PMessageReceivedListenerList = new ArrayList<>();
    private List<P2GMessageReceivedListener> mP2GMessageReceivedListenerList = new ArrayList<>();

    private boolean mP2PMessageReceivedCached = false;
    private boolean mP2GMessageReceivedCached = false;

    private String mCurrentMucRoomId;

    public static ChatManager getInstance() {
        if (null == sInstance) {
            sInstance = new ChatManager();
        }
        return sInstance;
    }

    public static void init() {
        Log.d(TAG, "init: ");
        ChatJniInterface.init();
    }

    public static void setCachePath(String path) {
        Log.d(TAG, "setCachePath: ");
        ChatJniInterface.setCachePath(path);
    }
    public static void login(String userName, String nickName, String password, String IP, String port) {
        Log.d(TAG, "login: ");
        try {
            if (null != userName && !"".equals(userName)
                    && null != nickName && !"".equals(nickName)
                    && null != password && !"".equals(password)
                    && null != IP && !"".equals(IP)
                    && null != port && !"".equals(port)) {
                mUserId = userName;
                mUserNickName = nickName;
                ChatJniInterface.login(userName, nickName, password, IP, port);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setGameSpecInfo(String gameID, String gameKey, String gameServerId) {
        Log.d(TAG, "setGameSpecInfo: ");
        try {
            if (null != gameID && !"".equals(gameID)
                    && null != gameKey && !"".equals(gameKey)
                    && null != gameServerId && !"".equals(gameServerId)) {
                ChatJniInterface.setGameSpecInfo(gameID, gameKey, gameServerId);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        Log.d(TAG, "update: ");
        ChatJniInterface.update();
    }

    public static void createMucRoom(String roomId, String roomTitle) {
        if (null == roomId || null == roomTitle
                || roomId.isEmpty() || roomTitle.isEmpty()) {
            Log.e(TAG, "createMucRoom: roomId or roomTitle can't be null or empty");
            return;
        }
        Log.d(TAG, "createMucRoom: roomId [" + roomId + "], " +
                "roomTitle [" + roomTitle + "]");
        ChatJniInterface.createMucRoom(roomId, roomTitle);
    }


    private void notifyMucRoomListChanged() {
        if (null != mMucRoomListListener) {
            mMucRoomListListener.onMucRoomListChanged();
        } else {
            mMucRoomListCached = true;
        }
    }

    private void notifyFriendListChanged() {
        if (null != mFriendListListener) {
            mFriendListListener.onFriendListChanged();
        } else {
            mFriendListCached = true;
        }
    }

    public void setOnLogin(boolean success) {

    }

    public void setMucRoomList(List<MucRoomItem> rooms) {
        mMucRoomList = rooms;
        notifyMucRoomListChanged();
    }

    public void setFriendList(List<FriendItem> friends) {
        mFriendList = friends;
        notifyFriendListChanged();
    }

    public List<MucRoomItem> getMucRoomList() {
        mMucRoomListCached = false;
        return mMucRoomList;
    }

    public List<FriendItem> getFriendList() {
        mFriendListCached = false;
        return mFriendList;
    }

    public void setMucRoomListListener(MucRoomListListener listener) {
        mMucRoomListListener = listener;
        if (mMucRoomListCached) {
            if (null != mMucRoomListListener) {
                mMucRoomListListener.onMucRoomListChanged();
                mMucRoomListCached = false;
            }
        }
    }

    public void setFriendListListener(FriendListListener listener) {
        mFriendListListener = listener;
        if (mFriendListCached) {
            if (null != mFriendListListener) {
                mFriendListListener.onFriendListChanged();
                mFriendListCached = false;
            }
        }
    }

    public MucRoomListListener getMucRoomListListener() {
        return mMucRoomListListener;
    }

    public FriendListListener getFriendListListener() {
        return mFriendListListener;
    }

    public void setLoginResult(boolean result) {
        mLoginResult = result;
        if (!result) {
            mUserId  = "UNKNOWN";
            mUserNickName = "UNKNOWN";
        }
        notifyLoginResult();
    }

    private void notifyLoginResult() {
        if (null != mOnLoginResultListener) {
            mOnLoginResultListener.onLoginResult(mLoginResult);
        } else {
            mOnLoginResultCached = true;
        }
    }

    public void setOnLoginResultListener(OnLoginResultListener listener) {
        mOnLoginResultListener = listener;
        if (mOnLoginResultCached) {
            if (null != mOnLoginResultListener) {
                mOnLoginResultListener.onLoginResult(mLoginResult);
                mOnLoginResultCached = false;
            }
        }
    }

    public void sendMsg(ChatMessage msg) {
        if (null == msg) {
            return;
        }
        ChatJniInterface.sendMsg(msg.getTo(),
                msg.getChatType().name(),
                msg.getMsgBodyType().name(),
                msg.getBody(),
                msg.getSubject(),
                msg.getUserData());
        onMessageReceived(msg);
    }

    public void registerP2PMessageReceivedListener(P2PMessageReceivedListener listener) {
        if (null!= listener) {
            mP2PMessageReceivedListenerList.add(listener);
            if (mP2PMessageReceivedCached) {
                notifyP2PMessageReceived();
                mP2PMessageReceivedCached = false;
            }
        }
    }

    public void unRegisterP2PMessageReceivedListener(P2PMessageReceivedListener listener) {
        mP2PMessageReceivedListenerList.remove(listener);
    }

    public void registerP2GMessageReceivedListener(P2GMessageReceivedListener listener) {
        if (null != listener) {
            mP2GMessageReceivedListenerList.add(listener);
            if (mP2GMessageReceivedCached) {
                notifyP2GMessageReceived();
                mP2GMessageReceivedCached = false;
            }
        }
    }

    public void unRegisterP2GMessageReceivedListener(P2GMessageReceivedListener listener) {
        mP2GMessageReceivedListenerList.remove(listener);
    }

    public void onMessageReceived(ChatMessage message) {
        Log.d(TAG, "onMessageReceived: ");
        if (null == message) {
            return;
        }
        if (message.getChatType() == ChatMessage.ChatType.P2P_CHAT) {
            String name = (message.getMsgType() == ChatMessage.MsgType.MSG_SEND) ?
                    message.getTo() : message.getFrom();
            if (!friendMsgCache.containsKey(name)) {
                friendMsgCache.put(name, new ArrayList<ChatMessage>());
            }
            friendMsgCache.get(name).add(message);
            notifyP2PMessageReceived();
        } else if (message.getChatType() == ChatMessage.ChatType.P2G_CHAT) {
            if (mUserId.equals(message.getFrom())) {
                return;
            }
            String name = (message.getMsgType() == ChatMessage.MsgType.MSG_SEND) ?
                    message.getTo() : message.getTo();
            if (!mucMsgCache.containsKey(name)) {
                mucMsgCache.put(name, new ArrayList<ChatMessage>());
            }
            mucMsgCache.get(name).add(message);
            notifyP2GMessageReceived();
        }
    }

    private void notifyP2PMessageReceived() {
        Log.d(TAG, "notifyP2PMessageReceived: ");
        if (!mP2PMessageReceivedListenerList.isEmpty()) {
            for (P2PMessageReceivedListener listener : mP2PMessageReceivedListenerList) {
                listener.onMessageReceived();
            }
        } else {
            mP2PMessageReceivedCached = true;
        }
    }

    private void notifyP2GMessageReceived() {
        Log.d(TAG, "notifyP2GMessageReceived: ");
        if (!mP2GMessageReceivedListenerList.isEmpty()) {
            for (P2GMessageReceivedListener listener : mP2GMessageReceivedListenerList) {
                listener.onMessageReceived();
            }
        } else {
            mP2GMessageReceivedCached = true;
        }
    }

    public List<ChatMessage> getFriendMessage(String fromUserId) {
        if (friendMsgCache.containsKey(fromUserId)) {
            return friendMsgCache.get(fromUserId);
        } else {
            return new ArrayList<>();
        }
    }

    public List<ChatMessage> getMucMessage(String mucId) {
        if (mucMsgCache.containsKey(mucId)) {
            return mucMsgCache.get(mucId);
        } else {
            return new ArrayList<>();
        }
    }

    public void onCreateMucRoom(String roomId, String roomTitle, int success) {
        //TODO
        Log.d(TAG, "onCreateMucRoom: roomId [" + roomId + "], " +
                "roomTitle [" + roomTitle + "]," +
                "success [" + success + "]");
        if (1 == success) { //创建聊天室成功，刷新聊天室列表
            getMucRoomList();
        }
    }

    public boolean isMucRoomIdExisted(String roomId) {
        return mMucRoomList.contains(roomId);
    }

    public boolean canCreateMucRoom() {
        return !(mMucRoomList.size() >= 10);
    }


    public void quitMucRoom(String roomId) {
        if (null != roomId && !roomId.isEmpty()) {
            ChatJniInterface.quitMucRoom(roomId);
        }
    }

    public String getUserId() { return mUserId; }
    public String getUserNickName() { return mUserNickName; }

    public void setOnGetMucRoomMembersListener(OnGetMucRoomMemberListListener listener) {
        mOnGetMucRoomMembersListener = listener;
    }

    public void getMucRoomMembers(String mucRoomId) {
        if (null != mucRoomId && !mucRoomId.isEmpty()) {
            if (mMucRoomMembersCache.containsKey(mucRoomId)) {
                if (null != mOnGetMucRoomMembersListener) {
                    mOnGetMucRoomMembersListener.onGetMucRoomMembersResult(mucRoomId, mMucRoomMembersCache.get(mucRoomId));
                }
            } else {
                ChatJniInterface.getMucRoomMembers(mucRoomId);
            }
        }
    }

    public void onGetMucRoomMembers(String mucRoomId, List<String> members, boolean success) {
        if (success) {
            if (!mMucRoomMembersCache.containsKey(mucRoomId)) {
                mMucRoomMembersCache.put(mucRoomId, new ArrayList<String>());
            }
            mMucRoomMembersCache.get(mucRoomId).clear();
            mMucRoomMembersCache.get(mucRoomId).addAll(members);
            if (null != mOnGetMucRoomMembersListener) {
                mOnGetMucRoomMembersListener.onGetMucRoomMembersResult(mucRoomId, mMucRoomMembersCache.get(mucRoomId));
            }
        } else {
            Log.e(TAG, "onGetMucRoomMembers: failed!");
        }
    }

    public void setCurrentMucRoom(String currentMucId) {
        mCurrentMucRoomId = currentMucId;
    }

    public String getCurrentMucRoomId() { return mCurrentMucRoomId; }

    public void inviteFriend(String currentMucRoomId, List<String> ids) {
        //ChatJniInterface.inviteFriend(currentMucRoomId, ids);
    }
}
