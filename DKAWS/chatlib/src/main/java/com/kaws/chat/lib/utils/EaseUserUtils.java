package com.kaws.chat.lib.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.chat.lib.R;
import com.kaws.chat.lib.controller.EaseUI;
import com.kaws.chat.lib.controller.EaseUI.EaseUserProfileProvider;
import com.kaws.chat.lib.domain.EaseUser;
import com.kaws.lib.fresco.Image;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * 根据username获取相应user
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, SimpleDraweeView imageView) {

        EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Image.displayImage(imageView, Uri.parse("res:///" + avatarResId), avatarResId);
            } catch (Exception e) {
                //正常的string路径
                Image.displayImage(imageView, Uri.parse(user.getAvatar()), R.drawable.ease_default_avatar);
            }
        } else {
            Image.displayImage(imageView, Uri.parse("res:///"  +R.drawable.ease_default_avatar), R.drawable.ease_default_avatar);
        }
    }

    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUAvatar(String userkey, SimpleDraweeView imageView) {
        if (info != null) {
            info.ongetUser(userkey, imageView);
            return;
        }
        Image.displayImage(imageView, Uri.parse(userkey), R.drawable.ease_default_avatar);
    }

    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {
                textView.setText(username);
            }
        }
    }

    private static IgetUserInfo info;

    public interface IgetUserInfo {
        public void ongetUser(String userkey, ImageView imageView);
    }

    public void setInfo(IgetUserInfo info) {
        this.info = info;
    }
}
