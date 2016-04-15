package wyqj.cancerprevent.doctorversion.activity.mine;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.utils.ToastUtils;

import wyqj.cancerprevent.doctorversion.R;

public class RecommendActivity extends LoadBaseActivity {
    private ListView llRecommend;
    private int[] icons = {R.drawable.kangaiweishi, R.drawable.cancerguide_icon};
    private String[] names = {"抗癌卫士用户版", "防癌卫士"};
    private String[] explains = {"中国最大的抗癌防癌互助服务平台肿瘤病症全程一站式服务。", "为健康人群用户提供抗癌防癌的知识资讯，癌症预防与早期发现。"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        showContentView();
        setTitle("精品推荐");
        llRecommend = (ListView) findViewById(R.id.ll_recommend);
        MyAdapter myAdapter = new MyAdapter();

        llRecommend.setAdapter(myAdapter);
        llRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳到相应的下载页面
                if (i == 0) {
                    try {
                        Uri uri = Uri.parse("market://details?id=" + "com.dzy.cancerprevention_anticancer.activity");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        ToastUtils.showToast(RecommendActivity.this, "软件市场里暂时没有找到抗癌卫士用户版", 2000, 2);
                    }
                } else {
                    try {
                        Uri uri = Uri.parse("market://details?id=" + "com.cancerprevention_guards");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        ToastUtils.showToast(RecommendActivity.this, "软件市场里暂时没有找到防癌卫士", 2000, 2);
                    }
                }
            }
        });
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View childView = View.inflate(RecommendActivity.this, R.layout.item_recommend, null);
            ImageView iv_recommend_icon = (ImageView) childView.findViewById(R.id.iv_recommend_icon);
            TextView tv_recommend_appexplain = (TextView) childView.findViewById(R.id.tv_recommend_appexplain);
            TextView tv_recommend_appname = (TextView) childView.findViewById(R.id.tv_recommend_appname);
            iv_recommend_icon.setImageResource(icons[i]);
            tv_recommend_appexplain.setText(explains[i]);
            tv_recommend_appname.setText(names[i]);
            return childView;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

    }
}
