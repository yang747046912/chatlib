package wyqj.cancerprevent.doctorversion.fragment.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.kaws.lib.common.base.NoTitleLoadBaseFragment;
import com.kaws.lib.common.widget.BlankView;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.mine.MyIncomeRecordAdapter;
import wyqj.cancerprevent.doctorversion.bean.IncomeRecordBean;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class MyIncomeRecordFragment extends NoTitleLoadBaseFragment {

    private XRecyclerView xRecyclerView;
    private BlankView blankView;
    private int page = 1;
    private MyIncomeRecordAdapter adapter;
    private int type; //0 表示收入记录 1表示提现记录
    private View headView;

    public static MyIncomeRecordFragment getInstance(int type) {
        MyIncomeRecordFragment fragment = new MyIncomeRecordFragment();
        Bundle b = new Bundle();
        b.putInt("Type", type);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int setContent() {
        return R.layout.fragment_my_income_record;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        type = getArguments().getInt("Type");
        initView();
        setUpView();
        getIncomeRecord();
    }

    private void setUpView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getIncomeRecord();
            }

            @Override
            public void onLoadMore() {
                page++;
                getIncomeRecord();
            }
        });
        xRecyclerView.setLayoutManager(layoutManager);
        adapter = new MyIncomeRecordAdapter();
        xRecyclerView.setAdapter(adapter);
        initHeadView();
    }

    private void initHeadView() {

        if (type == 0) {
            headView = View.inflate(getActivity(), R.layout.headerview_my_income_record, null);
            TextView txtTotalIncomeHeaderView = (TextView) headView.findViewById(R.id.txt_total_income_headerView);
            SQuser sQuser = SQuser.getInstance();
            UserBean.Doctor doctor = sQuser.getUserInfo().info;
            String totalMoney;
            if (String.valueOf(doctor.totalMoney).contains(".0")) {
                totalMoney = String.valueOf(doctor.totalMoney).replace(".0", "");
            } else {
                totalMoney = String.valueOf(doctor.totalMoney);
            }
            txtTotalIncomeHeaderView.setText(totalMoney);
        } else {
            headView = View.inflate(getActivity(), R.layout.headerview_my_withdraw_record, null);
        }
        xRecyclerView.addHeaderView(headView);
    }

    private void initView() {
        xRecyclerView = getView(R.id.xrecyclerview);
        blankView = getView(R.id.blank);
    }

    private void getIncomeRecord() {
        HttpService service = HttpUtils.getInstance().getHttpsService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        CustomCallBack<ArrayList<IncomeRecordBean>> callBack = new CustomCallBack<ArrayList<IncomeRecordBean>>() {
            @Override
            public void onSuccess(ArrayList<IncomeRecordBean> applyRecordBeans) {
                if (page == 1) {
                    showContentView();
                    if (applyRecordBeans.size() == 0) {
                        blankView.setVisibility(View.VISIBLE);
                        headView.setVisibility(View.GONE);
                    } else {
                        headView.setVisibility(View.VISIBLE);
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
        };
        if (type == 0) {
            service.getIncomeRecord(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, page, callBack);
        } else {
            service.getWithdrawRecord(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, page, callBack);
        }
    }


    @Override
    protected void onRefresh() {
        super.showRefrsh();
        getIncomeRecord();
    }
}
