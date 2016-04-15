package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：KawsDoctorversion
 * 类名称：MedicalBookEntity
 * 创建人：shilei
 * 修改人：shilei
 * 修改时间：2015/8/6 11:45
 * 修改备注：
 */

public class MedicalBookEntity {
    public String id;
    public String name;
    public int age;
    public String sex;
    @SerializedName("confirmed_date")
    public String confirmedDate;
    public String symptom;
    public String treatment;
    public String anamnesis;
    @SerializedName("diseased_states")
    public List<DiseasedStates> diseases;//患病信息

    public OtherInfoEntity diagnose;//确诊单
    @SerializedName("assay_report")
    public OtherInfoEntity assayReport; //化验报告
    @SerializedName("pathological_report")
    public OtherInfoEntity PathologicalReport;//病理检查

    @SerializedName("medicine_record")
    public OtherInfoEntity medicineRecord;//用药记录
    @SerializedName("re_examination")
    public OtherInfoEntity reExamination;//复查记录

    public OtherInfoEntity imaging;//影响学资料
    @SerializedName("wound_photo")
    public OtherInfoEntity woundPhoto;//伤口拍照
    @SerializedName("in_hospital_record")
    public OtherInfoEntity inHospitalRecord;//住院及其他

    public class DiseasedStates {
        public int id;
        public String name;
    }

    public class diagnose {
        public int id;
        public int type_id;
        public String content;
        public String record_date;
        public List<Images> images;
    }

    public class Images {
        @SerializedName("id")
        public int imageId;
        public String url;
    }

    public class OtherInfoEntity {
        public int id;
        @SerializedName("type_id")
        public int typeId;
        public String content;
        @SerializedName("record_date")
        public String recordDate;
        public List<Images> images;
    }

}
