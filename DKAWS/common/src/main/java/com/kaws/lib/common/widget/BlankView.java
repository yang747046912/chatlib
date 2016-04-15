package com.kaws.lib.common.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaws.lib.common.R;

/**
 * Created by 杨才 on 2016/2/2.
 */
public class BlankView extends LinearLayout {
    private TextView tip;
    private Button click;
    private ImageView noData;

    private String defaultTip;
    private boolean showButton;
    private Drawable drawable;
    private Drawable bDrawable;
    private ColorStateList colorStateList;

    public BlankView(Context context) {
        this(context, null);
    }

    public BlankView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BlankView);
        defaultTip = ta.getString(R.styleable.BlankView_text);
        showButton = ta.getBoolean(R.styleable.BlankView_showButton, false);
        drawable = ta.getDrawable(R.styleable.BlankView_drawable);
        colorStateList = ta.getColorStateList(R.styleable.BlankView_textColor);
        bDrawable = ta.getDrawable(R.styleable.BlankView_buttonBackGround);
        ta.recycle();
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.blank_view, this);
        tip = (TextView) findViewById(R.id.tip);
        tip.setText(defaultTip);
        if (colorStateList != null) {
            tip.setHintTextColor(colorStateList);
        }
        click = (Button) findViewById(R.id.btn_click);
        if (showButton) {
            click.setVisibility(VISIBLE);
        }
        if (bDrawable != null) {
            click.setBackgroundDrawable(bDrawable);
        }
        noData = (ImageView) findViewById(R.id.img_no_data);
        if (drawable != null) {
            noData.setImageDrawable(drawable);
        }
    }

    public void setText(CharSequence charSequence) {
        tip.setText(charSequence);
    }

    public void setOnItemClickListener(OnClickListener clickListener) {
        click.setOnClickListener(clickListener);
    }

    public void setImageDrawable(Drawable drawable) {
        noData.setImageDrawable(drawable);
    }

    public Button getClickButton() {
        return click;
    }
}
