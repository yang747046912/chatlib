package wyqj.cancerprevent.doctorversion.bean;

import com.kaws.lib.http.ParamNames;

/**
 * Created by czh on 2015/7/29.
 * 发送验证码失败的Bean
 */
public class ErrorBean {
    @ParamNames("code")
    private int code;

    @ParamNames("message")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
