package wyqj.cancerprevent.doctorversion.fragment.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.util.NetUtils;
import com.kaws.chat.lib.ui.EaseConversationListFragment;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.BlankView;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.chat.ChatActivity;
import wyqj.cancerprevent.doctorversion.activity.chat.EMchatConstant;
import wyqj.cancerprevent.doctorversion.interfaces.IdeleteConversation;


public class PatientConsultRecord extends EaseConversationListFragment {
    private TextView errorText;
    private IdeleteConversation ideleteConversation;

    private BlankView blankView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_consult_record, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideTitleBar();
        getView().findViewById(R.id.search_bar).setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        super.initView();
        View errorView = View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        blankView = (BlankView) getView().findViewById(R.id.blank_view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof IdeleteConversation) {
            ideleteConversation = (IdeleteConversation) activity;
        }
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // 注册上下文菜单
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.getUserName();
                if (username.equals(EMChatManager.getInstance().getCurrentUser()))
                    ToastUtils.showToast(getContext(), getString(R.string.Cant_chat_with_yourself), 2000, 3);
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(EMchatConstant.EXTRA_CHAT_TYPE, EMchatConstant.CHATTYPE_CHATROOM);
                        } else {
                            intent.putExtra(EMchatConstant.EXTRA_CHAT_TYPE, EMchatConstant.CHATTYPE_GROUP);
                        }
                    }
                    // it's single chat
                    intent.putExtra(EMchatConstant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean handled = false;
        boolean deleteMessage = false;
        /*if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
            handled = true;
        } else*/
        if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = true;
            handled = true;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        // 删除此会话
        EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup(), deleteMessage);
        refresh();
        if (ideleteConversation != null) {
            ideleteConversation.onDelete();
        }
        // 更新消息未读数
        //  ((MainActivity) getActivity()).updateUnreadLabel();

        return handled ? true : super.onContextItemSelected(item);
    }

    @Override
    protected void onloadConversationList(int conversationSize) {
        if (conversationSize == 0) {
            if (blankView.getVisibility() != View.VISIBLE) {
                blankView.setVisibility(View.VISIBLE);
            }
        } else {
            if (blankView.getVisibility() != View.GONE) {
                blankView.setVisibility(View.GONE);
            }
            blankView.setVisibility(View.GONE);
        }
    }
}
