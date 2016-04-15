package wyqj.cancerprevent.doctorversion.headview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.InviteDoctorRecordBean;

/**
 * Created by 杨才 on 2016/3/14.
 */
public class MyInviteRecordHead extends BaseHead<InviteDoctorRecordBean> {
    private Context mContext;
    private TextView tv_invited_doctors_count;
    private TextView tv_got_contribution_value;

    private View head;

    public MyInviteRecordHead(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View getHeadView(int layoutID) {
        head = View.inflate(mContext, layoutID, null);
        tv_invited_doctors_count = (TextView) head.findViewById(R.id.tv_invited_doctors_count);
        tv_got_contribution_value = (TextView) head.findViewById(R.id.tv_got_contribution_value);

        return head;
    }

    @Override
    public void setUpView(InviteDoctorRecordBean object) {
        tv_got_contribution_value.setText("" + object.gotContributionValue);
        tv_invited_doctors_count.setText("" + object.invitedDoctorsCount);
        List<InviteDoctorRecordBean.InviteDoctorBean> inviteDetails = object.inviteDetails;

        if (inviteDetails != null && inviteDetails.size() != 0) {
            head.setVisibility(View.VISIBLE);
        } else {
            head.setVisibility(View.GONE);
        }
    }

    public boolean isEmpty() {
        return head == null;
    }
}
