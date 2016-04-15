package com.kaws.chat.lib.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 项目名称：kaws-android
 * 类名称：ResizeImageView
 * 创建人：杨才
 * 修改人：杨才
 * 修改时间：2015/12/16 17:30
 * 修改备注：
 */
public class ResizeImageView extends ImageView {
    private int screenSize;

    public ResizeImageView(Context context) {
        this(context, null);
    }

    public ResizeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResizeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenSize = dm.widthPixels;
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        if (bWidth > screenSize * 3 / 8) {
            bWidth = screenSize * 3 / 8;
        } else if (bWidth < screenSize / 4) {
            bWidth = screenSize / 4;
        }

        if (bHeight > screenSize * 3 / 8) {
            bHeight = screenSize * 3 / 8;
        } else if (bHeight < screenSize / 4) {
            bHeight = screenSize / 4;
        }
        setLayoutParams(new RelativeLayout.LayoutParams(bWidth, bHeight));
        setScaleType(ScaleType.CENTER_CROP);
        super.setImageBitmap(bitmap);
    }
}
