package com.tap4fun.chatdemo;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.animation.Attention.Flash;
import com.flyco.animation.Attention.RubberBand;
import com.flyco.animation.Attention.ShakeVertical;
import com.flyco.animation.Attention.Swing;
import com.flyco.animation.Attention.Tada;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

public class AddFriendDialog extends BaseDialog<AddFriendDialog> {

    private EditText mFriendIdET;
    private TextView mOkTV;
    private TextView mCancelTV;

    public AddFriendDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(new BounceTopEnter());

        View inflate = View.inflate(mContext, R.layout.layout_add_friend, null);

        mFriendIdET = inflate.findViewById(R.id.add_friend_id_et);
        mOkTV = inflate.findViewById(R.id.add_friend_ok_tv);
        mCancelTV = inflate.findViewById(R.id.add_friend_cancel_tv);

        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mOkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friendId = mFriendIdET.getText().toString();
                if (!friendId.isEmpty()) {
                    //TODO
                    friendId = "local|2|g303|9001|" + friendId + "@chat.pf.tap4fun.com";
                    ChatManager.getInstance().sendFollowRequest(friendId, "");
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
