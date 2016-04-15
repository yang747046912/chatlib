package wyqj.cancerprevent.doctorversion.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JB on 2016/3/17.
 */
public class ApplyWithdrawBean {

    @SerializedName("has_set_account")
    private Boolean has_set_account;

    @SerializedName("account_number")
    private String account_number;

    @SerializedName("bank_name")
    private String bank_name;

    @SerializedName("account_name")
    private String account_name;

    @SerializedName("sub_branch")
    private String sub_branch;


    public String getSub_branch() {
        return sub_branch;
    }

    public Boolean getHas_set_account() {
        return has_set_account;
    }

    public String getAccount_number() {
        return account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getAccount_name() {
        return account_name;
    }
}
