package com.tap4fun.chatdemo;

import android.content.ContentProvider;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.ViewHolder> {
    private List<FriendItem> mFriendList;
    private InviteFriendActivity mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView friendImage;
        TextView friendId;
        TextView friendNickName;
        View friendItemView;

        public ViewHolder(View view) {
            super(view);
            friendItemView = view;
            friendImage = view.findViewById(R.id.friend_image);
            friendId = view.findViewById(R.id.friend_id);
            friendNickName = view.findViewById(R.id.friend_nick_name);
        }
    }

    public InviteFriendAdapter(InviteFriendActivity context, List<FriendItem> friendList) {
        this.mFriendList = friendList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.friend_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.friendItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                FriendItem friendItem = mFriendList.get(position);
                String id = friendItem.getId();
                List<String> ids = new ArrayList<>();
                ids.add(id);
                ChatManager.getInstance().inviteFriend(mContext.getMucRoomId(), ids);
                if (null != ActivityManager.getLastActivity()) {
                    ActivityManager.getLastActivity().finish();
                }
                /*Intent intent = new Intent("ACTION_INVITE_FRIEND");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("toUserId", friendItem.getId());
                intent.putExtra("toUserNickName", friendItem.getNickName());
                MyApplication.getContext().startActivity(intent);*/
            }
        });

        holder.friendItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                FriendItem friendItem = mFriendList.get(position);
                Toast.makeText(view.getContext(), "you long clicked view" + friendItem.getNickName(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendItem friendItem = mFriendList.get(position);
        holder.friendImage.setImageResource(friendItem.getImageId());
        holder.friendId.setText(friendItem.getId().substring(0, friendItem.getId().length() / 2));
        holder.friendNickName.setText(friendItem.getNickName());
    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }
}
