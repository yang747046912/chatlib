package com.kaws.lib.common.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by 杨才 on 2016/2/18.
 */
public class DataCacheUtil {
    public static long getCacheSize(Context cxt) {
        long fileSize = 0;
        File filesDir = cxt.getFilesDir();
        File cacheDir = cxt.getCacheDir();
        File externalCacheDir = cxt.getExternalCacheDir();

        fileSize += getDirSize(filesDir);
        fileSize += getDirSize(cacheDir);
        fileSize += getDirSize(externalCacheDir);

        return fileSize;
    }

    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); //
            }
        }
        return dirSize;
    }


    public static void clearAppCache(Context cxt) {
        clearCacheFolder(cxt.getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(cxt.getCacheDir(), System.currentTimeMillis());
        clearCacheFolder(cxt.getExternalCacheDir(), System.currentTimeMillis());
    }

    public static int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0KB";
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float temp = (float) size / 1024;
        if (temp >= 1024) {
            return df.format(temp / 1024) + "MB";
        } else {
            return df.format(temp) + "KB";
        }
    }
}
