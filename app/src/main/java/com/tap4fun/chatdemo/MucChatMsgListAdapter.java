package com.tap4fun.chatdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MucChatMsgListAdapter extends RecyclerView.Adapter<MucChatMsgListAdapter.ViewHolder> {
    private static final String TAG = "MucChatMsgListAdapter";
    private List<ChatMessage> mMsgList;

    public MucChatMsgListAdapter(List<ChatMessage> msgList) { this.mMsgList = msgList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.muc_msg_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ChatMessage msg = mMsgList.get(i);
        if (msg.getMsgType() == ChatMessage.MsgType.MSG_RECEIVED) {
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getBody());
        } else if (msg.getMsgType() == ChatMessage.MsgType.MSG_SEND) {
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightMsg.setText(msg.getBody());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            leftLayout = view.findViewById(R.id.left_muc_layout);
            rightLayout = view.findViewById(R.id.right_muc_layout);
            leftMsg = view.findViewById(R.id.left_muc_msg);
            rightMsg = view.findViewById(R.id.right_muc_msg);
        }
    }


}
