package wyqj.cancerprevent.doctorversion.adapter.mine;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.InviteDoctorRecordBean;

/**
 * Created by 杨才 on 2016/3/14.
 */
public class InviteRecordAdapter extends BaseRecyclerViewAdapter<InviteDoctorRecordBean.InviteDoctorBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewe = View.inflate(parent.getContext(), R.layout.item_myinvite_record, null);
        return new ViewHolder(viewe);
    }

    class ViewHolder extends BaseRecyclerViewHolder<InviteDoctorRecordBean.InviteDoctorBean> {
        TextView tx_invite_name;
        TextView tx_invite_time;
        TextView tx_invite_reward;

        public ViewHolder(View view) {
            super(view);
            tx_invite_name = (TextView) view.findViewById(R.id.tv_invite_record_name);
            tx_invite_time = (TextView) view.findViewById(R.id.tv_invite_record_time);
            tx_invite_reward = (TextView) view.findViewById(R.id.tv_invite_record_reward);
        }

        @Override
        public void onBindViewHolder(InviteDoctorRecordBean.InviteDoctorBean InviteDoctorBean, int position) {
            tx_invite_name.setText(InviteDoctorBean.name);
            tx_invite_time.setText(InviteDoctorBean.created_at.substring(0, 10));
            String amount = String.valueOf(InviteDoctorBean.amount);
            if (amount.contains(".0")) {
                amount = amount.replace(".0", "");
            }
            tx_invite_reward.setText(amount);
        }
    }
}
