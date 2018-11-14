package com.tap4fun.chatdemo;

public class MucMessage {
    private static final String TAG = "MucMessage";

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    private String userId;
    private String content;
    private int type;

    public MucMessage(String userId, String content, int type) {
        this.userId = userId;
        this.content = content;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
