package com.kaws.lib.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kaws.lib.common.R;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.DensityUtil;
import com.kaws.lib.common.utils.ScreenSizeUtil;

/**
 * Created by 杨才 on 2016/1/14.
 */
public class TipDialog {
    public Dialog dialog;
    private TextView title;
    private TextView message;
    private TextView psitive;
    private TextView negative;
    private Context context;

    public TipDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.notitle_dialog);
        View view = View.inflate(context, R.layout.tip_dialog, null);
        title = (TextView) view.findViewById(R.id.tv_titile);
        message = (TextView) view.findViewById(R.id.tv_message);
        psitive = (TextView) view.findViewById(R.id.tv_negative);
        negative = (TextView) view.findViewById(R.id.tv_positive);
        dialog.setContentView(view);
    }

    public void setTitle(CharSequence title) {
        this.title.setText(title);
        this.title.setVisibility(View.VISIBLE);
    }

    public void setMessage(CharSequence msg) {
        message.setText(msg);
        message.setVisibility(View.VISIBLE);
    }

    public void setPositive(String msg, final DialogInterface.OnClickListener listener) {
        psitive.setText(msg);
        psitive.setVisibility(View.VISIBLE);
        psitive.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (listener != null) {
                    listener.onClick(dialog, v.getId());
                }
            }
        });
    }

    public void setNegative(String msg, final DialogInterface.OnClickListener listener) {
        negative.setText(msg);
        negative.setVisibility(View.VISIBLE);
        negative.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (listener != null) {
                    listener.onClick(dialog, v.getId());
                }
            }
        });
    }

    public void show() {
        dialog.show();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = ScreenSizeUtil.getScreenWidth(context) - DensityUtil.dip2px(context, 30);    //宽度设置为屏幕的0.8
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);     //设置生效
        dialog.getWindow().setGravity(Gravity.CENTER);       //设置靠右对齐
    }


    public static void hintDiaglog(Context context, String msg) {
        final TipDialog tipDialog = new TipDialog(context);
        tipDialog.setPositive("好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        tipDialog.setMessage(msg);
        tipDialog.show();
    }
}
