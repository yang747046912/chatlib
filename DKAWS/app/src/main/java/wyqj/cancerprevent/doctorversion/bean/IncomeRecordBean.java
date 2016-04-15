package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 杨才 on 2016/2/21.
 */
public class IncomeRecordBean {
    public int id;
    public int action;
    public int status = -1;
    @SerializedName("created_at")
    public String createdAt;
    public String content;
    public int amount;
}
