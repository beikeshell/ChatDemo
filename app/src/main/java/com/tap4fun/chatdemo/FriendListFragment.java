package com.tap4fun.chatdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class FriendListFragment extends Fragment implements ChatManager.FriendListListener{

    private static final String TAG = "FriendListFragment";

    private List<FriendItem> friendItemList = new ArrayList<>();
    private FriendListAdapter mFriendListAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        //初始化recyclerView
        initFriends();
        recyclerView = view.findViewById(R.id.friend_list_recycler_view);
        recyclerView.setItemAnimator(new ScaleInAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mFriendListAdapter = new FriendListAdapter(getActivity(), friendItemList);
        AnimationAdapter adapter = new ScaleInAnimationAdapter(mFriendListAdapter);
        adapter.setFirstOnly(false);
        adapter.setDuration(500);
        adapter.setInterpolator(new OvershootInterpolator(.5f));
        recyclerView.setAdapter(adapter);

        ChatManager.getInstance().setFriendListListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initFriends() {
        friendItemList.clear();
        friendItemList.addAll(ChatManager.getInstance().getFriendList());

    }

    @Override
    public void onFriendListChanged() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                friendItemList.clear();
                friendItemList.addAll(ChatManager.getInstance().getFriendList());
                if (null != mFriendListAdapter) {
                    mFriendListAdapter.notifyDataSetChanged();
                    //mFriendListAdapter.notifyItemRangeChanged(0, friendItemList.size() - 1);
                }
                if (null != recyclerView) {
                    recyclerView.scrollToPosition(friendItemList.size() - 1);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        ChatManager.getInstance().setFriendListListener(null);
        super.onDestroyView();
    }
}
