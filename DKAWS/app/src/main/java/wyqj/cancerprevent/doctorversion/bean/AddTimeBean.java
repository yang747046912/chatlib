package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xiaguangcheng on 16/4/7.
 */
public class AddTimeBean {
    @SerializedName("date")
    private String date;

    @SerializedName("week")
    private List<Integer> week;

    public String getDate() {
        return date;
    }

    public List<Integer> getWeek() {
        return week;
    }

    public List<Integer> getDay() {
        return day;
    }

    @SerializedName("day")
    private List<Integer> day;

}
