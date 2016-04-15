package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;

import wyqj.cancerprevent.doctorversion.BuildConfig;
import wyqj.cancerprevent.doctorversion.R;

public class AboutUsActivity extends LoadBaseActivity {

    private RelativeLayout AboutusSina;
    private TextView tvAboutusVersion;
    /**拨打热线电话*/
    private RelativeLayout ll_telephone;
    /**官方网站*/
    private RelativeLayout rl_web;
    /**邮箱*/
    private RelativeLayout rl_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        showContentView();
        setTitle("关于我们");
        initView();
        setUpView();
    }

    private void setUpView() {
        tvAboutusVersion.setText("抗癌卫士医生版v" + BuildConfig.VERSION_NAME);
        AboutusSina.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Uri uri = Uri.parse("http://weibo.com/kangaiweishi");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ll_telephone.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+"400-101-1510"));
                startActivity(intent);
            }
        });

        rl_web.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Uri uri = Uri.parse("http://www.kangaiweishi.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        rl_email.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:wyqj@kangaiweishi.com"));
                startActivity(data);
            }
        });
    }

    private void initView() {
        /**客服电话的布局*/
        ll_telephone= (RelativeLayout) findViewById(R.id.ll_telephone);
        /**官方网站的布局*/
        rl_web= (RelativeLayout) findViewById(R.id.rl_web);
        /**邮箱的布局*/
        rl_email= (RelativeLayout) findViewById(R.id.rl_email);
        AboutusSina = (RelativeLayout) findViewById(R.id.aboutus_sina);
        tvAboutusVersion = (TextView) findViewById(R.id.tv_aboutus_version);
    }
}
