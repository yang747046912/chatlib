package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.TipDialog;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;

public class KindOfDiseaseActivity extends LoadBaseActivity {

    private TextView tvTitlebarRighttitle;
    private GridView gvKindofdiseaseDisease;
    private ArrayList<UserBean.Diseased> orginData = new ArrayList<>();//源数据
    private ArrayList<UserBean.Diseased> selectedDiseases = new ArrayList<>();//选择的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kind_of_disease);
        showContentView();
        setTitle("擅长病种");
        getOriginData();
        getIntentData();
        initView();
        setUpView();
    }

    private void setUpView() {
        tvTitlebarRighttitle.setVisibility(View.VISIBLE);
        tvTitlebarRighttitle.setText("保存");
        tvTitlebarRighttitle.setTextColor(getResources().getColor(R.color.but_bg));
        gvKindofdiseaseDisease.setAdapter(new ChooseDiseaseAdapter(orginData));
        tvTitlebarRighttitle.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                int length = selectedDiseases.size();
                if (length == 0) {
                    TipDialog.hintDiaglog(KindOfDiseaseActivity.this, "请选择一个擅长病种");
                    return;
                }
                Intent out = new Intent();
                out.putExtra(Constants.CHOOSE_DISEASE, selectedDiseases);
                setResult(RESULT_OK, out);
                finishDefault();
            }
        });
    }

    private void initView() {
        gvKindofdiseaseDisease = getView(R.id.gv_kindofdisease_disease);
        tvTitlebarRighttitle = getView(R.id.tv_titlebar_righttitle);
    }

    /**
     * 初始化数据
     */
    private void getOriginData() {
        String[] diseasenames = getResources().getStringArray(R.array.diseasenames);
        for (int i = 0; i < diseasenames.length; i++) {
            int id = i;
            if (id == 11 || id == 12 || id == 13) {
                id = id + 3;
            } else if (id == 14) {
                id = 18;
            } else if (id == 15) {
                id = 100;
            }
            orginData.add(new UserBean.Diseased(id, diseasenames[i]));
        }
    }

    private void getIntentData() {
        selectedDiseases = (ArrayList<UserBean.Diseased>) getIntent().getSerializableExtra(Constants.CHOOSE_DISEASE);
    }

    private boolean contains(String str) {
        for (UserBean.Diseased dis : selectedDiseases) {
            if (dis.name.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    private void remove(String diseaseName) {
        for (UserBean.Diseased dis : selectedDiseases) {
            if (dis.name.equalsIgnoreCase(diseaseName)) {
                selectedDiseases.remove(dis);
                return;
            }
        }
    }
    private void gray(Button btn_diseasekind_item) {
        btn_diseasekind_item.setBackgroundResource(R.drawable.shape_kindofdisease);
        btn_diseasekind_item.setTextColor(getResources().getColor(R.color.text_hint));
    }

    private void normal(Button btn_diseasekind_item) {
        btn_diseasekind_item.setBackgroundResource(R.drawable.selector_kindofdisease_gridview);
        btn_diseasekind_item.setTextColor(getResources().getColorStateList(R.drawable.selector_kindofdisease_text));
    }


    public class ChooseDiseaseAdapter extends BaseAdapter {
        private ArrayList<UserBean.Diseased> data = new ArrayList<>();

        public ChooseDiseaseAdapter(ArrayList<UserBean.Diseased> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            View childView = View.inflate(KindOfDiseaseActivity.this, R.layout.item_kindofdisease_disease, null);
            final Button btn_diseasekind_item = (Button) childView.findViewById(R.id.btn_diseasekind_item);
            final String name = data.get(i).name;
            btn_diseasekind_item.setText(name);
            if (contains("全科") && selectedDiseases.size() >= 1) {
                if (i == 0) {
                    normal(btn_diseasekind_item);
                    btn_diseasekind_item.setSelected(true);
                } else {
                    gray(btn_diseasekind_item);
                }
            } else {
                if (i == 0) {
                    if (!selectedDiseases.isEmpty()) {
                        gray(btn_diseasekind_item);
                    }
                }
            }
            btn_diseasekind_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkData(view, name)) return;
                    if (name.equalsIgnoreCase("全科") && contains(name)) {
                       remove(name);
                        for (int i = 1; i < data.size(); i++) {
                            View v = gvKindofdiseaseDisease.getChildAt(i);
                            Button btn = (Button) v.findViewById(R.id.btn_diseasekind_item);
                            normal(btn);
                        }
                    } else if (name.equalsIgnoreCase("全科") && !contains(name)) {
                        selectedDiseases.add(data.get(i));
                        for (int i = 1; i < data.size(); i++) {
                            View v = gvKindofdiseaseDisease.getChildAt(i);
                            Button btn = (Button) v.findViewById(R.id.btn_diseasekind_item);
                            gray(btn);
                        }
                    } else if (!name.equalsIgnoreCase("全科") && contains(name)) {
                       remove(name);
                        View v = gvKindofdiseaseDisease.getChildAt(0);
                        Button btn = (Button) v.findViewById(R.id.btn_diseasekind_item);
                        if (!selectedDiseases.isEmpty()) {
                            gray(btn);
                        } else {
                            normal(btn);
                            btn.setSelected(false);
                        }
                    } else if (!name.equalsIgnoreCase("全科") && !contains(name)) {
                        selectedDiseases.add(data.get(i));
                        View v = gvKindofdiseaseDisease.getChildAt(0);
                        Button btn = (Button) v.findViewById(R.id.btn_diseasekind_item);
                        if (!selectedDiseases.isEmpty()) {
                            gray(btn);
                        } else {
                            normal(btn);
                            btn.setSelected(false);
                        }
                    }
                    view.setSelected(contains(name));
                }
            });
            btn_diseasekind_item.setSelected(contains(name));
            return childView;
        }

        private boolean checkData(View view, String name) {
            if (!("全科".equals(name) && selectedDiseases.size() == 1 && view.isSelected())) {
                //选了其他的就不能选择全科     选了全科就不能选其他的
                if (contains("全科") && selectedDiseases.size() >= 1) {
                    TipDialog.hintDiaglog(KindOfDiseaseActivity.this, "您已选择了全科");
                    return true;
                }
                if ("全科".equals(name) && selectedDiseases.size() > 0) {
                    TipDialog.hintDiaglog(KindOfDiseaseActivity.this, "选择其他的就不能选择全科");
                    return true;
                }
            }
            return false;
        }
    }

}
