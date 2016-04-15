package wyqj.cancerprevent.doctorversion.activity.common;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.MainActivity;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class RegisterSuccessActivity extends BaseActivity {
    private Button btnRegistersuccessNext;
    private TextView tvRegistersuccessJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        btnRegistersuccessNext = (Button) findViewById(R.id.btn_registersuccess_next);
        tvRegistersuccessJump = (TextView) findViewById(R.id.tv_registersuccess_jump);
        btnRegistersuccessNext.setOnClickListener(listener);
        tvRegistersuccessJump.setOnClickListener(listener);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.btn_registersuccess_next:
                    //上传图片
                    int grade = SQuser.getInstance().getUserInfo().info.degree.id;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Constants.DOCTOR_ISMODIFY, false);
                    if (grade == 5) {
                        openActivity(UploadWorkPicDoctorActivity.class, bundle);
                    } else {
                        openActivity(UploadWorkPicActivity.class, bundle);
                    }
                    break;

                case R.id.tv_registersuccess_jump:
                    //跳进首页
                    openActivity(MainActivity.class);
                    finish();
                    break;
            }
        }
    };
}
