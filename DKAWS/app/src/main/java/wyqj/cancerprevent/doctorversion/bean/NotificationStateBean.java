package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaguangcheng on 16/4/7.
 */
public class NotificationStateBean {
    @SerializedName("has_message_notice")
    private boolean has_message_notice;

    @SerializedName("has_apply_notice")
    private boolean has_apply_notice;

    @SerializedName("has_user_comment_notice")
    private boolean has_user_comment_notice;

    public boolean isHas_message_notice() {
        return has_message_notice;
    }

    public boolean isHas_apply_notice() {
        return has_apply_notice;
    }

    public boolean isHas_user_comment_notice() {
        return has_user_comment_notice;
    }
}
