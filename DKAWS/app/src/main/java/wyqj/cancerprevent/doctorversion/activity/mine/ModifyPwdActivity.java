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

public class ModifyPwdActivity extends LoadBaseActivity {
    private EditText etModifypwdFirst;
    private EditText etModifypwdSecond;
    private Button btnModifyConfirm;
    private int doctorId;
    private EditText etModifypwdSecondtwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        showContentView();
        setTitle("修改密码");
        initView();
        setUpView();
    }

    private void setUpView() {
        btnModifyConfirm.setOnClickListener(listener);
    }

    private void initView() {
        etModifypwdFirst = (EditText) findViewById(R.id.et_modifypwd_first);
        etModifypwdSecond = (EditText) findViewById(R.id.et_modifypwd_second);
        btnModifyConfirm = (Button) findViewById(R.id.btn_modify_confirm);
        etModifypwdSecondtwo = (EditText) findViewById(R.id.et_modifypwd_secondtwo);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.btn_modify_confirm:
                    String first = etModifypwdFirst.getText().toString().trim();
                    String second = etModifypwdSecond.getText().toString().trim();
                    String secondtwo = etModifypwdSecondtwo.getText().toString().trim();
                    if (!second.equals(secondtwo)) {
                        TipDialog.hintDiaglog(ModifyPwdActivity.this, "两次密码输入不相同");
                        return;
                    }
                    if (second.length() < 6) {
                        TipDialog.hintDiaglog(ModifyPwdActivity.this, "请输入6-16位密码");
                        return;
                    }
                    requestModifyPwd(first, second);
                    break;
            }
        }
    };

    private void requestModifyPwd(String first, String second) {
        startProgressDialog();
        HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.ModifyPwd(HttpHead.getHeader("PUT"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, first, second, new CustomCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                stopProgressDialog();
                ToastUtils.showToast(ModifyPwdActivity.this, "修改密码成功", 2000, 1);
                finish();
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }
}
