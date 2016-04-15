package wyqj.cancerprevent.doctorversion.fragment.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseFragment;
import com.kaws.lib.common.utils.DebugUtil;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.constant.Constants;


public class Patient extends LoadBaseFragment {
    //    List<Fragment> fragmentList = new ArrayList<Fragment>();
    private String[] names = {"咨询记录", "患者列表", "申请记录"};
    private PagerAdapter pagerAdapter;
    private ViewPager viewpager_mypatient;
    /*咨询记录*/ PatientConsultRecord askRecordFragment;
    /*患者列表*/ PatientPatientList patientListFragment;
    /*申请记录*/ PatientApplyRecord applyRecordFragment;
    ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private ImageView back;
    private TextView title;
    private RadioGroup mGroup;
    private RadioButton left;
    private RadioButton middle;
    private RadioButton right;
    private LinearLayout private_doctor;

    @Override
    public int setContent() {
        return R.layout.fragment_patient;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        initView();
        setUpView();
    }

    private void initView() {
        viewpager_mypatient = getView(R.id.viewpager_mypatient);
        back = getView(R.id.btn_titlebar_back);
        back.setVisibility(View.GONE);
        title = getView(R.id.tv_titlebar_title);
        title.setVisibility(View.GONE);
        mGroup = getView(R.id.select_radiogroup);
        mGroup.setVisibility(View.VISIBLE);
        left = getView(R.id.rb_left);
        left.setText(names[0]);
        middle = getView(R.id.rb_middle);
        middle.setText(names[1]);
        right = getView(R.id.rb_right);
        right.setText(names[2]);
        private_doctor = getView(R.id.layout_private_doctor);
        private_doctor.setVisibility(View.GONE);
    }

    private void setUpView() {
        askRecordFragment = new PatientConsultRecord();
        patientListFragment = new PatientPatientList();
        applyRecordFragment = new PatientApplyRecord();
        fragmentList.add(askRecordFragment);
        fragmentList.add(patientListFragment);
        fragmentList.add(applyRecordFragment);

        if (pagerAdapter == null) {
            pagerAdapter = new PagerAdapter(getChildFragmentManager());//嵌套viewpager（内含fragment），内容不显示
            viewpager_mypatient.setAdapter(pagerAdapter);
        } else {
            pagerAdapter.notifyDataSetChanged();
        }

        viewpager_mypatient.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mGroup.check(R.id.rb_left);
                        break;
                    case 1:
                        mGroup.check(R.id.rb_middle);
                        break;
                    case 2:
                        mGroup.check(R.id.rb_right);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewpager_mypatient.setOffscreenPageLimit(3);
        initListener();
    }

    private void initListener() {
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_left:
                        setCurrentItem(0);
                        break;
                    case R.id.rb_middle:
                        setCurrentItem(1);
                        break;
                    case R.id.rb_right:
                        setCurrentItem(2);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setCurrentItem(int item) {
        if (viewpager_mypatient.getCurrentItem() != item) {
            viewpager_mypatient.setCurrentItem(item);
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        if (askRecordFragment != null) {
            askRecordFragment.refresh();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DebugUtil.debug("---->", "requestCode ：" + requestCode);

        DebugUtil.debug("---->", "resultCode ：" + resultCode);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == Constants.CODE_APPLY_ID) {
        }
    }
}
