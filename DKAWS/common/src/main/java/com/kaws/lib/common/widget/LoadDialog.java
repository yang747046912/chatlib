package com.kaws.lib.common.widget;

import android.app.Dialog;
import android.content.Context;

import com.kaws.lib.common.R;

/**
 * Created by 杨才 on 2016/1/18.
 */
public class LoadDialog {

    public static Dialog buildDialog(Context context) {
        Dialog progressDialog = new Dialog(context, R.style.notitle_dialog);
        progressDialog.setContentView(R.layout.net_loading);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
