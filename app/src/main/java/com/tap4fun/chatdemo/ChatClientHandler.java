package com.tap4fun.chatdemo;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatClientHandler {
    private static final String TAG = "ChatClientHandler";

    public static void onLogin(int success) {
        if (1 == success) {
            Log.d(TAG, "onLogin: login success.");
        } else {
            Log.d(TAG, "onLogin: login failed.");
        }
        ChatManager.getInstance().setLoginResult((1 == success) ? true : false);
    }

    public static void onGetMucRoomList(List<MucRoomItem> roomTitleList) {
        Log.d(TAG, "onGetMucRoomList: ");
        try {
            for (MucRoomItem item : roomTitleList) {
                Log.d(TAG, "onGetMucRoomList: " +
                        "room_id is [" + item.getId() + "], " +
                        "room_title is [" + item.getTitle() + "], " +
                        "room_image_id is [" + item.getImageId() +"].");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        List<MucRoomItem> rooms = new ArrayList<>();
        for (MucRoomItem item : roomTitleList) {
            rooms.add(new MucRoomItem(item.getId(), item.getTitle(), R.drawable.tx_muc_test1));
        }
        ChatManager.getInstance().setMucRoomList(rooms);
    }

    public static void onGetFriendList(List<FriendItem> friendItemList) {
        Log.d(TAG, "onGetFriendList: ");
        try {
            for (FriendItem item : friendItemList) {
                Log.d(TAG, "onGetFriendList: " +
                        "friend_id is [" + item.getId() + "], " +
                        "friend_nick_name is [" + item.getNickName() + "], " +
                        "friend_image_id is [" + item.getImageId() + "].");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        List<FriendItem> friends = new ArrayList<>();
        for (FriendItem item : friendItemList) {
            friends.add(new FriendItem(item.getId(), item.getNickName(), R.drawable.tx_muc_test2));
        }
        ChatManager.getInstance().setFriendList(friends);
    }
    public static void onMessageReceived(String from, String to, String chatType, String bodyType, String body) {
        Log.d(TAG, "onMessageReceived: " +
                "from [ " + from + "], " +
                "to [" + to + "], " +
                "chatType [" + chatType + "], " +
                "bodyType [" + bodyType + "], " +
                "body [" + body + "].");
        if (null == from || null == to || null == chatType || null == bodyType || null == body) {
            return;
        }
        ChatMessage msg = new ChatMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setMsgType(ChatMessage.MsgType.MSG_RECEIVED);
        msg.setChatType(ChatMessage.ChatType.valueOf(chatType));
        msg.setMsgBodyType(ChatMessage.MsgBodyType.valueOf(bodyType));
        msg.setBody(body);
        ChatManager.getInstance().onMessageReceived(msg);
    }

    public static void onCreateMucRoom(String roomId, String roomTitle, int success) {
        Log.d(TAG, "onCreateMucRoom: roomId [" + roomId + "], " +
                "roomTitle [" + roomTitle + "], " +
                "success [" + success + "].");
        ChatManager.getInstance().onCreateMucRoom(roomId, roomTitle, success);
    }
}
