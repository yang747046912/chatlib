package com.kaws.lib.common.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bugtags.library.Bugtags;
import com.kaws.lib.common.widget.LoadDialog;

/**
 * Created by 杨才 on 2016/1/10.
 */
public class BaseActivity extends FragmentActivity implements BaseFragment.IDialogAction {

    protected Dialog progressDialog;

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Bugtags.onDispatchTouchEvent(this, ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (hideInputMethod(this, v)) {
                    //  return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) { // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

    public void startProgressDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = LoadDialog.buildDialog(this);
            }
            progressDialog.show();
        } catch (Exception e) {
            Bugtags.sendException(e);

        }
    }

    public void stopProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            Bugtags.sendException(e);
        }
    }

    public void doUserActon(MotionEvent ev) {

    }

    @Override
    public void show() {
        startProgressDialog();
    }

    @Override
    public void dismiss() {
        stopProgressDialog();
    }

    /**
     * ******************************* 实现跳转效果 **************************************
     */
    /*
     * 通过传来的要跳转的类名
	 */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    public void openActivity(Class<?> pClass, Bundle pBundle) {
        openActivity(pClass, pBundle, null);
    }

    public void openActivity(Class<?> pClass, Bundle pBundle, Uri uri) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
    }

    /**
     * *************************** 关闭当前页面Finish *********************************
     */
    public void finishDefault() {
        finish();
    }
}