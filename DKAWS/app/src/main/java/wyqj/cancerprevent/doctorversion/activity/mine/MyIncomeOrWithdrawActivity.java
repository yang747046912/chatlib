package wyqj.cancerprevent.doctorversion.activity.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;

import java.util.ArrayList;
import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.fragment.mine.MyIncomeRecordFragment;

public class MyIncomeOrWithdrawActivity extends LoadBaseActivity {

    private RadioGroup mGroup;
    private RadioButton left;
    private RadioButton middle;
    private RadioButton right;
    private TextView title;
    private ViewPager vp_my_income_or_withdraw;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_income_or_withdraw);
        showContentView();
        initView();
        setUpView();
    }

    private void setUpView() {
        initFragment();
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        vp_my_income_or_withdraw.setAdapter(myViewPagerAdapter);
        vp_my_income_or_withdraw.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                    default:
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_left:
                        vp_my_income_or_withdraw.setCurrentItem(0);
                        break;
                    case R.id.rb_middle:
                        vp_my_income_or_withdraw.setCurrentItem(1);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initFragment() {
        fragmentList.add(MyIncomeRecordFragment.getInstance(0));//收入记录
        fragmentList.add(MyIncomeRecordFragment.getInstance(1));//体现记录
    }

    private void initView() {
        title = (TextView) findViewById(R.id.tv_titlebar_title);
        title.setVisibility(View.GONE);
        mGroup = (RadioGroup) findViewById(R.id.select_radiogroup);
        mGroup.setVisibility(View.VISIBLE);
        left = (RadioButton) findViewById(R.id.rb_left);
        left.setText("收入记录");
        middle = (RadioButton) findViewById(R.id.rb_middle);
        middle.setText("提现记录");
        right = (RadioButton) findViewById(R.id.rb_right);
        right.setVisibility(View.GONE);
        vp_my_income_or_withdraw = (ViewPager) findViewById(R.id.vp_my_income_or_withdraw);
    }


    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
