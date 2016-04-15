package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.common.utils.SharedPreferencesUtil;

import wyqj.cancerprevent.doctorversion.R;

public class SplashActivity extends BaseActivity {

    private SliderLayout splash;
    private int[] drawables = new int[]{R.mipmap.b_1, R.mipmap.b_2, R.mipmap.b_3, R.mipmap.b_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesUtil.getRunConfig(SplashActivity.this)) {
            openActivity(CanerLogo.class);
            finishDefault();
            return;
        }
        setContentView(R.layout.activity_splash);
        splash = getView(R.id.slider);
        int length = drawables.length;
        for (int i = 0; i < length; i++) {
            splash.addSlider(new GuidanceItem(this, drawables[i], i));
        }
        splash.disableCycle();
        splash.setPosition(0);
    }

    class GuidanceItem extends BaseSliderView {
        private int drawable;
        private int itemIndex;

        protected GuidanceItem(Context context, int drawable, int itemIndex) {
            this(context);
            this.drawable = drawable;
            this.itemIndex = itemIndex;
        }

        protected GuidanceItem(Context context) {
            super(context);
        }

        @Override
        public View getView() {
            View v = View.inflate(SplashActivity.this, R.layout.splash_item, null);
            v.setBackgroundResource(drawable);
            if (itemIndex == drawables.length - 1) {
                //intent.setVisibility(View.VISIBLE);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferencesUtil.saveRunConfig(SplashActivity.this, true);
                        openActivity(CanerLogo.class);
                        finishDefault();
                    }
                });
            }
            return v;
        }
    }
}
