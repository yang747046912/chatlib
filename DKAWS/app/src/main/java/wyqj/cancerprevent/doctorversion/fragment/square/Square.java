package wyqj.cancerprevent.doctorversion.fragment.square;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseFragment;

import java.util.ArrayList;
import java.util.List;

import wyqj.cancerprevent.doctorversion.R;


public class Square extends LoadBaseFragment {
    private ViewPager viewpagerConsult;
    /**
     * 左边框
     */
    private RadioButton rb_left;
    /**
     * 右边框
     */
    private RadioButton rb_right;
    /**
     * 中间框
     */
    private RadioButton rb_middle;
    /**
     * radioBUtton组
     */
    private RadioGroup select_radiogroup;
    /**
     * 后退箭头
     */
    private ImageView btn_titlebar_back;
    /**
     * 标题
     */
    private TextView tv_titlebar_title;
    private SquareConsult consultFragment;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private SquareAnswers myAnswerFragment;

    @Override
    public int setContent() {
        return R.layout.fragment_square;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        inintView();
        setUpView();
    }

    private void inintView() {
        viewpagerConsult = getView(R.id.viewpager_consult);
        tv_titlebar_title = getView(R.id.tv_titlebar_title);
        tv_titlebar_title.setVisibility(View.GONE);
        btn_titlebar_back = getView(R.id.btn_titlebar_back);
        btn_titlebar_back.setVisibility(View.GONE);
        select_radiogroup = getView(R.id.select_radiogroup);
        select_radiogroup.setVisibility(View.VISIBLE);
        rb_left = getView(R.id.rb_left);
        rb_right = getView(R.id.rb_right);
        rb_left.setText("咨询广场");
        rb_left.setChecked(true);
        rb_right.setText("我的回答");
        rb_middle = getView(R.id.rb_middle);
        rb_middle.setVisibility(View.GONE);
    }

    private void setUpView() {
        consultFragment = new SquareConsult();
        myAnswerFragment = new SquareAnswers();

        fragmentList.add(0, consultFragment);
        fragmentList.add(1, myAnswerFragment);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());//嵌套viewpager（内含fragment），内容不显示
        viewpagerConsult.setAdapter(myViewPagerAdapter);

        viewpagerConsult.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb_left.setChecked(true);
                        rb_right.setChecked(false);
                        break;
                    case 1:
                        rb_right.setChecked(true);
                        rb_left.setChecked(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        select_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rb_left:
                        viewpagerConsult.setCurrentItem(0);
                        break;
                    case R.id.rb_right:
                        viewpagerConsult.setCurrentItem(1);
                        break;

                }
            }
        });

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
