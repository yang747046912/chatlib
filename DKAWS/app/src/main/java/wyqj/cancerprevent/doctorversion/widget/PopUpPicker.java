package wyqj.cancerprevent.doctorversion.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.wheel.WheelView;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.MyWheelAdapter;

/**
 * Created by 杨才 on 2016/3/25.
 */
public class PopUpPicker extends PopupWindow {
    private OnOKListener listener;
    private Activity context;
    private TextView btn_pop_submit;
    private TextView btn_pop_cancel;
    private WheelView wheelView_popup;
    private String[] list_adapter;

    public PopUpPicker(Activity context) {
        super(context);
        this.context = context;
        View v = View.inflate(context, R.layout.popu_picker, null);
        setContentView(v);
        initView();
        setUpView();
        initPopUp();
    }

    private void initView() {
        btn_pop_submit = (TextView) getContentView().findViewById(R.id.btn_pop_submit);
        btn_pop_cancel = (TextView) getContentView().findViewById(R.id.btn_pop_cancel);
        wheelView_popup = (WheelView) getContentView().findViewById(R.id.wheelView_popup);
    }

    private void setUpView() {
        list_adapter = context.getResources().getStringArray(R.array.refuse_reason);
        wheelView_popup.setViewAdapter(new MyWheelAdapter(list_adapter, context));
        btn_pop_cancel.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                dismiss();
            }
        });
        btn_pop_submit.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (listener != null) {
                    listener.onOK(list_adapter[wheelView_popup.getCurrentItem()]);
                }
                dismiss();
            }
        });
    }

    public void initPopUp() {
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow的View
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popupwindow_style);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x55000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }


    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }

    public void show(int id) {
        View v = context.findViewById(id);
        showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {

        WindowManager.LayoutParams lp = (context.getWindow().getAttributes());
        lp.alpha = bgAlpha; // 0.0-1.0
        context.getWindow().setAttributes(lp);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }

    public void setListener(OnOKListener listener) {
        this.listener = listener;
    }

    public interface OnOKListener {
        void onOK(String relust);
    }
}


