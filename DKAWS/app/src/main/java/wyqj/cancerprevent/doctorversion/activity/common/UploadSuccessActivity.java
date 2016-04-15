package wyqj.cancerprevent.doctorversion.activity.common;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kaws.lib.common.base.BaseActivity;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.MainActivity;
import wyqj.cancerprevent.doctorversion.constant.Constants;

public class UploadSuccessActivity extends BaseActivity {
    private Button btnUploadEnter;
    private boolean isModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_success);

        btnUploadEnter = (Button) findViewById(R.id.btn_upload_enter);
        if (getIntent() != null) {
            isModify = getIntent().getBooleanExtra(Constants.DOCTOR_ISMODIFY, false);
        }
        btnUploadEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到首页
                if (isModify) {
                    finish();
                } else {
                    openActivity(MainActivity.class);
                }
            }
        });
    }
}
