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
    private NavigationView mNavView;

    private Timer updateTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mMainFmnTabTL = findViewById(R.id.main_fmn_tab);
        mMainFmnPagerVP = findViewById(R.id.main_fmn_pager);
        mMainFmnPagerVP.setAdapter(new PageAdapter(getSupportFragmentManager(), mMainFmnTabTL.getTabCount()));

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
