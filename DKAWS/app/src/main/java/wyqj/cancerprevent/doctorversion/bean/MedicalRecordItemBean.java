package wyqj.cancerprevent.doctorversion.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jb on 2016/3/22.
 */
public class MedicalRecordItemBean {

    @SerializedName("id")
    private Integer id;

    @SerializedName("content")
    private String content;

    @SerializedName("record_date")
    private String record_date;

    @SerializedName("images")
    private List<IllNoteOthersBean.ImageBean> images;

    public Integer getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getRecord_date() {
        return record_date;
    }

    public List<IllNoteOthersBean.ImageBean> getImages() {
        return images;
    }
}
