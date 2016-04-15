package wyqj.cancerprevent.doctorversion.bean;

import com.kaws.lib.http.ParamNames;

/**
 * Created by xiaguangcheng on 16/3/26.
 */
public class MedicalRecordBean {
    @ParamNames("id")
    private String id;
    @ParamNames("userkey")
    private String userkey;
    @ParamNames("name")
    private String name;
    @ParamNames("age")
    private String age;
    @ParamNames("sex")
    private String sex;
    @ParamNames("diseased_state_name")
    private String diseased_state_name;
    @ParamNames("is_default")
    private boolean is_default;
    @ParamNames("is_completed")
    private boolean is_completed;

    public String getId() {
        return id;
    }

    public String getUserkey() {
        return userkey;
    }

    public boolean is_default() {
        return is_default;
    }

    public boolean is_completed() {
        return is_completed;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getDiseased_state_name() {
        return diseased_state_name;
    }

    public String getConfirmed_date() {
        return confirmed_date;
    }

    public String getRelationship() {
        return relationship;
    }

    @ParamNames("confirmed_date")
    private String confirmed_date;
    @ParamNames("relationship")
    private String relationship;
}
