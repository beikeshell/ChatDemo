package com.tap4fun.chatdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;
import java.util.List;

public class MucRoomMemberListFragment extends Fragment implements ChatManager.OnGetMucRoomMemberListListener {
    private static final String TAG = MucRoomMemberListFragment.class.getSimpleName();

    private Button mInviteMemberBtn;
    private ListView mMucRoomMemberListLV;
    private ArrayAdapter<String> mMucRoomMemberListAdapter;
    private List<String> mMucRoomMemberList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_muc_room_member_list, container, false);

        mInviteMemberBtn = view.findViewById(R.id.invite_member);
        mMucRoomMemberListLV = view.findViewById(R.id.muc_room_member_list);
        mMucRoomMemberListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mMucRoomMemberList);
        mMucRoomMemberListLV.setAdapter(mMucRoomMemberListAdapter);

        ChatManager.getInstance().setOnGetMucRoomMembersListener(this);
        ChatManager.getInstance().getMucRoomMembers(ChatManager.getInstance().getCurrentMucRoomId());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        ChatManager.getInstance().getMucRoomMembers(ChatManager.getInstance().getCurrentMucRoomId());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMucRoomMemberListLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String memberId = mMucRoomMemberList.get(position);
                final NormalDialog dialog = new NormalDialog(getActivity())
                        .title(ChatManager.getInstance().getCurrentMucRoomId())
                        .content("确定将 " + memberId + " 移出聊天室？")
                        .showAnim(new Swing());
                dialog.setOnBtnClickL(
                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                dialog.dismiss();
                            }
                        },

                        new OnBtnClickL() {
                            @Override
                            public void onBtnClick() {
                                kickFromMucRoom(memberId);
                                dialog.dismiss();
                            }
                        }
                );
                dialog.show();
                return false;
            }
        });

        mInviteMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteMember();
            }
        });
    }

    private void inviteMember() {
        //邀请好友，启动邀请好友界面
        Intent intent = new Intent("ACTION_INVITE_FRIEND");
        intent.putExtra("mucRoomId", ChatManager.getInstance().getCurrentMucRoomId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(intent);
    }

    private void kickFromMucRoom(String memberId) {
        //TODO:踢出聊天室
        List<String> ids = new ArrayList<>();
        ids.add(memberId);
        ChatManager.getInstance().kickFromMucRoom(ChatManager.getInstance().getCurrentMucRoomId(), ids);
    }

    @Override
    public void onGetMucRoomMembersResult(final String mucRoomId, final List<String> members) {
        Log.d(TAG, "onGetMucRoomMembersResult: mucRoomId is [" + mucRoomId + "], " +
                "mCurrentMucRoomId is [" + ChatManager.getInstance().getCurrentMucRoomId() + "], " +
                "members is [" + members + "]");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: mucRoomId is [" + mucRoomId + "]," +
                        "mCurrentMucRoomId is [" + ChatManager.getInstance().getCurrentMucRoomId() + "]");
                if (mucRoomId.equals(ChatManager.getInstance().getCurrentMucRoomId())) {
                    mMucRoomMemberList.clear();
                    mMucRoomMemberList.addAll(members);
                    mMucRoomMemberListAdapter.notifyDataSetChanged();
                    mMucRoomMemberListLV.smoothScrollToPosition(0);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ChatManager.getInstance().setOnGetMucRoomMembersListener(null);
    }
}
