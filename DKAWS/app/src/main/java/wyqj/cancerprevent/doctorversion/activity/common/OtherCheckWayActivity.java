package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;

import wyqj.cancerprevent.doctorversion.R;

public class OtherCheckWayActivity extends LoadBaseActivity {

    private Button btnOthercheckCallphone;
    private TextView tv_othercheck_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_check_way);
        showContentView();
        setTitle("其他审核方式");
        btnOthercheckCallphone = (Button) findViewById(R.id.btn_othercheck_callphone);
        tv_othercheck_text = (TextView) findViewById(R.id.tv_othercheck_text);
        btnOthercheckCallphone.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.wyqj_telephone)));
                startActivity(intent);
            }
        });

        String ServiceNumber = tv_othercheck_text.getText().toString();
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.edt_modifing));
//        //部分文字的颜色改变
        SpannableStringBuilder builder1 = new SpannableStringBuilder(ServiceNumber);
        builder1.setSpan(span, 24, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_othercheck_text.setText(builder1);
        tv_othercheck_text.setTextSize(15);
    }
}
