package com.tap4fun.chatdemo;

import java.util.List;

public class ChatJniInterface {

    public static native void init();

    public static native void setCachePath(String path);

    public static native void login(String userId, String nickName, String password, String IP, String port);

    public static native void setGameSpecInfo(String gameId, String gameKey, String gameServerId);

    public static native void update();

    public static native void sendMsg(String toUserId, final String chatType, final String contentType, String content, String subject, String userData);

    public static native void createMucRoom(String roomId, String roomTitle);

    public static native void quitMucRoom(String roomId);

    public static native void getMucRoomList();

    public static native void getMucRoomMembers(String mucRoomId);

    //public static native void inviteFriend(String currentMucRoomId, List<String> ids);
}
