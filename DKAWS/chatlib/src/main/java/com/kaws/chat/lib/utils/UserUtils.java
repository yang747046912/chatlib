package com.kaws.chat.lib.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by 杨才 on 2016/1/7.
 */
public class UserUtils {
    private static UserUtils instance;

    public static UserUtils getInstance() {
        if (instance == null) {
            synchronized (UserUtils.class) {
                if (instance == null) {
                    instance = new UserUtils();
                }
            }
        }
        return instance;
    }

    private static IgetUserInfo info;

    public interface IgetUserInfo {
        public void onGetUserInfo(String userkey, SimpleDraweeView imageView, TextView name);

        public void onGetMyInfo(SimpleDraweeView imageView);
    }

    public void setInfo(IgetUserInfo info) {
        this.info = info;
    }

    /**
     * 设置用户头像
     *
     * @param username
     */
    public void setUAvatar(Context context, String userkey, SimpleDraweeView imageView) {
        if (info != null) {
            info.onGetUserInfo(userkey, imageView, null);
        }
        // Image.getInstance().displayImg(imageView, userkey, R.drawable.ease_default_avatar);
    }

    /**
     * 设置用户头像
     *
     * @param username
     */
    public void setUName(String userkey, TextView name) {
        if (info != null) {
            info.onGetUserInfo(userkey, null, name);
        }
        // Image.getInstance().displayImg(imageView, userkey, R.drawable.ease_default_avatar);
    }

    public void setMAvater(SimpleDraweeView imageView) {
        if (info != null) {
            info.onGetMyInfo(imageView);
        }
    }
}
