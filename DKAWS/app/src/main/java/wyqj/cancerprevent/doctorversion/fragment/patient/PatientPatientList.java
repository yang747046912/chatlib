package wyqj.cancerprevent.doctorversion.fragment.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kaws.lib.common.base.NoTitleLoadBaseFragment;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.BlankView;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.common.SearchActivity;
import wyqj.cancerprevent.doctorversion.adapter.patient.PatientListAdapter;
import wyqj.cancerprevent.doctorversion.bean.PatientListBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;


public class PatientPatientList extends NoTitleLoadBaseFragment {
    private XRecyclerView xRecyclerView;
    private BlankView blankView;
    private int page = 1;
    private PatientListAdapter adapter;
    private View search;

    @Override
    public int setContent() {
        return R.layout.fragment_patient_patient_list;
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
        search = getView(R.id.ic_titlebar_patientlist);
    }

    private void setUpView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getPatientList();
            }

            @Override
            public void onLoadMore() {
                page++;
                getPatientList();
            }
        });
        xRecyclerView.setLayoutManager(layoutManager);
        adapter = new PatientListAdapter();
        xRecyclerView.setAdapter(adapter);
        getPatientList();
        search.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                search.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(Constants.SEARCH_TYPE, 1);
                intent.putExtra(Constants.SEARCH_HINT, "请输入患者名称");
                startActivity(intent);
            }
        });
    }

    private void getPatientList() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.getPatientList(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, page, new CustomCallBack<ArrayList<PatientListBean>>() {
            @Override
            public void onSuccess(ArrayList<PatientListBean> applyRecordBeans) {

                if (page == 1) {
                    showContentView();
                    if (applyRecordBeans.size() == 0) {
                        blankView.setVisibility(View.VISIBLE);
                        search.setVisibility(View.GONE);
                    } else {
                        blankView.setVisibility(View.GONE);
                        search.setVisibility(View.VISIBLE);
                    }
                    adapter.clear();
                } else {
                    if (applyRecordBeans.size() == 0) {
                        xRecyclerView.noMoreLoading();
                    }
                }
                adapter.addAll(applyRecordBeans);
                adapter.notifyDataSetChanged();
                xRecyclerView.refreshComplete();
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
    protected void onRefresh() {
        super.showRefrsh();
        getPatientList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter.getItemCount() >0) {
            search.setVisibility(View.VISIBLE);
        }

    }
}
