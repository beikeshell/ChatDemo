package com.tap4fun.chatdemo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionBarContainer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.widget.NormalDialog;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.Timer;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private TabLayout mMainFmnTabTL;
    private ViewPager mMainFmnPagerVP;
    private BoomMenuButton mMainSettingBMB;
    private DrawerLayout mDrawerLayout;
    private TextView mMainTitleTV;
    private ImageView mToUserCenterIV;

    private Timer updateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mMainFmnTabTL = findViewById(R.id.main_fmn_tab);
        mMainFmnPagerVP = findViewById(R.id.main_fmn_pager);
        mMainFmnPagerVP.setAdapter(new PageAdapter(getSupportFragmentManager(), mMainFmnTabTL.getTabCount()));

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mMainTitleTV = findViewById(R.id.main_title);
        mToUserCenterIV = findViewById(R.id.main_to_user_center);
        mToUserCenterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        /*ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }*/


        mMainFmnTabTL.setupWithViewPager(mMainFmnPagerVP);

        //需要在setupWithViewPager之后设置TabLayout标签，否则标签不显示
        //mMainFmnTabTL.addTab(mMainFmnTabTL.newTab().setText("好友"));
        //mMainFmnTabTL.addTab(mMainFmnTabTL.newTab().setText("聊天室"));
        //mMainFmnTabTL.addTab(mMainFmnTabTL.newTab().setText("通知"));

        mMainFmnTabTL.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: " + mMainFmnTabTL.getSelectedTabPosition());
                mMainFmnPagerVP.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mMainSettingBMB = findViewById(R.id.main_setting);
        mMainSettingBMB.addBuilder(new HamButton.Builder()
                .normalTextRes(R.string.create_muc_room)
                .textSize(20)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        //创建聊天室，首先检测是否允许创建聊天室
                        if (!ChatManager.getInstance().canCreateMucRoom()) {
                            new NormalDialog(MainActivity.this)
                                    .title("温馨提示")
                                    .content("允许创建聊天室达到上限\n请先退出一个聊天室后再创建")
                                    .showAnim(new Swing())
                                    .show();
                        } else {
                            final CreateMucRoomDialog dialog = new CreateMucRoomDialog(MainActivity.this);
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(false);
                        }
                    }
                }));
        mMainSettingBMB.addBuilder(new HamButton.Builder()
                .normalTextRes(R.string.add_friend)
                .textSize(20)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        //TODO
                        final AddFriendDialog dialog = new AddFriendDialog(MainActivity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                })
        );

    }

}
