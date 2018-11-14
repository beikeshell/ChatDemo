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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MucChatActivity extends BaseActivity implements ChatManager.P2GMessageReceivedListener {
    private static final String TAG = "MucChatActivity";

    private List<ChatMessage> msgList = new ArrayList<>();

    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MucChatMsgListAdapter adapter;
    private String currentMucId;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muc_chat);
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
        inputText = findViewById(R.id.muc_chat_edit_text);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String content = inputText.getText().toString();
                if ("".equals(content)) {
                    return false;
                }
                //发送信息
                Log.d(TAG, "onEditorAction: send to muc [" + currentMucId + "]");
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMsgType(ChatMessage.MsgType.MSG_SEND);
                chatMessage.setTo(currentMucId);
                chatMessage.setBody(content);
                chatMessage.setChatType(ChatMessage.ChatType.P2G_CHAT);
                chatMessage.setMsgBodyType(ChatMessage.MsgBodyType.MESSAGE_BODY_TEXT);
                chatMessage.setSubject("");
                chatMessage.setUserData("");
                ChatManager.getInstance().sendMsg(chatMessage);
                /*MucMessage mucMessage = new MucMessage("", content, MucMessage.TYPE_SENT);
                msgList.add(mucMessage);
                adapter.notifyItemInserted(msgList.size() - 1);
                msgRecyclerView.scrollToPosition(msgList.size() - 1);*/
                inputText.setText("");
                return true;
            }
        });
        //send = findViewById(R.id.send_muc_chat_msg);
        msgRecyclerView = findViewById(R.id.muc_chat_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MucChatMsgListAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if ("".equals(content)) {
                    return;
                }
                MucMessage mucMessage = new MucMessage("", content, MucMessage.TYPE_SENT);
                msgList.add(mucMessage);
                adapter.notifyItemInserted(msgList.size() - 1);
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                inputText.setText("");
            }
        });*/
        currentMucId = getIntent().getStringExtra("currentMucId");
        ChatManager.getInstance().registerP2GMessageReceivedListener(this);
    }

    private void initMsgs() {
        /*MucMessage mucMessage = new MucMessage("", "hello. everyone.", MucMessage.TYPE_RECEIVED);
        msgList.add(mucMessage);
        MucMessage mucMessage1 = new MucMessage("", "Hi", MucMessage.TYPE_RECEIVED);
        msgList.add(mucMessage1);
        MucMessage mucMessage2 = new MucMessage("", "haha", MucMessage.TYPE_RECEIVED);
        msgList.add(mucMessage2);
        MucMessage mucMessage3 = new MucMessage("", "Nice to meet you.", MucMessage.TYPE_SENT);
        msgList.add(mucMessage3);
        MucMessage mucMessage4 = new MucMessage("", "Nice to meet you too.", MucMessage.TYPE_RECEIVED);
        msgList.add(mucMessage4);
        MucMessage mucMessage5 = new MucMessage("", "Have a good night.", MucMessage.TYPE_SENT);
        msgList.add(mucMessage5);*/
    }

    @Override
    public void onMessageReceived() {
        Log.d(TAG, "onMessageReceived: ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgList.clear();
                msgList.addAll(ChatManager.getInstance().getMucMessage(currentMucId));
                for (ChatMessage item : msgList) {
                    Log.d(TAG, "run: content is [" + item.getBody() + "]");
                }
                adapter.notifyDataSetChanged();
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        ChatManager.getInstance().unRegisterP2GMessageReceivedListener(this);
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
