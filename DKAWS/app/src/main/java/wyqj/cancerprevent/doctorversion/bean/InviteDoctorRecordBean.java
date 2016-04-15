package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：KawsDoctorversion
 * 类名称：InviteDoctorRecordBean
 * 创建人：jingbin
 * 修改人：jingbin
 * 修改时间：2016/2/22 16:30
 * 修改备注：
 */

public class InviteDoctorRecordBean {


    @SerializedName("invited_doctors_count")
    public int invitedDoctorsCount;
    @SerializedName("got_contribution_value")
    public int gotContributionValue;
    @SerializedName("invite_details")
    public List<InviteDoctorBean> inviteDetails;

    public class InviteDoctorBean {

        public int id;
        public String name;
        public String created_at;
        public float amount;

    }

    @Override
    public String toString() {
        return "InviteDoctorRecordBean{" +
                "invitedDoctorsCount=" + invitedDoctorsCount +
                ", gotContributionValue=" + gotContributionValue +
                ", inviteDetails=" + inviteDetails +
                '}';
    }
}
