package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.DataCacheUtil;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.TipDialog;

import cn.jpush.android.api.JPushInterface;
import wyqj.cancerprevent.doctorversion.BuildConfig;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.MainActivity;
import wyqj.cancerprevent.doctorversion.activity.chat.EaseUIHelper;
import wyqj.cancerprevent.doctorversion.activity.common.LoginAndRegister;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class SettingActivity extends LoadBaseActivity {
    private RelativeLayout rlSettingCache;
    private RelativeLayout rlSettingContact;
    private RelativeLayout rlSettingLikekaws;
    private RelativeLayout rlSettingModifypwd;
    private RelativeLayout rlSettingPushsetting;
    private RelativeLayout rlSettingRecommend;
    private Button btnSettingExit;
    private TextView tvSettingCurrentversion;
    private TextView tvTitlebarTitle;
    private TextView tvMyinfoCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        showContentView();
        setTitle("设置");
        initView();
        setUpView();
    }

    private void setUpView() {
        tvSettingCurrentversion.setText("当前版本：" + BuildConfig.VERSION_NAME);
        tvMyinfoCache.setText(DataCacheUtil.getFileSize(DataCacheUtil.getCacheSize(SettingActivity.this)));
        rlSettingCache.setOnClickListener(listener);
        rlSettingContact.setOnClickListener(listener);
        rlSettingLikekaws.setOnClickListener(listener);
        rlSettingModifypwd.setOnClickListener(listener);
        rlSettingPushsetting.setOnClickListener(listener);
        rlSettingRecommend.setOnClickListener(listener);
        btnSettingExit.setOnClickListener(listener);
    }

    private void initView() {
        rlSettingCache = (RelativeLayout) findViewById(R.id.rl_setting_cache);
        rlSettingContact = (RelativeLayout) findViewById(R.id.rl_setting_contact);
        rlSettingLikekaws = (RelativeLayout) findViewById(R.id.rl_setting_likekaws);
        rlSettingModifypwd = (RelativeLayout) findViewById(R.id.rl_setting_modifypwd);
        rlSettingPushsetting = (RelativeLayout) findViewById(R.id.rl_setting_pushsetting);
        rlSettingRecommend = (RelativeLayout) findViewById(R.id.rl_setting_recommend);
        btnSettingExit = (Button) findViewById(R.id.btn_setting_exit);
        tvSettingCurrentversion = (TextView) findViewById(R.id.tv_setting_currentversion);
        tvTitlebarTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        tvMyinfoCache = (TextView) findViewById(R.id.tv_myinfo_cache);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.rl_setting_cache://清除缓存
                    TipDialog tipDialog = new TipDialog(SettingActivity.this);
                    tipDialog.setMessage("确定清除缓存吗？");
                    tipDialog.setNegative("确定清除", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            DataCacheUtil.clearAppCache(SettingActivity.this);
                            tvMyinfoCache.setText("0KB");
                            ToastUtils.showToast(SettingActivity.this, "缓存清除成功", 2000, 1);
                        }
                    });
                    tipDialog.setPositive("不清除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    tipDialog.show();
                    break;
                case R.id.rl_setting_contact://联系客服
                    Intent contactIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.wyqj_telephone)));
                    startActivity(contactIntent);
                    break;
                case R.id.rl_setting_likekaws://好评
                    try {
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        ToastUtils.showToast(SettingActivity.this, "软件市场里暂时没有找到抗癌卫士医生版", 2000, 1);
                    }

                    break;
                case R.id.rl_setting_modifypwd://修改密码
                    openActivity(ModifyPwdActivity.class);
                    break;
                /**消息通知页面*/
                case R.id.rl_setting_pushsetting://消息通知
                    openActivity(InfoNoticeActivity.class);
                    break;
                case R.id.rl_setting_recommend://精品推荐
                    openActivity(RecommendActivity.class);
                    break;
                case R.id.btn_setting_exit://退出登录
                    TipDialog tipDialog1 = new TipDialog(SettingActivity.this);
                    tipDialog1.setTitle("退出");
                    tipDialog1.setMessage("退出后将无法收到推送消息");

                    tipDialog1.setNegative("确定退出", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            JPushInterface.stopPush(SettingActivity.this);
                            EaseUIHelper.getInstance().logout(true, null);
                            dialogInterface.dismiss();
                            SQuser.getInstance().clearUserNameAndPwd();
                            openActivity(LoginAndRegister.class);
                            MainActivity.instance.finishDefault();//关闭主页
                            finish();
                        }
                    });
                    tipDialog1.setPositive("不退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    tipDialog1.show();
                    break;

            }
        }
    };
}
