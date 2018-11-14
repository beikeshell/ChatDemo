package com.tap4fun.chatdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MucRoomListFragment extends Fragment implements ChatManager.MucRoomListListener{
    private static final String TAG = "MucRoomListFragment";
    private List<MucRoomItem> mucRoomList = new ArrayList<>();

    private RecyclerView recyclerView;
    private MucRoomListAdapter mucRoomListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_muc_room_list, container, false);

        initMucRooms();
        recyclerView = view.findViewById(R.id.muc_list_recycler_view);
        recyclerView.setItemAnimator(new FadeInAnimator());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mucRoomListAdapter = new MucRoomListAdapter(getActivity(), mucRoomList);
        AnimationAdapter adapter = new ScaleInAnimationAdapter(mucRoomListAdapter);
        adapter.setFirstOnly(false);
        adapter.setDuration(500);
        adapter.setInterpolator(new OvershootInterpolator(.5f));
        recyclerView.setAdapter(adapter);

        ChatManager.getInstance().setMucRoomListListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ChatManager.getInstance().setMucRoomListListener(this);
    }

    @Override
    public void onDestroy() {
        //ChatManager.getInstance().setMucRoomListListener(null);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        ChatManager.getInstance().setMucRoomListListener(null);
        super.onDestroyView();
    }

    private void initMucRooms() {
        Log.d(TAG, "initMucRooms: ");
        mucRoomList.clear();
        mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
        mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
        mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
        mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
        mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
        mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());

        for (MucRoomItem item : mucRoomList) {
            Log.d(TAG, "initMucRooms: " + item.getId());
        }
    }


    @Override
    public void onMucRoomListChanged() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mucRoomList.clear();
                mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
                mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
                mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
                mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
                mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
                mucRoomList.addAll(ChatManager.getInstance().getMucRoomList());
                if (null != mucRoomListAdapter) {
                    mucRoomListAdapter.notifyDataSetChanged();
                    //mucRoomListAdapter.notifyItemRangeChanged(0, mucRoomList.size() - 1);
                }
                if (null != recyclerView) {
                    recyclerView.scrollToPosition(0);
                }
            }
        });
    }
}
