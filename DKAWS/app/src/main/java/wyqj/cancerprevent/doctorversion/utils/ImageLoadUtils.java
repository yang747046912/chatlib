package wyqj.cancerprevent.doctorversion.utils;

import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.io.File;

import retrofit.mime.TypedFile;
import wyqj.cancerprevent.doctorversion.bean.UploadFileBean;
import wyqj.cancerprevent.doctorversion.bean.UptokenBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;

/**
 * Created by Administrator on 2016/4/11.
 */
public class ImageLoadUtils {
    private static void getUploadToken(final BaseActivity activity, final String filePath, final ImplUploadListener listener) {
        activity.startProgressDialog();
        HttpUtils.getInstance().getHttpService(HttpService.class).getQiniuUpToken(HttpHead.getHeader("GET"), new CustomCallBack<UptokenBean>() {
            @Override
            public void onSuccess(UptokenBean uptokenBean) {
                uploadFileToQinniu(activity, uptokenBean.getUptoken(), filePath, listener);
            }

            @Override
            public void onFailure() {
                if (listener != null) {
                    listener.onFailure();
                }
                activity.stopProgressDialog();
            }
        });
    }

    private static void uploadFileToQinniu(final BaseActivity activity, String token, String filePath, final ImplUploadListener listener) {
        File file = new File(filePath);
        TypedFile typedFile = new TypedFile("image/jpg", file);
        HttpUtils.getInstance().getQinniuService(HttpService.class).uploadImageQinniu(token, typedFile, new CustomCallBack<UploadFileBean>() {
            @Override
            public void onSuccess(UploadFileBean uploadFileBean) {
                if (listener != null) {
                    listener.onSuccess(uploadFileBean.getImageId());
                }
            }

            @Override
            public void onFailure() {
                if (listener != null) {
                    listener.onFailure();
                }
                activity.stopProgressDialog();
            }
        });
    }


    public static void uploadFile(final BaseActivity activity, final String filePath, ImplUploadListener listener) {
        File file = new File(filePath);
        if (!file.exists()) {
            if (listener != null) {
                listener.onFailure();
            }
            return;
        }
        getUploadToken(activity, filePath, listener);
    }

    public interface ImplUploadListener {
        void onSuccess(String imageID);

        void onFailure();
    }
}
