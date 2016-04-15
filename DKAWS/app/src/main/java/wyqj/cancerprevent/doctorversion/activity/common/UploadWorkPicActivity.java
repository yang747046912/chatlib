package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.bigimage.ViewBigImageActivity;
import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.fresco.Image;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.MainActivity;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.ImageLoadUtils;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class UploadWorkPicActivity extends LoadBaseActivity {

    private Button btnUploadpicUpload;
    private SimpleDraweeView ivUploadpicAddpic;
    private TextView tvUploadpicJump;
    private ImageView ivUploadpicDeletepic;
    private ImageView btnTitlebarBack;
    private String imageFile;
    private boolean ismodify = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_work_pic);
        showContentView();
        setTitle("手持工牌照片");
        initView();
        setUpView();
    }

    private void setUpView() {

        if (getIntent() != null) {
            ismodify = getIntent().getBooleanExtra(Constants.DOCTOR_ISMODIFY, true);
        }
        if (!ismodify) {//只有注册时候才能跳过
            tvUploadpicJump.setVisibility(View.VISIBLE);
        }
        Image.displayImage(ivUploadpicAddpic, Uri.parse("res:///" + R.drawable.add_uploadpic_student), R.drawable.add_uploadpic_student);
        btnUploadpicUpload.setOnClickListener(l);
        ivUploadpicAddpic.setOnClickListener(l);
        ivUploadpicDeletepic.setOnClickListener(l);
        tvUploadpicJump.setOnClickListener(l);
        btnTitlebarBack.setOnClickListener(l);
    }

    private void initView() {
        btnUploadpicUpload = (Button) findViewById(R.id.btn_uploadpic_upload);
        ivUploadpicAddpic = (SimpleDraweeView) findViewById(R.id.iv_uploadpic_addpic);
        tvUploadpicJump = (TextView) findViewById(R.id.tv_uploadpic_jump);
        ivUploadpicDeletepic = (ImageView) findViewById(R.id.iv_uploadpic_deletepic);
        btnTitlebarBack = (ImageView) findViewById(R.id.btn_titlebar_back);
    }

    private PerfectClickListener l = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {

            switch (view.getId()) {
                case R.id.btn_uploadpic_upload://选择完照片之后文字就 变为上传了
                    if (TextUtils.isEmpty(imageFile)) {
                        //其他验证
                        openActivity(OtherCheckWayActivity.class);
                    } else {
                        ImageLoadUtils.uploadFile(UploadWorkPicActivity.this, imageFile, new ImageLoadUtils.ImplUploadListener() {
                            @Override
                            public void onSuccess(String imageID) {
                                UpLoadPic(imageID);
                            }

                            @Override
                            public void onFailure() {
                                ToastUtils.showToast(UploadWorkPicActivity.this, "文件上传失败", 2000, 3);
                            }
                        });
                    }
                    break;
                case R.id.iv_uploadpic_addpic:
                    if (!TextUtils.isEmpty(imageFile)) {
                        //查看大图
                        Bundle bundle = new Bundle();
                        bundle.putInt("code", 0);
                        bundle.putInt("selet", 1);
                        bundle.putBoolean("isLocal", true);
                        ArrayList<String> list = new ArrayList<>();
                        list.add(imageFile);
                        bundle.putStringArrayList("imageuri", list);
                        Intent intent = new Intent(UploadWorkPicActivity.this, ViewBigImageActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Intent in = new Intent(UploadWorkPicActivity.this, SelectPhoto.class);
                        in.putExtra(Constants.IS_NEED_CROP, false);
                        startActivityForResult(in, Constants.CODE_SELECT_PHOTO);
                    }
                    break;
                case R.id.btn_titlebar_back:
                    if (ismodify) {
                        finish();
                    } else {
                        openActivity(RegisterSuccessActivity.class);
                    }
                    break;
                case R.id.tv_uploadpic_jump:
                    openActivity(MainActivity.class);
                    break;
                case R.id.iv_uploadpic_deletepic:
                    imageFile = "";
                    btnUploadpicUpload.setText("其他审核方式");
                    btnUploadpicUpload.setTextColor(getResources().getColor(R.color.but_bg));
                    btnUploadpicUpload.setSelected(false);
                    ivUploadpicDeletepic.setVisibility(View.GONE);
                    Image.displayImage(ivUploadpicAddpic, Uri.parse("res:///" + R.drawable.add_uploadpic_student), R.drawable.add_uploadpic_student);
                    break;
            }
        }
    };


    private void UpLoadPic(String imageid) {
        HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.upLoadPic(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, imageid, null, null, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {
                stopProgressDialog();
                SQuser.getInstance().getUserInfo().status = 3;
                Intent intent = new Intent(UploadWorkPicActivity.this, UploadSuccessActivity.class);
                if (ismodify) {
                    intent.putExtra(Constants.DOCTOR_ISMODIFY, true);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.CODE_SELECT_PHOTO && data != null) {
            String url = data.getStringExtra(Constants.SELECT_PHOTO);
            if (!TextUtils.isEmpty(url)) {
                imageFile = url;
                btnUploadpicUpload.setText("上传");
                ivUploadpicDeletepic.setVisibility(View.VISIBLE);
                Image.displayImage(ivUploadpicAddpic, Uri.parse("file://" + imageFile), R.drawable.add_uploadpic_student);
            }
        }
    }
}
