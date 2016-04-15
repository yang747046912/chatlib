package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.OnItemClickListener;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.BlankView;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.SearchAdapter;
import wyqj.cancerprevent.doctorversion.adapter.patient.PatientListAdapter;
import wyqj.cancerprevent.doctorversion.bean.ChooseBean;
import wyqj.cancerprevent.doctorversion.bean.PatientListBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class SearchActivity extends BaseActivity {
    /*变量声明*/
    private EditText serarchName;
    private RelativeLayout rlSearchShowTranslation;
    private ImageView icPersonalCustomClearEdt;
    private TextView tvSearcherCancel;
    private EditText etSearcherSearch;
    private XRecyclerView llSearcherResult;
    private BlankView ivSearcherBlankpage;
    private int searchType = 1; //搜索的标志 1表示患者 2 表示医院
    private String searchHint; //搜索默认的hint
    private BaseRecyclerViewAdapter adpter;
    private View blank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getIntentData();
        initView();
        setupView();
    }

    private void getIntentData() {
        Intent in = getIntent();
        searchType = in.getIntExtra(Constants.SEARCH_TYPE, 1);
        if (searchType == 1) {
            adpter = new PatientListAdapter();
        } else if (searchType == 2) {
            adpter = new SearchAdapter();
            adpter.setOnItemClickListener(new OnItemClickListener<ChooseBean>() {
                @Override
                public void onClick(ChooseBean chooseBean, int position) {
                    Intent out = new Intent();
                    out.putExtra(Constants.SEARCH_HOSPITAL, chooseBean);
                    setResult(RESULT_OK, out);
                    finishDefault();
                }
            });
        }
        searchHint = in.getStringExtra(Constants.SEARCH_HINT);
        if (TextUtils.isEmpty(searchHint)) {
            throw new RuntimeException("searchHint must not be null!!!");
        }
    }

    private void initView() {
        llSearcherResult = (XRecyclerView) findViewById(R.id.ll_searcher_result);
        etSearcherSearch = (EditText) findViewById(R.id.edt_personalCustom_search);
        tvSearcherCancel = (TextView) findViewById(R.id.btn_personalCustom_cancel);
        ivSearcherBlankpage = (BlankView) findViewById(R.id.iv_searcher_blankpage);
        icPersonalCustomClearEdt = (ImageView) findViewById(R.id.ic_personalCustom_clearEdt);
        rlSearchShowTranslation = (RelativeLayout) findViewById(R.id.rl_search_show_translation);
        serarchName = (EditText) findViewById(R.id.edt_personalCustom_search);
        blank = getView(R.id.blank_view);
    }

    private void setupView() {
        etSearcherSearch.setHint(searchHint);
        icPersonalCustomClearEdt.setOnClickListener(listener);
        blank.setOnClickListener(listener);
        tvSearcherCancel.setOnClickListener(listener);
        etSearcherSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    icPersonalCustomClearEdt.setVisibility(View.VISIBLE);
                    rlSearchShowTranslation.setVisibility(View.VISIBLE);
                    blank.setVisibility(View.GONE);
                } else if (searchType == 1) {
                    icPersonalCustomClearEdt.setVisibility(View.GONE);
                    rlSearchShowTranslation.setVisibility(View.GONE);
                    blank.setVisibility(View.VISIBLE);
                    adpter.clear();
                }
                String tmp = s.toString().trim();
                if (searchType == 1) {
                    seachPatient(tmp);
                } else if (searchType == 2) {
                    searchHospital(tmp);
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        llSearcherResult.setLayoutManager(layoutManager);
        llSearcherResult.setAdapter(adpter);
        llSearcherResult.setLoadingMoreEnabled(false);
        llSearcherResult.setPullRefreshEnabled(false);

        /** 点击键盘搜索键时隐藏软键盘*/
        hindSoftKeyboard();
    }



    /*访问网络*/
    private void seachPatient(String patientName) {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser squser = SQuser.getInstance();
        service.getSeachPatient(HttpHead.getHeader("GET"), squser.getUserInfo().token, squser.getUserInfo().doctorid, patientName, new CustomCallBack<ArrayList<PatientListBean>>() {
            @Override
            public void onSuccess(ArrayList<PatientListBean> patientListEntities) {
                if (patientListEntities.size() == 0) {
                    ivSearcherBlankpage.setVisibility(View.VISIBLE);
                } else {
                    ivSearcherBlankpage.setVisibility(View.GONE);
                }
                adpter.clear();
                adpter.addAll(patientListEntities);
                adpter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void searchHospital(String hospitalName) {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.searchHospital(HttpHead.getHeader("GET"), null, hospitalName, new CustomCallBack<List<ChooseBean>>() {
            @Override
            public void onSuccess(List<ChooseBean> chooseBeen) {
                if (chooseBeen.size() == 0) {
                    ivSearcherBlankpage.setVisibility(View.VISIBLE);
                } else {
                    ivSearcherBlankpage.setVisibility(View.GONE);
                }
                adpter.clear();
                adpter.addAll(chooseBeen);
                adpter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /*事件处理*/
    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            switch (view.getId()) {
                case R.id.blank_view:
                case R.id.btn_personalCustom_cancel:
                    finishDefault();
                    break;
                case R.id.ic_personalCustom_clearEdt:
                    etSearcherSearch.setText("");
                    icPersonalCustomClearEdt.setVisibility(View.GONE);
                    break;
            }
        }
    };

    /** 隐藏软键盘*/
    private void hindSoftKeyboard() {
        /** 处理键盘搜索键 */
        etSearcherSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (SearchActivity.this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (SearchActivity.this.getCurrentFocus() != null)
                        ((InputMethodManager) SearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

	/*返回操作*/

}
