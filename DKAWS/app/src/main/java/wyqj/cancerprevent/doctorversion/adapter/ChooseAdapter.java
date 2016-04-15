package wyqj.cancerprevent.doctorversion.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ChooseBean;

/**
 * Created by 杨才 on 2016/2/17.
 */
public class ChooseAdapter extends BaseAdapter {
    private List<ChooseBean> cityEntities = new ArrayList<ChooseBean>();
    private int selectItem=-1;

    public ChooseAdapter(List<ChooseBean> cityEntities) {
        this.cityEntities = cityEntities;
    }

    @Override
    public int getCount() {
        return cityEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return cityEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView item;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_poup_consult, null);
            item = (TextView) convertView.findViewById(R.id.tv_item_consult_popup);
            convertView.setTag(item);
        } else {
            item = (TextView) convertView.getTag();
        }
        item.setText(cityEntities.get(position).getName());
        if (position == selectItem) {
            item.setSelected(true);
        } else {
            item.setSelected(false);
        }
        return convertView;
    }

    public List<ChooseBean> getCityEntities() {
        return cityEntities;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }
}
