package com.kaws.lib.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kaws.lib.common.R;

/**
 * Created by 杨才 on 2016/2/3.
 */
public abstract class NoTitleLoadBaseFragment extends LoadBaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.no_title_loading_fragment, null);
        contentView = inflater.inflate(setContent(), null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(params);
        ll.addView(contentView);
        return ll;
    }
}
