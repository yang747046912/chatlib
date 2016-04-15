package wyqj.cancerprevent.doctorversion.activity.mobileconsult;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;
import wyqj.cancerprevent.doctorversion.widget.PopUpPicker;



public class RefuseDiagnoseActivity extends LoadBaseActivity {

    private RelativeLayout rlRefuseCause;
    private LinearLayout llRefuseDetailExplain;
    private Button Submit;
    private EditText etRefuseDetailCause;
    private TextView tvRefuseCause;

    /** 详细说明信息*/
    private String refuseDetailCause;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuse_diagnose);
        showContentView();
        setTitle("拒诊");
        initView();
        setUpView();
    }

    private void initView() {
        rlRefuseCause = getView(R.id.rl_refuse_diagnose_cause);
        Submit = getView(R.id.btn_refuse_diagnose_submit);
        llRefuseDetailExplain = getView(R.id.ll_refuse_detail_explain);
        etRefuseDetailCause = getView(R.id.et_refuse_detail_cause);
        tvRefuseCause = getView(R.id.tv_refuse_cause);
    }

    private void setUpView() {
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra(Constants.ORDER_ID);
        }
        rlRefuseCause.setOnClickListener(listener);
        Submit.setOnClickListener(listener);
        etRefuseDetailCause.addTextChangedListener(watcher);

    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.rl_refuse_diagnose_cause:
                    showRefuseCase();
                    break;
                case R.id.btn_refuse_diagnose_submit:
                    if (checkRefuseCause()) {
                        startProgressDialog();
                        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
                        SQuser sQuser = SQuser.getInstance();

                        String refuseItem = tvRefuseCause.getText().toString().trim();
                        /** 若没选择其他因素，就将此值置空*/
                        if (llRefuseDetailExplain.getVisibility() == View.GONE) {
                            refuseDetailCause = null;
                        }
                        service.postRefuseDiagnose(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, orderId, sQuser.getUserInfo().doctorid, refuseItem, refuseDetailCause, new CustomCallBack<ErrorBean>() {
                            @Override
                            public void onSuccess(ErrorBean errorBean) {
                                stopProgressDialog();
                                TipDialog.hintDiaglog(RefuseDiagnoseActivity.this, "提交成功");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                },1500);

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

        /**
         * 显示拒诊原因列表
         */
        private void showRefuseCase() {
            PopUpPicker popUpPicker = new PopUpPicker(RefuseDiagnoseActivity.this);
            popUpPicker.setListener(new PopUpPicker.OnOKListener() {
                @Override
                public void onOK(String relust) {
                    tvRefuseCause.setText(relust);
                    if ("其他因素,无法提供咨询".equals(relust)) {
                        llRefuseDetailExplain.setVisibility(View.VISIBLE);
                    } else {
                        llRefuseDetailExplain.setVisibility(View.GONE);
                    }
                }
            });
            popUpPicker.showAtLocation(rlRefuseCause, Gravity.BOTTOM, 0, 0);
        }

        /** 检查输入内容是否符合要求*/
        private boolean checkRefuseCause() {
            if (TextUtils.isEmpty(tvRefuseCause.getText().toString().trim())) {
                TipDialog.hintDiaglog(RefuseDiagnoseActivity.this, "请输入拒诊原因");
                return false;
            }
            if (llRefuseDetailExplain.getVisibility() == View.VISIBLE) {
                String refuseDetailCause = etRefuseDetailCause.getText().toString().trim();
                if (refuseDetailCause.length() < 10) {
                    TipDialog.hintDiaglog(RefuseDiagnoseActivity.this, "您输入的详细说明过短，请重新输入");
                    return false;
                }
                if (refuseDetailCause.length() > 200) {
                    TipDialog.hintDiaglog(RefuseDiagnoseActivity.this, "您输入的详细说明过长，请重新输入");
                    return false;
                }
            }
            return true;
        }
    };

    /**
     * 保存详细说明文字
     */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            refuseDetailCause = etRefuseDetailCause.getText().toString().trim();
        }
    };
}
