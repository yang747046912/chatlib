package wyqj.cancerprevent.doctorversion.bean;

import com.kaws.lib.http.ParamNames;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 杨才 on 2016/2/1.
 */
public class UserBean implements Serializable {
    @ParamNames("userkey")
    public String userkey;
    @ParamNames("id")
    public int doctorid;
    @ParamNames("status")
    public int status;
    @ParamNames("token")
    public String token;
    @ParamNames("doctor")
    public Doctor info;

    public class Doctor implements Serializable {
        @ParamNames("status")
        public int status;
        @ParamNames("name")
        public String name;
        @ParamNames("avatar_url")
        public String avatarUrl;
        //审核中的头像
        @ParamNames("applying_avatar_url")
        public String applying_avatar_url;
        @ParamNames("degree")
        public Degree degree;
        @ParamNames("hospital_name")
        public String hospitalName;
        public String department;
        @ParamNames("comment_count")
        public int commentCount;
        @ParamNames("is_confirmed")
        public boolean isConfirmed;
        @ParamNames("diseased_states")
        public ArrayList<Diseased> diseasedStates;
        public int[] disease_ids = new int[0];
        @ParamNames("hospital_id")
        public int hospitalId;
        @ParamNames("unblocked_amount")
        public float unblocked_amount;
        @ParamNames("current_amount")
        public float currentMoney;//当前收入
        @ParamNames("grand_total")
        public float totalMoney;//总计收入
        @ParamNames("invitation_code")
        public String invitationCode;
        @ParamNames("desc")
        public String desc;

        @ParamNames("question_rate")
        public QuestionRate questionRate; // 图文解答评价
        @ParamNames("phone_consulting_rating")
        public PhoneConsultingRating phoneConsultingRating; // 电话预约评价

        @ParamNames("qr_image")
        public QrImage qrImage;
        @ParamNames("is_applying_modify_profile")
        public boolean isApplyingModifyInfo;//我的资料申请修改信息状态

        @ParamNames("application_status")
        public int applicationStatus;//提现状态  1 代表正在提现  2 可以提现

        @ParamNames("share_link")
        public String shareLink;
        @ParamNames("unread_count")
        public int unreadCount;//未读消息
    }

    public class QuestionRate implements Serializable {
        @ParamNames("satisfaction")
        public String satisfaction;
        @ParamNames("dissatisfaction")
        public String dissatisfaction;
    }

    public class PhoneConsultingRating implements Serializable {
        @ParamNames("satisfaction")
        public String satisfaction;
        @ParamNames("dissatisfaction")
        public String dissatisfaction;
    }

    public class QrImage implements Serializable {
        public int id;
        public int width;
        public int height;
        public String url;
    }

    public class Degree implements Serializable {
        public int id;
        public String name;
    }

    public static class Diseased implements Serializable {
        public Diseased(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int id;
        public int step;
        public int position;
        public String name;

        @Override
        public String toString() {
            return name;
        }
    }
}
