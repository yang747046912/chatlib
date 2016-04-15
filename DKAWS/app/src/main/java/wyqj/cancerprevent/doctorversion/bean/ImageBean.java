package wyqj.cancerprevent.doctorversion.bean;

import com.kaws.lib.http.ParamNames;

/**
 * Created by xiaguangcheng on 16/3/26.
 */
public class ImageBean {
    @ParamNames("id")
    private String id;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    @ParamNames("url")

    private String url;
}
