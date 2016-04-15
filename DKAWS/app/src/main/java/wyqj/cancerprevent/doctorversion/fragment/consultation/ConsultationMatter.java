package wyqj.cancerprevent.doctorversion.fragment.consultation;

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
import wyqj.cancerprevent.doctorversion.activity.mobileconsult.AppointDetainActivity;
import wyqj.cancerprevent.doctorversion.adapter.consultation.ConsultationAdapter;
import wyqj.cancerprevent.doctorversion.bean.ConsultListBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.constant.ConsultationOrderStatus;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;


public class ConsultationMatter extends NoTitleLoadBaseFragment {
    private static final String CONSULTATION_ORDER_STATUS = "status";
    private ConsultationAdapter adapter;
    private XRecyclerView xRecyclerView;
    private BlankView blankView;
    private int page = 1;
    private int status;
    private ImpGetConsultingUnreadCount unreadCountListener;

    public static ConsultationMatter newInstance(int status) {
        ConsultationMatter consultationMatter = new ConsultationMatter();
        Bundle bundle = new Bundle();
        bundle.putInt(CONSULTATION_ORDER_STATUS, status);
        consultationMatter.setArguments(bundle);
        return consultationMatter;
    }

    @Override
    public int setContent() {
        return R.layout.fragment_consultation_matter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = getArguments().getInt(CONSULTATION_ORDER_STATUS, -1);
        if (status == -1) {
            throw new RuntimeException("status must not -1 ,please see the class ConsultationOrderStatus");
        }
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
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                phoneConsultingOrder();
            }

            @Override
            public void onLoadMore() {
                page++;
                phoneConsultingOrder();
            }
        });
        adapter = new ConsultationAdapter();
        adapter.setStatus(status);
        adapter.setOnItemClickListener(new OnItemClickListener<ConsultListBean.OrdersBean>() {
            @Override
            public void onClick(ConsultListBean.OrdersBean ordersBean, int position) {
                Intent in = new Intent(getActivity(), AppointDetainActivity.class);
                in.putExtra(Constants.ORDER_ID, ordersBean.getId());
                in.putExtra(Constants.APPLY_TO_APPOINTDETAIL, status);
                getActivity().startActivity(in);
                if (!adapter.getData().get(position).Hasread()) {
                    adapter.getData().get(position).setHas_read(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        xRecyclerView.setAdapter(adapter);
        phoneConsultingOrder();
        switch (status) {
            case ConsultationOrderStatus.WAITING:
                blankView.setText("没有新的预约");
                break;
            case ConsultationOrderStatus.CONFIRMED:
                blankView.setText("没有等待通话的预约");
                break;
            case ConsultationOrderStatus.FINISHED:
                blankView.setText("没有已完成的预约");
                break;
            default:
                break;
        }
    }

    private void phoneConsultingOrder() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.phoneConsultingOrder(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, status, Constants.PER_PAGE, page, new CustomCallBack<ConsultListBean>() {
            @Override
            public void onSuccess(ConsultListBean consultListBean) {
                if (unreadCountListener != null) {
                    int count = consultListBean.getConsulting_unread_count();
                    switch (status) {
                        case ConsultationOrderStatus.WAITING:
                            unreadCountListener.onWaiting(count);
                            break;
                        case ConsultationOrderStatus.CONFIRMED:
                            unreadCountListener.onConfirmed(count);
                            break;
                        case ConsultationOrderStatus.FINISHED:
                            unreadCountListener.onFinished(count);
                            break;
                        default:
                            break;
                    }
                }

                xRecyclerView.refreshComplete();
                List<ConsultListBean.OrdersBean> orders = consultListBean.getOrders();
                if (orders != null) {
                    if (page == 1) {
                        showContentView();
                        if (orders.size() == 0) {
                            blankView.setVisibility(View.VISIBLE);
                        } else {
                            blankView.setVisibility(View.GONE);
                        }
                        adapter.clear();
                    } else {
                        if (orders.size() == 0) {
                            xRecyclerView.noMoreLoading();
                        }
                    }
                    adapter.addAll(orders);
                    adapter.notifyDataSetChanged();
                }
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
        super.onRefresh();
        phoneConsultingOrder();
    }

    public void setUnreadCountListener(ImpGetConsultingUnreadCount unreadCountListener) {
        this.unreadCountListener = unreadCountListener;
    }

    public interface ImpGetConsultingUnreadCount {
        //  申请中: waiting, 待通话: confirmed, 已完成: finished
        void onWaiting(int count);

        void onConfirmed(int count);

        void onFinished(int count);
    }
}
