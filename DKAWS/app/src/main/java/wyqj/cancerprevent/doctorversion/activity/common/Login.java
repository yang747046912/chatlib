package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.util.PathUtil;
import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.ActivityStackUtil;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import cn.jpush.android.api.JPushInterface;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.MainActivity;
import wyqj.cancerprevent.doctorversion.activity.chat.EaseUIHelper;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.JpushHelper;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class Login extends BaseActivity {

    private EditText name;
    private EditText pwd;
    private CheckBox showPwd;
    private TextView login;
    private TextView forgetPwd;
    private ImageView login_back;
    private TextView tv_login_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setUpView();
        ActivityStackUtil.getInstance().add(this);
    }

    private void initView() {
        name = getView(R.id.et_login_username);
        pwd = getView(R.id.et_login_password);
        showPwd = getView(R.id.cb_login_showpwd);
        login = getView(R.id.tv_login_login);
        forgetPwd = getView(R.id.tv_login_forgetpassword);
        login_back = getView(R.id.btn_titlebar_back);
        tv_login_title = getView(R.id.tv_titlebar_title);
    }

    private void setUpView() {
        login_back.setImageResource(R.drawable.login_fanhui22x);
        login_back.setPadding(15, 0, 0, 0);
        tv_login_title.setText("登录");
        login.setOnClickListener(listener);
        forgetPwd.setOnClickListener(listener);
        login_back.setOnClickListener(listener);
        showPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int end = pwd.getSelectionEnd();
                int start = pwd.getSelectionStart();
                if (isChecked) {
                    //如果选中，显示密码
                    pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                Selection.setSelection(pwd.getText(), start, end);
            }
        });
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_login_forgetpassword:
                    openActivity(FindPwdActivity.class);
                    break;
                case R.id.tv_login_login:
                    String username = name.getText().toString().trim();
                    String password = pwd.getText().toString().trim();
                    login(username, password);
                    //   Share();
                    break;
                case R.id.btn_titlebar_back:
                    finishDefault();
                default:
                    break;
            }
        }
    };

    private boolean checkLoginInfo(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            TipDialog dialog = new TipDialog(Login.this);
            dialog.setMessage("手机号不能为空");
            dialog.setPositive("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            TipDialog dialog = new TipDialog(Login.this);
            dialog.setMessage("密码不能为空");
            dialog.setPositive("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return false;
        }
        return true;
    }

    private void login(final String name, final String pwd) {
        if (!checkLoginInfo(name, pwd)) {
            return;
        }
        startProgressDialog();
        HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
        service.login(HttpHead.getHeader("POST"), null, name, pwd, new CustomCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean loginBean) {
                SQuser.getInstance().saveUserInfo(loginBean);
                loginHXChat(loginBean.userkey, pwd, name);
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        ActivityStackUtil.getInstance().remove(this);
        super.onDestroy();
    }


    /**
     * 登陆环信
     *
     * @param username userkey
     * @param password 环信的密码都是123456
     */
    private void loginHXChat(final String username, final String password, final String realUserName) {

        EMChatManager.getInstance().login(username, "123456", new EMCallBack() {

            @Override
            public void onSuccess() {
                stopProgressDialog();

                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
//                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //     EaseUI.getInstance().logout(null);
                            Toast.makeText(getApplicationContext(), "登录即时聊天失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                EaseUIHelper.getInstance().notifyForRecevingEvents();
                PathUtil.getInstance().initDirs("kaws", "chatCache", Login.this);
                SQuser.getInstance().saveUserNameAndPwd(realUserName, password);//保存用户名和密码
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        openActivity(MainActivity.class);
                        JpushHelper.bindJPush(username);
                        ActivityStackUtil.getInstance().destoryAll();
                        if (JPushInterface.isPushStopped(Login.this)) {
                            JPushInterface.resumePush(Login.this);
                        }
                        finishDefault();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                stopProgressDialog();
                runOnUiThread(new Runnable() {
                    public void run() {
                        DebugUtil.debug("---Login---->", getString(R.string.Login_failed) + message);
                        ToastUtils.showToast(getApplicationContext(), getString(R.string.Login_failed) + message, 2000, 3);
                    }
                });
            }
        });
    }

}
