package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

/**
 * Created by Dw on 2015/9/15.
 */
public class KawsLoadingLayout extends LoadingLayout {

    private AnimationDrawable animationDrawable;

    public KawsLoadingLayout(Context context, PullToRefreshBase.Mode mode,
                             PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        // 初始化
        mHeaderImage.setImageResource(R.drawable.kaws_anim);
        animationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();
    }

    // 默认图片
    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.kaws_anim;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        // NO-OP
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        // NO-OP
    }

    // 下拉以刷新
    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
        if(animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }

    // 正在刷新时回调
    @Override
    protected void refreshingImpl() {
        // 播放帧动画
        animationDrawable.start();
    }

    // 释放以刷新
    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
    }

    // 重新设置
    @Override
    protected void resetImpl() {
        mHeaderImage.setVisibility(View.VISIBLE);
        if(animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }

}
