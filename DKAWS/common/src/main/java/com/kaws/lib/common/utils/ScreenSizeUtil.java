package com.kaws.lib.common.utils;

import android.content.Context;

/**
 * Created by 杨才 on 2016/2/2.
 */
public class ScreenSizeUtil {
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenheight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
