package wyqj.cancerprevent.doctorversion.app;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.chat.lib.utils.UserUtils;
import com.kaws.lib.common.cache.ACache;
import com.kaws.lib.common.utils.SDcardUtil;
import com.kaws.lib.common.utils.SharedPreferencesUtil;
import com.kaws.lib.fresco.Image;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.iflytek.SpeechTool;
import com.umeng.socialize.PlatformConfig;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wyqj.cancerprevent.doctorversion.BuildConfig;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.MainActivity;
import wyqj.cancerprevent.doctorversion.activity.chat.EaseUIHelper;
import wyqj.cancerprevent.doctorversion.bean.PatientBean;
import wyqj.cancerprevent.doctorversion.bean.SplashImgBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

/**
 * Created by 杨才 on 2016/1/29.
 */
public class App extends Application {

    private static App instance;
    private ACache cache;
    public boolean isRuning;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        cache = ACache.get(instance);
        HttpUtils.getInstance().init(this);
        Image.initFresco(this);
        SQuser.init(instance);
        downloadSplashImg();
        initUmeng();
        initJPush();
        initChat();
        SpeechTool.init(this);
        initBugTags();
        ActivityLifecycleCallbacks();
    }

    private void initBugTags() {
        BugtagsOptions options = new BugtagsOptions.Builder().
                versionName("Dkaws_" + BuildConfig.VERSION_NAME + "_debug").
                build();

        Bugtags.start("c08ee3d8171d2f682f5642ed29d83bde", this, Bugtags.BTGInvocationEventBubble, options);
    }

    private void initChat() {
        EaseUIHelper.getInstance().init(this);
        UserUtils.getInstance().setInfo(new UserUtils.IgetUserInfo() {
            @Override
            public void onGetUserInfo(String userkey, SimpleDraweeView imageView, TextView name) {
                String head = cache.getAsString(userkey + Constants.CHAT_AVATAR);
                String nick = cache.getAsString(userkey + Constants.CHAT_NAME);
                if (TextUtils.isEmpty(head) || TextUtils.isEmpty(nick)) {
                    getPatientInfo(userkey, imageView, name);
                } else {
                    if (imageView != null)
                        Image.displayRound(imageView, 10, Uri.parse(head), R.drawable.ease_default_avatar);
                    if (name != null)
                        name.setText(nick);
                }
            }

            @Override
            public void onGetMyInfo(SimpleDraweeView imageView) {
                SQuser sQuser = SQuser.getInstance();
                Image.displayRound(imageView, 10, Uri.parse(sQuser.getUserInfo().info.avatarUrl), R.drawable.ease_default_avatar);
            }
        });
    }

    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
    }

    private void initUmeng() {
        PlatformConfig.setQQZone("1104764006", "tY6rG3RJfsh8Kw6w");
        PlatformConfig.setWeixin("wxfc49d2ed774ea0d9", "c9ff1b822c97fdb4a50070f1e3d2aa72");
        PlatformConfig.setSinaWeibo("2215698705", "7ede48e7fe48fbb1a000be1f1abbf4f5");
    }

    public static App getInstance() {
        return instance;
    }


    /**
     * 下载启动图
     */
    private void downloadSplashImg() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.loadSplashImg(HttpHead.getHeader("GET"), new Callback<SplashImgBean>() {
            @Override
            public void success(final SplashImgBean splashImgBean, Response response) {
                String splash = SharedPreferencesUtil.getSplashUrl(instance);
                if (TextUtils.isEmpty(splash) || !splash.equalsIgnoreCase(splashImgBean.getUrl())) {
                    SharedPreferencesUtil.saveSplashUrl(instance, splashImgBean.getUrl());
                    long sen = System.currentTimeMillis() / 1000;
                    final String splashSavePath = SDcardUtil.getDiskCacheDir(instance) + File.separator + Constants.SPLASH_PATH + File.separator + sen;
                    Image.loadImage(instance, Uri.parse(splashImgBean.getUrl()), splashSavePath);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    private void getPatientInfo(final String toUserKey, final SimpleDraweeView imageView, final TextView name) {
        SQuser sQuse = SQuser.getInstance();
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.getPatientInfo(HttpHead.getHeader("GET"), sQuse.getUserInfo().token, toUserKey, "simple_medical", new Callback<PatientBean>() {
            @Override
            public void success(PatientBean patientEntity, Response response) {
                if (imageView != null)
                    Image.displayRound(imageView, 10, Uri.parse(patientEntity.avatarRrl), R.drawable.ease_default_avatar);
                if (name != null)
                    name.setText(patientEntity.username);
                cache.put(toUserKey + Constants.CHAT_AVATAR, patientEntity.avatarRrl, 5000);
                cache.put(toUserKey + Constants.CHAT_NAME, patientEntity.username, 5000);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }


    private void ActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof MainActivity) {
                    isRuning = true;
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity instanceof MainActivity) {
                    isRuning = false;
                }
            }
        });
    }
}
