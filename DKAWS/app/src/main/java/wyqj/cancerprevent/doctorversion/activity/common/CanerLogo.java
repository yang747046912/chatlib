package wyqj.cancerprevent.doctorversion.activity.common;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.util.PathUtil;
import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.common.utils.SharedPreferencesUtil;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.fresco.Image;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.MainActivity;
import wyqj.cancerprevent.doctorversion.activity.chat.EaseUIHelper;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.JpushHelper;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class CanerLogo extends BaseActivity {
    private ImageView canerLogo;
    private TextView jump;
    private boolean animationEnd;
    private boolean loginEnd;
    private boolean loginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_can_logo);
        canerLogo = getView(R.id.img_caner_logo);
        jump = getView(R.id.tv_jump);
        jump.setVisibility(View.GONE);
        jump.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                animationEnd();
            }
        });
        String splashSavePath = SharedPreferencesUtil.getSplashUrl(CanerLogo.this);
        DebugUtil.debug("splashSavePath : " + splashSavePath);
        if (!TextUtils.isEmpty(splashSavePath)) {
            File f = Image.getCachedImageOnDisk(Uri.parse( splashSavePath));
            if (f != null) {
                DebugUtil.debug("splash file is : " + f.getAbsolutePath());
                canerLogo.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
            } else {
                DebugUtil.debug("splash file is null ");
            }
        }
        String[] nameAndPwd = SQuser.getInstance().getUserNameAndPwd();
        if (!TextUtils.isEmpty(nameAndPwd[0]) || !TextUtils.isEmpty(nameAndPwd[1])) {
            login(nameAndPwd[0], nameAndPwd[1]);
        } else {
            loginEnd = true;
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        animation.setAnimationListener(animationListener);
        canerLogo.startAnimation(animation);
    }

    private void animationEnd() {
        synchronized (CanerLogo.this) {
            if (!animationEnd) {
                animationEnd = true;
                canerLogo.clearAnimation();
                if (loginEnd) {
                    if (!loginSuccess) {
                        openActivity(LoginAndRegister.class);
                        overridePendingTransition(R.anim.screen_down_in, R.anim.screen_up_out);
                        finish();
                    } else {
                        openActivity(MainActivity.class);
                        overridePendingTransition(R.anim.screen_down_in, R.anim.screen_up_out);
                        finish();
                    }
                }
            }
        }
    }

    private void loginEnd(boolean success) {
        synchronized (CanerLogo.this) {
            if (!loginEnd) {
                loginEnd = true;
                loginSuccess = success;
                if (animationEnd) {
                    if (!loginSuccess) {
                        openActivity(LoginAndRegister.class);
                        overridePendingTransition(R.anim.screen_down_in, R.anim.screen_up_out);
                        finish();
                    } else {
                        openActivity(MainActivity.class);
                        overridePendingTransition(R.anim.screen_down_in, R.anim.screen_up_out);
                        finish();
                    }
                }
            }
        }
    }

    /*
        * 实现监听跳转效果
        */
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            animationEnd();
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };


    private void login(final String name, final String pwd) {


        HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
        service.login(HttpHead.getHeader("POST"), null, name, pwd, new CustomCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean loginBean) {
                SQuser.getInstance().saveUserInfo(loginBean);
                loginHXChat(loginBean.userkey, pwd);
            }

            @Override
            public void onFailure() {
                loginEnd(false);
                SQuser.getInstance().clearUserNameAndPwd();
            }
        });
    }

    /**
     * 登陆环信
     *
     * @param username userkey
     * @param password 环信的密码都是123456
     */
    private void loginHXChat(final String username, final String password) {

        EMChatManager.getInstance().login(username, "123456", new EMCallBack() {

            @Override
            public void onSuccess() {
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
                PathUtil.getInstance().initDirs("kaws", "chatCache", CanerLogo.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JpushHelper.bindJPush(username);
                        if (JPushInterface.isPushStopped(CanerLogo.this)) {
                            JPushInterface.resumePush(CanerLogo.this);
                        }
                        loginEnd(true);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                loginEnd(false);
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
