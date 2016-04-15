package com.kaws.lib.common.baseadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 杨才 on 2016/1/29.
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * @param object   the data of bind
     * @param position the item positon of recyclerView
     */
    public abstract void onBindViewHolder(T object, final int position);
}
