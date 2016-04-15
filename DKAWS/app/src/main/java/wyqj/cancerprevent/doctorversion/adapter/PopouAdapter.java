package wyqj.cancerprevent.doctorversion.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaws.lib.common.event.PerfectClickListener;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.widget.ListViewPopup;

/**
 * 项目名称：KawsDoctorversion
 * 类名称：PopAdapter
 * 创建人：shilei
 * 修改人：shilei
 * 修改时间：2015/7/15 19:44
 * 修改备注：
 */

public class PopouAdapter extends BaseAdapter {
    /**这样默认就会显示擅长病种也就是第一个为选中状态*/
    private int selectItem = 0;
    public String[] list = new String[0];
    public Context context;
    private ListViewPopup.ISelecterLinstener linstener;

    public PopouAdapter(Context context, String[] list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final TextView item;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_poup_consult, null);
            item = (TextView) convertView.findViewById(R.id.tv_item_consult_popup);
            convertView.setTag(item);
        } else {
            item = (TextView) convertView.getTag();
        }
        item.setText(list[i]);
        /**如果相等,就显示为选中状态*/
        if (i == selectItem) {
            item.setSelected(true);
        } else {
            item.setSelected(false);
        }
        convertView.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (linstener != null) {
                    linstener.onSelected(i);
                }
                setSelectItem(i);
            }
        });
        return convertView;
    }

    @Override
    public Object getItem(int i) {
        return list[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    public void setLinstener(ListViewPopup.ISelecterLinstener linstener) {
        this.linstener = linstener;
    }
}
