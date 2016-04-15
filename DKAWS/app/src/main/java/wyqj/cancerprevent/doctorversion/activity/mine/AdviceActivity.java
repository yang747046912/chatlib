package wyqj.cancerprevent.doctorversion.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class AdviceActivity extends LoadBaseActivity {
    private EditText etAdviceEdit;
    private EditText etAdviceEmail;
    private EditText etAdvicePhone;
    private EditText etAdviceQq;
    private Button btnTitlebarSubmit;

    private String advice;
    private String qq;
    private String phone;
    private String email;
    private String connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        showContentView();
        setTitle("意见反馈");
        initView();
        setUpView();
    }

    private void setUpView() {
        btnTitlebarSubmit.setText("提交");
        btnTitlebarSubmit.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //TODO:请求网络将数据传给服务器
                takeoutData();
                if ("".equals(advice)) {
                    TipDialog.hintDiaglog(AdviceActivity.this, "没有输入反馈内容哦~");
                    return;
                }
                if ("".equals(qq) && "".equals(phone) && "".equals(email)) {
                    TipDialog.hintDiaglog(AdviceActivity.this, "请至少填写一个联系方式");
                    return;
                }
                startProgressDialog();
                HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
                SQuser sQuser = SQuser.getInstance();
                service.suggestion(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, advice, connect, new CustomCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        stopProgressDialog();
                        etAdviceEdit.setText(null);
                        etAdviceQq.setText(null);
                        etAdvicePhone.setText(null);
                        etAdviceEmail.setText(null);
                        ToastUtils.showToast(AdviceActivity.this, "反馈成功，抗癌卫士感谢您的支持", 2000, 1);
                    }

                    @Override
                    public void onFailure() {
                        stopProgressDialog();
                    }
                });
            }
        });
    }

    private void initView() {
        etAdviceEdit = (EditText) findViewById(R.id.et_advice_edit);
        etAdviceEmail = (EditText) findViewById(R.id.et_advice_email);
        etAdvicePhone = (EditText) findViewById(R.id.et_advice_phone);
        etAdviceQq = (EditText) findViewById(R.id.et_advice_qq);
        btnTitlebarSubmit = (Button) findViewById(R.id.btn_titlebar_submit);
        btnTitlebarSubmit.setVisibility(View.VISIBLE);//提交按钮
    }

    private void takeoutData() {
        advice = etAdviceEdit.getText().toString().trim();
        qq = etAdviceQq.getText().toString().trim();
        phone = etAdvicePhone.getText().toString().trim();
        email = etAdviceEmail.getText().toString().trim();
        connect = qq + "," + phone + "," + email;
    }
}
