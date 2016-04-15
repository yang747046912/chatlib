package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 项目名称：KawsDoctorversion
 * 类名称：QuestionDetailBean
 * 创建人：shilei
 * 修改人：shilei
 * 修改时间：2015/7/16 17:43
 * 修改备注：
 */

public class QuestionDetailBean {

    public ArrayList<comments> comments;
    public Question question;
    @SerializedName("is_answerable")
    public boolean isAnswerable;
    @SerializedName("doctor_status")
    public int doctorStatus;

    public class comments {
        public int id;
        @SerializedName("created_at")
        public String createdAt;
        public String content;
        @SerializedName("is_doctor")
        public boolean isDoctor;
        public Doctor doctor;

        public PatientBean getUser() {
            return user;
        }

        public void setUser(PatientBean user) {
            this.user = user;
        }

        public PatientBean user;
    }

    public class Question {
        public int id;
        public String title;
        @SerializedName("latest_commented_at")
        public String latestCommentedAt;
        @SerializedName("diseased_state")
        public DiseasedState diseasedState;
        public String content;

        public ArrayList<Images> images;
        public Questioner questioner;
        public Doctor doctor;
    }

//    id: 101,
//    url: "http://7xk154.com1.z0.glb.clouddn.com/c692feeb-0477-4ab6-ba1e-53dde1ae67f9",
//    width: 3264,
//    height: 2448

    public class Images {
        public int id;
        public String url;
        public int width;
        public int height;
    }

    public class DiseasedState {
        public int id;
        public String name;
    }

    public class Doctor {
        public int id;
        public String name;
        @SerializedName("avatar_url")
        public String avatarUrl;

    }

    public class Questioner {
        public String userkey;
        public String username;
        @SerializedName("avatar_url")
        public String avatarUrl;
    }
}
