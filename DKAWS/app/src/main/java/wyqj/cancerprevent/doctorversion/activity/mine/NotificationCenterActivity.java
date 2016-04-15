package wyqj.cancerprevent.doctorversion.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.baseadapter.OnItemClickListener;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.common.widget.BlankView;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.xrecyclerview.XRecyclerView;

import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.mine.NotificationCenterAdapter;
import wyqj.cancerprevent.doctorversion.bean.NotificationCenterListBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class NotificationCenterActivity extends LoadBaseActivity {

    /** 空白图*/
    private BlankView blank_view;
    private XRecyclerView xrecyclerview;
    private int page = 1;
    /** 结果适配器*/
    private NotificationCenterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);
        setTitle("消息中心");
        initView();
        setUpView();
    }

    private void initView() {
        xrecyclerview = (XRecyclerView) findViewById(R.id.xrecyclerview);
        blank_view = (BlankView) findViewById(R.id.blank_view_notification);
    }

    private void setUpView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationCenterActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getNotificationDetail();
            }

            @Override
            public void onLoadMore() {
                page++;
                getNotificationDetail();
            }
        });
        xrecyclerview.setLayoutManager(layoutManager);
        adapter = new NotificationCenterAdapter();
        xrecyclerview.setAdapter(adapter);
        getNotificationDetail();
        adapter.setOnItemClickListener(new OnItemClickListener<NotificationCenterListBean>() {
            @Override
            public void onClick(NotificationCenterListBean notificationCenterListBean, int position) {

            }
        });
    }

    /** 获取消息中心内容*/
    private void getNotificationDetail() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.getNotificationCenter(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, page, Constants.PER_PAGE, new CustomCallBack<List<NotificationCenterListBean>>() {
            @Override
            public void onSuccess(List<NotificationCenterListBean> notificationCenterListBeans) {
                showContentView();
                if (notificationCenterListBeans == null) {
                    blank_view.setVisibility(View.VISIBLE);
                    adapter.clear();
                } else {
                    if (page == 1) {
                        if (notificationCenterListBeans.size() == 0) {
                            blank_view.setVisibility(View.VISIBLE);
                        } else {
                            blank_view.setVisibility(View.GONE);
                        }
                        adapter.clear();
                    } else {
                        if (notificationCenterListBeans.size() == 0) {
                            xrecyclerview.noMoreLoading();
                        }
                    }
                    adapter.addAll(notificationCenterListBeans);
                }
                adapter.notifyDataSetChanged();
                xrecyclerview.refreshComplete();
            }

            @Override
            public void onFailure() {
                if (adapter.getItemCount() == 0) {
                    showRefrsh();
                }
                if (page > 1) {
                    page--;
                }
            }
        });
    }

    @Override
    protected void onRefresh() {
        super.showRefrsh();
        /** 点击重新加载*/
        getNotificationDetail();
    }
}

