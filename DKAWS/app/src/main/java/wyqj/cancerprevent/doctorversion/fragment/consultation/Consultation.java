package wyqj.cancerprevent.doctorversion.fragment.consultation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseFragment;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.BlankView;
import com.kaws.lib.common.widget.UnderlinePageIndicator;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.common.UploadWorkPicActivity;
import wyqj.cancerprevent.doctorversion.activity.common.UploadWorkPicDoctorActivity;
import wyqj.cancerprevent.doctorversion.activity.mobileconsult.AppointSettingActivity;
import wyqj.cancerprevent.doctorversion.bean.CheckBean;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.constant.ConsultationOrderStatus;
import wyqj.cancerprevent.doctorversion.constant.DoctorStatus;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;


public class Consultation extends LoadBaseFragment {
    private TextView setting;
    private ImageView back;
    private UnderlinePageIndicator mIndicator;
    private RadioGroup group;
    private ViewPager pager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private BlankView blankView;
    private LinearLayout content;
    //  申请中: waiting, 待通话: confirmed, 已完成: finished
    private ConsultationMatter waiting;
    private ConsultationMatter confirmed;
    private ConsultationMatter finished;


    @Override
    public int setContent() {
        return R.layout.fragment_consultation;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTitle("电话咨询");
        initView();
        setUpView();
    }

    private void initView() {
        setting = getView(R.id.tv_titlebar_righttitle);
        back = getView(R.id.btn_titlebar_back);
        group = getView(R.id.rg_group);
        mIndicator = getView(R.id.indicator);
        pager = getView(R.id.vp_pager);
        blankView = getView(R.id.blank);
        content = getView(R.id.ll_content);
    }

    private void setUpView() {
        setting.setText("预约设置");
        setting.setVisibility(View.VISIBLE);
        setting.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                SQuser sQuser = SQuser.getInstance();
                int state = sQuser.getUserInfo().status;
                if (state == DoctorStatus.CONFIRMED) {
                    getActivity().startActivity(new Intent(getActivity(), AppointSettingActivity.class));
                } else {
                    Uri uri = Uri.parse("tel:400-101-1510");
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);
                    startActivity(intent);
                }
            }
        });
        back.setVisibility(View.GONE);
        pager.setOffscreenPageLimit(3);
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private void checkDoctorState() {
        SQuser sQuser = SQuser.getInstance();
        if (sQuser.getUserInfo().status == DoctorStatus.CONFIRMED) {
            doAfterCheck();
        } else {
            setting.setText("致电客服");
            doCheck();
        }
    }

    private void doAfterCheck() {
        if (fragmentList.size() == 3) {
            return;
        }
        ConsultationMatter.ImpGetConsultingUnreadCount listener = new ConsultationMatter.ImpGetConsultingUnreadCount() {
            @Override
            public void onWaiting(int count) {

            }

            @Override
            public void onConfirmed(int count) {

            }

            @Override
            public void onFinished(int count) {

            }
        };
        showContentView();
        waiting = ConsultationMatter.newInstance(ConsultationOrderStatus.WAITING);
        confirmed = ConsultationMatter.newInstance(ConsultationOrderStatus.CONFIRMED);
        finished = ConsultationMatter.newInstance(ConsultationOrderStatus.FINISHED);
        waiting.setUnreadCountListener(listener);
        finished.setUnreadCountListener(listener);
        confirmed.setUnreadCountListener(listener);
        fragmentList.add(waiting);
        fragmentList.add(confirmed);
        fragmentList.add(finished);
        pager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager()));
        mIndicator.setViewPager(pager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int id) {
                switch (id) {
                    case 2:
                        group.check(R.id.rbtn_complete);
                        break;
                    case 1:
                        group.check(R.id.rbtn_to_be_call);
                        break;
                    case 0:
                        group.check(R.id.rbtn_applying);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn_complete:
                        pager.setCurrentItem(2);
                        break;
                    case R.id.rbtn_to_be_call:
                        pager.setCurrentItem(1);
                        break;
                    case R.id.rbtn_applying:
                        pager.setCurrentItem(0);
                        break;
                    default:
                        break;
                }
            }
        });

    }


    private void doCheck() {
        HttpService service = HttpUtils.getInstance().getHttpService(HttpService.class);
        final SQuser sQuser = SQuser.getInstance();
        service.confirmedStatus(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, new CustomCallBack<CheckBean>() {
            @Override
            public void onSuccess(CheckBean checkBean) {
                if (checkBean.getStatus() == DoctorStatus.CONFIRMED) {
                    SQuser.getInstance().getUserInfo().status = checkBean.getStatus();
                    doAfterCheck();
                } else if (checkBean.getStatus() == DoctorStatus.JUST_REGISTERED || checkBean.getStatus() == DoctorStatus.REGISTER_FAILED) {
                    showContentView();
                    content.setVisibility(View.GONE);
                    blankView.setVisibility(View.VISIBLE);
                    blankView.setText("此服务需要认证通过\n后才可以开启使用");
                    blankView.getClickButton().setVisibility(View.VISIBLE);
                    blankView.getClickButton().setOnClickListener(new PerfectClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            getToCheck();
                        }
                    });
                } else if (checkBean.getStatus() == DoctorStatus.PENDING) {
                    showContentView();
                    content.setVisibility(View.GONE);
                    blankView.setVisibility(View.VISIBLE);
                    blankView.getClickButton().setVisibility(View.GONE);
                    blankView.setText("认证正在审核中...\n请耐心等待");
                }
            }

            @Override
            public void onFailure() {
                showRefrsh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        checkDoctorState();
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
        doCheck();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            checkDoctorState();
        }
    }

    /**
     * 跳转的认证页面
     */
    private void getToCheck() {
        UserBean.Doctor d = SQuser.getInstance().getUserInfo().info;
        int status = SQuser.getInstance().getUserInfo().status;
        if (d != null) {
            switch (status) {
                case DoctorStatus.JUST_REGISTERED://aaa
                case DoctorStatus.REGISTER_FAILED:
                    if ("博士研究生".equals(d.degree.name)) {
                        Intent picIntent = new Intent(getActivity(), UploadWorkPicDoctorActivity.class);
                        startActivity(picIntent);
                    } else {
                        Intent picIntent = new Intent(getActivity(), UploadWorkPicActivity.class);
                        startActivity(picIntent);
                    }
                    break;
            }
        }
    }
}
