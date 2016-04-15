package com.kaws.lib.common.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨才 on 2016/2/1.
 */
public class ActivityStackUtil {

    private static ActivityStackUtil instance;

    private List<Activity> activities;

    public ActivityStackUtil() {
        this.activities = new ArrayList<>();
    }

    public static ActivityStackUtil getInstance() {
        synchronized (ActivityStackUtil.class) {
            if (instance == null) {
                instance = new ActivityStackUtil();
            }
        }
        return instance;
    }

    public void add(Activity activity) {
        activities.add(activity);
    }

    public void remove(Activity activity) {
        activities.remove(activity);
    }

    public void destoryAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
