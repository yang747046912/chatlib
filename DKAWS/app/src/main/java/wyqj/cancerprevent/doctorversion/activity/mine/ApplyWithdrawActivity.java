package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import org.bitlet.weupnp.LogUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ApplyMoneyBean;
import wyqj.cancerprevent.doctorversion.bean.ApplyWithdrawBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class ApplyWithdrawActivity extends LoadBaseActivity {

    private TextView txtMoneyApplyWithdraw;
    private EditText edtSubmitApplyWithdraw;
    private Button btnSubmitApplyWithdraw;
    private ImageView ivTakeMoneyAccount;
    private RelativeLayout rlTakemoneyBankinfo;
    private LinearLayout llEnsureAddBank;
    private TextView bankNumber;
    private ImageView bankIcon;
    private TextView bankName;

    private double inputMoney;//输入的金额
    private ApplyWithdrawBean applyWithdrawBean;
    private SQuser sQuser = SQuser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_withdraw);
        showContentView();
        setTitle("申请提现");
        initView();
        setUpView();
        checkIsClick();
        showBankAccountInfo();
    }

    private void setUpView() {
        btnSubmitApplyWithdraw.setClickable(false);
        ivTakeMoneyAccount.setOnClickListener(listener);
        rlTakemoneyBankinfo.setOnClickListener(listener);
        edtSubmitApplyWithdraw.setOnClickListener(listener);
        edtSubmitApplyWithdraw.addTextChangedListener(textWatcher);

        String unblockedAmount = String.valueOf(sQuser.getUserInfo().info.unblocked_amount);
        if (unblockedAmount.contains(".0")) {
            unblockedAmount = unblockedAmount.replace(".0", "");
        }
        txtMoneyApplyWithdraw.setText(unblockedAmount);
    }

    private void initView() {
        llEnsureAddBank = (LinearLayout) findViewById(R.id.ll_ensure_addbank);
        txtMoneyApplyWithdraw = (TextView) findViewById(R.id.txt_money_apply_withdraw);
        edtSubmitApplyWithdraw = (EditText) findViewById(R.id.edt_submit_apply_withdraw);
        btnSubmitApplyWithdraw = (Button) findViewById(R.id.btn_submit_apply_withdraw);
        ivTakeMoneyAccount = (ImageView) findViewById(R.id.iv_takemoney_account);
        rlTakemoneyBankinfo = (RelativeLayout) findViewById(R.id.rl_takemoney_bankinfo);
        bankIcon = (ImageView) findViewById(R.id.iv_bank_icon);
        bankName = (TextView) findViewById(R.id.tv_bank_name);
        bankNumber = (TextView) findViewById(R.id.tv_bank_number);
        llEnsureAddBank.setVisibility(View.VISIBLE);
        rlTakemoneyBankinfo.setVisibility(View.GONE);
    }


    private void processData(ApplyWithdrawBean applyWithdrawBean) {
        this.applyWithdrawBean = applyWithdrawBean;
        if (applyWithdrawBean.getHas_set_account()) {//是否有账号

            llEnsureAddBank.setVisibility(View.GONE);
            rlTakemoneyBankinfo.setVisibility(View.VISIBLE);

            String bankNameText = applyWithdrawBean.getBank_name();
            bankName.setText(bankNameText);//银行的名字
            int bankIconInt = getIconFromName(bankNameText);
            bankIcon.setImageResource(bankIconInt);//银行的图标
            String bankNumberString = applyWithdrawBean.getAccount_number();//账号
            /**银行卡账号隐藏*/
            if (bankNumberString.length() == 16) {//信用卡
                bankNumber.setText(bankNumberString.replace(bankNumberString.substring(0, 13), "**** **** **** **** "));
            }
            if (bankNumberString.length() == 19) {//储蓄卡
                bankNumber.setText(bankNumberString.replace(bankNumberString.substring(0, 16), "**** **** **** **** "));
            }
        } else {
            llEnsureAddBank.setVisibility(View.VISIBLE);
            rlTakemoneyBankinfo.setVisibility(View.GONE);
            edtSubmitApplyWithdraw.setSelected(false);
            edtSubmitApplyWithdraw.setClickable(false);
        }
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            Intent intent;
            switch (id) {
                case R.id.btn_submit_apply_withdraw://确定提现
                    if (checkIsOverMoney()) {
                        TipDialog tipDialog = new TipDialog(ApplyWithdrawActivity.this);
                        tipDialog.setTitle("提示");
                        tipDialog.setMessage("我们会在次月15号统一处理提现申请，您是否确定提现？");
                        tipDialog.setPositive("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        tipDialog.setNegative("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                applyTakeMoney((int) (inputMoney));
                            }
                        });
                        tipDialog.show();
                    }
                    break;
                case R.id.iv_takemoney_account: //添加银行卡账号
                    intent = new Intent(ApplyWithdrawActivity.this, AddBankCardActivity.class);
                    startActivityForResult(intent, Constants.CODE_APPLY_TAKEMONEY);
                    break;
                case R.id.rl_takemoney_bankinfo://更换银行卡账号
                    intent = new Intent(ApplyWithdrawActivity.this, AddBankCardActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.TAKE_MONEY_ACCOUNT_NUMBER, applyWithdrawBean.getAccount_number());
                    bundle.putString(Constants.TAKE_MONEY_BANK_NAME, applyWithdrawBean.getBank_name());
                    bundle.putString(Constants.TAKE_MONEY_ACCOUNT_NAME, applyWithdrawBean.getAccount_name());
                    bundle.putString(Constants.TAKE_MONEY_SUBBRANCH, applyWithdrawBean.getSub_branch());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Constants.CODE_APPLY_TAKEMONEY);
                    break;
            }
        }
    };

    /** 申请提现*/
    private void applyTakeMoney(int inputMoney) {
        startProgressDialog();
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.applyMoney(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, inputMoney, new CustomCallBack<ApplyMoneyBean>() {

            @Override
            public void onSuccess(ApplyMoneyBean applyMoneyBean) {
                stopProgressDialog();
                TipDialog tipDialog = new TipDialog(ApplyWithdrawActivity.this);
                tipDialog.setTitle("申请成功");
                tipDialog.setMessage("您申请提现成功了,我们的客服会尽快联系您，请耐心等待!");
                tipDialog.setNegative("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        stopProgressDialog();
                        Intent intent = getIntent();
                        intent.putExtra(Constants.TAKE_MONEY_STATUS, 1);
                        setResult(RESULT_OK, intent);
                        ApplyWithdrawActivity.this.finish();
                    }
                });
                tipDialog.show();
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }

        });
    }


    /** 根据银行名获取对应银行图标*/
    private int getIconFromName(String bankNameText) {
        int index = 0;
        for (int i = 0; i < Constants.banks.length; i++) {
            if (Constants.banks[i].equals(bankNameText)) {
                index = i;
            }
        }
        return Constants.resIds[index];
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            /** 根据条件决定“确定提现是否可点击” */
            String applyMoney = edtSubmitApplyWithdraw.getText().toString();
            if (!("".equals(applyMoney)) && (rlTakemoneyBankinfo.getVisibility() == View.VISIBLE)) {
                btnSubmitApplyWithdraw.setSelected(true);
                btnSubmitApplyWithdraw.setClickable(true);
                btnSubmitApplyWithdraw.setOnClickListener(listener);
            } else {
                btnSubmitApplyWithdraw.setSelected(false);
                btnSubmitApplyWithdraw.setClickable(false);
            }
        }
    };

    /**
     * 判断确定提现按钮是否可被点击
     */
    private void checkIsClick() {
        String applyMoney = edtSubmitApplyWithdraw.getText().toString();
        if (!("".equals(applyMoney)) && (bankNumber != null)) {
            edtSubmitApplyWithdraw.setSelected(true);
            edtSubmitApplyWithdraw.setClickable(true);
        } else {
            edtSubmitApplyWithdraw.setSelected(false);
            edtSubmitApplyWithdraw.setClickable(false);
        }
    }

    /**
     * 设置银行卡后"确定提现"可点击
     */
    private void showBankAccountInfo() {
        startProgressDialog();
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.applyWithdraw(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, new CustomCallBack<ApplyWithdrawBean>() {
            @Override
            public void onSuccess(ApplyWithdrawBean applyWithdrawBean) {
                stopProgressDialog();
                processData(applyWithdrawBean);
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
                edtSubmitApplyWithdraw.setSelected(false);
                edtSubmitApplyWithdraw.setClickable(false);
            }
        });
    }

    /**
     * 检查输入金额的正确性
     */
    private boolean checkIsOverMoney() {
        inputMoney = Double.valueOf(edtSubmitApplyWithdraw.getText().toString());
        double currentMoney = sQuser.getUserInfo().info.unblocked_amount;
        if (inputMoney > Integer.MAX_VALUE || inputMoney == 0) {
            TipDialog.hintDiaglog(ApplyWithdrawActivity.this, "请输入有效金额");
            return false;
        }
        if (inputMoney > currentMoney) {
            TipDialog.hintDiaglog(ApplyWithdrawActivity.this, "您申请的提现金额超限");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.CODE_APPLY_TAKEMONEY) {//添加银行卡返回
            checkIsClick();
            showBankAccountInfo();
        }
    }
}
