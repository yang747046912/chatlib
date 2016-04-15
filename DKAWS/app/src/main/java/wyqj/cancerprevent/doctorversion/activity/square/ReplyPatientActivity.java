package wyqj.cancerprevent.doctorversion.activity.square;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.iflytek.SpeechTool;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.CommentBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class ReplyPatientActivity extends LoadBaseActivity {

    private TextView confirm;
    private LinearLayout ll_radio;
    private EditText et_reply;
    private SpeechTool speechTool;
    private int questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_patient);
        setTitle("回复");
        getIntentData();
        showContentView();
        speechTool = new SpeechTool(this, new SpeechTool.IprintResult() {
            @Override
            public void print(String msg) {
                et_reply.append(msg);
                et_reply.setSelection(et_reply.length());
            }
        });
        initView();
        setUpView();
    }

    private void getIntentData() {
        questionId = getIntent().getIntExtra(Constants.QUESTION_ID, -1);
        if (questionId == -1) {
            throw new RuntimeException("this activity must have a qustion_id");
        }
    }

    private void setUpView() {
        confirm.setVisibility(View.VISIBLE);
        ll_radio.setOnClickListener(listener);
        confirm.setOnClickListener(listener);
    }

    private void initView() {
        et_reply = getView(R.id.et_reply);
        ll_radio = getView(R.id.ll_radio);
        confirm = getView(R.id.tv_confirm);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_radio:
                    speechTool.show();
                    break;
                case R.id.tv_confirm:
                    String content = et_reply.getText().toString().trim();
                    if (TextUtils.isEmpty(content)) {
                        TipDialog.hintDiaglog(ReplyPatientActivity.this, "请先输入内容");
                        return;
                    }
                    sendComment(content);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechTool.onDestroy();
    }


    private void sendComment(String content) {
        startProgressDialog();
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.sendComment(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, questionId, sQuser.getUserInfo().doctorid, content, new CustomCallBack<CommentBean>() {

            @Override
            public void onSuccess(CommentBean commentBean) {
                stopProgressDialog();
                CommentBean.ContributionLog contributionLog = commentBean.contribution_log;
                Intent intent = new Intent();
                if (contributionLog != null) {
                    String changed_amount = contributionLog.changed_amount;
                    String currentMoney = contributionLog.current_amount;
                    intent.putExtra(Constants.CHANGED_AMOUNT, changed_amount);
                    intent.putExtra(Constants.CURRENT_MONEY, currentMoney);
                }
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.screen_up_in, R.anim.screen_down_out);
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }
}
