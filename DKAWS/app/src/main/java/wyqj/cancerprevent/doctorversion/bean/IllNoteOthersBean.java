package wyqj.cancerprevent.doctorversion.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jb on 2016/3/22.
 */
public class IllNoteOthersBean {

    @SerializedName("content")
    private String content;

    @SerializedName("images")
    private List<ImageBean> images;

    public String getContent() {
        return content;
    }

    public List<ImageBean> getImages() {
        return images;
    }

    public class ImageBean {

        @SerializedName("id")
        private String id;
        @SerializedName("url")
        private String url;

        public String getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }
    }
}
