package com.kaws.chat.lib.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.chat.lib.R;
import com.kaws.lib.bigimage.ViewBigImageActivity;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.fresco.Image;

import java.io.File;
import java.util.ArrayList;


public class EaseChatRowImage extends EaseChatRowFile {

    protected SimpleDraweeView imageView;
    private ImageMessageBody imgBody;
    private int screen;

    public EaseChatRowImage(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        DisplayMetrics d = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(d);
        screen = d.widthPixels;
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_picture : R.layout.ease_row_sent_picture, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (SimpleDraweeView) findViewById(R.id.image);
    }


    @Override
    protected void onSetUpView() {
        imgBody = (ImageMessageBody) message.getBody();
        // 接收方向的消息
        if (message.direct == EMMessage.Direct.RECEIVE) {
            Image.displayChatImage(imageView, Uri.parse(imgBody.getRemoteUrl()), R.drawable.ease_default_image, new Image.ILoadProgress() {
                @Override
                public void onProgressChange(int level) {
                    progressBar.setVisibility(View.VISIBLE);
                    percentageView.setVisibility(View.VISIBLE);
                    percentageView.setText("" + level + "%");
                }

                @Override
                public void onLoadSuccess() {
                    progressBar.setVisibility(View.GONE);
                    percentageView.setVisibility(View.GONE);
                }

                @Override
                public void onLoadFailure() {
                    progressBar.setVisibility(View.GONE);
                    percentageView.setVisibility(View.GONE);
                }
            });
            return;
        }

        String filePath = imgBody.getLocalUrl();
        DebugUtil.debug("--filePath--->", "" + filePath);
        if (filePath != null) {
            Image.displayChatImage(imageView, Uri.parse("file://" + filePath), R.drawable.ease_default_image, new Image.ILoadProgress() {
                @Override
                public void onProgressChange(int level) {

                }

                @Override
                public void onLoadSuccess() {

                }

                @Override
                public void onLoadFailure() {

                }
            });
        }
        handleSendMessage();
    }


    @Override
    protected void onUpdateView() {
        super.onUpdateView();
    }

    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, ViewBigImageActivity.class);
        File file = new File(imgBody.getLocalUrl());
        Bundle bundle = new Bundle();
        bundle.putInt("selet", 1);
        bundle.putInt("code", 0);
        ArrayList<String> imageuri = new ArrayList<String>();

        if (file.exists()) {
            bundle.putBoolean("isLocal", true);
            imageuri.add(imgBody.getLocalUrl());
        } else {
            imageuri.add(imgBody.getRemoteUrl());
        }
        bundle.putStringArrayList("imageuri", imageuri);
        if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked
                && message.getChatType() != ChatType.GroupChat) {
            try {
                EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
                message.isAcked = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
