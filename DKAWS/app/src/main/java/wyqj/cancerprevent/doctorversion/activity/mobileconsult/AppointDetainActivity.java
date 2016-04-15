package wyqj.cancerprevent.doctorversion.activity.mobileconsult;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.kaws.lib.bigimage.ViewBigImageActivity;
import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.utils.TimeUtil;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.common.SelectPhoto;
import wyqj.cancerprevent.doctorversion.activity.square.NewIllNoteActivity;
import wyqj.cancerprevent.doctorversion.adapter.PicGridAdapter;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.bean.ImageBean;
import wyqj.cancerprevent.doctorversion.bean.MedicalRecordBean;
import wyqj.cancerprevent.doctorversion.bean.OrderDetailBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.constant.ConsultationOrderStatus;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

/**
 * Created by xiaguangcheng on 16/3/23.
 */
public class AppointDetainActivity extends LoadBaseActivity implements View.OnClickListener {
    public int apply_code;
    /**
     * 患者姓名
     */
    private TextView text_patientname;
    /**
     * 患病种类
     */
    private TextView text_categary;
    /**
     * 患者年龄
     */

    private TextView text_age;
    /**
     * 患者性别
     */

    private TextView text_sex;
    /**
     * 确诊日期
     */

    private TextView text_sickdata;
    /**
     * 接听人姓名
     */

    private TextView text_phonename;
    /**
     * 与患者关系
     */

    private TextView text_relation;
    /**
     * 预约时间
     */

    private TextView text_appointtime;
    /**
     * 用户上传图片
     */
    private GridView gv_detail_pic;
    /**
     * 接诊
     */
    private Button accept;
    /**
     * 需完善
     */
    private Button supply;
    /**
     * 拒诊
     */
    private Button reject;
    /**
     * 病历本按钮
     */
    private Button btn_titlebar_submit;
    /**
     * 二次确认框
     */
    private TipDialog tipDialog;
    /**
     * 未上传图片的文字提示
     */
    private TextView notupload;
    private HttpService httpService;
    private ArrayList<String> listImages;
    private SQuser sQuser = SQuser.getInstance();
    private String orderId;
    private Intent intent;

    /**
     * 订单详情中的患者userkey
     */
    private String userkey;

    /**
     * 'kaws_pending' # 待付款
     * 'kaws_waiting'  # 待审核
     * 'kaws_confirmed' # 待通话
     * 'kaws_returned' # 需完善
     * 'kaws_finished'  # 已通话
     * 'kaws_over'  # 已完成
     * 'kaws_refunding'  # 退款中
     * 'kaws_refunded'  # 已退款
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpService = HttpUtils.getInstance().getHttpService(HttpService.class);
        if (getIntent() != null) {
            orderId = getIntent().getStringExtra(Constants.ORDER_ID);
            apply_code = getIntent().getIntExtra(Constants.APPLY_TO_APPOINTDETAIL, -1);
            if (TextUtils.isEmpty(orderId)) {
                throw new RuntimeException("orderId must not be null");
            }

            if (apply_code == -1) {
                throw new RuntimeException("apply_code must not be -1");
            }
        }
        initView();
        setUpView();

    }

    private void bindListener() {
        accept.setOnClickListener(this);
        supply.setOnClickListener(this);
        reject.setOnClickListener(this);
        /**病历本的点击事件*/
        btn_titlebar_submit.setVisibility(View.VISIBLE);
        btn_titlebar_submit.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    private void setUpView() {
        initData();
    }

    private void initData() {
        /**获得电话预约的详情*/
        httpService.getOrderDetails(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, orderId, sQuser.getUserInfo().doctorid, new CustomCallBack<OrderDetailBean>() {

            @Override
            public void onSuccess(OrderDetailBean orderDetailBean) {
                showContentView();
                listImages = new ArrayList<String>();
                if (orderDetailBean != null) {
                    text_phonename.setText(orderDetailBean.getUser_name());
                    text_appointtime.setText(TimeUtil.getTranslateTime(orderDetailBean.getCall_at()));
                    MedicalRecordBean medicalRecordBean = orderDetailBean.getMedical_record();
                    if (medicalRecordBean != null) {
                        text_relation.setText(medicalRecordBean.getRelationship());
                        text_patientname.setText(medicalRecordBean.getName());
                        text_age.setText(medicalRecordBean.getAge());
                        text_categary.setText(medicalRecordBean.getDiseased_state_name());
                        text_sex.setText(medicalRecordBean.getSex());
                        text_sickdata.setText(TimeUtil.getTranslateTime(medicalRecordBean.getConfirmed_date()));
                        userkey = medicalRecordBean.getUserkey();
                    }
                    List<ImageBean> list = orderDetailBean.getImages();
                    /**设置图片*/
                    if (list != null && list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            listImages.add(list.get(i).getUrl());
                        }
                        gv_detail_pic.setNumColumns(4);
                        PicGridAdapter picGridAdapter = new PicGridAdapter(AppointDetainActivity.this, listImages);
                        gv_detail_pic.setAdapter(picGridAdapter);
                        /**图片的点击事件*/
                        gv_detail_pic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("code", position);
                                bundle.putInt("selet", 2);
                                bundle.putStringArrayList("imageuri", listImages);
                                Intent intent = new Intent(AppointDetainActivity.this, ViewBigImageActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }

                    bindListener();

                }
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        setContentView(R.layout.activity_appointdetail);
        setTitle("预约详情");
        /**用户的个人信息*/
        text_age = (TextView) findViewById(R.id.text_age);
        text_appointtime = (TextView) findViewById(R.id.text_appointtime);
        text_categary = (TextView) findViewById(R.id.text_categary);
        text_patientname = (TextView) findViewById(R.id.text_patientname);
        text_phonename = (TextView) findViewById(R.id.text_phonename);
        text_relation = (TextView) findViewById(R.id.text_relation);
        text_sex = (TextView) findViewById(R.id.text_sex);
        text_sickdata = (TextView) findViewById(R.id.text_sickdata);
        /**用户上传的图片*/
        gv_detail_pic = (GridView) findViewById(R.id.gv_detail_pic);
        /**接诊*/
        accept = (Button) findViewById(R.id.accept);

        /**需完善*/
        supply = (Button) findViewById(R.id.supply);
        /**拒诊*/
        reject = (Button) findViewById(R.id.reject);
        /**病历本按钮*/
        btn_titlebar_submit = (Button) findViewById(R.id.btn_titlebar_submit);
        btn_titlebar_submit.setText("病历本");
        /**未上传图片提醒*/
        notupload = (TextView) findViewById(R.id.notupload);
        tipDialog = new TipDialog(AppointDetainActivity.this);

        if (apply_code == ConsultationOrderStatus.WAITING) {
            /**申请中进入订单详情页面*/

        } else if (apply_code == ConsultationOrderStatus.CONFIRMED) {
            /**待通话进入订单详情页面*/
            supply.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);
            accept.setText("联系客服");
        } else if (apply_code == ConsultationOrderStatus.FINISHED) {
            getView(R.id.ll_bottom).setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**接诊按钮点击事件*/
            case R.id.accept:
                if (apply_code == ConsultationOrderStatus.CONFIRMED) {
                    /**联系客服*/
                    Intent in = new Intent(this, SelectPhoto.class);
                    in.putExtra(Constants.ORDERDETAIL_TO_SELECTPHOTO, true);
                    in.putExtra(Constants.ORDER_ID, orderId);
                    startActivityForResult(in, Constants.APPOINTDETAIL_TO_SELECTPHOTO_REQUEST_CODE);
                } else {
                    tipDialog.setTitle("确认接诊");
                    tipDialog.show();
                    tipDialog.setNegative("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    tipDialog.setPositive("接诊", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            accept();

                        }
                    });
                }
                break;
            /**需完善按钮点击事件*/
            case R.id.supply:
                tipDialog.setTitle("退回给用户");
                tipDialog.show();
                tipDialog.setMessage("需要用户完善资料");
                tipDialog.setNegative("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                tipDialog.setPositive("退回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent = new Intent(AppointDetainActivity.this, NeedPerfectInformationActivity.class);
                        intent.putExtra(Constants.ORDER_ID, orderId);
                        startActivityForResult(intent, Constants.APPOINTDETAIL_TO_COMPLETE_REQUEST_CODE);
                        dialog.cancel();
                    }
                });
                break;
            /**拒诊按钮点击事件*/
            case R.id.reject:
                tipDialog.show();
                tipDialog.setTitle("拒绝接诊");
                tipDialog.setNegative("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                tipDialog.setPositive("拒诊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent = new Intent(AppointDetainActivity.this, RefuseDiagnoseActivity.class);
                        intent.putExtra(Constants.ORDER_ID, orderId);
                        startActivityForResult(intent, Constants.APPOINTDETAIL_TO_REJECT_REQUEST_CODE);
                        dialog.cancel();
                    }
                });
                break;
            /**病历本点击事件*/
            case R.id.btn_titlebar_submit:
                Intent intent = new Intent(AppointDetainActivity.this, NewIllNoteActivity.class);
                intent.putExtra(Constants.PATIENT_USERKEY, userkey);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.APPOINTDETAIL_TO_COMPLETE_REQUEST_CODE) {
            /**需完善提交成功*/
            finish();
        } else if (resultCode == RESULT_OK && requestCode == Constants.APPOINTDETAIL_TO_REJECT_REQUEST_CODE) {
            /**已拒诊提交成功*/
            finish();
        } else if (resultCode == RESULT_OK && requestCode == Constants.APPOINTDETAIL_TO_SELECTPHOTO_REQUEST_CODE) {
            /**给客服留言提交成功*/
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 接诊
     */
    private void accept() {
        startProgressDialog();
        httpService.postAcceptOrder(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, orderId, sQuser.getUserInfo().doctorid, new CustomCallBack<ErrorBean>() {

            @Override
            public void onSuccess(ErrorBean errorBean) {
                stopProgressDialog();
                ToastUtils.showToast(AppointDetainActivity.this, "接诊成功", 1500, 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);

            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }

        });
    }

}
