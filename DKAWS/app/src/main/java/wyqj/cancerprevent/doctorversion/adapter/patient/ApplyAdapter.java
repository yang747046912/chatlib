package wyqj.cancerprevent.doctorversion.adapter.patient;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ApplyRecordBean;

/**
 * Created by 杨才 on 2016/2/2.
 */
public class ApplyAdapter extends BaseRecyclerViewAdapter<ApplyRecordBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applyrecord, null);
        return new ViewHolder(view);
    }


    class ViewHolder extends BaseRecyclerViewHolder<ApplyRecordBean> {
        TextView applyRecord;
        TextView tv_nothing;
        TextView status;
        ImageView arrow;

        public ViewHolder(View childView) {
            super(childView);
            applyRecord = (TextView) childView.findViewById(R.id.tv_item_applyrecord);
            status = (TextView) childView.findViewById(R.id.tv_apply_status);
            tv_nothing = (TextView) childView.findViewById(R.id.tv_nothing);
            arrow = (ImageView) childView.findViewById(R.id.img_arrow);
        }

        @Override
        public void onBindViewHolder(final ApplyRecordBean object, final int position) {
            ApplyRecordBean.Patient patient = object.patient;
            if (patient != null) {
                applyRecord.setText(patient.username);
            }
            int status = object.agreeStatus;
            if (status == 1) {//申请中
                this.status.setVisibility(View.GONE);
                arrow.setVisibility(View.VISIBLE);
                applyRecord.setTextColor(itemView.getContext().getResources().getColor(R.color.but_bg));
                tv_nothing.setTextColor(itemView.getContext().getResources().getColor(R.color.text_common));
            } else if (status == 3) {//已同意
                arrow.setVisibility(View.GONE);
                applyRecord.setTextColor(itemView.getContext().getResources().getColor(R.color.text_hint));
                tv_nothing.setTextColor(itemView.getContext().getResources().getColor(R.color.text_hint));
                applyRecord.setTextSize(17);
                this.status.setVisibility(View.VISIBLE);
                this.status.setText("已同意");
            } else if (status == 2) {//已拒绝
                arrow.setVisibility(View.GONE);
                applyRecord.setTextColor(itemView.getContext().getResources().getColor(R.color.text_hint));
                tv_nothing.setTextColor(itemView.getContext().getResources().getColor(R.color.text_hint));
                this.status.setVisibility(View.VISIBLE);
                applyRecord.setTextSize(17);
                this.status.setText("已拒绝");
            }
            if (listener != null) {
                if (status == 1) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onClick(object, position);
                        }
                    });
                } else {
                    itemView.setClickable(false);
                }
            }
        }
    }
}
