package wyqj.cancerprevent.doctorversion.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaws.lib.common.widget.wheel.adapter.WheelViewAdapter;

public class MyWheelAdapter implements WheelViewAdapter {

	public String[] list_adapter;
	private Context context;

	public MyWheelAdapter(String[] list_adapter, Context context) {
		this.list_adapter = list_adapter;
		this.context = context;
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public int getItemsCount() {
		return list_adapter == null ? 0 : list_adapter.length;
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		TextView txt;
		if (convertView == null) {
			txt = new TextView(context);
			convertView = txt;
			convertView.setTag(txt);
		} else {
			txt = (TextView) convertView.getTag();
		}
		txt.setGravity(Gravity.CENTER);
		txt.setTextSize(20);
		txt.setText(list_adapter[index]);
		return convertView;
	}

	@Override
	public View getEmptyItem(View convertView, ViewGroup parent) {
		return null;
	}
}