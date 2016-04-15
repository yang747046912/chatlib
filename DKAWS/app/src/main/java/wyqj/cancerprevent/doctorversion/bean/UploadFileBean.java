package wyqj.cancerprevent.doctorversion.bean;

import com.kaws.lib.http.ParamNames;

/**
 * Created by Administrator on 2016/4/11.
 */
public class UploadFileBean {
    @ParamNames("image_id")
    private String imageId;
    @ParamNames("image_url")
    private String imageUrl;

    public String getImageId() {
        return imageId;
    }
}
