package wyqj.cancerprevent.doctorversion.activity.square;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;
import com.kaws.lib.xrecyclerview.XRecyclerView;

import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.square.AnswersAdapter;
import wyqj.cancerprevent.doctorversion.adapter.square.TimelineAdapter;
import wyqj.cancerprevent.doctorversion.bean.MedicalRecordItemBean;
import wyqj.cancerprevent.doctorversion.bean.QuestionBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class IllNoteOthersTimelineActivity extends LoadBaseActivity {

    private ImageView ivTimelineLine;
    private XRecyclerView xrecyclerview;
    private TimelineAdapter adapter;
    private int page = 1;
    private String patientUserkey;
    private int type_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ill_note_others_timeline);
        setTitle("资料详情");
        initView();
        getExtras();
        setUpView();
    }

    private void initView() {
        xrecyclerview = (XRecyclerView) findViewById(R.id.xrecyclerview);
        ivTimelineLine = (ImageView) findViewById(R.id.iv_timeline_line);
    }

    private void setUpView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(IllNoteOthersTimelineActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrecyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getInfoDetail();
            }

            @Override
            public void onLoadMore() {
                page++;
                DebugUtil.debug("---->", "page: " + page);
                getInfoDetail();
            }
        });
        xrecyclerview.setLayoutManager(layoutManager);
        adapter = new TimelineAdapter();
        xrecyclerview.setAdapter(adapter);
        getInfoDetail();
    }

    private void getInfoDetail() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        SQuser sQuser = SQuser.getInstance();
        service.getIllNoteOthers(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, patientUserkey, type_id, page, Constants.PER_PAGE, new CustomCallBack<List<MedicalRecordItemBean>>() {
            @Override
            public void onSuccess(List<MedicalRecordItemBean> medicalRecordItemBean) {

                if (page == 1) {
                    showContentView();
//                    if (myAnswerBeans.size() == 0) {
//                        blankView.setVisibility(View.VISIBLE);
//                    }else {
//                        blankView.setVisibility(View.GONE);
//                    }
                    adapter.clear();
                } else {
                    if (medicalRecordItemBean.size() == 0) {
                        xrecyclerview.noMoreLoading();
                    }
                }
                adapter.addAll(medicalRecordItemBean);
                adapter.notifyDataSetChanged();
                xrecyclerview.refreshComplete();
                ivTimelineLine.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure() {
                xrecyclerview.refreshComplete();
                if (adapter.getItemCount() == 0) {
                    showRefrsh();
                }
                if (page > 1) {
                    page--;
                }
                ivTimelineLine.setVisibility(View.GONE);
            }
        });
    }

    private void getExtras() {
        patientUserkey = getIntent().getStringExtra("patientUserkey");
        int temp = getIntent().getExtras().getInt("position");
        if (temp == 0) {
            type_id = Constants.MEDICAL_RECORD_LABORATORY_REPORT;
        }
        if (temp == 1) {
            type_id = Constants.MEDICAL_RECORD_IMAGING_CHECK;
        }
        if (temp == 2) {
            type_id = Constants.MEDICAL_RECORD_PATHOLOGICAL_ANALYSIS;
        }
        if (temp == 3) {
            type_id = Constants.MEDICAL_RECORD_DIAGNOSED_SINGLE;
        }
        if (temp == 4) {
            type_id = Constants.MEDICAL_RECORD_MEDICATION_RECORDS;
        }
        if (temp == 5) {
            type_id = Constants.MEDICAL_RECORD_REVIEW_RECORD;
        }
        if (temp == 6) {
            type_id = Constants.MEDICAL_RECORD_HOSPITALIZATION_AND_OTHER;
        }
        if (temp == 7) {
            type_id = Constants.MEDICAL_RECORD_WOUND_PICTURES;
        }
    }
}
