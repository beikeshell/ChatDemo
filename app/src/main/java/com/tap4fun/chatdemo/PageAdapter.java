package com.tap4fun.chatdemo;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {

    private static final String TAG = "PageAdapter";
    private List<String> titles = new ArrayList<>();

    private int num;
    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public PageAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
        titles.add(0, "好友");
        titles.add(1, "聊天室");
        titles.add(2, "通知");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {

        return createFragment(position);
    }

    @Override
    public int getCount() {
        return num;
    }

    private Fragment createFragment(int pos) {
        Fragment fragment = mFragmentHashMap.get(pos);

        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new FriendListFragment();
                    Log.i(TAG, "FriendListFragment");
                    break;
                case 1:
                    fragment = new MucRoomListFragment();
                    Log.i(TAG, "MucRoomListFragment");
                    break;
                case 2:
                    fragment = new NotifyListFragment();
                    Log.i(TAG, "NotifyListFragment");
                    break;
                default:
                    Log.e(TAG, "createFragment: no tab matched!");
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }
}
