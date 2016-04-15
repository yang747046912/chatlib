package wyqj.cancerprevent.doctorversion.activity.mine;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.FileUtil;
import com.kaws.lib.fresco.Image;

import java.io.File;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class MyErweimaActivity extends LoadBaseActivity {
    private TextView tvErweimaGrade;
    private TextView tvErweimaHospital;
    private TextView tvErweimaName;
    private TextView tvErweimaOffice;
    private SimpleDraweeView ivErweimaMyerweima;
    private SimpleDraweeView ivErweimaTouxiang;
    private ImageView ivTitlebarDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_erweima);
        showContentView();
        setTitle("我的二维码");
        initView();
        setUpView();
    }

    private void setUpView() {
        SQuser sQuser = SQuser.getInstance();
        final UserBean.Doctor doctor = sQuser.getUserInfo().info;
        if (doctor != null) {
            tvErweimaName.setText(doctor.name);
            tvErweimaGrade.setText(" - " + doctor.degree.name);
            tvErweimaHospital.setText(doctor.hospitalName);
            tvErweimaOffice.setText(doctor.department);
            if (doctor.qrImage != null) {
                Image.displayImage(ivErweimaMyerweima, Uri.parse(doctor.qrImage.url));
            }
            Image.displayRound(ivErweimaTouxiang, 10, Uri.parse(doctor.avatarUrl));
        }
        ivTitlebarDownload.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (doctor !=null && doctor.qrImage != null) {
                    File file = Image.getCachedImageOnDisk(Uri.parse(doctor.qrImage.url));
                    FileUtil.saveFile(MyErweimaActivity.this, file);
                }
            }
        });
    }

    private void initView() {
        tvErweimaGrade = (TextView) findViewById(R.id.tv_erweima_grade);
        tvErweimaHospital = (TextView) findViewById(R.id.tv_mine_hospital);
        tvErweimaName = (TextView) findViewById(R.id.tv_erweima_name);
        tvErweimaOffice = (TextView) findViewById(R.id.tv_erweima_office);
        ivErweimaTouxiang = (SimpleDraweeView) findViewById(R.id.iv_erweima_touxiang);//头像
        ivErweimaMyerweima = (SimpleDraweeView) findViewById(R.id.iv_erweima_myerweima);//二维码
        ivTitlebarDownload = (ImageView) findViewById(R.id.iv_titlebar_download);//下载
        ivTitlebarDownload.setVisibility(View.VISIBLE);
    }

}
