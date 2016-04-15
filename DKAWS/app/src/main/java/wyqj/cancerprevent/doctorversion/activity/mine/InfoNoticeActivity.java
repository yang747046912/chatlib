package wyqj.cancerprevent.doctorversion.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.CommonUtil;
import com.kaws.lib.common.utils.SharedPreferencesUtil;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.bean.NotificationStateBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class InfoNoticeActivity extends LoadBaseActivity {
    private boolean sixin;
    private boolean patientapply;
    private boolean quesAndAnswer;
    private ImageView ivNoticeSixin;
    private ImageView ivNoticePatientquestion;
    private ImageView ivNoticePatientapply;

    /**私信通知  开启 -> 1; 关闭 -> 0*/
    private  int has_message_notice=2;
    /**申请通知*/
    private int has_apply_notice=2;

    /**追加通知*/
    private int has_user_comment_notice=2;

    private SQuser sQuser = SQuser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_notice);
        showContentView();
        setTitle("消息通知");
        initView();
        setUpView();
    }

    private void setUpView() {
        sixin = SharedPreferencesUtil.getSiXinConfig(this);
        patientapply = SharedPreferencesUtil.getPatientNoticeConfig(this);
        quesAndAnswer = SharedPreferencesUtil.getQuesAnswerConfig(this);
        ivNoticePatientapply.setSelected(patientapply);
        ivNoticePatientquestion.setSelected(quesAndAnswer);
        ivNoticeSixin.setSelected(sixin);

        ivNoticePatientapply.setOnClickListener(listener);
        ivNoticePatientquestion.setOnClickListener(listener);
        ivNoticeSixin.setOnClickListener(listener);
        /**获取状态*/
        getStatus();


    }

    /**
     * 初始化三个按钮的状态
     * @param notificationStateBean 三个按钮状态存储对象
     */
    private void initStatus(NotificationStateBean notificationStateBean){
        if (notificationStateBean != null) {
            /**设置患者申请*/
            ivNoticePatientapply.setSelected(notificationStateBean.isHas_apply_notice());
            /**设置私信通知*/
            ivNoticeSixin.setSelected(notificationStateBean.isHas_message_notice());
            /**设置追加评论*/
            ivNoticePatientquestion.setSelected(notificationStateBean.isHas_user_comment_notice());
            if (notificationStateBean.isHas_apply_notice()) {
                has_apply_notice = 1;
            } else {
                has_apply_notice = 0;
            }

            if (notificationStateBean.isHas_message_notice()) {
                has_message_notice = 1;
            } else {
                has_message_notice = 0;
            }

            if (notificationStateBean.isHas_user_comment_notice()) {
                has_user_comment_notice = 1;
            } else {
                has_user_comment_notice = 0;
            }
        }
    }
    /**
     * 设置通知状态
     * @param has_apply_notice 0 close,1 open
     * @param has_message_notice 0 close,1 open
     * @param has_user_comment_notice 0 close,1 open
     */
    private void setStatus(int has_apply_notice,int has_message_notice,int has_user_comment_notice, final boolean falg, final String tips){
        HttpUtils.getInstance().getHttpService(HttpService.class).postNotificationSetting(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid,has_message_notice,has_apply_notice,has_user_comment_notice, new CustomCallBack<NotificationStateBean>() {
            @Override
            public void onSuccess(NotificationStateBean notificationStateBean) {
                stopProgressDialog();
                initStatus(notificationStateBean);
                if(falg){
                    ToastUtils.showToast(InfoNoticeActivity.this,tips, 2000, 3);

                }
            }

            @Override
            public void onFailure() {

            }
        });
    }
    private void initView() {
        ivNoticePatientapply = (ImageView) findViewById(R.id.iv_notice_patientapply);
        ivNoticePatientquestion = (ImageView) findViewById(R.id.iv_notice_patientquestion);
        ivNoticeSixin = (ImageView) findViewById(R.id.iv_notice_sixin);
    }

    /**
     * 获得三个按钮的状态
     */
    private void getStatus(){
        startProgressDialog();
        HttpUtils.getInstance().getHttpService(HttpService.class).getNotificationSettingsStatus(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, new CustomCallBack<NotificationStateBean>() {
            @Override
            public void onSuccess(NotificationStateBean notificationStateBean) {
                stopProgressDialog();
                initStatus(notificationStateBean);
            }

            @Override
            public void onFailure() {

            }
        });
    }
    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                /**申请提醒*/
                case R.id.iv_notice_patientapply:
                    if(has_apply_notice==0){
                        //关闭状态被点击,将被打开
                        startProgressDialog();
                        has_apply_notice=1;
                        setStatus(has_apply_notice,has_message_notice,has_user_comment_notice,false,"");
                    }else if(has_apply_notice==1){
                        //打开状态被点击,将关闭
                        startProgressDialog();
                        has_apply_notice=0;
                        setStatus(has_apply_notice, has_message_notice, has_user_comment_notice, true, "关闭后您将无法收到患者的申请通知");
                    }else{
                        //获取当前的状态,如果是打开,就关闭;如果是关闭,就打开
                        if(CommonUtil.isNetWorkConnected(InfoNoticeActivity.this)){
                            /**如果此时有网络,就再次请求最新状态*/
                            getStatus();
                        }else{
                            /**无网的情况下,可以自己玩*/
                            ivNoticePatientapply.setSelected(!ivNoticePatientapply.isSelected());
                        }
                    }
                    break;
                /**追加提醒*/
                case R.id.iv_notice_patientquestion:
                    if(has_user_comment_notice==0){
                        //关闭状态-->打开
                        has_user_comment_notice=1;
                        startProgressDialog();
                        setStatus(has_apply_notice,has_message_notice,has_user_comment_notice,false,"");
                    }else if(has_user_comment_notice==1){
                        //打开状态-->关闭
                        has_user_comment_notice=0;
                        startProgressDialog();
                        setStatus(has_apply_notice, has_message_notice, has_user_comment_notice, true, "关闭后您将无法收到患者向您追问的消息通知");

                    }else {
                        //如果是关闭则打开,如果是打开,则关闭
                        //获取当前的状态,如果是打开,就关闭;如果是关闭,就打开
                        if(CommonUtil.isNetWorkConnected(InfoNoticeActivity.this)){
                            /**如果此时有网络,就再次请求最新状态*/
                            getStatus();
                        }else{
                            /**无网的情况下,可以自己玩*/
                            ivNoticePatientquestion.setSelected(!ivNoticePatientapply.isSelected());
                        }
                    }
                    break;
                /**私信提醒*/
                case R.id.iv_notice_sixin:
                    if(has_message_notice==0){
                        //close-->open
                        has_message_notice=1;
                        setStatus(has_apply_notice,has_message_notice,has_user_comment_notice,false,"");

                    }else if(has_message_notice==1){
                        //open-->close
                        has_message_notice=0;
                        setStatus(has_apply_notice,has_message_notice,has_user_comment_notice,true,"关闭后您将无法收到私信的消息通知");

                    }else{
                        // if close,then open;if open, then close
                        //获取当前的状态,如果是打开,就关闭;如果是关闭,就打开
                        if(CommonUtil.isNetWorkConnected(InfoNoticeActivity.this)){
                            /**如果此时有网络,就再次请求最新状态*/
                            getStatus();
                        }else{
                            /**无网的情况下,可以自己玩*/
                            ivNoticeSixin.setSelected(!ivNoticePatientapply.isSelected());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

}
