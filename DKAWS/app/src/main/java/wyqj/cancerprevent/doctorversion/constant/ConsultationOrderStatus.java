package wyqj.cancerprevent.doctorversion.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 杨才 on 2016/3/27.
 */
public class ConsultationOrderStatus {
    //  申请中: waiting, 待通话: confirmed, 已完成: finished
    public static final int WAITING = 1;
    public static final int CONFIRMED = 2;
    public static final int FINISHED = 3;

    /**
     * 待付款
     */
    public static final String KAWS_PENDING = "kaws_pending";//待付款
    /**
     * 待审核
     */
    public static final String KAWS_WAITING = "kaws_waiting";// 待审核
    /**
     * 待通话
     */
    public static final String KAWS_CONFIRMED = "kaws_confirmed";// 待通话
    /**
     * 需完善
     */
    public static final String KAWS_RETURNED = "kaws_returned";//需完善
    /**
     * 已通话
     */
    public static final String KAWS_FINISHED = "kaws_finished"; //已通话
    /**
     * 已完成
     */
    public static final String KAWS_OVER = "kaws_over";//已完成
    /**
     * 退款中
     */
    public static final String KAWS_REFUNDING = "kaws_refunding";//退款中
    /**
     * 已退款
     */
    public static final String KAWS_REFUNDED = "kaws_refunded";//已退款


    public static Map<String, String> orderState = new HashMap<>();
    static {
        orderState.put(KAWS_REFUNDED,"已退款");
        orderState.put(KAWS_REFUNDING,"退款中");
        orderState.put(KAWS_OVER,"已完成");
        orderState.put(KAWS_FINISHED,"已通话");
        orderState.put(KAWS_RETURNED,"需完善");
        orderState.put(KAWS_CONFIRMED,"待通话");
        orderState.put(KAWS_WAITING,"待审核");
        orderState.put(KAWS_PENDING,"待付款");
    }

}
