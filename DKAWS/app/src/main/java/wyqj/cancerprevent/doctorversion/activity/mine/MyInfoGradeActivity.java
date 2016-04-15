package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.TipDialog;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.constant.Constants;

public class MyInfoGradeActivity extends LoadBaseActivity {

    private EditText etMyinfoGrade;
    private Button btnTitlebarSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_grade);
        showContentView();
        setTitle("备注");
        initView();
        setUpView();
    }

    private void setUpView() {
        btnTitlebarSubmit.setText("保存");
        btnTitlebarSubmit.setOnClickListener(listener);

        if (getIntent() != null) {
            String grade = getIntent().getStringExtra(Constants.CHANGE_GRADE);
            etMyinfoGrade.setText(grade);
            etMyinfoGrade.setSelection(grade.length());
        }
    }

    private void initView() {
        etMyinfoGrade = (EditText) findViewById(R.id.et_myinfo_grade);
        btnTitlebarSubmit = (Button) findViewById(R.id.btn_titlebar_submit);
        btnTitlebarSubmit.setVisibility(View.VISIBLE);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_titlebar_submit:
                    String grade = etMyinfoGrade.getText().toString().trim();
                    if (TextUtils.isEmpty(grade)) {
                        TipDialog.hintDiaglog(MyInfoGradeActivity.this, "请输入您要修改的职称");
                        return;
                    }
                    Intent intent = getIntent();
                    intent.putExtra(Constants.MY_INFO_GRADE, grade);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    };
}
