package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：KawsDoctorversion
 * 类名称：ApplyRecordBean
 * 创建人：shilei
 * 修改人：shilei
 * 修改时间：2015/7/16 14:02
 * 修改备注：
 */

public class ApplyRecordBean {

    public String id;
    public Patient patient;
    @SerializedName("agree_status")
    public int agreeStatus;
    @SerializedName("latest_commented_at")
    public String latestCommentedAt;


    public class Patient {
        public String userkey;
        public String username;
        @SerializedName("avatar_url")
        public String avatarurl;
    }
}
