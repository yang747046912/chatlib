package wyqj.cancerprevent.doctorversion.activity.patient;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.fresco.Image;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ApplyDetailBean;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.bean.PatientBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class ApplyDetailActivity extends LoadBaseActivity {

    private SimpleDraweeView ivApplyAvatar;
    private TextView tvApplyName;
    private TextView tvApplyAge;
    private TextView tvApplyArea;
    private TextView tvApplySex;
    private TextView tvApplyTime;
    private TextView tvApplyDiseasekind;
    private Button btnApplyAgree;
    private Button btnApplyCancel;
    private TextView tvTitlebarRighttitle;
    private LinearLayout llApplyRelationship;
    private TextView tvApplyRelationship;
    private View viewApplyRelationCutline;
    private String applyId;
    private String patientUserkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);
        setTitle("申请详情");
        applyId = getIntent().getStringExtra(Constants.APPLY_ID);
        if (TextUtils.isEmpty(applyId)) {
            throw new RuntimeException("applyID is null");
        }
        initView();
        setUpView();
        getApplyDetail();
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_apply_agree:
                    agreeApply();
                    break;
                case R.id.btn_apply_cancel:
                    cancelApply();
                    break;
                case R.id.tv_titlebar_righttitle:
//                    Intent intent = new Intent(ApplyDetailActivity.this, NewIllNoteActivity.class);
//                    intent.putExtra(Constants.PATIENT_USERKEY, patientUserkey);
//                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    };

    private void setUpView() {
        tvTitlebarRighttitle.setText("病历本");
        btnApplyAgree.setOnClickListener(listener);
        btnApplyCancel.setOnClickListener(listener);
        tvTitlebarRighttitle.setOnClickListener(listener);
    }

    private void initView() {
        ivApplyAvatar = (SimpleDraweeView) findViewById(R.id.iv_apply_avatar);
        tvApplyName = (TextView) findViewById(R.id.tv_apply_name);
        tvApplyAge = (TextView) findViewById(R.id.tv_apply_age);
        tvApplyArea = (TextView) findViewById(R.id.tv_apply_area);
        tvApplySex = (TextView) findViewById(R.id.tv_apply_sex);
        tvApplyTime = (TextView) findViewById(R.id.tv_apply_time);
        tvApplyDiseasekind = (TextView) findViewById(R.id.tv_apply_diseasekind);
        btnApplyAgree = (Button) findViewById(R.id.btn_apply_agree);
        btnApplyCancel = (Button) findViewById(R.id.btn_apply_cancel);
        tvTitlebarRighttitle = (TextView) findViewById(R.id.tv_titlebar_righttitle);
        llApplyRelationship = (LinearLayout) findViewById(R.id.ll_apply_relationship);
        tvApplyRelationship = (TextView) findViewById(R.id.tv_apply_relationship);
        viewApplyRelationCutline = findViewById(R.id.view_apply_relation_cutline);
    }

    private void getApplyDetail() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.applyDetail(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, applyId, new CustomCallBack<ApplyDetailBean>() {
            @Override
            public void onSuccess(ApplyDetailBean applyDetailBean) {
                showContentView();
                ProcessData(applyDetailBean);
            }

            @Override
            public void onFailure() {

            }
        });
    }


    private void ProcessData(ApplyDetailBean applyDetailEntity) {
        PatientBean patient = applyDetailEntity.patient;
        if (patient != null) {
            Image.displayRound(ivApplyAvatar, 10, Uri.parse(patient.avatarRrl));
            tvApplyName.setText(patient.username);
            patientUserkey = patient.userkey;
            tvApplyAge.setText(String.valueOf(patient.age));
            tvApplyArea.setText(patient.area);
            tvApplySex.setText(patient.sex);
            tvApplyTime.setText(patient.sickDate);
            boolean isCompletedRecord = patient.isCompletedRecord;
            if (isCompletedRecord) {
                tvTitlebarRighttitle.setVisibility(View.VISIBLE);
            } else {
                tvTitlebarRighttitle.setVisibility(View.GONE);
            }
            /**
             * 患病种类只显示第一个
             */
            if (patient.diseases != null && patient.diseases.size() > 0) {
                tvApplyDiseasekind.setText(patient.diseases.get(0).name);
            } else {
                tvApplyDiseasekind.setText("");
            }

            if ("2".equals(patient.type)) {//家属
                llApplyRelationship.setVisibility(View.VISIBLE);
                viewApplyRelationCutline.setVisibility(View.VISIBLE);
                tvApplyRelationship.setText(patient.relationship);
            } else {
                viewApplyRelationCutline.setVisibility(View.GONE);
                llApplyRelationship.setVisibility(View.GONE);
            }
        }
    }


    private void agreeApply() {
        startProgressDialog();
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.agreeApply(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, applyId, sQuser.getUserInfo().doctorid, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {
                stopProgressDialog();
                SQuser.getInstance().needonRefresh = true;
                finish();
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }

    private void cancelApply() {
        startProgressDialog();
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.cancelApply(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, applyId, sQuser.getUserInfo().doctorid, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {
                stopProgressDialog();
                SQuser.getInstance().needonRefresh = true;
                finish();

            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }
}
