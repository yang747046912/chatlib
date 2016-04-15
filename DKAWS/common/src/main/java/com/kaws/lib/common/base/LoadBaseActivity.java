package com.kaws.lib.common.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.R;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.ScreenSizeUtil;

/**
 * Created by 杨才 on 2016/2/3.
 */
public class LoadBaseActivity extends BaseActivity {
    private ProgressBar progressBbar;
    private View contentView;
    private View refresh;
    private ImageView err;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        View ll =  getLayoutInflater().inflate(R.layout.loading_fragment, null);
        contentView = getLayoutInflater().inflate(layoutResID, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(params);
        RelativeLayout mContainer = (RelativeLayout) ll.findViewById(R.id.container);
        mContainer.addView(contentView);
        getWindow().setContentView(ll);
        progressBbar = getView(R.id.progress_bar);
        refresh = getView(R.id.btn_refresh);
        err = getView(R.id.img_err);
        ViewGroup.LayoutParams peer = err.getLayoutParams();
        peer.height = ScreenSizeUtil.getScreenheight(this)/3;
        peer.width = ScreenSizeUtil.getScreenWidth(this)/3;
        err.setLayoutParams(peer);
        refresh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showLoading();
                onRefresh();
            }
        });
        contentView.setVisibility(View.GONE);
        title = getView(R.id.tv_titlebar_title);
        getView(R.id.btn_titlebar_back).setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                finishDefault();
            }
        });
    }

    public void setTitle(CharSequence text){
        title.setText(text);
    }
    protected void showLoading() {
        if (progressBbar.getVisibility() != View.VISIBLE) {
            progressBbar.setVisibility(View.VISIBLE);
        }
        if (contentView.getVisibility() != View.GONE) {
            contentView.setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
    }

    protected void showContentView() {
        if (progressBbar.getVisibility() != View.GONE) {
            progressBbar.setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.GONE) {
            refresh.setVisibility(View.GONE);
        }
        if (contentView.getVisibility() != View.VISIBLE) {
            contentView.setVisibility(View.VISIBLE);
        }
    }

    protected void showRefrsh() {
        if (progressBbar.getVisibility() != View.GONE) {
            progressBbar.setVisibility(View.GONE);
        }
        if (refresh.getVisibility() != View.VISIBLE) {
            refresh.setVisibility(View.VISIBLE);
        }
        if (contentView.getVisibility() != View.GONE) {
            contentView.setVisibility(View.GONE);
        }
    }

    protected void onRefresh() {

    }
}
