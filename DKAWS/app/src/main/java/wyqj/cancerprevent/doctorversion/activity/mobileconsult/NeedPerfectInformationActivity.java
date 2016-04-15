package wyqj.cancerprevent.doctorversion.activity.mobileconsult;

import android.content.Intent;
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
public class NeedPerfectInformationActivity extends LoadBaseActivity implements View.OnClickListener {
    /**需完善页面的选择框*/
    private GridView gv_select_reason;

    /**提供的六个需要完善的信息*/
    private ArrayList<String> listRaw;

    /**用户选中的信息*/
    private ArrayList<String> listSelected;
    private SelectPerfectReasonAdapter selectPerfectReasonAdapter;

    /**详细说明*/
    private EditText edit_content;

    /**提交按钮*/
    private Button btn_commit;
    /**缓存当前页面当输入框详细说明当内容,如果用户离开此页,缓存当信息则消失*/
    private String content="";

    /**详细说明当文字*/
    private TextView detail_tip;

    /**需要完善的订单号*/
    private String orderId;

    private SQuser sQuser = SQuser.getInstance();
    private HttpService httpService;
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
        listRaw.add("血液检查结果");
        listRaw.add("印象学结果");
        listRaw.add("病理结果");
        listRaw.add("出院诊断");
        listRaw.add("用药情况");
        listRaw.add("其他因素");
        selectPerfectReasonAdapter=new SelectPerfectReasonAdapter(NeedPerfectInformationActivity.this,false,gv_select_reason);
        gv_select_reason.setAdapter(selectPerfectReasonAdapter);

        selectPerfectReasonAdapter.setRawList(listRaw);
        btn_commit.setOnClickListener(this);
        /**通过接口返回的数据,获得用户选中的条目*/
        selectPerfectReasonAdapter.setSelectReaseonListener(new SelectPerfectReasonAdapter.SelectReaseonListener() {
            @Override
            public void selectReason(int position, boolean isChecked) {
                /**当医生点选其他因素时,显示输入框*/
                if(isChecked){
                    listSelected.add(listRaw.get(position));
                    if(position==5){
                        /**如果有缓存的信息,就显示缓存信息*/
                        edit_content.setText(content);
                        detail_tip.setVisibility(View.VISIBLE);
                        edit_content.setVisibility(View.VISIBLE);
                        edit_content.requestFocus();
                        edit_content.setFocusable(true);
                    }
                }else{
                    listSelected.remove(listRaw.get(position));
                    /**当医生取消其他因素时,隐藏输入框*/
                    if(position==5){
                        /**当隐藏输入框时,缓存输入信息*/
                        if(!TextUtils.isEmpty(edit_content.getText().toString())){
                            content=edit_content.getText().toString();
                        }
                        detail_tip.setVisibility(View.INVISIBLE);
                        edit_content.setVisibility(View.INVISIBLE);
                    }
                }


            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_needperfectinformation);
        setTitle("需完善信息");
        /**选择需要完善的信息*/
        gv_select_reason= getView(R.id.gv_select_reason);
        /**提交按钮*/
        btn_commit= (Button) findViewById(R.id.btn_commit);
        /**详细说明输入框*/
        edit_content= (EditText) findViewById(R.id.edit_content);
        detail_tip= (TextView) findViewById(R.id.detail_tip);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit:
                /**获取最新的输入框内容*/
                String editContent =edit_content.getText().toString();
                if(TextUtils.isEmpty(editContent)){
                    editContent="";
                }
                if(listSelected.size()==0){
                    ToastUtils.showToast(this,"请选择需要完善的信息,若没有该选项,选择其他信息可以输入更多内容",2000,3);
                    return;
                }
                startProgressDialog();
                httpService.postReturnOrder(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, orderId, sQuser.getUserInfo().doctorid, editContent, listSelected, new CustomCallBack<ErrorBean>() {


                    @Override
                    public void onSuccess(ErrorBean errorBean) {
                        stopProgressDialog();
                        ToastUtils.showToast(NeedPerfectInformationActivity.this,"提交成功",1500,1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent =new Intent();
                                setResult(RESULT_OK,intent);
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
