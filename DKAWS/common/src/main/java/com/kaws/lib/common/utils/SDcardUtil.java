package com.kaws.lib.common.utils;

import android.content.Context;
import android.os.Environment;

/**
 * Created by 杨才 on 2016/2/1.
 */
public class SDcardUtil {
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
//            cachePath = context.getExternalCacheDir().getAbsolutePath();
            cachePath = Environment.getExternalStorageDirectory().toString();
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }
}
