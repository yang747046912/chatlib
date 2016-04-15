package com.kaws.lib.common.event;

import android.view.View;
import android.view.View.OnClickListener;

import java.util.Calendar;

/**
 * 避免在1秒内出发多次点击
 * Created by 杨才 on 2016/1/15.
 */
public abstract class PerfectClickListener implements OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private int id = -1;

    @Override
    public void onClick(View v) {
        int mId = v.getId();
        if (id != mId) {
            id = mId;
            onNoDoubleClick(v);
            return;
        }
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    protected abstract void onNoDoubleClick(View v);
}
