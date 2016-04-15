package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 项目名称：KawsDoctorversion
 * 类名称：PatientEntity
 * 创建人：shilei
 * 修改人：shilei
 * 修改时间：2015/7/27 13:59
 * 修改备注：
 */

public class PatientBean {

    public String userkey;
    public String username;
    public int level;
    @SerializedName("avatar_url")
    public String avatarRrl;
    public int age;
    @SerializedName("sick_date")
    public String sickDate;
    public String sex;
    public String area;
    @SerializedName("diseased_states")
    public ArrayList<PatientDiseases> diseases;

    public String type;//2-->家属  1--->患者
    public String relationship;//与患者关系

    @SerializedName("has_completed_medical_record")
    public boolean isCompletedRecord;

    public class PatientDiseases {
        public int id;
        public String name;
        public int step;
        public int position;
    }
}
