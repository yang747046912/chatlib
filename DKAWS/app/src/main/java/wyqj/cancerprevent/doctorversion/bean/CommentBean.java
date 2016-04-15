package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Acer on 2015/7/26.
 */
public class CommentBean {

    @SerializedName("contribution_log")
    public ContributionLog contribution_log;

    public class ContributionLog {
        public String changed_amount;
        public String current_amount;
    }
}
