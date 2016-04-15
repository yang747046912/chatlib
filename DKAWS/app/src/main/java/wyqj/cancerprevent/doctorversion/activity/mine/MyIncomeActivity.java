package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.TipDialog;

import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class MyIncomeActivity extends LoadBaseActivity {

    private RelativeLayout rlMyincomeHowtodo;
    private RelativeLayout rlMyincomeRecord;
    private RelativeLayout rlMyinviteRecord;
    private Button btnMyincomeTakemoney;
    private TextView tvMyincomeNowTotal;
    private String currentMoney;
    private String unblockedAmount;
    private String totalMoney;
    private int applicationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income);
        showContentView();
        setTitle("我的收入");
        initView();
        setUpView();
    }

    private void setUpView() {
        rlMyincomeHowtodo.setOnClickListener(listener);
        rlMyincomeRecord.setOnClickListener(listener);
        rlMyinviteRecord.setOnClickListener(listener);
        btnMyincomeTakemoney.setOnClickListener(listener);

        UserBean.Doctor doctor = SQuser.getInstance().getUserInfo().info;
        formatMoney(doctor);
        tvMyincomeNowTotal.setText(currentMoney);// 当前收入
        applicationStatus = doctor.applicationStatus;
        if (applicationStatus == 1) {//正在提现
            btnMyincomeTakemoney.setText("提现处理中...");
            btnMyincomeTakemoney.setBackgroundColor(Color.parseColor("#e7e7e7"));
        }
    }

    private void initView() {
        tvMyincomeNowTotal = (TextView) findViewById(R.id.tv_myincome_now_total);
        btnMyincomeTakemoney = (Button) findViewById(R.id.btn_myincome_takemoney);
        rlMyincomeHowtodo = (RelativeLayout) findViewById(R.id.rl_myincome_howtodo);
        rlMyincomeRecord = (RelativeLayout) findViewById(R.id.rl_myincome_record);
        rlMyinviteRecord = (RelativeLayout) findViewById(R.id.rl_myinvite_record);
    }


    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.btn_myincome_takemoney://申请提现
                    if (applicationStatus == 1) {
                        TipDialog.hintDiaglog(MyIncomeActivity.this, "您的提现申请正在处理中，请耐心等待");
                        return;
                    }
                    if (Float.valueOf(currentMoney) == 0.0) {
                        TipDialog.hintDiaglog(MyIncomeActivity.this, "您的现有收入为0，不能申请提现");
                        return;
                    }
                    Intent intent = new Intent(MyIncomeActivity.this, ApplyWithdrawActivity.class);
                    startActivityForResult(intent, Constants.CODE_MY_INCOME);
                    break;
                case R.id.rl_myincome_howtodo://如何创收
                    int status = SQuser.getInstance().getUserInfo().status;
                    if (2 == status || 5 == status) {//未审核和审核失败
                        hintDialog();
                    } else if ("3".equals(status)) {//正在审核
                        TipDialog.hintDiaglog(MyIncomeActivity.this, "您的身份信息正在审核中,通过后才可以进行操作,请耐心等待");
                    } else {
                        Bundle b = new Bundle();
                        b.putBoolean(Constants.MAKE_MONEY, true);
                        openActivity(CommonQuestionActivity.class, b);
                    }
                    break;
                case R.id.rl_myincome_record://收入/提现记录
                    openActivity(MyIncomeOrWithdrawActivity.class);
                    break;
                case R.id.rl_myinvite_record://邀请医生记录
                    openActivity(MyInviteRecordActivity.class);
                    break;
                default:
                    break;
            }
        }
    };


    /** 入小数位为0则不显示 */
    private void formatMoney(UserBean.Doctor doctor) {
        if (String.valueOf(doctor.currentMoney).contains(".0")) {
            currentMoney = String.valueOf(doctor.currentMoney).replace(".0", "");
        } else {
            currentMoney = String.valueOf(doctor.currentMoney);
        }
        if (String.valueOf(doctor.unblocked_amount).contains(".0")) {
            unblockedAmount = String.valueOf(doctor.unblocked_amount).replace(".0", "");
        } else {
            unblockedAmount = String.valueOf(doctor.unblocked_amount);
        }
        if (String.valueOf(doctor.totalMoney).contains(".0")) {
            totalMoney = String.valueOf(doctor.totalMoney).replace(".0", "");
        } else {
            totalMoney = String.valueOf(doctor.totalMoney);
        }
    }

    private void hintDialog() {
        TipDialog tipDialog = new TipDialog(this);
        tipDialog.setTitle("请先认证");
        tipDialog.setMessage("请先上传您的身份认证信息,审核通过后才可以进一步操作");
        tipDialog.setNegative("上传认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        tipDialog.setPositive("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        tipDialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        int takeMoneyStatus = 0;//可提现状态值
        if (requestCode == Constants.CODE_MY_INCOME) { //申请提现界面返回
                takeMoneyStatus = data.getIntExtra(Constants.TAKE_MONEY_STATUS, 0);
                if (takeMoneyStatus == 1) {//正在提现
                    btnMyincomeTakemoney.setText("提现处理中...");
                    btnMyincomeTakemoney.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    btnMyincomeTakemoney.setOnClickListener(listener);
                } else {
                    btnMyincomeTakemoney.setOnClickListener(listener);
                }
            }
    }
}
