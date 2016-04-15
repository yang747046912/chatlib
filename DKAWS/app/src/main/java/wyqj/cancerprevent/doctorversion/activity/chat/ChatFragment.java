package wyqj.cancerprevent.doctorversion.activity.chat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.kaws.chat.lib.EaseConstant;
import com.kaws.chat.lib.ui.EaseChatFragment;
import com.kaws.chat.lib.widget.chatrow.EaseCustomChatRowProvider;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.regex.Pattern;

import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

/**
 * Created by 杨才 on 2015/12/10.
 */
public class ChatFragment extends EaseChatFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideTitleBar();
        setChatFragmentListener(new EaseChatFragmentListener() {
            @Override
            public void onResendMsg(final EMMessage message) {
                TipDialog dialog = new TipDialog(getActivity());
                dialog.setTitle("重发");
                dialog.setMessage("确认重发消息");
                dialog.setPositive("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resendMessage(message);
                        dialog.dismiss();
                    }
                });
                dialog.setNegative("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

            @Override
            public void onSetMessageAttributes(EMMessage message) {

            }

            @Override
            public void onEnterToChatDetails() {

            }

            @Override
            public void onAvatarClick(String username) {
            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        });
    }

    @Override
    protected void sendMessage(final EMMessage message) {
        if (message.getType() == EMMessage.Type.TXT) {
            String msg = ((TextMessageBody) message.getBody()).getMessage();
            Pattern p = Pattern.compile("^\\s*$");
            if (p.matcher(msg).matches()) {
                Toast.makeText(getActivity(), "不能发送空白消息", Toast.LENGTH_LONG).show();
                return;
            }
        }
        if (chatFragmentListener != null) {
            //设置扩展属性
            chatFragmentListener.onSetMessageAttributes(message);
        }
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }
        //发送消息
        EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
            @Override
            public void onSuccess() {
                if (message.getType() == EMMessage.Type.TXT) {
                    sendMessageByJPush(((TextMessageBody) message.getBody()).getMessage(), 1);
                } else if (message.getType() == EMMessage.Type.IMAGE) {
                    sendMessageByJPush("[图片]", 2);
                } else if (message.getType() == EMMessage.Type.VOICE) {
                    sendMessageByJPush("[语音]", 3);
                }
            }

            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
        //刷新ui
        messageList.refreshSelectLast();

    }


    private void sendMessageByJPush(String content, int messageType) {
        SQuser sQuser = SQuser.getInstance();
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.instantMessage(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, toChatUsername, content, messageType, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {

            }

            @Override
            public void onFailure() {

            }
        });

    }


}