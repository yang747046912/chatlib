package wyqj.cancerprevent.doctorversion.fragment.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.bigimage.ViewBigImageActivity;
import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.common.base.LoadBaseFragment;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.fresco.Image;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.common.SelectPhoto;
import wyqj.cancerprevent.doctorversion.activity.common.UploadWorkPicActivity;
import wyqj.cancerprevent.doctorversion.activity.common.UploadWorkPicDoctorActivity;
import wyqj.cancerprevent.doctorversion.activity.mine.AboutUsActivity;
import wyqj.cancerprevent.doctorversion.activity.mine.AdviceActivity;
import wyqj.cancerprevent.doctorversion.activity.mine.CommonQuestionActivity;
import wyqj.cancerprevent.doctorversion.activity.mine.InviteFriendActivity;
import wyqj.cancerprevent.doctorversion.activity.mine.MyErweimaActivity;
import wyqj.cancerprevent.doctorversion.activity.mine.MyIncomeActivity;
import wyqj.cancerprevent.doctorversion.activity.mine.MyInfoActivity;
import wyqj.cancerprevent.doctorversion.activity.mine.NotificationCenterActivity;
import wyqj.cancerprevent.doctorversion.activity.mine.SettingActivity;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.constant.DoctorStatus;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.ImageLoadUtils;
import wyqj.cancerprevent.doctorversion.utils.SQuser;


public class Mine extends LoadBaseFragment {

    private static final String TAG = "Mine";
    private ImageView btnTitlebarBack;
    private ImageView tvMineNotificationRedPoint;
    private RelativeLayout rlMineNotificationCenter;
    private SimpleDraweeView avater;
    private TextView txtDoctorIconStatusMime;
    private TextView tvMineGrade;
    private TextView tvMineHospital;
    private TextView tvMineName;
    private TextView tvMineOffice;
    private TextView tvMineMymoney;
    /* 评价部分*/
    private TextView tvQuestionSatisfaction;
    private TextView tvQuestionDissatisfaction;
    private TextView tvConsultSatisfaction;
    private TextView tvConsultDissatisfaction;
    private String currentMoney;
    private int status;
    private RelativeLayout rlMineCheck;
    private TextView tvMineCheckInfo;
    private RelativeLayout rlMineAboutus;
    private RelativeLayout rlMineSetting;
    private RelativeLayout rlMineOption;
    private RelativeLayout rlMineQuestion;
    private RelativeLayout rlMineInvite;
    private RelativeLayout rlMineMyerweima;
    private RelativeLayout rlMineIncome;
    private RelativeLayout rlMineInfo;
    /*上传图片 选择图片后 不刷新页面  当上传完成后再刷新*/
    private boolean startUpload = false;

    @Override
    public int setContent() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        setTitle("我的");
        initView();
        setUpView();
        getMyInfo();
    }

    private void setUpView() {
        btnTitlebarBack.setVisibility(View.GONE);
        rlMineNotificationCenter.setVisibility(View.VISIBLE);
        rlMineAboutus.setOnClickListener(listener);
        rlMineSetting.setOnClickListener(listener);
        rlMineOption.setOnClickListener(listener);
        rlMineQuestion.setOnClickListener(listener);
        rlMineInvite.setOnClickListener(listener);
        rlMineMyerweima.setOnClickListener(listener);
        rlMineIncome.setOnClickListener(listener);
        rlMineInfo.setOnClickListener(listener);
        avater.setOnClickListener(listener);
        rlMineCheck.setOnClickListener(listener);
        rlMineNotificationCenter.setOnClickListener(listener);
    }

    private void initView() {
        btnTitlebarBack = getView(R.id.btn_titlebar_back);
        rlMineNotificationCenter = getView(R.id.rl_mine_notification_center);
        tvMineNotificationRedPoint = getView(R.id.tv_mine_notification_red_point);
        avater = getView(R.id.iv_mine_touxiang);
        txtDoctorIconStatusMime = getView(R.id.txt_doctor_icon_status_mine);
        tvMineGrade = getView(R.id.tv_mine_grade);
        tvMineHospital = getView(R.id.tv_mine_hospital);
        tvMineName = getView(R.id.tv_mine_name);
        tvMineOffice = getView(R.id.tv_mine_office);
        tvMineMymoney = getView(R.id.tv_mine_mymoney);
        rlMineCheck = getView(R.id.rl_mine_check);
        tvMineCheckInfo = getView(R.id.tv_mine_check_info);
        tvQuestionSatisfaction = getView(R.id.tv_question_satisfaction);
        tvQuestionDissatisfaction = getView(R.id.tv_question_dissatisfaction);
        tvConsultSatisfaction = getView(R.id.tv_consult_satisfaction);
        tvConsultDissatisfaction = getView(R.id.tv_consult_dissatisfaction);
        rlMineAboutus = getView(R.id.rl_mine_aboutus);
        rlMineSetting = getView(R.id.rl_mine_setting);
        rlMineOption = getView(R.id.rl_mine_option);
        rlMineQuestion = getView(R.id.rl_mine_question);
        rlMineInvite = getView(R.id.rl_mine_invite);
        rlMineMyerweima = getView(R.id.rl_mine_myerweima);
        rlMineIncome = getView(R.id.rl_mine_income);
        rlMineInfo = getView(R.id.rl_mine_info);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.rl_mine_aboutus:
                    //跳转到我们
                    startActivity(new Intent(getContext(), AboutUsActivity.class));
                    break;
                case R.id.rl_mine_setting:
                    //跳转到设置界面
                    startActivity(new Intent(getContext(), SettingActivity.class));
                    break;
                case R.id.rl_mine_option:
                    //跳转到意见反馈
                    startActivity(new Intent(getContext(), AdviceActivity.class));
                    break;
                case R.id.rl_mine_question:
                    //跳转到常见问题
                    startActivity(new Intent(getContext(), CommonQuestionActivity.class));
                    break;
                case R.id.rl_mine_invite:
                    //跳转到邀请好友
                    if (status == 2 || 5 == status) {//未审核和审核失败
                        hintDialog();
                    } else if ("3".equals(status)) {//正在审核
                        TipDialog.hintDiaglog(getActivity(), "您的身份信息正在审核中,通过后才可以进行操作,请耐心等待");
                    } else {//认证医生
                        startActivity(new Intent(getContext(), InviteFriendActivity.class));
                    }
                    break;
                case R.id.rl_mine_myerweima:
                    //跳转到我的二维码
                    if (status == 2 || 5 == status) {//未审核和审核失败
                        hintDialog();
                    } else if ("3".equals(status)) {//正在审核
                        TipDialog.hintDiaglog(getActivity(), "您的身份信息正在审核中,通过后才可以进行操作,请耐心等待");
                    } else {//认证医生
                        startActivity(new Intent(getContext(), MyErweimaActivity.class));
                    }
                    break;
                case R.id.rl_mine_income:
                    //跳转到我的收入
                    if (status == 2 || 5 == status) {//未审核和审核失败
                        hintDialog();
                    } else if ("3".equals(status)) {//正在审核
                        TipDialog.hintDiaglog(getActivity(), "您的身份信息正在审核中,通过后才可以进行操作,请耐心等待");
                    } else {//认证医生
                        startActivity(new Intent(getContext(), MyIncomeActivity.class));
                    }
                    break;
                case R.id.rl_mine_info:
                    //跳转到我的资料
                    startActivity(new Intent(getContext(), MyInfoActivity.class));
                    break;
                case R.id.iv_mine_touxiang:
                    // 修改或查看头像
                    UserBean.Doctor doctor = SQuser.getInstance().getUserInfo().info;
                    if (doctor != null) {
                        if (!TextUtils.isEmpty(doctor.applying_avatar_url)) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("selet", 1);
                            bundle.putInt("code", 0);
                            bundle.putBoolean("isLocal", false);
                            ArrayList<String> imageuri = new ArrayList<>();
                            imageuri.add(doctor.applying_avatar_url);
                            bundle.putStringArrayList("imageuri", imageuri);
                            Intent intent = new Intent(getContext(), ViewBigImageActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getContext(), SelectPhoto.class);
                            intent.putExtra(Constants.URL_OF_PHOTO, doctor.avatarUrl);
                            startActivityForResult(intent, Constants.CODE_SELECT_PHOTO);
                        }
                    }
                    break;
                case R.id.rl_mine_check://审核认证
                    getToCheck();
                    break;
                case R.id.rl_mine_notification_center://消息中心
                    startActivity(new Intent(getContext(), NotificationCenterActivity.class));
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 跳转的认证页面
     */
    private void getToCheck() {
        UserBean.Doctor d = SQuser.getInstance().getUserInfo().info;
        if (d != null) {
            switch (status) {
                case 2://aaa
                    if ("博士研究生".equals(d.degree.name)) {
                        Intent picIntent = new Intent(getActivity(), UploadWorkPicDoctorActivity.class);
                        startActivity(picIntent);
                    } else {
                        Intent picIntent = new Intent(getActivity(), UploadWorkPicActivity.class);
                        startActivity(picIntent);
                    }
                    break;
                case 5:
                    if ("博士研究生".equals(d.degree.name)) {
                        Intent picIntent = new Intent(getActivity(), UploadWorkPicDoctorActivity.class);
                        startActivity(picIntent);
                    } else {
                        Intent picIntent = new Intent(getActivity(), UploadWorkPicActivity.class);
                        startActivity(picIntent);
                    }
                    break;
            }
        }
    }

    private void getMyInfo() {
        HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.getMyInfo(HttpHead.getHeader("GET"),
                sQuser.getUserInfo().token,
                sQuser.getUserInfo().doctorid,
                new CustomCallBack<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        DebugUtil.debug("---userBean---->", userBean.toString());
                        SQuser.getInstance().saveUserInfo(userBean.info);
                        processData(SQuser.getInstance().getUserInfo());
                    }

                    @Override
                    public void onFailure() {
                    }
                });
    }

    private void processData(UserBean doctorInfoEntity) {
        UserBean.Doctor doctor = doctorInfoEntity.info;
        if (doctor == null) {
            return;
        }
        if (!TextUtils.isEmpty(doctor.applying_avatar_url)) {
            Image.displayRound(avater, 10, Uri.parse(doctor.applying_avatar_url));
            txtDoctorIconStatusMime.setVisibility(View.VISIBLE);
        } else {
            Image.displayRound(avater, 10, Uri.parse(doctor.avatarUrl));
            txtDoctorIconStatusMime.setVisibility(View.GONE);
        }
        // 通知中心红点根据未读消息显示
        if (doctor.unreadCount > 0) {
            tvMineNotificationRedPoint.setVisibility(View.VISIBLE);
        }
        tvMineName.setText(doctor.name);
        tvMineGrade.setText(" - " + doctor.degree.name);
        tvMineHospital.setText(doctor.hospitalName);
        tvMineOffice.setText(doctor.department);
        formatMoney(doctor);//格式化金额
        tvMineMymoney.setText(currentMoney);
        // 图文解答评价
        tvQuestionSatisfaction.setText(doctor.questionRate.satisfaction + "人");
        tvQuestionDissatisfaction.setText(doctor.questionRate.dissatisfaction + "人");
        // 电话预约评价
        tvConsultSatisfaction.setText(doctor.phoneConsultingRating.satisfaction + "人");
        tvConsultDissatisfaction.setText(doctor.phoneConsultingRating.dissatisfaction + "人");
        status = doctorInfoEntity.status;
        DebugUtil.debug("医生的状态：" + status);

        switch (status) {
            case DoctorStatus.JUST_REGISTERED:
                rlMineCheck.setVisibility(View.VISIBLE);
                tvMineCheckInfo.setText("未提交审核");
                break;
            case DoctorStatus.PENDING:
                rlMineCheck.setVisibility(View.VISIBLE);
                tvMineCheckInfo.setText("正在审核中……");
                tvMineCheckInfo.setCompoundDrawables(null, null, null, null);
                tvMineCheckInfo.setTextColor(getResources().getColor(R.color.cutline));
                break;
            case DoctorStatus.CONFIRMED:
                rlMineCheck.setVisibility(View.GONE);
                break;
            case DoctorStatus.REGISTER_FAILED:
                rlMineCheck.setVisibility(View.VISIBLE);
                tvMineCheckInfo.setText("审核失败,请重新审核");
                break;
            default:
                break;
        }
    }

    /** 我的收入小数位为0则不显示 */
    private void formatMoney(UserBean.Doctor doctor) {
        if (String.valueOf(doctor.currentMoney).contains(".0")) {
            currentMoney = String.valueOf(doctor.currentMoney).replace(".0", "");
        } else {
            currentMoney = String.valueOf(doctor.currentMoney);
        }
    }

    private void hintDialog() {
        TipDialog tipDialog = new TipDialog(getContext());
        tipDialog.setTitle("请先认证");
        tipDialog.setMessage("请先上传您的身份认证信息,审核通过后才可以进一步操作");
        tipDialog.setNegative("上传认证", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getToCheck();

            }
        });
        tipDialog.setPositive("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        tipDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) {
            return;
        }
        if (requestCode == Constants.CODE_SELECT_PHOTO && data != null) {
            String url = data.getStringExtra(Constants.SELECT_PHOTO);
            if (!TextUtils.isEmpty(url)) {
                startUpload = true;
                ImageLoadUtils.uploadFile((BaseActivity) getActivity(), url, new ImageLoadUtils.ImplUploadListener() {
                    @Override
                    public void onSuccess(String imageID) {
                        UpLoadPic(imageID);
                    }

                    @Override
                    public void onFailure() {
                        ToastUtils.showToast(getActivity(), "文件上传失败", 2000, 3);
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!startUpload) {
            getMyInfo();
        } else {
            startUpload = false;
        }
        DebugUtil.debug(TAG, "onResume");
    }

    /** 上传照片*/
    private void UpLoadPic(String imageid) {

        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.modifyAvatar(HttpHead.getHeader("PUT"), sQuser.getUserInfo().token, "" + sQuser.getUserInfo().doctorid, imageid, new CustomCallBack<ErrorBean>() {

            @Override
            public void onSuccess(ErrorBean errorBean) {
                ToastUtils.showToast(getActivity(), "头像已申请审核", 2000, 1);
                getMyInfo();
                stopProgressDialog();
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        DebugUtil.debug(TAG, "onHiddenChanged" + hidden);
        if (!hidden) {
            getMyInfo();
        }
    }
}
