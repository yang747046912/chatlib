package wyqj.cancerprevent.doctorversion.activity.mobileconsult;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.consultation.SelectPerfectReasonAdapter;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

/**
 * Created by xiaguangcheng on 16/3/24.
 */
public class CustomerServiceMessageActivity extends LoadBaseActivity implements View.OnClickListener{
    /**选择给客服留言的条目*/
    private GridView gv_select_reason;

    /**留言选择的数据*/
    private ArrayList<String> listRaw;

    private SelectPerfectReasonAdapter selectPerfectReasonAdapter;

    /**服务类型*/
    private TextView tip_choice;
    private TextView detail_tip;
    private EditText edit_content;
    /**提交给客服的留言*/
    private Button btn_commit;
    private ArrayList<String> listSelected;
    private String cacheContent="";

    private SQuser sQuser = SQuser.getInstance();
    private HttpService httpService;

    private String orderId;
    private int currentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpService= HttpUtils.getInstance().getHttpService(HttpService.class);
        if(getIntent()!=null){
            orderId=getIntent().getStringExtra(Constants.ORDER_ID);
        }
        initView();
        setUpView();
    }

    private void setUpView() {
        listRaw=new ArrayList<>();
        listSelected=new ArrayList<>();
        listRaw.add("取消预约");
        listRaw.add("更改时间");
        listRaw.add("补全资料");
        listRaw.add("其他");
        selectPerfectReasonAdapter=new SelectPerfectReasonAdapter(CustomerServiceMessageActivity.this,true,gv_select_reason);
        gv_select_reason.setAdapter(selectPerfectReasonAdapter);
        selectPerfectReasonAdapter.setRawList(listRaw);
        selectPerfectReasonAdapter.setSelectReaseonListener(new SelectPerfectReasonAdapter.SelectReaseonListener() {
            @Override
            public void selectReason(int position, boolean isChecked) {
                /**当医生点选其他因素时,显示输入框*/
                if(isChecked){
                    listSelected.clear();
                    listSelected.add(listRaw.get(position));
                    currentId=position+1;
                    if(position==3){
                        /**如果有缓存的信息,就显示缓存信息*/
//                    if(!TextUtils.isEmpty(content)){
                        edit_content.setText(cacheContent);
//                    }
                        detail_tip.setVisibility(View.VISIBLE);
                        edit_content.setVisibility(View.VISIBLE);
                        edit_content.requestFocus();
                        edit_content.setFocusable(true);
                    }else {
                        /**当隐藏输入框时,缓存输入信息*/
                        if(!TextUtils.isEmpty(edit_content.getText().toString())){
                            cacheContent=edit_content.getText().toString();
                        }
                        detail_tip.setVisibility(View.INVISIBLE);
                        edit_content.setVisibility(View.INVISIBLE);
                    }
                }else{
//                    /**当医生取消其他因素时,隐藏输入框*/
//                    listSelected.remove(listRaw.get(position));
//                    if(position==3){
//                        /**当隐藏输入框时,缓存输入信息*/
//                        if(!TextUtils.isEmpty(edit_content.getText().toString())){
//                            cacheContent=edit_content.getText().toString();
//                        }
//                        detail_tip.setVisibility(View.INVISIBLE);
//                        edit_content.setVisibility(View.INVISIBLE);
//                    }
                }
            }
        });

    }

    private void initView() {
        setContentView(R.layout.activity_needperfectinformation);
        setTitle("给客服留言");
        gv_select_reason= (GridView) findViewById(R.id.gv_select_reason);
        tip_choice= (TextView) findViewById(R.id.tip_chioce);
        tip_choice.setText("服务类型");
        /**提交按钮*/
        btn_commit= (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        detail_tip= (TextView) findViewById(R.id.detail_tip);
        /**医生输入的内容*/
        edit_content= (EditText) findViewById(R.id.edit_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:

                String content=edit_content.getText().toString();
                if(TextUtils.isEmpty(content)){
                    content="";
                }
                if(currentId==-1){
                    ToastUtils.showToast(this,"请至少选择一个选项,若无该选项,请选择其他因素,手动输入",1000,3);
                    return;
                }
                startProgressDialog();
                httpService.postMessageForOrder(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, orderId, sQuser.getUserInfo().doctorid, currentId, content, new CustomCallBack<ErrorBean>() {


                    @Override
                    public void onSuccess(ErrorBean errorBean) {
                        stopProgressDialog();
                        ToastUtils.showToast(CustomerServiceMessageActivity.this,"提交成功",1000,1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setResult(RESULT_OK);
                                finish();

                            }
                        },1500);
                    }

                    @Override
                    public void onFailure() {
                        stopProgressDialog();
                    }


                });
                break;
        }
    }
}
