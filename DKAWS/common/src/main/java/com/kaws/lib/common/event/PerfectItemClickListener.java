package com.kaws.lib.common.event;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.Calendar;

/**
 * 避免在1秒内出发多次点击
 * Created by 杨才 on 2016/1/18.
 */
public abstract class PerfectItemClickListener implements OnItemClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private int mId = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position != mId) {
            mId = position;
            onNoDoubleItemClick(parent, view, position, id);
            return;
        }
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleItemClick(parent, view, position, id);
        }
    }

    public abstract void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id);
}
