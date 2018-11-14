package com.tap4fun.chatdemo;

public class FriendItem {
    private String id;
    private String nickName;
    private int imageId;

    public FriendItem(String id, String nickName, int imageId) {
        this.id = id;
        this.nickName = nickName;
        this.imageId = imageId;
    }

    public String getId() { return this.id; }
    public String getNickName() { return this.nickName; }
    public int getImageId() { return this.imageId; }

}
