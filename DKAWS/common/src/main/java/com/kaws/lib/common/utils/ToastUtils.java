package com.kaws.lib.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaws.lib.common.R;

/**
 * Created by 杨才 on 2016/1/26.
 */
public class ToastUtils {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
            mToast = null;
        }
    };

    public static void showToast(Context mContext, String text, int duration, int type) {
        mHandler.removeCallbacks(r);
        if (mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        }
        LinearLayout toastView = (LinearLayout) mToast.getView();
        TextView msg = (TextView) toastView.getChildAt(0);
        msg.setCompoundDrawablePadding(10);
        Drawable drawable = null;
        if (type == 1) {
            drawable = mContext.getResources().getDrawable(R.drawable.ic_toast_success);
        } else if (type == 2) {
            drawable = mContext.getResources().getDrawable(R.drawable.ic_toast_false);
        } else if (type == 3) {
            drawable = mContext.getResources().getDrawable(R.drawable.ic_toast_tips);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            msg.setCompoundDrawables(null, drawable, null, null);
        }
        msg.setText(text);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mHandler.postDelayed(r, duration);
        mToast.show();
    }
}
