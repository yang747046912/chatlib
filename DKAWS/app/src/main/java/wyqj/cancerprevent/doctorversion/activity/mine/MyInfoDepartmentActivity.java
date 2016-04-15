package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.SharedPreferencesUtil;
import com.kaws.lib.common.widget.TipDialog;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.constant.Constants;

public class MyInfoDepartmentActivity extends LoadBaseActivity {

    private ImageView btnTitlebarBack;
    private EditText etMyinfoDepartment;
    private Button btnTitlebarSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_department);
        showContentView();
        setTitle("所在科室");
        initView();
        setUpView();
    }

    private void setUpView() {
        btnTitlebarSubmit.setText("保存");
        btnTitlebarBack.setOnClickListener(listener);
        btnTitlebarSubmit.setOnClickListener(listener);

        if (getIntent() != null) {
            String department = getIntent().getStringExtra(Constants.CHANGE_DEPARTMENT);
            etMyinfoDepartment.setText(department);
            etMyinfoDepartment.setSelection(department.length());
        }
    }

    private void initView() {
        btnTitlebarBack = (ImageView) findViewById(R.id.btn_titlebar_back);
        etMyinfoDepartment = (EditText) findViewById(R.id.et_myinfo_department);
        btnTitlebarSubmit = (Button) findViewById(R.id.btn_titlebar_submit);
        btnTitlebarSubmit.setVisibility(View.VISIBLE);//保存按钮
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_titlebar_back:
                    String department = etMyinfoDepartment.getText().toString().trim();
                    if ("".equals(department)) {
                        TipDialog.hintDiaglog(MyInfoDepartmentActivity.this, "请输入您所在科室");
                        return;
                    }
                    tipIsSaveDepartment();
                    break;
                case R.id.btn_titlebar_submit:
                    saveDepartment();
                    break;
            }
        }
    };

    /**
     * 提示是否保存科室
     */
    private void tipIsSaveDepartment() {
        TipDialog tipDialog = new TipDialog(MyInfoDepartmentActivity.this);
        tipDialog.setMessage("是否保存当前信息？");

        tipDialog.setPositive("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        tipDialog.setNegative("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                String department = etMyinfoDepartment.getText().toString().trim();
                SharedPreferencesUtil.saveChangeDepartmentMime(MyInfoDepartmentActivity.this, department);
                Intent intent = getIntent();
                intent.putExtra(Constants.MY_INFO_DEPARTMENT, department);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tipDialog.show();
    }

    private void saveDepartment() {
        String department = etMyinfoDepartment.getText().toString().trim();
        if ("".equals(department)) {
            TipDialog.hintDiaglog(MyInfoDepartmentActivity.this, "请输入您所在科室");
            return;
        }
        SharedPreferencesUtil.saveChangeDepartmentMime(MyInfoDepartmentActivity.this, department);
        Intent intent = getIntent();
        intent.putExtra(Constants.MY_INFO_DEPARTMENT, department);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveDepartment();
        }
        return super.onKeyDown(keyCode, event);
    }
}
