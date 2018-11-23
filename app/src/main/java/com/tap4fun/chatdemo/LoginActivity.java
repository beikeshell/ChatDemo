package com.tap4fun.chatdemo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.roger.catloadinglibrary.CatLoadingView;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends FullScreenActivity implements ChatManager.OnLoginResultListener {

    static {
        System.loadLibrary("chat_jni");
    }

    public static Timer updateTimer;

    private static final String TAG = "LoginActivity";

    private Button mLoginBtn;
    private EditText mUserNameEt;
    private EditText mNickNameEt;
    private EditText mPasswordEt;
    private EditText mServerIPEt;
    private EditText mServerPortEt;
    private EditText mGameIdEt;
    private EditText mGameKeyEt;
    private EditText mGameServerIdEt;
    private FloatingActionButton mFab;
    private CardView mCv;

    private String mUserName;
    private String mNickName;
    private String mPassword;
    private String mServerIP;
    private String mServerPort;
    private String mGameId;
    private String mGameKey;
    private String mGameServerId;

    private CatLoadingView mCatLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        setListener();
        initLogic();
    }

    private void initLogic() {
        ChatManager.getInstance().init();
        //设置缓存路经
        String cachePath = getApplicationContext().getExternalCacheDir().getPath();
        ChatManager.getInstance().setCachePath(cachePath);

        //这部分逻辑需要放到单独的逻辑当中
        updateTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    ChatManager.getInstance().update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updateTimer.schedule(task, 0, 30);
    }

    private void setListener() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: login button clicked.");
                //Explode explode = new Explode();
                //explode.setDuration(500);
                //getWindow().setExitTransition(explode);
                //getWindow().setEnterTransition(explode);

                try {
                    mUserName = mUserNameEt.getText().toString();
                    mNickName = mNickNameEt.getText().toString();
                    mPassword = mPasswordEt.getText().toString();
                    mServerIP = mServerIPEt.getText().toString();
                    mServerPort = mServerPortEt.getText().toString();
                    mGameId = mGameIdEt.getText().toString();
                    mGameKey = mGameKeyEt.getText().toString();
                    mGameServerId = mGameServerIdEt.getText().toString();

                    if (null == mUserName || mUserName.isEmpty()) { return; }
                    if (null == mNickName || mNickName.isEmpty()) { return; }
                    if (null == mPassword || mPassword.isEmpty()) { return; }
                    if (null == mServerIP || mServerIP.isEmpty()) { return; }
                    if (null == mServerPort || mServerPort.isEmpty()) { return; }
                    mUserName = "local|2|g303|9001|" + mUserName + "@chat.pf.tap4fun.com";

                    //登录聊天服务器
                    ChatManager.getInstance().login(mUserName, mPassword, mNickName, mServerIP, mServerPort);
                    mCatLoadingView = new CatLoadingView();
                    mCatLoadingView.show(getSupportFragmentManager(), "正在登录");

                    //设置游戏相关信息
                    if (null != mGameId && !mGameId.isEmpty()
                            && null != mGameKey && !mGameKey.isEmpty()
                            && null != mGameServerId && !mGameServerId.isEmpty()) {
                        ChatManager.getInstance().setGameSpecInfo(mGameId, mGameKey, mGameServerId);
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                //TODO启动注册界面
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, mFab, mFab.getTransitionName());
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class), options.toBundle());
            }
        });

        ChatManager.getInstance().setOnLoginResultListener(this);
    }

    private void initView() {
        mLoginBtn = findViewById(R.id.login);
        mUserNameEt = findViewById(R.id.user_name);
        mNickNameEt = findViewById(R.id.nick_name);
        mPasswordEt = findViewById(R.id.password);
        mServerIPEt = findViewById(R.id.server_ip);
        mServerPortEt = findViewById(R.id.server_port);
        mGameIdEt = findViewById(R.id.game_id);
        mGameKeyEt = findViewById(R.id.game_key);
        mGameServerIdEt = findViewById(R.id.game_server_id);
        mFab = findViewById(R.id.fab);
        mCv = findViewById(R.id.cv);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mFab.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoginResult(final boolean success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCatLoadingView.dismiss();
                if (success) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
