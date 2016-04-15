package wyqj.cancerprevent.doctorversion.bean;

import java.io.Serializable;

/**
 * Created by 杨才 on 2016/2/4.
 */
public class RegisterBean implements Serializable {
    public String mobile;
    public String password;
    public String name;
    public int degree;
    public String department;
    public int hospitalId;
    public int[] disease_ids = new int[0];
    public String hospitalName;
    public String invitationCode;
    public String verifyCode;
    public String person_intro;
}
