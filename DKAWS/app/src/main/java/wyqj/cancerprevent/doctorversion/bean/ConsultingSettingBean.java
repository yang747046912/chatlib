package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaguangcheng on 16/4/7.
 */
public class ConsultingSettingBean {
    @SerializedName("dates")
    private List<String> dates;

    @SerializedName("can_consult")
    private boolean can_consult;

    public List<String> getDates() {
        return dates;
    }

    public boolean isCan_consult() {
        return can_consult;
    }

    public String getPrice() {
        return price;
    }

    public String getPhone_number() {
        return phone_number;
    }

    @SerializedName("price")
    private String price;

    @SerializedName("phone_number")
    private String phone_number;
}
