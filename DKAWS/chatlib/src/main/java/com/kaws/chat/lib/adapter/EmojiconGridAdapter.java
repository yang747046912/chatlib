package com.kaws.chat.lib.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.util.DensityUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.chat.lib.R;
import com.kaws.chat.lib.domain.EaseEmojicon;
import com.kaws.chat.lib.domain.EaseEmojicon.Type;
import com.kaws.chat.lib.utils.EaseSmileUtils;
import com.kaws.lib.fresco.Image;

import java.util.List;

public class EmojiconGridAdapter extends ArrayAdapter<EaseEmojicon> {

    private final int itemSize;
    private final int itemSizeH;
    private Type emojiconType;


    public EmojiconGridAdapter(Context context, int textViewResourceId, List<EaseEmojicon> objects, EaseEmojicon.Type emojiconType) {
        super(context, textViewResourceId, objects);
        this.emojiconType = emojiconType;

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        itemSize = dm.widthPixels / 7;
        itemSizeH = DensityUtil.dip2px(context, 130) / 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (emojiconType == Type.BIG_EXPRESSION) {
                convertView = View.inflate(getContext(), R.layout.ease_row_big_expression, null);
            } else {
                convertView = View.inflate(getContext(), R.layout.ease_row_expression, null);
            }
        }

        SimpleDraweeView imageView = (SimpleDraweeView) convertView.findViewById(R.id.iv_expression);
        AbsListView.LayoutParams alp = new AbsListView.LayoutParams(itemSize, itemSizeH);
        convertView.setLayoutParams(alp);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemSizeH * 7 / 10, itemSizeH * 7 / 10);
        imageView.setLayoutParams(lp);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_name);
        EaseEmojicon emojicon = getItem(position);
        if (textView != null && emojicon.getName() != null) {
            textView.setText(emojicon.getName());
        }
        if (EaseSmileUtils.DELETE_KEY.equals(emojicon.getEmojiText())) {
            imageView.setImageResource(R.drawable.ease_delete_expression);
        } else {
            if (emojicon.getIcon() != 0) {
                imageView.setImageResource(emojicon.getIcon());
            } else if (emojicon.getIconPath() != null) {
                Image.displayImage(imageView, Uri.parse("file://" + emojicon.getIconPath()), R.drawable.ease_default_expression);
            }
        }
        return convertView;
    }

}
