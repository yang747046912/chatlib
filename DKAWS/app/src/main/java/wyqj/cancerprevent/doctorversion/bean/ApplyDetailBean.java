package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;
import com.kaws.lib.http.ParamNames;

/**
 * Created by 杨才 on 2016/3/2.
 */
public class ApplyDetailBean {
    public String id;
    @SerializedName("agree_status")
    public int agreeStatus;
    @ParamNames("patient")
    public PatientBean patient;
}
