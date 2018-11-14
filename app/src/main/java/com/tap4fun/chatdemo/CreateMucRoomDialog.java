package com.tap4fun.chatdemo;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.animation.Attention.Swing;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.base.BaseDialog;

public class CreateMucRoomDialog extends BaseDialog<CreateMucRoomDialog> {

    private static final String TAG = CreateMucRoomDialog.class.getSimpleName();

    private EditText mMucRoomIdET;
    private EditText mMucroomTitleET;
    private TextView mOkTV;
    private TextView mCancelTV;

    public CreateMucRoomDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(new BounceTopEnter());

        View inflate = View.inflate(mContext, R.layout.layout_create_muc_room, null);

        mMucRoomIdET = inflate.findViewById(R.id.create_muc_room_id_et);
        mMucroomTitleET = inflate.findViewById(R.id.create_muc_room_title_et);
        mOkTV = inflate.findViewById(R.id.create_muc_room_ok_tv);
        mCancelTV = inflate.findViewById(R.id.create_muc_room_cancel_tv);

        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mOkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roomId = mMucRoomIdET.getText().toString();
                String roomTitle = mMucroomTitleET.getText().toString();
                if (!roomId.isEmpty() && !roomTitle.isEmpty()) {
                    if (ChatManager.getInstance().isMucRoomIdExisted(roomId)) {
                        new NormalDialog(mContext)
                                .title("温馨提示")
                                .content("聊天室已经存在\n请修改聊天室ID后重新创建")
                                .showAnim(new Swing())
                                .show();
                    } else {
                        ChatManager.getInstance().createMucRoom(roomId, roomTitle);
                    }
                }
                dismiss();
            }
        });

        mCancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
