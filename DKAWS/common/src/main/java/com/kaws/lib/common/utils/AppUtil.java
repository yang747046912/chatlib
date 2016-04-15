package com.kaws.lib.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by 杨才 on 2016/2/26.
 */
public class AppUtil {

    public static  boolean isAppRunning(Context contexxt) {
        PackageManager pm = contexxt.getPackageManager();
        // 保存所有正在运行的包名 以及它所在的进程信息
        ActivityManager mActivityManager = (ActivityManager) contexxt.getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包
            // 输出所有应用程序的包名
            for (int i = 0; i < pkgNameList.length; i++) {
                String pkgName = pkgNameList[i];
                // 加入至map对象里
                if (pkgName.equalsIgnoreCase(contexxt.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
