package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.cache.ACache;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.ActivityStackUtil;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.bean.RegisterBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;

public class Register extends LoadBaseActivity {

    private ImageView login_back;
    private Button register;
    private EditText etRegitsterPhone;
    private EditText etRegisterVerifycode;
    private EditText etRegisterPassword;
    private TextView btnRegisterObtaincode;
    private CheckBox showPwd;
    private TextView rgeitsterVoice;
    private TextView regiseterdeclare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        showContentView();
        setTitle("注册");
        initView();
        setUpView();
        ActivityStackUtil.getInstance().add(this);
        ACache aCache = ACache.get(Register.this);
        aCache.remove(Constants.REGISTER);
    }

    private void setUpView() {
        login_back.setImageResource(R.drawable.login_fanhui22x);
        login_back.setPadding(15, 0, 0, 0);
        register.setOnClickListener(listener);
        btnRegisterObtaincode.setOnClickListener(listener);
        showPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int end = etRegisterPassword.getSelectionEnd();
                int start = etRegisterPassword.getSelectionStart();
                if (isChecked) {
                    //如果选中，显示密码
                    etRegisterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    etRegisterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                Selection.setSelection(etRegisterPassword.getText(), start, end);
            }
        });
        setClickSpan(regiseterdeclare, 24, 36, ClickSpan.TYPE_DECLARE);
        setClickSpan(rgeitsterVoice, 10, 15, ClickSpan.TYPE_VOICE);
    }


    private void initView() {
        login_back = getView(R.id.btn_titlebar_back);
        register = getView(R.id.btn_register_registe);
        etRegitsterPhone = getView(R.id.et_regitster_phone);
        btnRegisterObtaincode = getView(R.id.btn_register_obtaincode);
        etRegisterVerifycode = getView(R.id.et_register_verifycode);
        etRegisterPassword = getView(R.id.et_register_password);
        showPwd = getView(R.id.cb_register_showpwd);
        rgeitsterVoice = getView(R.id.tv_register_voice);
        regiseterdeclare = getView(R.id.tv_regiseter_fuwudeclare);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_register_registe:
                    doRegister();
                    break;
                case R.id.btn_register_obtaincode:
                    doGetVerifyCode();
                    break;
                default:
                    break;
            }
        }
    };

    private void doRegister() {
        String phone = etRegitsterPhone.getText().toString().trim();
        String verifycode = etRegisterVerifycode.getText().toString().trim();
        String password = etRegisterPassword.getText().toString().trim();

        ACache aCache = ACache.get(Register.this);
        RegisterBean bean = new RegisterBean();
        bean.mobile = phone;
        bean.verifyCode = verifycode;
        bean.password = password;
        aCache.put(Constants.REGISTER, bean);
     //   openActivity(ChooseGradeActivity.class);测试用
        if (checkNumber(phone, verifycode, password)) {
            register(phone, verifycode, password);
        }
    }

    private void register(final String phone, final String code, final String password) {
        HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
        service.verifyCode(HttpHead.getHeader("GET"), null, phone, code, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {
                ACache aCache = ACache.get(Register.this);
                RegisterBean bean = new RegisterBean();
                bean.mobile = phone;
                bean.verifyCode = code;
                bean.password = password;
                aCache.put(Constants.REGISTER, bean);
                openActivity(ChooseGradeActivity.class);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private String checkPhoneNumber(String phone) {
        String info = null;
        if (TextUtils.isEmpty(phone)) {
            info = "手机号码不能为空";
        } else if (phone.length() < 11) {
            info = "请正确输入手机号码";
        }
        return info;
    }

    private boolean checkNumber(String phone, String code, String password) {
        String info = null;
        String phoneInfo = checkPhoneNumber(phone);
        if (!TextUtils.isEmpty(phoneInfo)) {
            info = phoneInfo;
        } else if (TextUtils.isEmpty(code)) {
            info = "手机验证码不能为空";
        } else if (TextUtils.isEmpty(password)) {
            info = "手机密码不能为空";
        } else if (password.length() < 6 || password.length() > 16) {
            info = "请输入6-16位密码";
        }
        if (TextUtils.isEmpty(info)) {
            return true;
        }
        final TipDialog tipDialog = new TipDialog(this);
        tipDialog.setPositive("好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        tipDialog.setMessage(info);
        tipDialog.show();
        return false;
    }


    private void doGetVerifyCode() {
        String phone = etRegitsterPhone.getText().toString().trim();
        String phoneInfo = checkPhoneNumber(phone);
        if (!TextUtils.isEmpty(phoneInfo)) {
            final TipDialog tipDialog = new TipDialog(this);
            tipDialog.setPositive("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            tipDialog.setMessage(phoneInfo);
            tipDialog.show();
            return;
        }
        getVerifyCode(phone);
    }

    private void getVerifyCode(String phone) {
        HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
        service.obtainCode(HttpHead.getHeader("POST"), null, phone, 2, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {

                final TipDialog tipDialog = new TipDialog(Register.this);
                tipDialog.setPositive("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                tipDialog.setMessage("发送验证码成功，请等待");
                tipDialog.show();
                new MycountTimer(60000, 1000).start();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /*
     * 实现60秒倒计时的操作
	 */
    class MycountTimer extends CountDownTimer {

        public MycountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btnRegisterObtaincode.setText("重新获取");
            btnRegisterObtaincode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnRegisterObtaincode.setClickable(false);
            btnRegisterObtaincode.setText(millisUntilFinished / 1000 + "S");
        }
    }

    private class ClickSpan extends ClickableSpan {
        public static final int TYPE_VOICE = 0;
        public static final int TYPE_DECLARE = 1;
        int type = -1;

        public ClickSpan(int type) {
            this.type = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.edt_modifing));
        }

        @Override
        public void onClick(View widget) {//点击事件
            if (type == 0) {
                dosendVoiceVerifyCode();
            } else if (type == 1) {
                openActivity(AgreementActivity.class);
            }
        }
    }

    private void setClickSpan(TextView tv, int start, int end, int type) {
        String strTmp = tv.getText().toString();
        SpannableString spannableString = new SpannableString(strTmp);
        spannableString.setSpan(new ClickSpan(type), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void dosendVoiceVerifyCode() {
        String phone = etRegitsterPhone.getText().toString().trim();
        String phoneInfo = checkPhoneNumber(phone);
        if (!TextUtils.isEmpty(phoneInfo)) {
            final TipDialog tipDialog = new TipDialog(this);
            tipDialog.setPositive("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            tipDialog.setMessage(phoneInfo);
            tipDialog.show();
            return;
        }
        sendVoiceVerifyCode(phone);
    }

    private void sendVoiceVerifyCode(String phone) {
        HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
        service.sendGeneralRegisterVerifyCode(HttpHead.getHeader("POST"), phone, "voice", 2, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {

                final TipDialog tipDialog = new TipDialog(Register.this);
                tipDialog.setPositive("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                tipDialog.setMessage("发送验证码成功，请等待");
                tipDialog.show();
                new MycountTimer(60000, 1000).start();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackUtil.getInstance().remove(this);
    }
}
