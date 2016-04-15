package wyqj.cancerprevent.doctorversion.http;

import com.kaws.lib.common.utils.CommonUtil;
import com.kaws.lib.common.utils.ToastUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wyqj.cancerprevent.doctorversion.app.App;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;

/**
 * Created by 杨才 on 2016/2/1.
 */
public abstract class CustomCallBack<T> implements Callback<T> {

    @Override
    public void success(T t, Response response) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure();

    @Override
    public void failure(RetrofitError retrofitError) {
        onFailure();
        if (!CommonUtil.isNetWorkConnected(App.getInstance())) {
            ToastUtils.showToast(App.getInstance(), "无法连接服务器，请检查网络", 2000, 2);
        } else {
            if (retrofitError != null) {
                try {
                    ErrorBean errorBean = (ErrorBean) retrofitError.getBodyAs(ErrorBean.class);
                    if (errorBean != null && errorBean.getMessage() != null) {
                        ToastUtils.showToast(App.getInstance(), errorBean.getMessage(), 2000, 2);
                    } else {
                        ToastUtils.showToast(App.getInstance(), "数据加载失败,请重试", 2000, 2);
                    }
                } catch (Exception exception) {
                    ToastUtils.showToast(App.getInstance(), "数据加载失败,请重试", 2000, 2);
                }
            }
        }
    }
}
