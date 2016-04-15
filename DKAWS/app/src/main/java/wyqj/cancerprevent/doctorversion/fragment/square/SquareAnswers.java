package wyqj.cancerprevent.doctorversion.fragment.square;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.kaws.lib.common.base.NoTitleLoadBaseFragment;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.common.widget.BlankView;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.xrecyclerview.XRecyclerView;

import java.util.List;

import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.square.AnswersAdapter;
import wyqj.cancerprevent.doctorversion.bean.QuestionBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;


public class SquareAnswers extends NoTitleLoadBaseFragment {
    private XRecyclerView xRecyclerView;
    private BlankView blankView;
    private int page = 1;
    private AnswersAdapter adapter;

    @Override
    public int setContent() {
        return R.layout.fragment_square_answers;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setUpView();
    }

    private void setUpView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getMyAnswer();
            }

            @Override
            public void onLoadMore() {
                page++;
                DebugUtil.debug("---->", "page: " + page);
                getMyAnswer();
            }
        });
        xRecyclerView.setLayoutManager(layoutManager);
        adapter = new AnswersAdapter();
        xRecyclerView.setAdapter(adapter);
        getMyAnswer();
    }

    private void initView() {
        xRecyclerView = getView(R.id.xrecyclerview);
        blankView = getView(R.id.blank);
    }

    private void getMyAnswer() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.getMyAnswer(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, page, Constants.PER_PAGE, new CustomCallBack<List<QuestionBean>>() {
            @Override
            public void onSuccess(List<QuestionBean> myAnswerBeans) {

                if (page == 1) {
                    showContentView();
                    if (myAnswerBeans.size() == 0) {
                        blankView.setVisibility(View.VISIBLE);
                    }else {
                        blankView.setVisibility(View.GONE);
                    }
                    adapter.clear();
                } else {
                    if (myAnswerBeans.size() == 0) {
                        xRecyclerView.noMoreLoading();
                    }
                }
                adapter.addAll(myAnswerBeans);
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
        getMyAnswer();
    }
}
