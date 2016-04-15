package wyqj.cancerprevent.doctorversion.bean;

import com.kaws.lib.http.ParamNames;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaguangcheng on 16/3/26.
 */
public class OrderDetailBean {
    @ParamNames("user_phone_number")
    private String user_phone_number;

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getCall_at() {
        return call_at;
    }

    public MedicalRecordBean getMedical_record() {
        return medical_record;
    }

    public List<ImageBean> getImages() {
        return images;
    }

    @ParamNames("user_name")

    private String user_name;
    @ParamNames("call_at")
    private String call_at;
    @ParamNames("state")
    private String state;
    @ParamNames("medical_record")
    private MedicalRecordBean medical_record;
    @ParamNames("images")
    private List<ImageBean> images;

    @ParamNames("details")
    private String details;

    public String getState() {
        return state;
    }

    public String getDetails() {
        return details;
    }
}
