package wyqj.cancerprevent.doctorversion.bean;

/**
 * Created by 杨才 on 2016/3/27.
 */
public class CheckBean {

    /**
     * errors : []
     * is_confirmed : true
     * is_phd : false
     * status : 4
     */

    private boolean is_confirmed;
    private boolean is_phd;
    private int status;


    public boolean isIs_confirmed() {
        return is_confirmed;
    }

    public void setIs_confirmed(boolean is_confirmed) {
        this.is_confirmed = is_confirmed;
    }

    public boolean isIs_phd() {
        return is_phd;
    }

    public void setIs_phd(boolean is_phd) {
        this.is_phd = is_phd;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
