package wyqj.cancerprevent.doctorversion.constant;

import wyqj.cancerprevent.doctorversion.R;

/**
 * Created by 杨才 on 2016/2/1.
 */
public class Constants {
    public final static int PER_PAGE = 20;
    public final static String SPLASH_PATH = "splash_path";
    public final static int headRadiusPixels = 5;
    public final static String[] banks = new String[]{"工商银行", "建设银行", "中国银行", "招商银行", "交通银行", "平安银行", "农业银行", "中信银行", "浦发银行", "兴业银行", "民生银行", "光大银行", "华夏银行", "广发银行", "北京银行"};
    public final static int[] resIds = new int[]{R.mipmap.bank_gongshang, R.mipmap.bank_jianshe, R.mipmap.bank_zhongguo, R.mipmap.bank_zhaoshang, R.mipmap.bank_jiaotong, R.mipmap.bank_pingan, R.mipmap.bank_nongye, R.mipmap.bank_zhongxin, R.mipmap.bank_pufa, R.mipmap.bank_xingye, R.mipmap.bank_minsheng, R.mipmap.bank_guangda, R.mipmap.bank_huaxia, R.mipmap.bank_guangfa, R.mipmap.bank_beijing};
    /**
     * 问题的id
     */
    public static final String QUESTION_ID = "question_id";
    //注册时用来保护注册信息
    public static final String REGISTER = "register";
    public static final String DOCTOR = "doctor";
    public static final String CHOOSE_HOSPITAL = "ChooseBean";
    public static final String CHOOSE_DISEASE = "Choosedisease";
    public static final String PERSONAL_PROFILE = "Personalprofile";
    public static final String NOT_FIND_HOSPITAL = "NotfindHospital";
    public static final String MAKE_MONEY = "make_money";
    public static final String SELECT_PHOTO = "select_photo";
    public static final String URL_OF_PHOTO = "select_photo_data";
    public static final String DOCTOR_ISMODIFY = "doctor_ismodify";
    public static final String IS_NEED_CROP = "is_need_crop";
    public static final String CHAT_NAME = "_chat_name";
    public static final String CHAT_AVATAR = "_chat_avatar";
    public static final String APPLY_ID = "apply_id";
    public static final String CHANGE_NAME = "change_name";
    public static final String MY_INFO_NAME = "my_info_name";
    public static final String CHANGE_DEPARTMENT = "change_department";
    public static final String MY_INFO_DEPARTMENT = "my_info_department";
    public static final String CHANGE_GRADE = "change_grade";
    public static final String MY_INFO_GRADE = "my_info_grade";
    public static final String SEARCH_TYPE = "search_type";
    public static final String SEARCH_HINT = "search_hint";
    public static final String SEARCH_HOSPITAL = "search_hospital";
    public static final String TAKE_MONEY_STATUS = "take_money_status";
    public static final String TAKE_MONEY_ACCOUNT_NUMBER = "take_money_account_number";
    public static final String TAKE_MONEY_BANK_NAME = "take_money_bank_name";
    public static final String TAKE_MONEY_ACCOUNT_NAME = "take_money_account_name";
    public static final String TAKE_MONEY_SUBBRANCH = "take_money_subbranch";
    public static final String SELECT_BANK_ICON = "select_bank_icon";
    public static final String SELECT_BANK_NAME = "select_bank_name";
    public static final String PATIENT_USERKEY = "toUserKey";
    public static final String CHANGED_AMOUNT = "changed_amount";
    public static final String CURRENT_MONEY = "current_Money";
    public static final String ORDER_ID = "orderId";
    //***************************************************
    public static final int CODE_CHOOSE_HOSPITAL = 100;
    public static final int CODE_CHOOSE_DISEASE = 101;
    public static final int CODE_PERSONAL_PROFILE = 102;
    public static final int CODE_NOT_FIND_HOSPITAL = 103;
    public static final int CODE_SELECT_PHOTO = 104;
    public static final int CODE_SELECT_PHOTO_DOCTOR = 105;
    public static final int CODE_SELECT_PHOTO_STUDENT = 106;
    public static final int CODE_APPLY_ID = 107;
    public static final int CODE_SELECT_NAME = 108;
    public static final int CODE_SELECT_HOSPITAL = 109;
    public static final int CODE_SELECT_DEPARTMENT = 110;
    public static final int CODE_SELECT_DISEASE = 111;
    public static final int CODE_MIME_PERSONAL_PROFILE = 112;
    public static final int CODE_SELECT_GRADE = 113;
    public static final int CODE_SEARCH_HOSPITAL = 114;
    public static final int CODE_MY_INCOME = 115;
    public static final int CODE_APPLY_TAKEMONEY = 116;
    public static final int CODE_BANKCARD_ICON = 117;
    public static final int CODE_REPLY_PATIENT = 118;

    /**
     * 病历本
     */
    /*确诊单*/
    public static final int MEDICAL_RECORD_DIAGNOSED_SINGLE = 1;
    /*化验报告*/
    public static final int MEDICAL_RECORD_LABORATORY_REPORT = 2;
    /*影像学检查*/
    public static final int MEDICAL_RECORD_IMAGING_CHECK = 3;
    /*病理分析*/
    public static final int MEDICAL_RECORD_PATHOLOGICAL_ANALYSIS = 4;
    /*用药记录*/
    public static final int MEDICAL_RECORD_MEDICATION_RECORDS = 5;
    /*复查记录*/
    public static final int MEDICAL_RECORD_REVIEW_RECORD = 6;
    /*住院*/
    public static final int MEDICAL_RECORD_HOSPITALIZATION_AND_OTHER = 7;
    /*伤口拍照*/
    public static final int MEDICAL_RECORD_WOUND_PICTURES = 8;

    /**预约设置跳转增加时间*/
    public static final int APPOINTSETTING_TO_ADDTIME_REQUEST_CODE=1001;
    /**增加时间左上关闭返回到预约设置*/
    public static final int ADDTIME_IMAGE_TO_APPOINTSETTING_RESULT_CODE=1002;
    /**增加时间保存成功返回到预约设置*/
    public static final int ADDTIME_SAVE_TO_APPOINTSETTING_RESULT_CODE=1003;
    /**增加时间物理键返回到预约设置*/
    public static final int ADDTIME_REBACK_TO_APPOINTSETTING_RESULT_CODE=1004;
    /**电话咨询申请中进入订单详情*/
    public static final String APPLY_TO_APPOINTDETAIL="apply";
    /**
     * 拒诊请求码
     */
    public static final int APPOINTDETAIL_TO_REJECT_REQUEST_CODE = 1005;
    /**
     * 需完善的请求码
     */
    public static final int APPOINTDETAIL_TO_COMPLETE_REQUEST_CODE = 1006;

    /**订单详情联系客服跳转selectPhoto*/
    public static final String ORDERDETAIL_TO_SELECTPHOTO="contact";
    /**预约设置的两周时间集合*/
    public static final String ALLWEEKLIST="allWeekList";
    /**预约设置选中某天的日期在两周集合中的位置*/
    public static final String SELECTDAYPOSITION="selectDayPosition";
    /**预约设置中,今天*/
    public static final String CURRENTDAY="currentday";

    /**给客服留言,从selectphoto 跳转到CustomerServiceMessageActivity*/
    public static final int SELECTPHOTO_TO_CUSTOMMESSAGE_REQUEST_CODE=1007;

    /**订单详情 到 联系客服的选择页面*/
    public static final int APPOINTDETAIL_TO_SELECTPHOTO_REQUEST_CODE=1008;



}
