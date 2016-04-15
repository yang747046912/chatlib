package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.SharedPreferencesUtil;
import com.kaws.lib.common.widget.TipDialog;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.constant.Constants;

public class MyInfoNameActivity extends LoadBaseActivity {

    private ImageView btnTitlebarBack;
    private EditText etMyinfoName;
    private Button btnTitlebarSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_name);
        showContentView();
        setTitle("姓名");
        initView();
        setUpView();
    }

    private void setUpView() {
        btnTitlebarSubmit.setText("保存");
        btnTitlebarBack.setOnClickListener(listener);
        btnTitlebarSubmit.setOnClickListener(listener);

        if (getIntent() != null) {
            String name = getIntent().getStringExtra(Constants.CHANGE_NAME);
            etMyinfoName.setText(name);
            etMyinfoName.setSelection(name.length());
        }
    }

    private void initView() {
        btnTitlebarBack = (ImageView) findViewById(R.id.btn_titlebar_back);
        etMyinfoName = (EditText) findViewById(R.id.et_myinfo_name);
        btnTitlebarSubmit = (Button) findViewById(R.id.btn_titlebar_submit);
        btnTitlebarSubmit.setVisibility(View.VISIBLE);//保存按钮
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_titlebar_back:
                    String name = etMyinfoName.getText().toString().trim();
                    if ("".equals(name)) {
                        TipDialog.hintDiaglog(MyInfoNameActivity.this, "请输入您的姓名");
                        return;
                    }
                    tipIsSaveName();
                    break;
                case R.id.btn_titlebar_submit:
                    saveName();
                    break;
            }
        }
    };

    /**
     * 提示是否保存名字
     * */
    private void tipIsSaveName() {
        TipDialog tipDialog = new TipDialog(MyInfoNameActivity.this);
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
                String name = etMyinfoName.getText().toString().trim();
                SharedPreferencesUtil.saveChangeNameMime(MyInfoNameActivity.this, name);
                Intent intent = getIntent();
                intent.putExtra(Constants.MY_INFO_NAME, name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        tipDialog.show();
    }

    private void saveName(){
        String name = etMyinfoName.getText().toString().trim();
        if ("".equals(name)) {
            TipDialog.hintDiaglog(MyInfoNameActivity.this, "请输入您的姓名");
            return;
        }
        SharedPreferencesUtil.saveChangeNameMime(MyInfoNameActivity.this, name);
        Intent intent = getIntent();
        intent.putExtra(Constants.MY_INFO_NAME, name);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tipIsSaveName();
        }
        return super.onKeyDown(keyCode, event);
    }
}
