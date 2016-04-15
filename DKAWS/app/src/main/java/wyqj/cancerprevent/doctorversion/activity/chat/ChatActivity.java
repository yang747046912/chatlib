package wyqj.cancerprevent.doctorversion.activity.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.square.NewIllNoteActivity;
import wyqj.cancerprevent.doctorversion.bean.PatientBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;


@SuppressLint("NewApi")
public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private String tag = "KChatActivity";
    private ChatFragment chatFragment;



    /**
     * 目标用户的key
     */
    private String toUserKey = "";

    private TextView tvTitlebarTitle;
    /*返回键*/
    private ImageView btnTitlebarBack;
    private TextView tvTitlebarRighttitle;

    private TextView tvChatSexAgeArea;
    private TextView tvChatDiseaseTime;
    private LinearLayout headChat;

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toUserKey.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        setContentView(R.layout.activity_chat1);
        tvTitlebarTitle = (TextView) findViewById(R.id.tv_titlebar_title);
        btnTitlebarBack = (ImageView) findViewById(R.id.btn_titlebar_back);
        tvTitlebarRighttitle = (TextView) findViewById(R.id.tv_titlebar_righttitle);
        View cutline = findViewById(R.id.view_titlebar_topline);
        headChat = (LinearLayout) findViewById(R.id.ll_header_chat);
        headChat.setVisibility(View.GONE);
        cutline.setVisibility(View.GONE);
        tvTitlebarRighttitle.setText("病历本");
        tvTitlebarRighttitle.setVisibility(View.GONE);
        tvChatSexAgeArea = (TextView) findViewById(R.id.tv_chat_sex_age_area);
        tvChatDiseaseTime = (TextView) findViewById(R.id.tv_chat_disease_time);
        bindLinster();
    }

    private void bindLinster() {
        btnTitlebarBack.setOnClickListener(this);
        tvTitlebarRighttitle.setOnClickListener(this);
    }

    protected void initData() {
        getIntentInfo();
        getPatientInfo();

        chatFragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("userId", toUserKey);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, chatFragment).commit();
    }

    private void getPatientInfo() {
        SQuser sQuse = SQuser.getInstance();
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.getPatientInfo(HttpHead.getHeader("GET"), sQuse.getUserInfo().token, toUserKey, "simple_medical", new CustomCallBack<PatientBean>() {
            @Override
            public void onSuccess(PatientBean patientEntity) {
                String sexAgeArea = patientEntity.sex + " | " + patientEntity.age + " | " + patientEntity.area;
                tvChatSexAgeArea.setText(sexAgeArea);
                tvTitlebarTitle.setText(patientEntity.username);
                tvTitlebarTitle.setVisibility(View.VISIBLE);
                if (patientEntity.diseases != null) {
                    String diseasename = "";
                    if (patientEntity.diseases.size() > 0) {
                        diseasename = patientEntity.diseases.get(0).name;
                    }
                    String diseaseAndTime = "患病种类：" + diseasename + " | " + "确诊日期：" + patientEntity.sickDate;
                    tvChatDiseaseTime.setText(diseaseAndTime);
                }
                boolean isCompletedRecord = patientEntity.isCompletedRecord;
                if (isCompletedRecord) {
                    tvTitlebarRighttitle.setVisibility(View.VISIBLE);
                }
                headChat.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private void getIntentInfo() {
        if (getIntent() != null) {
            toUserKey = getIntent().getStringExtra("userId");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_righttitle:
                //跳转到病历本
                Intent intent = new Intent(ChatActivity.this, NewIllNoteActivity.class);
                intent.putExtra(Constants.PATIENT_USERKEY, toUserKey);
                startActivity(intent);
                break;
        }
    }
}
