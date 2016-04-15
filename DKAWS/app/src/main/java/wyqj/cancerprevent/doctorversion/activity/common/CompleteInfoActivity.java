package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.cache.ACache;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.ActivityStackUtil;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ChooseBean;
import wyqj.cancerprevent.doctorversion.bean.RegisterBean;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class CompleteInfoActivity extends LoadBaseActivity {

    private Button btnCompleteinfoKindofdisease;
    private EditText etCompleteinfoName;
    private EditText etCompleteinfoOffice;
    private EditText etCompleteinfoInvitecode;
    private EditText etCompleteinfoWhichhospital;
    private Button btnCompleteinfoComplete;
    private TextView btn_person_intro_kindofdisease;
    private View viewCompleteinfoOfficeline;
    private ChooseBean hospital;
    private ArrayList<UserBean.Diseased> disease = new ArrayList<>();
    private String perIntro = "";
    private RegisterBean registerBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
        showContentView();
        setTitle("完善基本信息");
        ActivityStackUtil.getInstance().add(this);
        initView();
        setUpView();
    }

    private void setUpView() {
        btnCompleteinfoComplete.setOnClickListener(listener);
        etCompleteinfoWhichhospital.setOnClickListener(listener);
        btnCompleteinfoKindofdisease.setOnClickListener(listener);
        btn_person_intro_kindofdisease.setOnClickListener(listener);
        ACache aCache = ACache.get(this);
        registerBean = (RegisterBean) aCache.getAsObject(Constants.REGISTER);
        if (registerBean != null) {
            if (registerBean.degree == 5) {
                etCompleteinfoOffice.setVisibility(View.GONE);
                viewCompleteinfoOfficeline.setVisibility(View.GONE);
            }
        }
    }

    private void initView() {
        btnCompleteinfoKindofdisease = getView(R.id.btn_completeinfo_kindofdisease);
        etCompleteinfoName = getView(R.id.et_completeinfo_name);
        etCompleteinfoOffice = getView(R.id.et_completeinfo_office);
        etCompleteinfoInvitecode = getView(R.id.et_completeinfo_invitecode);
        etCompleteinfoWhichhospital = getView(R.id.et_completeinfo_whichhospital);
        btnCompleteinfoComplete = getView(R.id.btn_completeinfo_complete);
        viewCompleteinfoOfficeline = getView(R.id.view_completeinfo_officeline);
        btn_person_intro_kindofdisease = getView(R.id.btn_person_intro_kindofdisease);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            Intent intent;
            switch (id) {
                case R.id.btn_completeinfo_complete://完成按钮
                    doRealRegister();
                    break;
                case R.id.et_completeinfo_whichhospital:
                    intent = new Intent(CompleteInfoActivity.this, ChooseHospitalActivity.class);
                    startActivityForResult(intent, Constants.CODE_CHOOSE_HOSPITAL);
                    break;
                case R.id.btn_completeinfo_kindofdisease:
                    //跳转到选择擅长病种页面
                    intent = new Intent(CompleteInfoActivity.this, KindOfDiseaseActivity.class);
                    intent.putExtra(Constants.CHOOSE_DISEASE, disease);
                    startActivityForResult(intent, Constants.CODE_CHOOSE_DISEASE);
                    break;
                case R.id.btn_person_intro_kindofdisease:
                    intent = new Intent(CompleteInfoActivity.this, PerIntroActivity.class);
                    intent.putExtra(Constants.PERSONAL_PROFILE, perIntro);
                    startActivityForResult(intent, Constants.CODE_PERSONAL_PROFILE);
                    break;
                default:
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
        if (requestCode == Constants.CODE_CHOOSE_HOSPITAL) {
            ChooseBean bean = (ChooseBean) data.getSerializableExtra(Constants.CHOOSE_HOSPITAL);
            etCompleteinfoWhichhospital.setText(bean.getName());
            hospital = bean;
            registerBean.hospitalId = bean.getId();
            registerBean.hospitalName = bean.getName();
        } else if (requestCode == Constants.CODE_CHOOSE_DISEASE) {
            disease = (ArrayList<UserBean.Diseased>) data.getSerializableExtra(Constants.CHOOSE_DISEASE);
            String tmp = "";
            int[] disease_ids = new int[disease.size()];
            for (int i = 0; i < disease.size(); i++) {
                tmp += disease.get(i).name + ",";
                disease_ids[i] = disease.get(i).id;
            }
            tmp = tmp.substring(0, tmp.length() - 1);
            btnCompleteinfoKindofdisease.setText(tmp);
            registerBean.disease_ids = disease_ids;
        } else if (requestCode == Constants.CODE_PERSONAL_PROFILE) {
            perIntro = data.getStringExtra(Constants.PERSONAL_PROFILE);
            if (!"".equals(perIntro)) {
                btn_person_intro_kindofdisease.setText(perIntro);
                btn_person_intro_kindofdisease.setTextColor(getResources().getColor(R.color.black));
                registerBean.person_intro = perIntro;
            } else {
                btn_person_intro_kindofdisease.setText("请输入个人简介");
                btn_person_intro_kindofdisease.setTextColor(getResources().getColor(R.color.text_hint));
                registerBean.person_intro = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackUtil.getInstance().remove(this);
    }

    private Boolean checkInfo() {
//        TODO：注意这个地方如果是博士的话是没有科室的
        String name = etCompleteinfoName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            TipDialog.hintDiaglog(CompleteInfoActivity.this, "请输入您的真实姓名");
            return false;
        }
        registerBean.name = name;
        if (TextUtils.isEmpty(registerBean.hospitalName)) {
            TipDialog.hintDiaglog(CompleteInfoActivity.this, "请选择您所在的医院");
            return false;
        }
        String office = etCompleteinfoOffice.getText().toString().trim();
        if (registerBean.degree != 5 && TextUtils.isEmpty(office)) {
            TipDialog.hintDiaglog(CompleteInfoActivity.this, "请输入您所在的科室");
            return false;
        }
        registerBean.department = office;
        if (registerBean.disease_ids.length == 0) {
            TipDialog.hintDiaglog(CompleteInfoActivity.this, "请选择您的擅长病种");
            return false;
        }
        if (TextUtils.isEmpty(registerBean.person_intro)) {
            TipDialog.hintDiaglog(CompleteInfoActivity.this, "请输入个人简介");
            return false;
        }

        //如果邀请码为空则 赋值为null
        String invitecode = etCompleteinfoInvitecode.getText().toString().trim();
        if (TextUtils.isEmpty(invitecode)) {
            registerBean.invitationCode = null;
        } else {
            registerBean.invitationCode = invitecode;
        }
        //如果科室为空  同时需满足是否为博士研究生  则赋值为null
        if (TextUtils.isEmpty(registerBean.department) && registerBean.degree == 5) {
            registerBean.department = null;
        }
        return true;
    }

    private void doRealRegister() {
        if (!checkInfo()) {
            return;
        }
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.registerUser(HttpHead.getHeader("POST"), null, registerBean.mobile, registerBean.password, registerBean.name, registerBean.degree == -1 ? null : registerBean.degree, registerBean.department, registerBean.hospitalId == -1 ? null : registerBean.hospitalId, registerBean.disease_ids, registerBean.hospitalId != -1 ? null : registerBean.hospitalName, registerBean.invitationCode, registerBean.verifyCode, registerBean.person_intro, new CustomCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean userBean) {
                SQuser.getInstance().saveUserInfo(userBean);
                Intent intent = new Intent(CompleteInfoActivity.this, RegisterSuccessActivity.class);
                startActivity(intent);
                ActivityStackUtil.getInstance().destoryAll();
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
