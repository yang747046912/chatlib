package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;
import com.kaws.lib.http.ParamNames;

/**
 * 项目名称：KawsDoctorversion
 * 类名称：QuestionBean
 * 创建人：shilei
 * 修改人：shilei
 * 修改时间：2015/7/15 20:03
 * 修改备注：
 */

public class QuestionBean {
    public int id;
    public String title;
    @SerializedName("created_at")
    public String time;
    @SerializedName("diseased_state")
    public DiseaseState diseaseState;
    public int price;
    public User user;
    @ParamNames("latest_commented_at")
    public String latestCommentTime;
    public class DiseaseState {
        @SerializedName("id")
        public int diseaseId;
        @SerializedName("name")
        public String diseaseName;

    }

    public class User {
        public String userkey;
        public String username;
        @SerializedName("avatar_url")
        public String avatarUrl;
    }
}
