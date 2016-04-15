package wyqj.cancerprevent.doctorversion.utils;

import android.os.Handler;
import android.os.Message;

import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wyqj.cancerprevent.doctorversion.app.App;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.http.HttpService;

/**
 * Created by 杨才 on 2016/2/26.
 */
public class JpushHelper {
    private static final int MSG_SET_ALIAS = 1001;
    private static final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    DebugUtil.debug("--Jpush-->", "Set alias in handler.");
                    JPushInterface.setAliasAndTags(App.getInstance(), (String) msg.obj, null, mAliasCallback);
                    break;

                default:
                    DebugUtil.debug("--Jpush-->", "Unhandled msg" + msg.what);
                    break;
            }

        }
    };

    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            switch (code) {
                case 0:
                    bJPush();
                    DebugUtil.debug("--Jpush-->", "极光推送绑定成功，给后端服务器发送用户信息");
                    break;

                case 6002:
                    //logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    handler.sendMessageDelayed(handler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    DebugUtil.debug("--Jpush-->", "极光推送绑定失败");
                    break;
            }
        }
    };


    private static void bJPush() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.bindJpsh(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().userkey, sQuser.getUserInfo().userkey, new Callback<ErrorBean>() {
            @Override
            public void success(ErrorBean restError, Response response) {
                DebugUtil.debug("--Jpush-->", "极光推送信息发送成功");
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                DebugUtil.debug("--Jpush-->", "极光推送信息发送失败");
            }
        });
    }

    public static void bindJPush(String alias) {
        handler.sendMessage(handler.obtainMessage(MSG_SET_ALIAS, alias));
    }
}
