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

public class PerIntroActivity extends LoadBaseActivity {

    private Button btn_use_v3_title_bar;
    private EditText edt_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_intro);
        showContentView();
        setTitle("个人简介");
        initView();
        setUpView();
    }

    private void setUpView() {
        btn_use_v3_title_bar.setVisibility(View.VISIBLE);
        btn_use_v3_title_bar.setText("保存");
        btn_use_v3_title_bar.setBackgroundResource(R.drawable.shape_confirm);
        btn_use_v3_title_bar.setHeight(44);
        String perIntro = getIntent().getStringExtra(Constants.PERSONAL_PROFILE);
        if (!TextUtils.isEmpty(perIntro)) {
            edt_content.setText(perIntro);
            edt_content.setSelection(perIntro.length());
        }

        getView(com.kaws.lib.common.R.id.btn_titlebar_back).setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                String tmp = edt_content.getText().toString().trim();
                finishToResult(tmp);
            }
        });

        btn_use_v3_title_bar.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                String tmp = edt_content.getText().toString().trim();
                if (TextUtils.isEmpty(tmp)) {
                    TipDialog.hintDiaglog(PerIntroActivity.this, "请输入您的个人简介");
                    return;
                }
                finishToResult(tmp);
            }
        });
    }

    private void finishToResult(String tmp) {
        Intent intent = getIntent();
        intent.putExtra(Constants.PERSONAL_PROFILE, tmp);
        setResult(RESULT_OK, intent);
        finishDefault();
    }

    private void initView() {
        btn_use_v3_title_bar = getView(R.id.btn_titlebar_submit);
        edt_content = getView(R.id.person_intro_content);
    }
}
