package wyqj.cancerprevent.doctorversion.activity.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.util.EMLog;
import com.kaws.chat.lib.controller.EaseUI;
import com.kaws.chat.lib.model.EaseNotifier;
import com.kaws.lib.common.BuildConfig;
import com.kaws.lib.common.utils.AppUtil;
import com.kaws.lib.common.utils.DebugUtil;

import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.MainActivity;
import wyqj.cancerprevent.doctorversion.activity.common.Login;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class EaseUIHelper {
    private static EaseUIHelper instance;


    protected static final String TAG = "DemoHelper";

    private EaseUI easeUI;

    /**
     * EMEventListener
     */
    protected EMEventListener eventListener = null;


    private boolean alreadyNotified = false;


    private Context appContext;


    private EMConnectionListener connectionListener;


    private EaseUIHelper() {
    }

    public synchronized static EaseUIHelper getInstance() {
        if (instance == null) {
            instance = new EaseUIHelper();
        }
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context) {
        if (EaseUI.getInstance().init(context)) {
            appContext = context;
            //设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
            EMChat.getInstance().setDebugMode(BuildConfig.DEBUG);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //调用easeui的api设置providers
            setEaseUIProviders();
            //设置chat options
            setChatoptions();
            //设置全局监听
            setGlobalListeners();
            EMChat.getInstance().setAutoLogin(false);
        }
    }

    private void setChatoptions() {
        //easeui库默认设置了一些options，可以覆盖
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        options.allowChatroomOwnerLeave(false);
    }

    protected void setEaseUIProviders() {
//        //需要easeui库显示用户头像和昵称设置此provider
//        easeUI.setUserProfileProvider(new EaseUserProfileProvider() {
//
//            @Override
//            public EaseUser getUser(String username) {
//                return getUserInfo(username);
//            }
//        });
//
//        //不设置，则使用easeui默认的
//        easeUI.setSettingsProvider(new EaseSettingsProvider() {
//
//            @Override
//            public boolean isSpeakerOpened() {
//                return demoModel.getSettingMsgSpeaker();
//            }
//
//            @Override
//            public boolean isMsgVibrateAllowed(EMMessage message) {
//                return demoModel.getSettingMsgVibrate();
//            }
//
//            @Override
//            public boolean isMsgSoundAllowed(EMMessage message) {
//                return demoModel.getSettingMsgSound();
//            }
//
//            @Override
//            public boolean isMsgNotifyAllowed(EMMessage message) {
//                if(message == null){
//                    return demoModel.getSettingMsgNotification();
//                }
//                if(!demoModel.getSettingMsgNotification()){
//                    return false;
//                }else{
//                    //如果允许新消息提示
//                    //屏蔽的用户和群组不提示用户
//                    String chatUsename = null;
//                    List<String> notNotifyIds = null;
//                    // 获取设置的不提示新消息的用户或者群组ids
//                    if (message.getChatType() == ChatType.Chat) {
//                        chatUsename = message.getFrom();
//                        notNotifyIds = demoModel.getDisabledIds();
//                    } else {
//                        chatUsename = message.getTo();
//                        notNotifyIds = demoModel.getDisabledGroups();
//                    }
//
//                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            }
//        });
//        //设置表情provider
//        easeUI.setEmojiconInfoProvider(new EaseEmojiconInfoProvider() {
//
//            @Override
//            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
//                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
//                for(EaseEmojicon emojicon : data.getEmojiconList()){
//                    if(emojicon.getIdentityCode().equals(emojiconIdentityCode)){
//                        return emojicon;
//                    }
//                }
//                return null;
//            }
//
//            @Override
//            public Map<String, Object> getTextEmojiconMapping() {
//                //返回文字表情emoji文本和图片(resource id或者本地路径)的映射map
//                return null;
//            }
//        });
//
//        //不设置，则使用easeui默认的
//        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {
//
//            @Override
//            public String getTitle(EMMessage message) {
//                //修改标题,这里使用默认
//                return null;
//            }
//
//            @Override
//            public int getSmallIcon(EMMessage message) {
//                //设置小图标，这里为默认
//                return 0;
//            }
//
//            @Override
//            public String getDisplayedText(EMMessage message) {
//                // 设置状态栏的消息提示，可以根据message的类型做相应提示
//                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
//                if(message.getType() == Type.TXT){
//                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
//                }
//                EaseUser user = getUserInfo(message.getFrom());
//                if(user != null){
//                    return getUserInfo(message.getFrom()).getNick() + ": " + ticker;
//                }else{
//                    return message.getFrom() + ": " + ticker;
//                }
//            }
//
//            @Override
//            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
//                return null;
//                // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
//            }
//
//            @Override
//            public Intent getLaunchIntent(EMMessage message) {
//                //设置点击通知栏跳转事件
//                Intent intent = new Intent(appContext, ChatActivity.class);
//                //有电话时优先跳转到通话页面
//                if(isVideoCalling){
//                    intent = new Intent(appContext, VideoCallActivity.class);
//                }else if(isVoiceCalling){
//                    intent = new Intent(appContext, VoiceCallActivity.class);
//                }else{
//                    ChatType chatType = message.getChatType();
//                    if (chatType == ChatType.Chat) { // 单聊信息
//                        intent.putExtra("userId", message.getFrom());
//                        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
//                    } else { // 群聊信息
//                        // message.getTo()为群聊id
//                        intent.putExtra("userId", message.getTo());
//                        if(chatType == ChatType.GroupChat){
//                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
//                        }else{
//                            intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
//                        }
//
//                    }
//                }
//                return intent;
//            }
//        });

        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
            @Override
            public String getDisplayedText(EMMessage message) {
                return null;
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
            }

            @Override
            public String getTitle(EMMessage message) {
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                return 0;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {


                if (AppUtil.isAppRunning(appContext)) {
                    if (SQuser.getInstance().isLogin()) {
                        Intent intent1 = new Intent(appContext, MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        appContext.startActivity(intent1);
                    }
                } else {
                    Intent intent1 = new Intent(appContext, Login.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    appContext.startActivity(intent1);
                }
                return null;
            }
        });
    }

    /**
     * 设置全局事件监听
     */
    protected void setGlobalListeners() {
        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                DebugUtil.debug("----->", "error " + error);
                if (error == EMError.USER_REMOVED) {
                    onCurrentAccountRemoved();
                } else if (error == EMError.CONNECTION_CONFLICT) {
                    onConnectionConflict();
                }
            }

            @Override
            public void onConnected() {
                DebugUtil.debug("----->", "onConnected ");
            }
        };
        //注册连接监听
        EMChatManager.getInstance().addConnectionListener(connectionListener);
        //注册消息事件监听
        registerEventListener();

    }


    /**
     * 账号在别的设备登录
     */
    protected void onConnectionConflict() {
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EMchatConstant.ACCOUNT_CONFLICT, true);
        appContext.startActivity(intent);
    }

    /**
     * 账号被移除
     */
    protected void onCurrentAccountRemoved() {
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EMchatConstant.ACCOUNT_REMOVED, true);
        appContext.startActivity(intent);
    }


    /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void registerEventListener() {
        eventListener = new EMEventListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onEvent(EMNotifierEvent event) {
                EMMessage message = null;
                if (event.getData() instanceof EMMessage) {
                    message = (EMMessage) event.getData();
                    EMLog.d(TAG, "receive the event : " + event.getEvent() + ",id : " + message.getMsgId());
                }

                switch (event.getEvent()) {
                    case EventNewMessage:
                        //应用在后台，不需要刷新UI,通知栏提示新消息
                        if (!easeUI.hasForegroundActivies()) {
                            getNotifier().onNewMsg(message);
                        }
                        break;
                    case EventOfflineMessage:
                        if (!easeUI.hasForegroundActivies()) {
                            EMLog.d(TAG, "received offline messages");
                            List<EMMessage> messages = (List<EMMessage>) event.getData();
                            getNotifier().onNewMesg(messages);
                        }
                        break;
                    // below is just giving a example to show a cmd toast, the app should not follow this
                    // so be careful of this
                    case EventNewCMDMessage: {

                        EMLog.d(TAG, "收到透传消息");
                        //获取消息body
                        CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                        final String action = cmdMsgBody.action;//获取自定义action

                        //获取扩展属性 此处省略
                        //message.getStringAttribute("");
                        EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action, message.toString()));
                        final String str = appContext.getString(R.string.receive_the_passthrough);

                        final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
                        IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);

                        if (broadCastReceiver == null) {
                            broadCastReceiver = new BroadcastReceiver() {

                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    // TODO Auto-generated method stub
                                    Toast.makeText(appContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
                                }
                            };

                            //注册广播接收者
                            appContext.registerReceiver(broadCastReceiver, cmdFilter);
                        }

                        Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
                        broadcastIntent.putExtra("cmd_value", str + action);
                        appContext.sendBroadcast(broadcastIntent, null);

                        break;
                    }
                    case EventDeliveryAck:
                        message.setDelivered(true);
                        break;
                    case EventReadAck:
                        message.setAcked(true);
                        break;
                    // add other events in case you are interested in
                    default:
                        break;
                }

            }
        };

        EMChatManager.getInstance().registerEventListener(eventListener);
    }

    /**
     * 是否登录成功过
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMChat.getInstance().isLoggedIn();
    }

    /**
     * 退出登录
     *
     * @param unbindDeviceToken 是否解绑设备token(使用GCM才有)
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        EMChatManager.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * 获取消息通知类
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }


    void endCall() {
        try {
            EMChatManager.getInstance().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized void notifyForRecevingEvents() {
        if (alreadyNotified) {
            return;
        }
        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        EMChat.getInstance().setAppInited();
        alreadyNotified = true;
    }

    synchronized void reset() {
        alreadyNotified = false;
    }

    public void pushActivity(Activity activity) {
        easeUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.popActivity(activity);
    }

}
