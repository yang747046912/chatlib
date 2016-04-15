package wyqj.cancerprevent.doctorversion.fragment.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kaws.lib.common.base.NoTitleLoadBaseFragment;
import com.kaws.lib.common.baseadapter.OnItemClickListener;
import com.kaws.lib.common.widget.BlankView;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.xrecyclerview.XRecyclerView;

import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.patient.ApplyDetailActivity;
import wyqj.cancerprevent.doctorversion.adapter.patient.ApplyAdapter;
import wyqj.cancerprevent.doctorversion.bean.ApplyRecordBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;


public class PatientApplyRecord extends NoTitleLoadBaseFragment {

    private XRecyclerView xRecyclerView;
    private BlankView blankView;
    private int page = 1;
    private ApplyAdapter adapter;

    @Override
    public int setContent() {
        return R.layout.fragment_patient_apply_record;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setUpView();
    }

    private void initView() {
        xRecyclerView = getView(R.id.xrecyclerview);
        blankView = getView(R.id.blank);
    }

    private void setUpView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getApplyRecord();
            }

            @Override
            public void onLoadMore() {
                page++;
                getApplyRecord();
            }
        });
        xRecyclerView.setLayoutManager(layoutManager);
        adapter = new ApplyAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener<ApplyRecordBean>() {
            @Override
            public void onClick(ApplyRecordBean applyRecordBean, int position) {
                int status = applyRecordBean.agreeStatus;
                if (status == 2 || status == 3) {
                    return;
                } else {
                    Intent intent = new Intent(getActivity(), ApplyDetailActivity.class);
                    intent.putExtra(Constants.APPLY_ID, applyRecordBean.id);
                    startActivity(intent);
                }
            }
        });
        xRecyclerView.setAdapter(adapter);
        getApplyRecord();
    }

    private void getApplyRecord() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.getApplyRecord(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, page, Constants.PER_PAGE, new CustomCallBack<List<ApplyRecordBean>>() {
            @Override
            public void onSuccess(List<ApplyRecordBean> applyRecordBeans) {
                xRecyclerView.refreshComplete();
                if (page == 1) {
                    showContentView();
                    if (applyRecordBeans.size() == 0) {
                        blankView.setVisibility(View.VISIBLE);
                    } else {
                        blankView.setVisibility(View.GONE);
                    }
                    adapter.clear();
                } else {
                    if (applyRecordBeans.size() == 0) {
                        xRecyclerView.noMoreLoading();
                    }
                }
                adapter.addAll(applyRecordBeans);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                xRecyclerView.refreshComplete();
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
    public void onResume() {
        super.onResume();
        if (SQuser.getInstance().needonRefresh) {
            SQuser.getInstance().needonRefresh = false;
            getApplyRecord();
        }
    }

    @Override
    protected void onRefresh() {
        super.showRefrsh();
        getApplyRecord();
    }
}
