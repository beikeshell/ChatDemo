package com.tap4fun.chatdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class P2PChatActivity extends BaseActivity implements ChatManager.P2PMessageReceivedListener {
    private static final String TAG = "P2PChatActivity";

    private List<ChatMessage> msgList = new ArrayList<>();

    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private P2PChatMsgListAdapter adapter;
    private String toUserId;
    private String toUserNickName;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2p_chat);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mNavView= findViewById(R.id.nav_view);
        mNavView.setCheckedItem(R.id.nav_call);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }



        initMsgs();
        inputText = findViewById(R.id.p2p_chat_edit_text);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.d(TAG, "onEditorAction: Send p2p msg");
                Log.d(TAG, "onEditorAction: toUserId is [" + toUserId + "]");
                String content = inputText.getText().toString();
                if ("".equals(content)) {
                    return false;
                }
                //发送信息
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMsgType(ChatMessage.MsgType.MSG_SEND);
                chatMessage.setTo(toUserId);
                chatMessage.setBody(content);
                chatMessage.setChatType(ChatMessage.ChatType.P2P_CHAT);
                chatMessage.setMsgBodyType(ChatMessage.MsgBodyType.MESSAGE_BODY_TEXT);
                chatMessage.setSubject("");
                chatMessage.setUserData("");
                ChatManager.getInstance().sendMsg(chatMessage);

                inputText.setText("");
                return true;
            }
        });
        //send = findViewById(R.id.send_p2p_chat_msg);
        msgRecyclerView = findViewById(R.id.p2p_chat_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new P2PChatMsgListAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if ("".equals(content)) {
                    return;
                }
                P2PMessage msg = new P2PMessage(content, P2PMessage.TYPE_SENT);
                msgList.add(msg);
                adapter.notifyItemInserted(msgList.size() - 1);
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                inputText.setText("");
                //发送信息
                ChatManager.getInstance().sendP2PMsg(msg);
            }
        });*/
        toUserId = getIntent().getStringExtra("toUserId");
        toUserNickName = getIntent().getStringExtra("toUserNickName");

        ChatManager.getInstance().registerP2PMessageReceivedListener(this);
    }

    private void initMsgs() {
    }

    @Override
    public void onMessageReceived() {
        Log.d(TAG, "onMessageReceived: ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgList.clear();
                msgList.addAll(ChatManager.getInstance().getFriendMessage(toUserId));
                for (ChatMessage msg : msgList) {
                    Log.d(TAG, "run: content is [" + msg.getBody() + "].");
                }
                adapter.notifyDataSetChanged();
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        ChatManager.getInstance().unRegisterP2PMessageReceivedListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            }
            case R.id.backup: {
                Toast.makeText(this, "Your clicked Backup", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.delete: {
                Toast.makeText(this, "Your clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.settings: {
                Toast.makeText(this, "Your clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            }
            default: {
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
}
