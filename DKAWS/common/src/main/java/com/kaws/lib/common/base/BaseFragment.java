package com.kaws.lib.common.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by 杨才 on 2016/1/18.
 */
public class BaseFragment extends Fragment {
    private IDialogAction dialogAction;

    protected <T extends View> T getView(int id) {
        return (T) getView().findViewById(id);
    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof IDialogAction) {
            dialogAction = (IDialogAction) context;
        } else {
            throw new IllegalArgumentException(context + " must implement interface " + BaseFragment.IDialogAction.class.getSimpleName());
        }
    }
    interface IDialogAction {
        public void show();

        public void dismiss();
    }
    protected void startProgressDialog() {
        if (dialogAction != null) {
            dialogAction.show();
        }
    }
    protected void stopProgressDialog() {
        if (dialogAction != null) {
            dialogAction.dismiss();
        }
    }
}
