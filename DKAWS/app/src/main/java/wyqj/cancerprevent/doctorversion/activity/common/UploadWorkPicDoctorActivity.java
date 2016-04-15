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
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.fresco.Image;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.MainActivity;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.constant.DoctorStatus;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.ImageLoadUtils;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class UploadWorkPicDoctorActivity extends LoadBaseActivity {
    private SimpleDraweeView ivUploadpicDoctorAddpic;
    private SimpleDraweeView ivUploadpicStudentAddpic;
    private Button btnUploadpicDoctorUpload;
    private TextView tvUploadpicDoctorJump;
    private ImageView btnTitlebarBack;
    private ImageView ivUploadpicDoctorDelete;
    private ImageView ivUploadpicStudentDelete;
    private boolean ismodify;
    private String doctorImage;
    private String studentImage;
    private String doctorImageID;
    private String studentImageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_work_pic_doctor);
        showContentView();
        setTitle("职业医师证/学生证");
        initView();
        setUpView();
    }

    private void setUpView() {
        if (getIntent() != null) {
            ismodify = getIntent().getBooleanExtra(Constants.DOCTOR_ISMODIFY, true);
        }
        if (!ismodify) {//只有注册时候才能跳过
            tvUploadpicDoctorJump.setVisibility(View.VISIBLE);
        }
        Image.displayImage(ivUploadpicDoctorAddpic, Uri.parse("res:///" + R.drawable.add_uploadpic_student), R.drawable.add_uploadpic_student);
        Image.displayImage(ivUploadpicStudentAddpic, Uri.parse("res:///" + R.drawable.add_uploadpic_student), R.drawable.add_uploadpic_student);
        ivUploadpicDoctorAddpic.setOnClickListener(listener);
        ivUploadpicStudentAddpic.setOnClickListener(listener);
        btnUploadpicDoctorUpload.setOnClickListener(listener);
        tvUploadpicDoctorJump.setOnClickListener(listener);
        btnTitlebarBack.setOnClickListener(listener);
        ivUploadpicDoctorDelete.setOnClickListener(listener);
        ivUploadpicStudentDelete.setOnClickListener(listener);
    }

    private void initView() {
        ivUploadpicDoctorAddpic = (SimpleDraweeView) findViewById(R.id.iv_uploadpic_doctor_addpic);
        ivUploadpicStudentAddpic = (SimpleDraweeView) findViewById(R.id.iv_uploadpic_student_addpic);
        btnUploadpicDoctorUpload = (Button) findViewById(R.id.btn_uploadpic_doctor_upload);
        tvUploadpicDoctorJump = (TextView) findViewById(R.id.tv_uploadpic_doctor_jump);
        btnTitlebarBack = (ImageView) findViewById(R.id.btn_titlebar_back);
        ivUploadpicDoctorDelete = (ImageView) findViewById(R.id.iv_uploadpic_doctor_delete);
        ivUploadpicStudentDelete = (ImageView) findViewById(R.id.iv_uploadpic_student_delete);
    }


    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {

            switch (view.getId()) {
                case R.id.iv_uploadpic_doctor_addpic:
                    selectPhoto(doctorImage, Constants.CODE_SELECT_PHOTO_DOCTOR);
                    break;
                case R.id.iv_uploadpic_student_addpic:
                    selectPhoto(studentImage, Constants.CODE_SELECT_PHOTO_STUDENT);
                    break;
                case R.id.btn_uploadpic_doctor_upload://上传照片
                    beforeUpload();
                    break;
                case R.id.tv_uploadpic_doctor_jump://跳入首页
                    openActivity(MainActivity.class);
                    break;
                case R.id.btn_titlebar_back://返回
                    if (ismodify) {
                        finish();
                    } else {
                        openActivity(RegisterSuccessActivity.class);
                    }
                    break;
                case R.id.iv_uploadpic_student_delete:

                    ivUploadpicStudentDelete.setVisibility(View.GONE);
                    studentImage = null;
                    studentImageID = null;
                    Image.displayImage(ivUploadpicStudentAddpic, Uri.parse("res:///" + R.drawable.add_uploadpic_student), R.drawable.add_uploadpic_student);
                    break;
                case R.id.iv_uploadpic_doctor_delete:
                    Image.displayImage(ivUploadpicDoctorAddpic, Uri.parse("res:///" + R.drawable.add_uploadpic_student), R.drawable.add_uploadpic_student);
                    ivUploadpicDoctorDelete.setVisibility(View.GONE);
                    doctorImage = null;
                    doctorImageID = null;
                    break;
            }
        }
    };


    private void selectPhoto(String path, int SelectCode) {
        if (!TextUtils.isEmpty(path)) {
            //查看大图
            Bundle bundle = new Bundle();
            bundle.putInt("code", 0);
            bundle.putInt("selet", 1);
            bundle.putBoolean("isLocal", true);
            ArrayList<String> list = new ArrayList<>();
            list.add(path);
            bundle.putStringArrayList("imageuri", list);
            Intent intent = new Intent(UploadWorkPicDoctorActivity.this, ViewBigImageActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Intent in = new Intent(UploadWorkPicDoctorActivity.this, SelectPhoto.class);
            in.putExtra(Constants.IS_NEED_CROP, false);
            startActivityForResult(in, SelectCode);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (data != null) {
            if (requestCode == Constants.CODE_SELECT_PHOTO_DOCTOR) {
                String url = data.getStringExtra(Constants.SELECT_PHOTO);
                if (!TextUtils.isEmpty(url)) {
                    doctorImage = url;
                    doctorImageID = null;
                    ivUploadpicDoctorDelete.setVisibility(View.VISIBLE);
                    Image.displayImage(ivUploadpicDoctorAddpic, Uri.parse("file://" + doctorImage), R.drawable.add_uploadpic_student);
                }
            } else if (requestCode == Constants.CODE_SELECT_PHOTO_STUDENT) {
                String url = data.getStringExtra(Constants.SELECT_PHOTO);
                if (!TextUtils.isEmpty(url)) {
                    studentImage = url;
                    studentImageID = null;
                    ivUploadpicStudentDelete.setVisibility(View.VISIBLE);
                    Image.displayImage(ivUploadpicStudentAddpic, Uri.parse("file://" + studentImage), R.drawable.add_uploadpic_student);
                }
            }
        }
//        if (requestCode == Constants.CODE_SELECT_PHOTO_DOCTOR && data != null) {
//            String url = data.getStringExtra(Constants.SELECT_PHOTO);
//            if (!TextUtils.isEmpty(url)) {
//                imageFile = url;
//                btnUploadpicUpload.setText("上传");
//                ivUploadpicDeletepic.setVisibility(View.VISIBLE);
//                Image.displayImage(ivUploadpicAddpic, Uri.parse("file://" + imageFile), R.drawable.add_uploadpic_student);
//            }
//        }
    }

    private void UpLoadPic() {
        if (TextUtils.isEmpty(doctorImageID) || TextUtils.isEmpty(studentImageID)) {
            return;
        }
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.upLoadPic(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, null, studentImageID, doctorImageID, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {
                SQuser.getInstance().getUserInfo().status = DoctorStatus.PENDING;
                stopProgressDialog();
                ToastUtils.showToast(UploadWorkPicDoctorActivity.this, "上传图片成功", 2000, 1);
                //跳转到上传成功的界面
                Intent intent = new Intent(UploadWorkPicDoctorActivity.this, UploadSuccessActivity.class);
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

    private void beforeUpload() {
        if (doctorImage == null) {
            TipDialog.hintDiaglog(UploadWorkPicDoctorActivity.this, "请上传您的执业医师证");
            return;
        } else if (TextUtils.isEmpty(doctorImageID)) {
            uploadDoctorImage(doctorImage);
        }
        if (studentImage == null) {
            TipDialog.hintDiaglog(UploadWorkPicDoctorActivity.this, "请上传您的学生证");
            return;
        } else if (TextUtils.isEmpty(studentImageID)) {
            uploadStudentImage(studentImage);
        }
        UpLoadPic();
    }


    private void uploadDoctorImage(String doctorImage) {
        ImageLoadUtils.uploadFile(this, doctorImage, new ImageLoadUtils.ImplUploadListener() {
            @Override
            public void onSuccess(String imageID) {
                doctorImageID = imageID;
                UpLoadPic();
            }

            @Override
            public void onFailure() {
                ToastUtils.showToast(UploadWorkPicDoctorActivity.this, "执业医师证上传失败", 2000, 3);
            }
        });
    }

    private void uploadStudentImage(final String studentImage) {
        ImageLoadUtils.uploadFile(this, studentImage, new ImageLoadUtils.ImplUploadListener() {
            @Override
            public void onSuccess(String imageID) {
                studentImageID = imageID;
                UpLoadPic();
            }

            @Override
            public void onFailure() {
                ToastUtils.showToast(UploadWorkPicDoctorActivity.this, "学生证上传失败", 2000, 3);
            }
        });
    }
}
