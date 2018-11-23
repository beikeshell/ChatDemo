package com.tap4fun.chatdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendActivity extends BaseActivity {

    private static final String TAG = InviteFriendActivity.class.getSimpleName();

    private List<FriendItem> mFriendList = new ArrayList<>();
    private InviteFriendAdapter mInviteFriendAdapter;
    private RecyclerView mRecycleView;

    private String mMucRoomId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        mMucRoomId = getIntent().getStringExtra("mucRoomId");

        mRecycleView = findViewById(R.id.friend_list_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);

        mFriendList = ChatManager.getInstance().getFriendList();

        mInviteFriendAdapter = new InviteFriendAdapter(this, mFriendList);

        mRecycleView.setAdapter(mInviteFriendAdapter);

    }

    public String getMucRoomId() { return mMucRoomId; }
}
