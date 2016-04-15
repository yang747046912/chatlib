package wyqj.cancerprevent.doctorversion.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.common.widget.TipDialog;

import java.util.Calendar;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.chat.EMchatConstant;
import wyqj.cancerprevent.doctorversion.activity.chat.EMchatCreat;
import wyqj.cancerprevent.doctorversion.activity.chat.EaseUIHelper;
import wyqj.cancerprevent.doctorversion.activity.common.Login;
import wyqj.cancerprevent.doctorversion.fragment.consultation.Consultation;
import wyqj.cancerprevent.doctorversion.fragment.mine.Mine;
import wyqj.cancerprevent.doctorversion.fragment.patient.Patient;
import wyqj.cancerprevent.doctorversion.fragment.square.Square;
import wyqj.cancerprevent.doctorversion.interfaces.IdeleteConversation;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class MainActivity extends BaseActivity implements EMEventListener, IdeleteConversation {
    public static BaseActivity instance;
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private Mine mine;
    private Patient patient;
    private Square square;
    private Consultation consultation;
    private final static int TAG_MINE = 2;
    private final static int TAG_PATIENT = 0;
    private final static int TAG_SQUARE = 1;
    private final static int TAG_CONSULTATION = 3;
    //表示当前处于的fragment
    private int tag = TAG_CONSULTATION;
    private TextView unreadMsg;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    private TipDialog conflictDialog;
    private TipDialog accountRemovedDialog;
    private long lastClickTime = 0;
    private static final long MIN_CLICK_DELAY_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EMchatCreat.Oncreat(savedInstanceState, this);
        setContentView(R.layout.activity_main);
        initView();
        setUpView();
        if (getIntent().getBooleanExtra(EMchatConstant.ACCOUNT_CONFLICT, false)) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(EMchatConstant.ACCOUNT_REMOVED, false)) {
            showAccountRemovedDialog();
        }
        instance = this;
    }

    private void initView() {
        radioGroup = getView(R.id.main_radiogroup);
        unreadMsg = getView(R.id.unread_msg_number);
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        mine = new Mine();
        patient = new Patient();
        square = new Square();
        consultation = new Consultation();
        ft.add(R.id.fl_container, mine);
        ft.add(R.id.fl_container, patient);
        ft.add(R.id.fl_container, square);
        ft.add(R.id.fl_container, consultation);
        ft.hide(mine);
        ft.hide(square);
        ft.hide(patient);
        ft.show(consultation);
        ft.commit();
    }

    private void showFragment(int index) {
        if (index == tag) {
            return;
        } else {
            tag = index;
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (tag == TAG_PATIENT) {
                ft.show(patient);
                ft.hide(square);
                ft.hide(mine);
                ft.hide(consultation);
            } else if (tag == TAG_SQUARE) {
                ft.hide(patient);
                ft.show(square);
                ft.hide(mine);
                ft.hide(consultation);
            } else if (tag == TAG_MINE) {
                ft.hide(patient);
                ft.hide(square);
                ft.show(mine);
                ft.hide(consultation);
            } else if (tag == TAG_CONSULTATION) {
                ft.hide(patient);
                ft.hide(square);
                ft.hide(mine);
                ft.show(consultation);
            }
            ft.commit();
        }
    }

    private void setUpView() {
        initFragment();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_main_mypatient:
                        showFragment(TAG_PATIENT);
                        break;
                    case R.id.rb_main_consultquare:
                        showFragment(TAG_SQUARE);
                        break;
                    case R.id.rb_main_mine:
                        showFragment(TAG_MINE);
                        break;
                    case R.id.rb_main_consultation:
                        showFragment(TAG_CONSULTATION);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        EMchatCreat.saveSate(outState, isConflict, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        if (conflictDialog != null && !conflictDialog.dialog.isShowing()) {
            conflictDialog.show();
            return;
        }
        EaseUIHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            conflictDialog = new TipDialog(this);
            conflictDialog.setTitle(st);
            isConflict = true;
            conflictDialog.setMessage(getString(R.string.connect_conflict));
            conflictDialog.setPositive(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SQuser.getInstance().clearUserNameAndPwd();
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
            });
            conflictDialog.dialog.setCancelable(false);
            conflictDialog.show();
            isConflict = true;
        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        if (accountRemovedDialog != null && !accountRemovedDialog.dialog.isShowing()) {
            accountRemovedDialog.show();
            return;
        }
        EaseUIHelper.getInstance().logout(true, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            accountRemovedDialog = new TipDialog(this);
            accountRemovedDialog.setTitle(st5);
            accountRemovedDialog.setMessage(getString(R.string.ease_user_remove));
            accountRemovedDialog.setPositive(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    SQuser.getInstance().clearUserNameAndPwd();
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
            });
            accountRemovedDialog.dialog.setCancelable(false);
            accountRemovedDialog.show();
            isCurrentAccountRemoved = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUIWithMessage();
        EaseUIHelper.getInstance().pushActivity(this);
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(EMchatConstant.ACCOUNT_CONFLICT, false)) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(EMchatConstant.ACCOUNT_REMOVED, false)) {
            showAccountRemovedDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EaseUIHelper.getInstance().popActivity(this);
    }

    @Override
    public void onEvent(EMNotifierEvent event) {
        switch (event.getEvent()) {
            case EventNewMessage: // 普通消息
            {
                EMMessage message = (EMMessage) event.getData();
                // 提示新消息
                EaseUIHelper.getInstance().getNotifier().onNewMsg(message);
                refreshUIWithMessage();
                if (patient != null) {
                    patient.refresh();
                }
                break;
            }

            case EventOfflineMessage: {
                break;
            }

            case EventConversationListChanged: {
                break;
            }

            default:
                break;
        }
    }

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                int count = getUnreadMsgCountTotal();
                if (count > 0) {
                    unreadMsg.setText(String.valueOf(count));
                    unreadMsg.setVisibility(View.VISIBLE);
                } else {
                    unreadMsg.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
        for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    @Override
    public void onDelete() {
        refreshUIWithMessage();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                Toast.makeText(this, "再按一次退出抗癌卫士医生版", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
