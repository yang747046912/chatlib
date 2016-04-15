package wyqj.cancerprevent.doctorversion.bean;

import com.kaws.lib.http.ParamNames;

/**
 * 项目名称：WorkSpace_SourceTree
 * 类名称：SplashImgBean
 * 创建人：xiaguangcheng
 * 修改人：xiaguangcheng
 * 修改时间：2015/9/6 14:22
 * 修改备注：
 */

public class SplashImgBean {
    //splashimg 地址
    @ParamNames("url")
    private String url;
    @ParamNames("type")
    private int type;
    @ParamNames("item_pk")
    private String item_pk;

    public String getItem_pk() {
        return item_pk;
    }

    public int getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
