package com.tap4fun.chatdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotifyListAdapter extends RecyclerView.Adapter<NotifyListAdapter.ViewHolder> {

    private List<NotifyItem> mNotifyList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.left_notify_msg);
        }

    }

    public NotifyListAdapter(List<NotifyItem> notifyList) {
        this.mNotifyList = notifyList;
    }

    @NonNull
    @Override
    public NotifyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notify_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyListAdapter.ViewHolder viewHolder, int i) {
        NotifyItem item = mNotifyList.get(i);
        viewHolder.msg.setText(item.getMsg());
    }

    @Override
    public int getItemCount() {
        return mNotifyList.size();
    }
}
