package wyqj.cancerprevent.doctorversion.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.kaws.lib.common.utils.AppUtil;
import com.kaws.lib.common.utils.DebugUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;
import wyqj.cancerprevent.doctorversion.activity.chat.ChatActivity;
import wyqj.cancerprevent.doctorversion.activity.common.Login;
import wyqj.cancerprevent.doctorversion.activity.mine.NotificationCenterActivity;
import wyqj.cancerprevent.doctorversion.activity.patient.ApplyDetailActivity;
import wyqj.cancerprevent.doctorversion.activity.square.QuestionDetailActivity;
import wyqj.cancerprevent.doctorversion.app.App;
import wyqj.cancerprevent.doctorversion.bean.JPushBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.constant.DoctorPushNotificationType;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            //	processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            openNotification(context, bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }


    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        JPushBean jPushEntity = new Gson().fromJson(extras, JPushBean.class);
        int type = jPushEntity.from.type;
        String item_pk = jPushEntity.from.item_pk;
        DebugUtil.debug("极光推送JPush", bundle.toString());
        DebugUtil.debug("极光返回的json：" + extras);
        DebugUtil.debug("极光推送发过来的type：" + type);
        DebugUtil.debug("极光推送发过来的item_pk：" + item_pk);
        DebugUtil.debug("is app running " + AppUtil.isAppRunning(context));
        if (!App.getInstance().isRuning || !SQuser.getInstance().isLogin()) {
            Intent intent1 = new Intent(context, Login.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            return;
        }
        Intent intent = null;
        switch (type) {
            case DoctorPushNotificationType.APPEND_QUESTION_COMMENT:
                intent = new Intent(context, QuestionDetailActivity.class);
                int questionId = Integer.valueOf(item_pk);
                intent.putExtra(Constants.QUESTION_ID, questionId);
                break;
            case DoctorPushNotificationType.CHAT_FROM_USER:
                intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userId", item_pk);
                break;
            case DoctorPushNotificationType.NEW_APPLICATION:
                intent = new Intent(context, ApplyDetailActivity.class);
                intent.putExtra(Constants.APPLY_ID, item_pk);
                break;
            case DoctorPushNotificationType.NOTIFICATION_CENTER:
                intent = new Intent(context, NotificationCenterActivity.class);
                break;
            case DoctorPushNotificationType.PC_APPLYING:
            default:
                break;
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
