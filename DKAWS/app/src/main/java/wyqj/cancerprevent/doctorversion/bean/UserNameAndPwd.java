package wyqj.cancerprevent.doctorversion.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/12.
 */
public class UserNameAndPwd implements Serializable {
    private String userName;
    private String pwd;

    public UserNameAndPwd(String userName, String pwd) {
        this.userName = userName;
        this.pwd = pwd;
    }

    public String getUserName() {
        return userName;
    }

    public String getPwd() {
        return pwd;
    }
}