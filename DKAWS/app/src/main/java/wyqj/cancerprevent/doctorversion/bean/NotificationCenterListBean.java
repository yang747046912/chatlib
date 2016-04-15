package wyqj.cancerprevent.doctorversion.bean;

/**
 * Created by 景彬 on 2016/3/28.
 * 我的 -- 通知中心
 */
public class NotificationCenterListBean {
    private String id;
    /**
     * 1019为电话咨询详情页的Type, 客户端也是
     */
    private int type;
    private String title;
    private String content;
    private String item_pk;
    private String created_at;

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getItem_pk() {
        return item_pk;
    }

    public String getCreated_at() {
        return created_at;
    }

}
