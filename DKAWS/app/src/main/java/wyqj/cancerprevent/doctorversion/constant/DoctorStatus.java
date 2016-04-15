package wyqj.cancerprevent.doctorversion.constant;

/**
 * Created by 杨才 on 2016/2/22.
 */
public class DoctorStatus {
    public static final int UNREGISTERED = 1;// 没有注册
    /**注册 但没有上传资料并认证*/
    public static final int JUST_REGISTERED = 2;
    /**注册 待确认*/
    public static final int PENDING = 3;
    /** 注册 已经确认*/
    public static final int CONFIRMED = 4;
    /**审核失败*/
    public static final int REGISTER_FAILED = 5;
}
