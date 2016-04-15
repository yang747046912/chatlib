package wyqj.cancerprevent.doctorversion.activity.mine;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.http.AppConfig;
import com.kaws.lib.http.HttpHead;

import java.util.HashMap;
import java.util.Map;

import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class CommonQuestionActivity extends LoadBaseActivity {

    private WebView webViewHowtoMakemoney;
    private RelativeLayout rlMoneyWebview;
    private boolean isMakeMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_question);
        showContentView();
        isMakeMoney = getIntent().getBooleanExtra(Constants.MAKE_MONEY, false);
        if (isMakeMoney) {
            setTitle("如何创收");
        } else {
            setTitle("常见问题");
        }
        webViewHowtoMakemoney = (WebView) findViewById(R.id.webView_howto_makemoney);
        rlMoneyWebview = (RelativeLayout) findViewById(R.id.rl_money_webview);
        initWebView();
    }

    private void initWebView() {
        WebSettings ws = webViewHowtoMakemoney.getSettings();
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setBuiltInZoomControls(false);// 隐藏缩放按钮
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setSupportMultipleWindows(true);// 新加

        String url;
        if (isMakeMoney) {
            url = AppConfig.BASEURL + "/d/income_producing.html";
            setTitle("如何创收");
        } else {
            url = AppConfig.BASEURL + "/d/faq.html";
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("a", HttpHead.getHeader("GET"));
        map.put("token", SQuser.getInstance().getUserInfo().token);
        webViewHowtoMakemoney.loadUrl(url, map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rlMoneyWebview.removeView(webViewHowtoMakemoney);
        webViewHowtoMakemoney.removeAllViews();
        webViewHowtoMakemoney.destroy();
        webViewHowtoMakemoney = null;
    }
}
