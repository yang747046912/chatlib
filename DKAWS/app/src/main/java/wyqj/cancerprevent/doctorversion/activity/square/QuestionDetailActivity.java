package wyqj.cancerprevent.doctorversion.activity.square;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.xrecyclerview.XRecyclerView;

import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.square.QuestionDetailAdapter;
import wyqj.cancerprevent.doctorversion.bean.QuestionDetailBean;
import wyqj.cancerprevent.doctorversion.headview.QuestionDetailHead;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class QuestionDetailActivity extends LoadBaseActivity {
    private XRecyclerView xRecyclerView;
    private QuestionDetailAdapter adapter;
    private Integer questionId;
    private QuestionDetailHead head;
    private int doctorStatus;
    private LinearLayout ll_reply;
    private TextView tvTitlebarRighttitle;
    /**
     * 病历本
     */
    private String patientUserkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        setTitle("问题详情");
        initView();
        if (getIntent() != null) {
            questionId = getIntent().getIntExtra(Constants.QUESTION_ID, 0);
            if (0 == questionId) {
                questionId = null;
            }
        }
        setUpView();
    }

    private void initView() {
        ll_reply = getView(R.id.ll_reply);
        xRecyclerView = getView(R.id.xrecyclerview);
        tvTitlebarRighttitle = (TextView) findViewById(R.id.tv_titlebar_righttitle);
        tvTitlebarRighttitle.setText("病历本");
        tvTitlebarRighttitle.setVisibility(View.GONE);
    }

    private void setUpView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getQuestionDetail();
            }

            @Override
            public void onLoadMore() {
            }
        });
        xRecyclerView.setLayoutManager(layoutManager);
        adapter = new QuestionDetailAdapter();
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingMoreEnabled(false);
        head = new QuestionDetailHead(this);
        xRecyclerView.addHeaderView(head.getHeadView(R.layout.headerview_questiondetail));
        getQuestionDetail();
        tvTitlebarRighttitle.setOnClickListener(listener);
        ll_reply.setOnClickListener(listener);

        /*ll_reply.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (doctorStatus != 4) {//判断认证医生
                    TipDialog tipDialog = new TipDialog(QuestionDetailActivity.this);
                    tipDialog.setMessage("非认证医生不能回答问题");
                    tipDialog.setPositive("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    tipDialog.show();
                    return;
                }

                Intent intent = new Intent(QuestionDetailActivity.this, ReplyPatientActivity.class);
                intent.putExtra(Constants.QUESTION_ID, questionId);
                startActivityForResult(intent, Constants.CODE_REPLY_PATIENT);
                overridePendingTransition(R.anim.screen_down_in, R.anim.screen_up_out);
            }
        });*/
    }

    private void getQuestionDetail() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.questionDetail(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, questionId, sQuser.getUserInfo().doctorid, new CustomCallBack<QuestionDetailBean>() {
            @Override
            public void onSuccess(QuestionDetailBean questionDetailBean) {
                showContentView();
                head.setUpView(questionDetailBean);
                adapter.clear();
                adapter.addAll(questionDetailBean.comments);
                adapter.notifyDataSetChanged();
                xRecyclerView.refreshComplete();
                doctorStatus = questionDetailBean.doctorStatus;
                if (questionDetailBean.question != null && questionDetailBean.question.questioner != null) {
                    patientUserkey = questionDetailBean.question.questioner.userkey;
                }
                boolean isAnswerable = questionDetailBean.isAnswerable;
                tvTitlebarRighttitle.setVisibility(View.VISIBLE);
                if (isAnswerable) {
                    ll_reply.setVisibility(View.VISIBLE);
                } else {
                    ll_reply.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure() {
                xRecyclerView.refreshComplete();
                tvTitlebarRighttitle.setVisibility(View.GONE);
                if (adapter.getItemCount() == 0) {
                    showRefrsh();
                }
            }
        });
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        getQuestionDetail();
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            Intent intent;
            int id = v.getId();
            switch (id) {
                case R.id.ll_reply:
                    if (doctorStatus != 4) {//判断认证医生
                        TipDialog tipDialog = new TipDialog(QuestionDetailActivity.this);
                        tipDialog.setMessage("非认证医生不能回答问题");
                        tipDialog.setPositive("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        tipDialog.show();
                        return;
                    }

                    intent = new Intent(QuestionDetailActivity.this, ReplyPatientActivity.class);
                    intent.putExtra(Constants.QUESTION_ID, questionId);
                    startActivityForResult(intent, Constants.CODE_REPLY_PATIENT);
                    overridePendingTransition(R.anim.screen_down_in, R.anim.screen_up_out);
                    break;
                case R.id.tv_titlebar_righttitle:
                    //跳转到病历本
                    intent = new Intent(QuestionDetailActivity.this, NewIllNoteActivity.class);
                    intent.putExtra(Constants.PATIENT_USERKEY, patientUserkey);
                    startActivity(intent);
                    break;
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.CODE_REPLY_PATIENT) {
            String changed_amount = data.getStringExtra(Constants.CHANGED_AMOUNT);
            String currentMoney = data.getStringExtra(Constants.CURRENT_MONEY);
            if (!TextUtils.isEmpty(changed_amount) && !TextUtils.isEmpty(currentMoney)) {
                String tip = "回答问题成功，获得了" + changed_amount + "元," + "现有" + currentMoney + "元";
                ToastUtils.showToast(this, tip, 2000, 1);
            }
            getQuestionDetail();
        }
    }
}
