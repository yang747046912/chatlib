package wyqj.cancerprevent.doctorversion.adapter.square;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.bigimage.ViewBigImageActivity;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;
import com.kaws.lib.common.utils.DensityUtil;
import com.kaws.lib.common.utils.ScreenSizeUtil;
import com.kaws.lib.fresco.Image;

import java.util.ArrayList;
import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.IllNoteOthersBean;
import wyqj.cancerprevent.doctorversion.bean.MedicalRecordItemBean;

/**
 * Created by 景彬 on 2016/3/22.
 */
public class TimelineAdapter extends BaseRecyclerViewAdapter<MedicalRecordItemBean> {
    private ArrayList<String> imageUrlList;

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline_new_ill_note_others, null);
        imageUrlList = new ArrayList<>();
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseRecyclerViewHolder<MedicalRecordItemBean> {
        TextView time;
        TextView content;
        GridView gridView;

        public ViewHolder(View childView) {
            super(childView);
            time = (TextView) childView.findViewById(R.id.time_item_timeline_others);
            content = (TextView) childView.findViewById(R.id.content_item_timeline_others);
            gridView = (GridView) childView.findViewById(R.id.gdv_item_timeline_others);
        }

        @Override
        public void onBindViewHolder(final MedicalRecordItemBean medicalRecordItemBean, int position) {
            String time = null;
            String content = null;
            List<IllNoteOthersBean.ImageBean> imageBeanList = null;
            if (medicalRecordItemBean != null) {
                time = medicalRecordItemBean.getRecord_date();
                content = medicalRecordItemBean.getContent();
                if (time != null) {
                    this.time.setText(time.substring(0, 10));
                }
                this.content.setText(content);
                gridView.setAdapter(new NinePicsAdapter(medicalRecordItemBean.getImages(), itemView.getContext()));
                imageBeanList = medicalRecordItemBean.getImages();
            }


            final List<IllNoteOthersBean.ImageBean> finalImageBeanList = imageBeanList;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int length = finalImageBeanList.size();
                    imageUrlList.clear();
                    for (int x = 0; x < length; x++) {
                        imageUrlList.add(finalImageBeanList.get(x).getUrl());
                    }

                    Bundle bundle = new Bundle();
                    bundle.putInt("code", i);
                    bundle.putInt("selet", 2);
                    bundle.putStringArrayList("imageuri", imageUrlList);
                    Intent intent = new Intent(adapterView.getContext(), ViewBigImageActivity.class);
                    intent.putExtras(bundle);
                    adapterView.getContext().startActivity(intent);
                }
            });
        }
    }

    class NinePicsAdapter extends BaseAdapter {
        private final int itemWidthAndHeight;
        List<IllNoteOthersBean.ImageBean> list;

        public NinePicsAdapter(List<IllNoteOthersBean.ImageBean> list, Context context) {
            this.list = list;
            itemWidthAndHeight = (ScreenSizeUtil.getScreenWidth(context) - DensityUtil.dip2px(context, 46)) / 3;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View childView = null;
            ViewHolder holder;
            if (childView == null) {
                childView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nine_pics, viewGroup, false);
                holder = new ViewHolder();
                holder.simpleView = (SimpleDraweeView) childView.findViewById(R.id.img_item_nine_pics);
                childView.setTag(holder);
            } else {
                childView = view;
                holder = (ViewHolder) childView.getTag();
            }
            holder.simpleView.setLayoutParams(new RelativeLayout.LayoutParams(itemWidthAndHeight, itemWidthAndHeight));
            Image.displayImage(holder.simpleView, Uri.parse(list.get(i).getUrl()), R.mipmap.default_pic);
            return childView;
        }

        class ViewHolder {
            SimpleDraweeView simpleView;
        }
    }
}
