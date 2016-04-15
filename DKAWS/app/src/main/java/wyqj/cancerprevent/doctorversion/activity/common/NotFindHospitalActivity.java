package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.TipDialog;

import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;

public class NotFindHospitalActivity extends LoadBaseActivity {

    private EditText etNotfindHospital;
    private Button btnNotfindSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_find_hospital);
        showContentView();
        setTitle("请输入医院全称");
        initView();
        setUpView();
    }

    private void setUpView() {
        btnNotfindSave.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                String tmp = etNotfindHospital.getText().toString().trim();
                if (TextUtils.isEmpty(tmp)) {
                    TipDialog.hintDiaglog(NotFindHospitalActivity.this, "请输入您所在的医院全称");
                    return;
                }
                Intent data = new Intent();
                data.putExtra(Constants.NOT_FIND_HOSPITAL, tmp);
                setResult(RESULT_OK, data);
                finishDefault();
            }
        });
    }

    private void initView() {
        etNotfindHospital = getView(R.id.et_notfind_hospital);
        btnNotfindSave = getView(R.id.btn_notfind_save);
    }
}
