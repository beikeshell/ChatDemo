package com.tap4fun.chatdemo;

import java.lang.Character.UnicodeBlock;

public class MucRoomItem {
    private String id;
    private String title;
    private int imageId;

    public MucRoomItem() {}

    public MucRoomItem(String id, String title, int imageId) {
        this.id = id;
        this.title = title;
        this.imageId = imageId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    

    public String getId() { return this.id; }
    public String getTitle() { return this.title; }
    public int getImageId() { return this.imageId; }
}
