package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.event.PerfectItemClickListener;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.ChooseAdapter;
import wyqj.cancerprevent.doctorversion.bean.ChooseBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;

public class ChooseHospitalActivity extends LoadBaseActivity {

    private TextView serchName;
    private TextView tvTitlebarRighttitle;
    private LinearLayout btnChoosehospitalSearcher;
    private ListView llChoosehospitalCity;
    private RelativeLayout choose_hospital;
    private ListView llChoosehospitalHospital;
    private ChooseAdapter cityAdapter;
    private int mPosition = -1;//当前选中城市的id
    private ChooseAdapter hospitalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_hospital);
        setTitle("请选择所在医院");
        initView();
        setUpView();
        getCity();
    }

    private void setUpView() {
        serchName.setText("搜索您所在的医院");
        serchName.setTextColor(getResources().getColor(R.color.text_hint));
        tvTitlebarRighttitle.setVisibility(View.VISIBLE);
        tvTitlebarRighttitle.setOnClickListener(listener);
        btnChoosehospitalSearcher.setOnClickListener(listener);
        llChoosehospitalCity.setOnItemClickListener(new PerfectItemClickListener() {
            @Override
            public void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mPosition) {
                    return;
                }
                mPosition = position;
                int cityID = cityAdapter.getCityEntities().get(position).getId();
                cityAdapter.setSelectItem(position);
                getHosipital(cityID);
            }
        });
        llChoosehospitalHospital.setOnItemClickListener(new PerfectItemClickListener() {
            @Override
            public void onNoDoubleItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra(Constants.CHOOSE_HOSPITAL, hospitalAdapter.getCityEntities().get(position));
                setResult(RESULT_OK, data);
                finishDefault();
            }
        });
    }

    private void initView() {
        serchName = getView(R.id.serch_name);
        tvTitlebarRighttitle = getView(R.id.tv_titlebar_righttitle);
        btnChoosehospitalSearcher = getView(R.id.btn_choosehospital_searcher);
        llChoosehospitalCity = getView(R.id.ll_choosehospital_city);
        choose_hospital = getView(R.id.choose_hospital);
        llChoosehospitalHospital = getView(R.id.ll_choosehospital_hospital);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            Intent intent;
            int id = v.getId();
            switch (id) {
                case R.id.tv_titlebar_righttitle:
                    intent = new Intent(ChooseHospitalActivity.this, NotFindHospitalActivity.class);
                    startActivityForResult(intent, Constants.CODE_NOT_FIND_HOSPITAL);
                    break;
                case R.id.btn_choosehospital_searcher:
                    choose_hospital.setVisibility(View.GONE);
                    Intent in = new Intent(ChooseHospitalActivity.this, SearchActivity.class);
                    in.putExtra(Constants.SEARCH_TYPE, 2);
                    in.putExtra(Constants.SEARCH_HINT, "请输入您所在的医院");
                    startActivityForResult(in, Constants.CODE_SEARCH_HOSPITAL);
                    break;
                default:
                    break;
            }
        }
    };

    private void getCity() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.getHospitalCity(HttpHead.getHeader("GET"), null, new CustomCallBack<List<ChooseBean>>() {
            @Override
            public void onSuccess(List<ChooseBean> chooseBeans) {
                cityAdapter = new ChooseAdapter(chooseBeans);
                llChoosehospitalCity.setAdapter(cityAdapter);
                cityAdapter.notifyDataSetChanged();
                cityAdapter.setSelectItem(0);
              //  llChoosehospitalCity.setItemChecked(0, true);
                getHosipital(chooseBeans.get(0).getId());
                showContentView();
            }

            @Override
            public void onFailure() {
                if (cityAdapter == null) {
                    showRefrsh();
                }
            }
        });
    }

    private void getHosipital(int provincheId) {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        service.getCityHospital(HttpHead.getHeader("GET"), null, provincheId, new CustomCallBack<List<ChooseBean>>() {
            @Override
            public void onSuccess(List<ChooseBean> chooseBeans) {
                hospitalAdapter = new ChooseAdapter(chooseBeans);
                hospitalAdapter.notifyDataSetChanged();
                llChoosehospitalHospital.setAdapter(hospitalAdapter);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.CODE_NOT_FIND_HOSPITAL) {
            if (data != null) {
                Intent out = new Intent();
                out.putExtra(Constants.CHOOSE_HOSPITAL, new ChooseBean(-1, data.getStringExtra(Constants.NOT_FIND_HOSPITAL)));
                setResult(RESULT_OK, out);
                finishDefault();
            }
        } else if (requestCode == Constants.CODE_SEARCH_HOSPITAL) {
            if (data != null) {
                Intent out = new Intent();
                out.putExtra(Constants.CHOOSE_HOSPITAL, data.getSerializableExtra(Constants.SEARCH_HOSPITAL));
                setResult(RESULT_OK, out);
                finishDefault();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        choose_hospital.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onRefresh() {
        super.showRefrsh();
        getCity();
    }
}
