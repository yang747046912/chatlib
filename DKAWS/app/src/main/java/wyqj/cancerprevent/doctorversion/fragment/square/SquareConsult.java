package wyqj.cancerprevent.doctorversion.fragment.square;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.NoTitleLoadBaseFragment;
import com.kaws.lib.common.event.PerfectClickListener;
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
import wyqj.cancerprevent.doctorversion.widget.ListViewPopup;


public class SquareConsult extends NoTitleLoadBaseFragment {
    private final static String[] DISEASE_NAMES = {"擅长病种", "全科", "肺癌", "肝癌", "胃癌", "食道癌", "直肠癌", "宫颈癌", "乳腺癌", "鼻咽癌", "白血病", "淋巴癌", "卵巢癌", "甲状腺癌", "肾癌", "胰腺癌", "其他"};
    private final static String[] QUESTIONS = {"最新提问", "价格最高"};
    private XRecyclerView xRecyclerView;
    private int page = 1;
    private AnswersAdapter adapter;
    private BlankView blankView;
    private String sortBy = "created_at";
    private Integer diseaseKind = null;
    private RelativeLayout goodAtKind;
    private RelativeLayout questionKind;
    private TextView goodAtKindName;
    private TextView questionKindName;
    private ListViewPopup goodAtPopup;
    private ListViewPopup questionPopup;

    @Override
    public int setContent() {
        return R.layout.fragment_square_consult;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setUpView();
        getQuestionList();
    }

    private void setUpView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getQuestionList();
            }

            @Override
            public void onLoadMore() {
                page++;
                getQuestionList();
            }
        });
        xRecyclerView.setLayoutManager(layoutManager);
        adapter = new AnswersAdapter();
        xRecyclerView.setAdapter(adapter);
        goodAtKind.setOnClickListener(listener);
        questionKind.setOnClickListener(listener);
    }

    private void initView() {
        xRecyclerView = getView(R.id.xrecyclerview);
        blankView = getView(R.id.blank);
        goodAtKind = getView(R.id.rl_consult_goodat);
        questionKind = getView(R.id.rl_consult_question);
        goodAtKindName = getView(R.id.tv_consult_goodat);
        questionKindName = getView(R.id.tv_consult_newquestion);
    }

    private void getQuestionList() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.getQuestionList(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sortBy, sQuser.getUserInfo().doctorid, diseaseKind, page, Constants.PER_PAGE, new CustomCallBack<List<QuestionBean>>() {
            @Override
            public void onSuccess(List<QuestionBean> myAnswerBeans) {
                if (page == 1) {
                    showContentView();
                    if (myAnswerBeans.size() == 0) {
                        blankView.setVisibility(View.VISIBLE);
                    } else {
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

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.rl_consult_question:
                    showQuestion();
                    break;
                case R.id.rl_consult_goodat:
                    showGoodAtPopup();
                    break;
                default:
                    break;
            }
        }
    };

    private void showGoodAtPopup() {
        goodAtKind.setSelected(true);
        if (goodAtPopup == null) {
            goodAtPopup = new ListViewPopup(getActivity(), new ListViewPopup.ISelecterLinstener() {
                @Override
                public void onSelected(int gradeID) {
                    if (blankView.getVisibility() != View.GONE) {
                        blankView.setVisibility(View.GONE);
                    }
                    showLoading();
                    page = 1;
                    goodAtKindName.setText(DISEASE_NAMES[gradeID]);
                    if (gradeID == 0) {
                        diseaseKind = null;
                    } else {
                        gradeID = gradeID - 1;
                        if (gradeID == 11 || gradeID == 12 || gradeID == 13) {
                            gradeID = gradeID + 3;
                        } else if (gradeID == 14) {
                            gradeID = 18;
                        } else if (gradeID == 15) {
                            gradeID = 100;
                        }
                        diseaseKind = gradeID;
                    }
                    goodAtPopup.dismiss();
                    xRecyclerView.reset();
                    getQuestionList();
                }
            }, DISEASE_NAMES);
        }
        goodAtPopup.setFocusable(true);
        if (!goodAtPopup.isShowing()) {
            goodAtPopup.showAsDropDown(goodAtKind);
        }
        goodAtPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                goodAtKind.setSelected(false);
            }
        });
    }

    private void showQuestion() {
        questionKind.setSelected(true);
        if (questionPopup == null) {
            questionPopup = new ListViewPopup(getActivity(), new ListViewPopup.ISelecterLinstener() {
                @Override
                public void onSelected(int gradeID) {
                    if (blankView.getVisibility() != View.GONE) {
                        blankView.setVisibility(View.GONE);
                    }
                    showLoading();
                    page = 1;
                    questionKindName.setText(QUESTIONS[gradeID]);
                    questionKindName.setSelected(false);
                    if (gradeID == 0) {
                        sortBy = "created_at";
                    } else {
                        sortBy = null;
                    }
                    questionPopup.dismiss();
                    xRecyclerView.reset();
                    getQuestionList();
                }
            }, QUESTIONS);
        }
        if (!questionPopup.isShowing()) {
            questionPopup.showAsDropDown(questionKind);
        }
        questionPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                questionKind.setSelected(false);
            }
        });
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        getQuestionList();
    }
}
