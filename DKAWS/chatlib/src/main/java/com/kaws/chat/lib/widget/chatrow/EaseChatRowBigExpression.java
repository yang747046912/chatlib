package com.kaws.chat.lib.widget.chatrow;

import android.content.Context;
import android.net.Uri;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.chat.lib.EaseConstant;
import com.kaws.chat.lib.R;
import com.kaws.chat.lib.controller.EaseUI;
import com.kaws.chat.lib.domain.EaseEmojicon;
import com.kaws.lib.fresco.Image;

/**
 * 大表情(动态表情)
 */
public class EaseChatRowBigExpression extends EaseChatRowText {

    private SimpleDraweeView imageView;


    public EaseChatRowBigExpression(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_bigexpression : R.layout.ease_row_sent_bigexpression, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (SimpleDraweeView) findViewById(R.id.image);
    }


    @Override
    public void onSetUpView() {
        String emojiconId = message.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null);
        EaseEmojicon emojicon = null;
        if (EaseUI.getInstance().getEmojiconInfoProvider() != null) {
            emojicon = EaseUI.getInstance().getEmojiconInfoProvider().getEmojiconInfo(emojiconId);
        }
        if (emojicon != null) {
            if (emojicon.getBigIcon() != 0) {
                Image.displayImage(imageView, Uri.parse("res:///" + emojicon.getBigIcon()), R.drawable.ease_default_avatar);
            } else if (emojicon.getBigIconPath() != null) {
                Image.displayImage(imageView, Uri.parse(emojicon.getBigIconPath()), R.drawable.ease_default_avatar);
            } else {
                Image.displayImage(imageView, Uri.parse("res:///" + R.drawable.ease_default_avatar), R.drawable.ease_default_avatar);
            }
        }

        handleTextMessage();
    }
}
