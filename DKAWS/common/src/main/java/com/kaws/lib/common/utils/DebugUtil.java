package com.kaws.lib.common.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

//在代码中要打印log,就直接DebugUtil.debug(....).然后如果发布的时候,就直接把这个类的DEBUG 改成false,这样所有的log就不会再打印在控制台.
public class DebugUtil {
    private static final String TAG = "HSL";
    private static final boolean DEBUG = true;

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void debug(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void debug(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void error(String tag, String error) {
        Log.e(tag, error);
    }

    public static void error(String error) {
        Log.e(TAG, error);
    }
}