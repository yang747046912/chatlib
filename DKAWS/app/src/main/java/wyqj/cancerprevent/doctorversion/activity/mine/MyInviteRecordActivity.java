package wyqj.cancerprevent.doctorversion.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.widget.BlankView;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.xrecyclerview.XRecyclerView;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.mine.InviteRecordAdapter;
import wyqj.cancerprevent.doctorversion.bean.InviteDoctorRecordBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.headview.MyInviteRecordHead;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class MyInviteRecordActivity extends LoadBaseActivity {
    private XRecyclerView xRecyclerView;
    private int page = 1;
    private MyInviteRecordHead head;
    private InviteRecordAdapter adapter;
    private BlankView blankView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invite_record);
        setTitle("邀请医生记录");
        xRecyclerView = getView(R.id.xrecyclerview);
        blankView = (BlankView) findViewById(R.id.blank);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        head = new MyInviteRecordHead(this);
        xRecyclerView.addHeaderView(head.getHeadView(R.layout.headerview_invite_doctor_record));
        adapter = new InviteRecordAdapter();
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getInviteDoctorRecord();
            }

            @Override
            public void onLoadMore() {
                page++;
                getInviteDoctorRecord();
            }
        });
        xRecyclerView.setAdapter(adapter);
        getInviteDoctorRecord();
    }

    private void getInviteDoctorRecord() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.getInviteDoctorRecord(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, page, Constants.PER_PAGE, new CustomCallBack<InviteDoctorRecordBean>() {
            @Override
            public void onSuccess(InviteDoctorRecordBean inviteDoctorRecordBean) {

                head.setUpView(inviteDoctorRecordBean);
                if (page == 1) {
                    showContentView();
                    if (inviteDoctorRecordBean.inviteDetails != null && inviteDoctorRecordBean.inviteDetails.size() != 0) {
                        blankView.setVisibility(View.GONE);
                    } else {
                        blankView.setVisibility(View.VISIBLE);
                    }
                    adapter.clear();
                } else {
                    if (inviteDoctorRecordBean.inviteDetails.size() == 0) {
                        xRecyclerView.noMoreLoading();
                    }
                }
                adapter.addAll(inviteDoctorRecordBean.inviteDetails);
                adapter.notifyDataSetChanged();
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onFailure() {
                if (page > 1) {
                    page--;
                }
                xRecyclerView.refreshComplete();
                if (adapter.getItemCount() == 0) {
                    showRefrsh();
                }
            }
        });
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        getInviteDoctorRecord();
    }
}
