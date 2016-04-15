package wyqj.cancerprevent.doctorversion.utils;

import android.content.Context;

import com.kaws.lib.common.cache.ACache;

import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.bean.UserNameAndPwd;

/**
 * Created by 杨才 on 2016/2/1.
 */
public class SQuser {
    private static final String SQUSER = "squser";
    private static final String SQUSER_NAME_AND_PWD = "squser_name_and_pwd";
    private static SQuser instance;
    private UserBean user;
    private boolean isLogin = false;
    public boolean needonRefresh = false;

    public boolean isLogin() {
        return isLogin;
    }

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static SQuser getInstance() {
        synchronized (SQuser.class) {
            if (instance == null) {
                instance = new SQuser();
            }
        }
        return instance;
    }

    private SQuser() {
    }

    public void saveUserInfo(UserBean user) {
        synchronized (SQuser.class) {
            this.user = new UserBean();
            this.user.doctorid = user.doctorid;
            this.user.status = user.status;
            this.user.token = user.token;
            this.user.userkey = user.userkey;
            this.user.info = user.info;
            this.isLogin = true;
        }
        ACache aCache = ACache.get(mContext);
        aCache.put(SQUSER, this.user);
    }

    public void saveUserInfo(UserBean.Doctor doctor) {
        this.user.info = doctor;
        this.user.status = doctor.status;
        ACache aCache = ACache.get(mContext);
        aCache.put(SQUSER, this.user);
    }

    public UserBean getUserInfo() {
        synchronized (SQuser.class) {
            if (user == null) {
                ACache aCache = ACache.get(mContext);
                user = (UserBean) aCache.getAsObject(SQUSER);
            }
        }
        return user;
    }

    @Override
    public String toString() {
        return "SQuser{" +
                "user=" + user +
                ", isLogin=" + isLogin +
                '}';
    }

    public void saveUserNameAndPwd(String userName, String pwd) {
        ACache aCache = ACache.get(mContext);
        aCache.put(SQUSER_NAME_AND_PWD, new UserNameAndPwd(userName, pwd));
    }

    public void clearUserNameAndPwd() {
        ACache aCache = ACache.get(mContext);
        aCache.remove(SQUSER_NAME_AND_PWD);
    }

    public String[] getUserNameAndPwd() {
        ACache aCache = ACache.get(mContext);
        UserNameAndPwd object = (UserNameAndPwd) aCache.getAsObject(SQUSER_NAME_AND_PWD);
        if (object == null) {
            return new String[]{null, null};
        }
        return new String[]{object.getUserName(), object.getPwd()};
    }


}
