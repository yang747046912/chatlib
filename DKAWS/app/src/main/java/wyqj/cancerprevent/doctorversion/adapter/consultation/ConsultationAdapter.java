package wyqj.cancerprevent.doctorversion.adapter.consultation;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.TimeUtil;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ConsultListBean;
import wyqj.cancerprevent.doctorversion.constant.ConsultationOrderStatus;

/**
 * Created by 杨才 on 2016/3/24.
 */
public class ConsultationAdapter extends BaseRecyclerViewAdapter<ConsultListBean.OrdersBean> {

    private int status;

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_consultat, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseRecyclerViewHolder<ConsultListBean.OrdersBean> {
        private TextView tag;
        private TextView name;
        private TextView age;
        private TextView sex;
        private TextView nowTime;
        private TextView consultationTime;
        private TextView state;

        public ViewHolder(View itemView) {
            super(itemView);
            tag = (TextView) itemView.findViewById(R.id.tv_diease_tag);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            age = (TextView) itemView.findViewById(R.id.tv_age);
            sex = (TextView) itemView.findViewById(R.id.tv_sex);
            nowTime = (TextView) itemView.findViewById(R.id.tv_now_time);
            consultationTime = (TextView) itemView.findViewById(R.id.tv_consultation_time);
            state = (TextView) itemView.findViewById(R.id.tv_state);
        }

        @Override
        public void onBindViewHolder(final ConsultListBean.OrdersBean object, final int position) {
            ConsultListBean.OrdersBean.MedicalRecordBean bean = object.getMedical_record();
            if (bean != null) {
                tag.setText(bean.getDiseased_state_name());
                name.setText(bean.getName());
                age.setText(bean.getAge());
                sex.setText(bean.getSex());
            }
            consultationTime.setText("预约时间：" + TimeUtil.timeFormat(object.getCall_at()));
            if (status == ConsultationOrderStatus.WAITING) {
                nowTime.setText(TimeUtil.getTranslateTime(object.getCreated_at()));
                if (TimeUtil.less4hours(object.getCall_at())) {
                    consultationTime.setTextColor(consultationTime.getContext().getResources().getColor(R.color.red));
                } else {
                    consultationTime.setTextColor(consultationTime.getContext().getResources().getColor(R.color.txt_hint_color));
                }
                if (object.Hasread()) {
                    itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.item_gray));
                } else {
                    itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.white));
                }
                if (TextUtils.isEmpty(object.getState())) {
                    state.setVisibility(View.GONE);
                } else {
                    state.setText(getState(object.getState()));
                }
            } else if (status == ConsultationOrderStatus.CONFIRMED) {
                nowTime.setText(TimeUtil.getTranslateTimeLater(object.getCall_at()));
                if (TimeUtil.lessOneHour(object.getCall_at())) {
                    nowTime.setTextColor(nowTime.getContext().getResources().getColor(R.color.red));
                } else {
                    nowTime.setTextColor(nowTime.getContext().getResources().getColor(R.color.txt_hint_color));
                }
                state.setVisibility(View.GONE);
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.white));
            } else if (status == ConsultationOrderStatus.FINISHED) {
                if (TextUtils.isEmpty(object.getState())) {
                    state.setVisibility(View.GONE);
                } else {
                    state.setVisibility(View.VISIBLE);
                    state.setText(getState(object.getState()));
                }
                nowTime.setText(TimeUtil.getTime(object.getUpdatedAt()));
                itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.white));
            }

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

    private String getState(String state) {
        if (TextUtils.isEmpty(state)) {
            return "";
        }
        String tmp = ConsultationOrderStatus.orderState.get(state);
        if (TextUtils.isEmpty(tmp)) {
            return "";
        }
        return tmp;
    }
}
