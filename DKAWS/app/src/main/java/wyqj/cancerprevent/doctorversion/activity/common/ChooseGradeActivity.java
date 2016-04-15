package wyqj.cancerprevent.doctorversion.activity.common;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.cache.ACache;
import com.kaws.lib.common.event.PerfectItemClickListener;
import com.kaws.lib.common.utils.ActivityStackUtil;

import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.RegisterBean;

public class ChooseGradeActivity extends LoadBaseActivity {
    private static final String[] gradenames = {"主任医师", "副主任医师", "主治医师", "住院医师", "博士研究生"};
    private ListView grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_grade);
        showContentView();
        setTitle("选择您的职称");
        grade = getView(R.id.ll_choosegrade_grade);
        grade.setAdapter(new ChooseGradeAdapter());
        grade.setOnItemClickListener(new PerfectItemClickListener() {
            @Override
            public void onNoDoubleItemClick(AdapterView<?> parent, View view, int i, long id) {
                ACache aCache = ACache.get(ChooseGradeActivity.this);
                RegisterBean bean = (RegisterBean) aCache.getAsObject(Constants.REGISTER);
                if (bean != null) {
                    if (i == 4) {
                        bean.degree = i + 1;
                    } else {
                        bean.degree = i;
                    }
                    aCache.put(Constants.REGISTER, bean);
                }
                openActivity(CompleteInfoActivity.class);
            }
        });
        ActivityStackUtil.getInstance().add(this);
    }

    class ChooseGradeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return gradenames.length;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(ChooseGradeActivity.this, R.layout.item_choosegrade_grade, null);
            TextView tv_choosegrade_gradename = (TextView) view1.findViewById(R.id.tv_choosegrade_gradename);
            tv_choosegrade_gradename.setText(gradenames[i]);
            return view1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

    }

    @Override
    protected void onDestroy() {
        ActivityStackUtil.getInstance().remove(this);
        super.onDestroy();
    }
}
