package wyqj.cancerprevent.doctorversion.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.common.utils.DensityUtil;
import com.kaws.lib.common.utils.ScreenSizeUtil;
import com.kaws.lib.fresco.Image;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;

/**
 * 项目名称：KawsDoctorversion
 * 类名称：PicGridAdapter
 * 创建人：shilei
 * 修改人：shilei
 * 修改时间：2015/7/16 19:34
 * 修改备注：
 */

public class PicGridAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<String> list;
    int itemWidthAndHeight;

    public PicGridAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
        itemWidthAndHeight = (ScreenSizeUtil.getScreenWidth(context) - DensityUtil.dip2px(context, 30)) / 3;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View childView = null;
        ViewHolder holder;
        if (childView == null) {
            childView = View.inflate(context, R.layout.item_pic_grid, null);
            holder = new ViewHolder();
            holder.imageView = (SimpleDraweeView) childView.findViewById(R.id.iv_item_picgrid);
            childView.setTag(holder);
        } else {
            childView = view;
            holder = (ViewHolder) childView.getTag();
        }
        holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(itemWidthAndHeight, itemWidthAndHeight));
        Image.displayImage(holder.imageView, Uri.parse(list.get(i)));
        return childView;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    class ViewHolder {
        SimpleDraweeView imageView;
    }

}
