package com.tap4fun.chatdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;

import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {
    private List<FriendItem> mFriendList;
    private Activity mContext;

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

    public FriendListAdapter(Activity context, List<FriendItem> friendList) {
        this.mContext = context;
        this.mFriendList = friendList;
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
                Intent intent = new Intent("ACTION_P2P_CHAT");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("toUserId", friendItem.getId());
                intent.putExtra("toUserNickName", friendItem.getNickName());
                MyApplication.getContext().startActivity(intent);
            }
        });

        holder.friendItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                final FriendItem friendItem = mFriendList.get(position);
                final String[] actionItems = {"解除好友关系"};
                final ActionSheetDialog dialog = new ActionSheetDialog(mContext, actionItems, null);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String action = actionItems[position];
                        if (action.equals("解除好友关系")) {
                            final NormalDialog normalDialog = new NormalDialog(mContext)
                                    .title("解除好友关系")
                                    .content("确定与 " + friendItem.getId() + " 解除好友关系？")
                                    .showAnim(new Swing());
                            normalDialog.setOnBtnClickL(
                                    new OnBtnClickL() {
                                        @Override
                                        public void onBtnClick() {
                                            normalDialog.dismiss();
                                        }
                                    },

                                    new OnBtnClickL() {
                                        @Override
                                        public void onBtnClick() {
                                            ChatManager.getInstance().unFollowRequest(friendItem.getId());
                                            normalDialog.dismiss();
                                        }
                                    }
                            );
                            normalDialog.show();
                        }
                        dialog.dismiss();
                    }
                });
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
