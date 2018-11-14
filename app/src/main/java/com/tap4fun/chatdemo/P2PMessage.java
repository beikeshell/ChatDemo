package com.tap4fun.chatdemo;

public class P2PMessage {
    private static final String TAG = "P2PMessage";

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;

    private String content;
    private int type;

    public P2PMessage(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() { return this.content; }
    public int getType() { return this.type; }

}
