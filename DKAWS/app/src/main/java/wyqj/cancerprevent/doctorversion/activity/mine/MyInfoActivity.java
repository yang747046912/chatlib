package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.common.ChooseHospitalActivity;
import wyqj.cancerprevent.doctorversion.activity.common.KindOfDiseaseActivity;
import wyqj.cancerprevent.doctorversion.activity.common.PerIntroActivity;
import wyqj.cancerprevent.doctorversion.bean.ChooseBean;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class MyInfoActivity extends LoadBaseActivity {


    private ImageView ivMyinfoRightarrow1;
    private ImageView ivMyinfoRightarrow2;
    private ImageView ivMyinfoRightarrow3;
    private ImageView ivMyinfoRightarrow4;
    private ImageView ivMyinfoRightarrow5;
    private ImageView ivMyinfoRightarrow6;
    private ImageView ivMyinfoRightarrow7;

    private TextView tvMyinfoUsername;
    private TextView tvMyinfoGooddisease;
    private TextView tvMyinfoGrade;
    private TextView tvMyinfoGradeChange;
    private TextView tvMyinfoHospital;
    private TextView tvMyinfoOffice;
    private TextView txtZhiCheng;
    private TextView tvMyinfoVerifycode;
    private TextView txtMyinfoIntroduction;
    private View viewMyinfoKeshiLine;
    private LinearLayout llMyinfoTouxiang;
    private LinearLayout llMyinfoXingming;
    private LinearLayout llMyinfoKeshi;
    private LinearLayout llMyinfoBingzhong;
    private LinearLayout llMyinfoYiyuan;
    private RelativeLayout rlMyinfoIntroduction;
    private RelativeLayout rlMyinfoZhichen;

    private SimpleDraweeView ivMyinfoTouxiang;
    private Button btnMyinfoTel;
    private UserBean.Doctor doctor;
    private ArrayList<UserBean.Diseased> disease = new ArrayList<>();
    private ChooseBean hospital;
    private String perIntro = "";
    private String grade = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        showContentView();
        setTitle("我的资料");
        initView();
        setUpView();
    }

    private void setUpView() {
        processData(SQuser.getInstance().getUserInfo().info);
    }

    private void initView() {
        llMyinfoTouxiang = (LinearLayout) findViewById(R.id.ll_myinfo_touxiang);
        llMyinfoXingming = (LinearLayout) findViewById(R.id.ll_myinfo_xingming);
        llMyinfoYiyuan = (LinearLayout) findViewById(R.id.ll_myinfo_yiyuan);
        rlMyinfoZhichen = (RelativeLayout) findViewById(R.id.rl_myinfo_zhichen);
        llMyinfoKeshi = (LinearLayout) findViewById(R.id.ll_myinfo_keshi);
        llMyinfoBingzhong = (LinearLayout) findViewById(R.id.ll_myinfo_bingzhong);
        rlMyinfoIntroduction = (RelativeLayout) findViewById(R.id.rl_myinfo_introduction);
        tvMyinfoUsername = (TextView) findViewById(R.id.tv_myinfo_username);
        viewMyinfoKeshiLine = findViewById(R.id.view_myinfo_keshi_line);
        tvMyinfoGooddisease = (TextView) findViewById(R.id.tv_myinfo_gooddisease);
        txtMyinfoIntroduction = (TextView) findViewById(R.id.txt_myinfo_introduction);
        txtZhiCheng = (TextView) findViewById(R.id.txt_zhicheng);
        tvMyinfoGrade = (TextView) findViewById(R.id.tv_myinfo_grade);
        tvMyinfoGradeChange = (TextView) findViewById(R.id.txt_myinfo_grade_change);
        tvMyinfoHospital = (TextView) findViewById(R.id.tv_myinfo_hospital);
        tvMyinfoOffice = (TextView) findViewById(R.id.tv_myinfo_office);
        tvMyinfoVerifycode = (TextView) findViewById(R.id.tv_myinfo_verifycode);
        ivMyinfoTouxiang = (SimpleDraweeView) findViewById(R.id.iv_myinfo_touxiang);
        btnMyinfoTel = (Button) findViewById(R.id.btn_myinfo_tel);
        ivMyinfoRightarrow1 = (ImageView) findViewById(R.id.iv_myinfo_rightarrow1);
        ivMyinfoRightarrow2 = (ImageView) findViewById(R.id.iv_myinfo_rightarrow2);
        ivMyinfoRightarrow3 = (ImageView) findViewById(R.id.iv_myinfo_rightarrow3);
        ivMyinfoRightarrow4 = (ImageView) findViewById(R.id.iv_myinfo_rightarrow4);
        ivMyinfoRightarrow5 = (ImageView) findViewById(R.id.iv_myinfo_rightarrow5);
        ivMyinfoRightarrow6 = (ImageView) findViewById(R.id.iv_myinfo_rightarrow6);
        ivMyinfoRightarrow7 = (ImageView) findViewById(R.id.iv_myinfo_rightarrow7);
        llMyinfoTouxiang.setVisibility(View.GONE);
    }

    /** 箭头是否显示*/
    private void arrowStatues(int isVisibled) {
        ivMyinfoRightarrow1.setVisibility(isVisibled);
        ivMyinfoRightarrow2.setVisibility(isVisibled);
        ivMyinfoRightarrow3.setVisibility(isVisibled);
        ivMyinfoRightarrow4.setVisibility(isVisibled);
        ivMyinfoRightarrow5.setVisibility(isVisibled);
        ivMyinfoRightarrow6.setVisibility(isVisibled);
        ivMyinfoRightarrow7.setVisibility(isVisibled);
    }

    /** 是否可点击*/
    private void clickStatues(Boolean isClickabled) {
        if (isClickabled) {
            llMyinfoTouxiang.setOnClickListener(listener);
            llMyinfoXingming.setOnClickListener(listener);
            llMyinfoYiyuan.setOnClickListener(listener);
            llMyinfoKeshi.setOnClickListener(listener);
            rlMyinfoZhichen.setOnClickListener(listener);
            llMyinfoBingzhong.setOnClickListener(listener);
            rlMyinfoIntroduction.setOnClickListener(listener);
        } else {
            llMyinfoTouxiang.setClickable(isClickabled);
            llMyinfoXingming.setClickable(isClickabled);
            llMyinfoYiyuan.setClickable(isClickabled);
            llMyinfoKeshi.setClickable(isClickabled);
            rlMyinfoZhichen.setClickable(isClickabled);
            llMyinfoBingzhong.setClickable(isClickabled);
            rlMyinfoIntroduction.setClickable(isClickabled);
        }
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            Intent intent;
            switch (id) {
                case R.id.ll_myinfo_xingming://姓名
                    intent = new Intent(MyInfoActivity.this, MyInfoNameActivity.class);
                    intent.putExtra(Constants.CHANGE_NAME, tvMyinfoUsername.getText().toString());
                    startActivityForResult(intent, Constants.CODE_SELECT_NAME);
                    break;
                case R.id.ll_myinfo_yiyuan://医院
                    intent = new Intent(MyInfoActivity.this, ChooseHospitalActivity.class);
                    startActivityForResult(intent, Constants.CODE_SELECT_HOSPITAL);
                    break;
                case R.id.ll_myinfo_keshi://科室
                    intent = new Intent(MyInfoActivity.this, MyInfoDepartmentActivity.class);
                    intent.putExtra(Constants.CHANGE_DEPARTMENT, tvMyinfoOffice.getText().toString());
                    startActivityForResult(intent, Constants.CODE_SELECT_DEPARTMENT);
                    break;
                case R.id.ll_myinfo_bingzhong://病种
                    intent = new Intent(MyInfoActivity.this, KindOfDiseaseActivity.class);
                    intent.putExtra(Constants.CHOOSE_DISEASE, disease);
                    startActivityForResult(intent, Constants.CODE_SELECT_DISEASE);
                    break;
                case R.id.rl_myinfo_introduction://个人简介
                    intent = new Intent(MyInfoActivity.this, PerIntroActivity.class);
                    intent.putExtra(Constants.PERSONAL_PROFILE, perIntro);
                    startActivityForResult(intent, Constants.CODE_MIME_PERSONAL_PROFILE);
                    break;
                case R.id.rl_myinfo_zhichen://备注
                    intent = new Intent(MyInfoActivity.this, MyInfoGradeActivity.class);
                    intent.putExtra(Constants.CHANGE_GRADE, grade);
                    startActivityForResult(intent, Constants.CODE_SELECT_GRADE);
                    break;
                case R.id.btn_myinfo_tel://申请修改资料
                    applyUpdateInfo();
                    break;
            }
        }
    };


    private void processData(UserBean.Doctor doctor) {
        this.doctor = doctor;
        if (doctor == null) {
            return;
        }
        tvMyinfoUsername.setText(doctor.name);
        tvMyinfoVerifycode.setText(doctor.invitationCode);
        tvMyinfoHospital.setText(doctor.hospitalName);
        tvMyinfoGrade.setText(doctor.degree.name);
        if ("博士研究生".equals(doctor.degree.name)) {
            llMyinfoKeshi.setVisibility(View.GONE);
            viewMyinfoKeshiLine.setVisibility(View.GONE);
        } else {
            tvMyinfoOffice.setVisibility(View.VISIBLE);
            tvMyinfoOffice.setText(doctor.department);
        }
        disease = doctor.diseasedStates;//擅长病种
        perIntro = doctor.desc;//个人简介
        grade = doctor.degree.name;//职称，备注
        String diseasedStates = doctor.diseasedStates.toString();
        tvMyinfoGooddisease.setText(diseasedStates.substring(1, diseasedStates.length() - 1));
        txtMyinfoIntroduction.setText(doctor.desc);
        boolean isModifyInfo = doctor.isApplyingModifyInfo;
        if (isModifyInfo) {
            btnMyinfoTel.setText("审核中……");
            btnMyinfoTel.setBackgroundColor(Color.parseColor("#e7e7e7"));
            btnMyinfoTel.setClickable(false);
            arrowStatues(View.GONE);
            clickStatues(false);
        } else {
            btnMyinfoTel.setOnClickListener(listener);
            arrowStatues(View.VISIBLE);
            clickStatues(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {//返回码都为：RESULT_OK
            return;
        }
        if (requestCode == Constants.CODE_SELECT_NAME) {//姓名
            String myinfoName = data.getStringExtra(Constants.MY_INFO_NAME);
            tvMyinfoUsername.setText(myinfoName);
            doctor.name = myinfoName;
        } else if (requestCode == Constants.CODE_SELECT_HOSPITAL) {//医院
            ChooseBean bean = (ChooseBean) data.getSerializableExtra(Constants.CHOOSE_HOSPITAL);
            tvMyinfoHospital.setText(bean.getName());
            hospital = bean;
            doctor.hospitalId = bean.getId();
            doctor.hospitalName = bean.getName();
        }else if (requestCode == Constants.CODE_SELECT_DEPARTMENT) {//科室
            String myinfoDepartment = data.getStringExtra(Constants.MY_INFO_DEPARTMENT);
            tvMyinfoOffice.setText(myinfoDepartment);
            doctor.department = myinfoDepartment;
        } else if (requestCode == Constants.CODE_SELECT_DISEASE) {//病种
            disease = (ArrayList<UserBean.Diseased>) data.getSerializableExtra(Constants.CHOOSE_DISEASE);
            String tmp = "";
            int[] disease_ids = new int[disease.size()];
            for (int i = 0; i < disease.size(); i++) {
                tmp += disease.get(i).name + ",";
                disease_ids[i] = disease.get(i).id;
            }
            tmp = tmp.substring(0, tmp.length() - 1);
            tvMyinfoGooddisease.setText(tmp);
            doctor.disease_ids = disease_ids;
        } else if (requestCode == Constants.CODE_MIME_PERSONAL_PROFILE) {//个人简介
            perIntro = data.getStringExtra(Constants.PERSONAL_PROFILE);
            txtMyinfoIntroduction.setText(perIntro);
            doctor.desc = perIntro;
        } else if (requestCode == Constants.CODE_SELECT_GRADE) {//备注（职称）
            String grade = data.getStringExtra(Constants.MY_INFO_GRADE);
            if (grade.equals(doctor.degree.name)) {
                tvMyinfoGradeChange.setTextColor(getResources().getColor(R.color.txt_hint_color));
                txtZhiCheng.setTextColor(getResources().getColor(R.color.text_mine_service));
                tvMyinfoGradeChange.setText("请输入您要修改的职称");
            } else {
                tvMyinfoGradeChange.setTextColor(getResources().getColor(R.color.text_mine_service));
                txtZhiCheng.setTextColor(getResources().getColor(R.color.txt_hint_color));
                tvMyinfoGradeChange.setText(grade);
            }
            this.grade = grade;
        }
    }


    /**
     * 申请修改资料
     * */
    private void applyUpdateInfo() {
        TipDialog tipDialog = new TipDialog(MyInfoActivity.this);
        tipDialog.setMessage("确定申请修改您的资料么？");

        tipDialog.setPositive("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        tipDialog.setNegative("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                // 更新资料
                updateInfo();
            }
        });
        tipDialog.show();
    }

    /** 更新资料 */
    private void updateInfo() {
        startProgressDialog();
        if (doctor.disease_ids == null) {
            disease = doctor.diseasedStates;
            int[] disease_ids = new int[disease.size()];
            for (int i = 0; i < disease.size(); i++) {
                disease_ids[i] = disease.get(i).id;
            }
            doctor.disease_ids = disease_ids;
        }
        String remark = tvMyinfoGradeChange.getText().toString();
        if ("请输入您要修改的职称".equals(remark)) {
            remark = doctor.degree.name;
        }
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.modifyInfo(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, doctor.name, doctor.department, doctor.hospitalId==-1?null:doctor.hospitalId,
                doctor.hospitalName, doctor.disease_ids, doctor.desc, remark,  new CustomCallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        stopProgressDialog();
                        TipDialog.hintDiaglog(MyInfoActivity.this, "申请提交成功，我们会尽快审核，请耐心等待！");
                        btnMyinfoTel.setText("审核中……");
                        btnMyinfoTel.setBackgroundColor(Color.parseColor("#e7e7e7"));
                        btnMyinfoTel.setClickable(false);
                        arrowStatues(View.GONE);
                        clickStatues(false);
                    }

                    @Override
                    public void onFailure() {
                        stopProgressDialog();
                    }
                });
    }
}
