package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：KawsDoctorversion
 * 类名称：RestError
 * 创建人：jingbin
 * 修改人：jingbin
 * 修改时间：2016/3/17 15:52
 * 修改备注：
 */

public class RestError {

    @SerializedName("code")
    public int code;

    @SerializedName("message")
    public String message;
}
