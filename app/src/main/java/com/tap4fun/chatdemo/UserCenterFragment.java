package com.tap4fun.chatdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UserCenterFragment extends Fragment {
    private static final String TAG = UserCenterFragment.class.getSimpleName();

    private TextView mUserIdTV;
    private TextView mNickNameTV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_center, container, false);

        mUserIdTV = view.findViewById(R.id.uc_user_id);
        mNickNameTV = view.findViewById(R.id.uc_user_nick_name);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserIdTV.setText(ChatManager.getInstance().getUserId());
        mNickNameTV.setText(ChatManager.getInstance().getUserNickName());
    }
}
