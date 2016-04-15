package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xiaguangcheng on 16/4/7.
 */
public class CanConsultBean {
    @SerializedName("can_consult")
    private boolean can_consult;

    public boolean isCan_consult() {
        return can_consult;
    }
}
