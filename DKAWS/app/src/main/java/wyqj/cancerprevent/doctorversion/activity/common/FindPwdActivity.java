package wyqj.cancerprevent.doctorversion.activity.common;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class FindPwdActivity extends LoadBaseActivity {

    private EditText etForgetpwdPhone;
    private TextView btnForgetpwdVerifycode;
    private EditText etForgetpwdNewpwd;
    private EditText etForgetpwdVerifycode;
    private EditText etForgetpwdVerifypwd;
    private Button btnForgetpwdConfirm;

    private String phone;
    private String verifycode;
    private String firstPassword = "";
    private String secondPassword;
    private MycountTimer mycountTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        showContentView();
        setTitle("找回密码");
        initView();
        setUpView();
    }

    private void initView() {
        etForgetpwdPhone = (EditText) findViewById(R.id.et_forgetpwd_phone);
        btnForgetpwdVerifycode = (TextView) findViewById(R.id.tv_forgetpwd_verifycode);
        etForgetpwdNewpwd = (EditText) findViewById(R.id.et_forgetpwd_newpwd);
        etForgetpwdVerifycode = (EditText) findViewById(R.id.et_forgetpwd_verifycode);
        etForgetpwdVerifypwd = (EditText) findViewById(R.id.et_forgetpwd_verifypwd);
        btnForgetpwdConfirm = (Button) findViewById(R.id.btn_forgetpwd_confirm);
    }

    private void setUpView() {
        btnForgetpwdVerifycode.setOnClickListener(listener);
        btnForgetpwdConfirm.setOnClickListener(listener);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
            switch (id) {
                case R.id.tv_forgetpwd_verifycode:
                    phone = etForgetpwdPhone.getText().toString().trim();
                    if (!isMobileNO(phone)) {
                        TipDialog.hintDiaglog(FindPwdActivity.this, "请输入正确的手机号");
                        return;
                    }
                    startProgressDialog();
                    service.obtainfindpwdCode(HttpHead.getHeader("POST"), phone, 2, new CustomCallBack<ErrorBean>() {
                        @Override
                        public void onSuccess(ErrorBean errorBean) {
                            stopProgressDialog();
                            TipDialog.hintDiaglog(FindPwdActivity.this, "发送验证码成功，请等待");
                            mycountTimer = new MycountTimer(60000, 1000);
                            mycountTimer.start();
                        }

                        @Override
                        public void onFailure() {
                            stopProgressDialog();
                        }
                    });
                    break;
                case R.id.btn_forgetpwd_confirm:
                    takeoutData();
                    boolean isRight = checkNumber(phone, verifycode, firstPassword, secondPassword);
                    if (isRight) {
                        startProgressDialog();
                        SQuser sQuser = SQuser.getInstance();
                        service.findPwd(HttpHead.getHeader("PUT"), null, phone, verifycode, firstPassword, new CustomCallBack<String>(){

                            @Override
                            public void onSuccess(String s) {
                                stopProgressDialog();
                                Toast.makeText(FindPwdActivity.this, "密码修改成功!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure() {
                                stopProgressDialog();
                            }
                        });
                    }
                    break;
            }
        }
    };

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("[1][34578]\\d{9}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private Boolean checkNumber(String phone, String code, String firstPassword, String secondPassword) {
        //访问网络判断验证码
        if (TextUtils.isEmpty(phone)) {
            TipDialog.hintDiaglog(FindPwdActivity.this, "手机号码不能为空");
            return false;
        }
        if (!isMobileNO(phone)) {
            TipDialog.hintDiaglog(FindPwdActivity.this, "请输入正确的手机号");
            return false;
        }
        if (TextUtils.isEmpty(code)) {
            TipDialog.hintDiaglog(FindPwdActivity.this, "手机验证码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(firstPassword)) {
            TipDialog.hintDiaglog(FindPwdActivity.this, "请输入密码");
            return false;
        }
        if (firstPassword.length() < 6) {
            TipDialog.hintDiaglog(FindPwdActivity.this, "请输入6-16位密码");
            return false;
        }
        if (!firstPassword.equals(secondPassword)) {
            TipDialog.hintDiaglog(FindPwdActivity.this, "2次输入的密码不一致");
            return false;
        }
        return true;
    }

    private void takeoutData() {
        phone = etForgetpwdPhone.getText().toString().trim();
        firstPassword = etForgetpwdNewpwd.getText().toString().trim();
        verifycode = etForgetpwdVerifycode.getText().toString().trim();
        secondPassword = etForgetpwdVerifypwd.getText().toString().trim();
    }

    /**
     * 实现60秒倒计时的操作
	 */
    class MycountTimer extends CountDownTimer {

        public MycountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btnForgetpwdVerifycode.setText("重新获取");
            btnForgetpwdVerifycode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnForgetpwdVerifycode.setClickable(false);
            btnForgetpwdVerifycode.setText(millisUntilFinished / 1000 + "S");
        }
    }
}
