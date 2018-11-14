package com.tap4fun.chatdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;

import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

public class MucRoomListAdapter extends RecyclerView.Adapter<MucRoomListAdapter.ViewHolder> {
    private static final String TAG = "MucRoomListAdapter";
    private List<MucRoomItem> mMucRoomList;
    private Activity mContext;

    static class ViewHolder extends RecyclerView.ViewHolder implements AnimateViewHolder {
        ImageView mucRoomImage;
        TextView mucRoomId;
        TextView mucRoomTitle;
        View mucRoomItemView;

        public ViewHolder(View view) {
            super(view);
            mucRoomItemView = view;
            mucRoomId = view.findViewById(R.id.muc_room_id);
            mucRoomTitle = view.findViewById(R.id.muc_room_title);
            mucRoomImage = view.findViewById(R.id.muc_room_image);
        }

        @Override
        public void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
            /*ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
            ViewCompat.setAlpha(itemView, 0);*/
        }

        @Override
        public void preAnimateRemoveImpl(RecyclerView.ViewHolder holder) {

        }

        @Override
        public void animateAddImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
            /*ViewCompat.animate(itemView)
                    .translationY(0)
                    .alpha(1)
                    .setDuration(300)
                    .setListener(listener)
                    .start();*/
        }

        @Override
        public void animateRemoveImpl(RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener) {
            /*ViewCompat.animate(itemView)
                    .translationY(-itemView.getHeight() * 0.3f)
                    .alpha(0)
                    .setDuration(300)
                    .setListener(listener)
                    .start();*/
        }
    }

    public MucRoomListAdapter(Activity context, List<MucRoomItem> mucRoomList) {
        this.mContext = context;
        this.mMucRoomList = mucRoomList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.muc_room_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.mucRoomItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MucRoomItem mucRoomItem = mMucRoomList.get(position);
                //TODO
                Intent intent = new Intent("ACTION_MUC_CHAT");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("currentMucId", mucRoomItem.getId());
                MyApplication.getContext().startActivity(intent);

            }
        });

        holder.mucRoomItemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                final MucRoomItem mucRoomItem = mMucRoomList.get(position);
                final String[] actionItems = {"退出聊天室", "邀请好友"};
                final ActionSheetDialog dialog = new ActionSheetDialog(mContext, actionItems, null);
                dialog.isTitleShow(false).show();
                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String action = actionItems[position];
                        if (action.equals("退出聊天室")) {
                            //退出聊天室
                            //TODO
                            Log.d(TAG, "onOperItemClick: exit muc room");
                            final NormalDialog normalDialog = new NormalDialog(mContext);
                            normalDialog.title("退出聊天室");
                            normalDialog.content("确定退出聊天室 " + mucRoomItem.getId() + " ?");
                            normalDialog.showAnim(new Swing());

                            normalDialog.setOnBtnClickL(
                                    new OnBtnClickL() {
                                        @Override
                                        public void onBtnClick() { //左边按钮按下
                                            normalDialog.dismiss();
                                        }
                                    },
                                    new OnBtnClickL() { //右边按钮按下
                                        @Override
                                        public void onBtnClick() {
                                            ChatManager.getInstance().quitMucRoom(mucRoomItem.getId());
                                            normalDialog.dismiss();
                                        }
                                    });
                            normalDialog.show();

                        } else if (action.equals("邀请好友")) {
                            //邀请好友加入聊天室
                            //TODO
                            Log.d(TAG, "onOperItemClick: invite friend");
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
        MucRoomItem mucRoomItem = mMucRoomList.get(position);
        holder.mucRoomImage.setImageResource(mucRoomItem.getImageId());
        holder.mucRoomId.setText(mucRoomItem.getId());
        holder.mucRoomTitle.setText(mucRoomItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return mMucRoomList.size();
    }
}
