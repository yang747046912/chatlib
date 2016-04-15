package com.kaws.lib.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.R;
import com.kaws.lib.common.event.PerfectClickListener;

/**
 * Created by 杨才 on 2016/1/18.
 */
public abstract class LoadBaseFragment extends BaseFragment {
    private ProgressBar progressBbar;
    protected View contentView;
    private View refresh;
    private ImageView err;
    private TextView title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ll = inflater.inflate(R.layout.loading_fragment, null);
        contentView = inflater.inflate(setContent(), null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(params);
        RelativeLayout mContainer = (RelativeLayout) ll.findViewById(R.id.container);
        mContainer.addView(contentView);
        return ll;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBbar = getView(R.id.progress_bar);
        refresh = getView(R.id.btn_refresh);
        err = getView(R.id.img_err);
        title = getView(R.id.tv_titlebar_title);
        refresh.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showLoading();
                onRefresh();
            }
        });
        contentView.setVisibility(View.GONE);
    }

    public abstract int setContent();

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

    public void setTitle(CharSequence text){
        title.setText(text);
    }
}
