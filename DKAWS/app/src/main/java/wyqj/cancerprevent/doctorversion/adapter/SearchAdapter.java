package wyqj.cancerprevent.doctorversion.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;
import com.kaws.lib.common.event.PerfectClickListener;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ChooseBean;

/**
 * Created by 杨才 on 2016/3/17.
 */
public class SearchAdapter extends BaseRecyclerViewAdapter<ChooseBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(parent.getContext(), R.layout.item_poup_consult, null);
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseRecyclerViewHolder<ChooseBean> {
        private TextView item;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.tv_item_consult_popup);
        }

        @Override
        public void onBindViewHolder(final ChooseBean object, final int position) {
            item.setText(object.getName());
            if (listener != null) {
                itemView.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        listener.onClick(object, position);
                    }
                });
            }
        }
    }

}
