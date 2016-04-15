package wyqj.cancerprevent.doctorversion.http;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import wyqj.cancerprevent.doctorversion.bean.AddTimeBean;
import wyqj.cancerprevent.doctorversion.bean.ApplyDetailBean;
import wyqj.cancerprevent.doctorversion.bean.ApplyMoneyBean;
import wyqj.cancerprevent.doctorversion.bean.ApplyRecordBean;
import wyqj.cancerprevent.doctorversion.bean.ApplyWithdrawBean;
import wyqj.cancerprevent.doctorversion.bean.CanConsultBean;
import wyqj.cancerprevent.doctorversion.bean.CheckBean;
import wyqj.cancerprevent.doctorversion.bean.ChooseBean;
import wyqj.cancerprevent.doctorversion.bean.CommentBean;
import wyqj.cancerprevent.doctorversion.bean.ConsultListBean;
import wyqj.cancerprevent.doctorversion.bean.ConsultingSettingBean;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.bean.IncomeRecordBean;
import wyqj.cancerprevent.doctorversion.bean.InviteDoctorRecordBean;
import wyqj.cancerprevent.doctorversion.bean.MedicalBookEntity;
import wyqj.cancerprevent.doctorversion.bean.MedicalRecordItemBean;
import wyqj.cancerprevent.doctorversion.bean.NotificationCenterListBean;
import wyqj.cancerprevent.doctorversion.bean.NotificationStateBean;
import wyqj.cancerprevent.doctorversion.bean.OrderDetailBean;
import wyqj.cancerprevent.doctorversion.bean.PatientBean;
import wyqj.cancerprevent.doctorversion.bean.PatientListBean;
import wyqj.cancerprevent.doctorversion.bean.QuestionBean;
import wyqj.cancerprevent.doctorversion.bean.QuestionDetailBean;
import wyqj.cancerprevent.doctorversion.bean.RestError;
import wyqj.cancerprevent.doctorversion.bean.SplashImgBean;
import wyqj.cancerprevent.doctorversion.bean.UploadFileBean;
import wyqj.cancerprevent.doctorversion.bean.UptokenBean;
import wyqj.cancerprevent.doctorversion.bean.UserBean;

/**
 * Created by 杨才 on 2016/2/1.
 */
public interface HttpService {

    /**
     * 获取splashImg图片
     */
    @GET("/d/splash_image")
    void loadSplashImg(@Header("a") String a, Callback<SplashImgBean> response);

    @FormUrlEncoded
    @POST("/d/sign_in")
    void login(@Header("a") String a, @Header("token") String token, @Field("account") String account, @Field("password") String password, Callback<UserBean> cb);

    /**
     * 申请记录
     *
     * @param doctorId
     * @param page
     * @param perPage
     * @param callback
     */
    @GET("/d/applications")
    void getApplyRecord(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctorId, @Query("page") int page, @Query("per_page") int perPage, Callback<List<ApplyRecordBean>> callback);

    /**
     * @param doctorId
     * @param page
     * @param perPage
     * @param callback
     */
    @GET("/d/answered_questions")
    void getMyAnswer(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctorId, @Query("page") int page, @Query("per_page") int perPage, Callback<List<QuestionBean>> callback);

    /**
     * 问题详情
     *
     * @param question_id
     * @param doctorId
     * @param callback
     */
    @GET("/d/questions/{question_id}")
    void questionDetail(@Header("a") String a, @Header("token") String token, @Path("question_id") int question_id, @Query("doctor_id") int doctorId, Callback<QuestionDetailBean> callback);


    /**
     * 获取咨询广场的未答问题列表
     *
     * @param sortBy  分类  最新问题 / 价格最高
     * @param disease 病种
     * @param cb      回调
     */
    @GET("/d/questions")
    void getQuestionList(@Header("a") String a, @Header("token") String token, @Query("sort_by") String sortBy, @Query("doctor_id") int doctorId, @Query("diseased_state_id") Integer disease, @Query("page") Integer page, @Query("per_page") Integer perPage, Callback<List<QuestionBean>> cb);

    /**
     * 发送手机验证码（普通注册）
     */
    @FormUrlEncoded
    @POST("/register/verify_code/send")
    void sendGeneralRegisterVerifyCode(@Header("a") String a, @Field("mobile") String mobile, @Field("mode") String mode, @Field("app_type") int app_type, Callback<ErrorBean> response);

    @FormUrlEncoded
    @POST("/register/verify_code/send")
    void obtainCode(@Header("a") String a, @Header("token") String token, @Field("mobile") String mobile, @Field("app_type") Integer appType, Callback<ErrorBean> cb);

    @GET("/register/verify_code/check")
    void verifyCode(@Header("a") String a, @Header("token") String token, @Query("mobile") String mobile, @Query("verify_code") String verifyCode, Callback<ErrorBean> cb);

    @GET("/hospitals/provinces")
    void getHospitalCity(@Header("a") String a, @Header("token") String token, Callback<List<ChooseBean>> cb);

    @GET("/provinces/{province_id}/hospitals")
    void getCityHospital(@Header("a") String a, @Header("token") String token, @Path("province_id") Integer province_id, Callback<List<ChooseBean>> cb);

    /**
     * 医生用这个注册
     *
     * @param mobile
     * @param password
     * @param username
     * @param grade          医生等级
     * @param department     科室
     * @param hospitalId
     * @param disease_ids    病情的id
     * @param invitationCode 邀请码
     * @param verifyCode     手机验证码
     * @param cb
     */
    @FormUrlEncoded
    @POST("/d/register")
    void registerUser(@Header("a") String a, @Header("token") String token, @Field("mobile") String mobile, @Field("password") String password, @Field("name") String username, @Field("degree") Integer grade, @Field("department") String department, @Field("hospital_id") Integer hospitalId, @Field("disease_ids[]") int disease_ids[], @Field("hospital_name") String hospitalName, @Field("invitation_code") String invitationCode, @Field("verify_code") String verifyCode, @Field("desc") String person_intro, Callback<UserBean> cb);

    /**
     * 获取医生信息  我的界面
     *
     * @param doctorId
     * @param callback
     */
    @GET("/d/home")
    void getMyInfo(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctorId, Callback<UserBean> callback);

    /**
     * 修改密码
     *
     * @param doctorId
     * @param first
     * @param second
     * @param callback
     */
    @Multipart
    @PUT("/d/password")
    void ModifyPwd(@Header("a") String a, @Header("token") String token, @Part("doctor_id") int doctorId, @Part("old_password") String first, @Part("new_password") String second, Callback<String> callback);

    /**
     * 提交建议
     *
     * @param doctorId
     * @param content
     * @param connect
     * @param callback
     */
    @FormUrlEncoded
    @POST("/d/suggestion")
    void suggestion(@Header("a") String a, @Header("token") String token, @Field("doctor_id") int doctorId, @Field("content") String content, @Field("connect") String connect, Callback<String> callback);

    @GET("/d/income_logs")
    void getIncomeRecord(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctorId, @Query("page") int page, Callback<ArrayList<IncomeRecordBean>> callback);

    @GET("/d/cash_withdrawal_applications")
    void getWithdrawRecord(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctorId, @Query("page") int page, Callback<ArrayList<IncomeRecordBean>> callback);

    /**
     * 修改头像
     *
     * @param doctorId
     * @param photoFile
     * @param callback
     */
    @Multipart
    @PUT("/d/12/avatar")
    void modifyAvatar(@Header("a") String a, @Header("token") String token, @Part("doctor_id") String doctorId, @Part("image_id") String image_id, Callback<ErrorBean> callback);


    /**
     * 上传照片
     *
     * @param doctorId
     */
    @Multipart
    @POST("/d/v12/{doctor_id}/document_photos")
    void upLoadPic(@Header("a") String a, @Header("token") String token, @Path("doctor_id") int doctorId, @Part("badge_photo_image_id") String badge_photo_image_id, @Part("student_id_photo_image_id") String student_id_photo_image_id, @Part("licensure_photo_image_id") String licensure_photo_image_id, Callback<ErrorBean> callback);


    /**
     * 获取患者的信息
     *
     * @param patientuserkey
     * @param style          返回User的丰富程度; min -> 获取昵称头像等, simple_medical -> 获取患病信息等
     * @param callback
     */
    @GET("/users/{userkey}")
    void getPatientInfo(@Header("a") String a, @Header("token") String token, @Path("userkey") String patientuserkey, @Query("style") String style, Callback<PatientBean> callback);

    @FormUrlEncoded
    @POST("/d/instant_messages")
    void instantMessage(@Header("a") String a, @Header("token") String token, @Field("doctor_id") Integer doctor_id, @Field("to_userkey") String to_userkey, @Field("content") String content, @Field("type") Integer type, Callback<ErrorBean> callback);

    /**
     * 获取我的患者列表
     *
     * @param doctorId
     * @param page
     * @param callback
     */
    @GET("/d/subscribed_patients")
    void getPatientList(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctorId, @Query("page") int page, Callback<ArrayList<PatientListBean>> callback);

    @FormUrlEncoded
    @POST("/notifications/binding")
    void bindJpsh(@Header("a") String a, @Header("token") String token, @Field("userkey") String doctorUserkey, @Field("user_id") String doctorId, Callback<ErrorBean> callback);

    /**
     * 申请详情
     *
     * @param applyId
     * @param callback
     */
    @GET("/d/applications/{application_id}")
    void applyDetail(@Header("a") String a, @Header("token") String token, @Path("application_id") String applyId, Callback<ApplyDetailBean> callback);


    /**
     * 同意申请
     *
     * @param applyId
     * @param doctorId
     */
    @FormUrlEncoded
    @POST("/d/applications/{application_id}/accept")
    void agreeApply(@Header("a") String a, @Header("token") String token, @Path("application_id") String applyId, @Field("doctor_id") int doctorId, Callback<ErrorBean> callback);

    /**
     * 拒绝申请
     *
     * @param applyId
     * @param doctorId
     */
    @FormUrlEncoded
    @POST("/d/applications/{application_id}/reject")
    void cancelApply(@Header("a") String a, @Header("token") String token, @Path("application_id") String applyId, @Field("doctor_id") int doctorId, Callback<ErrorBean> callback);

    @GET("/d/doctors_reg_invite_info")
    void getInviteDoctorRecord(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctorId, @Query("page") int page, @Query("per_page") int perPage, Callback<InviteDoctorRecordBean> callback);

    /**
     * 申请修改信息
     *
     * @param doctorId
     * @param username
     * @param department   科室
     * @param hospitalId
     * @param hospitalName
     * @param diseased_ids 病情的id
     * @param desc         个人简历修改
     * @param remark       备注
     * @param callback
     */
    @FormUrlEncoded
    @POST("/d/apply_modify_profile")
    void modifyInfo(@Header("a") String a, @Header("token") String token, @Field("doctor_id") int doctorId, @Field("name") String username, @Field("department") String department,
                    @Field("hospital_id") Integer hospitalId, @Field("hospital_name") String hospitalName, @Field("diseased_ids[]") int[] diseased_ids, @Field("desc") String desc,
                    @Field("remark") String remark, Callback<String> callback);

    @GET("/d/subscribed_patients/search")
    void getSeachPatient(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctorId, @Query("keyword") String keyword, Callback<ArrayList<PatientListBean>> callback);

    /**
     * 搜索医院的接口
     *
     * @param keyword
     * @param cb
     */
    @GET("/hospitals/search")
    void searchHospital(@Header("a") String a, @Header("token") String token, @Query("keyword") String keyword, Callback<List<ChooseBean>> cb);

    /**
     * 申请提现，确定添加
     *
     * @param doctor_id
     * @param callback
     */
    @GET("/d/account_info")
    void applyWithdraw(@Header("a") String a, @Header("token") String token, @Query("doctor_id") Integer doctor_id, Callback<ApplyWithdrawBean> callback);

    /**
     * 申请提现
     *
     * @param doctorId
     * @param callback
     */
    @FormUrlEncoded
    @POST("/d/cash_withdrawal_application")
    void applyMoney(@Header("a") String a, @Header("token") String token, @Field("doctor_id") int doctorId, @Field("amount") int amount, Callback<ApplyMoneyBean> callback);

    @FormUrlEncoded
    @POST("/d/account_info")
    void addBankcard(@Header("a") String a, @Header("token") String token, @Field("doctor_id") Integer doctor_id, @Field("account_number") String account_number, @Field("bank_name") String bank_name, @Field("account_name") String account_name, @Field("sub_branch") String sub_branch, Callback<RestError> callback);

    /**
     * 病历本获取数据
     *
     * @param patientUserkey
     * @param callback
     */
    @GET("/d/medical_record")
    void IllNote(@Header("a") String a, @Header("token") String token, @Query("userkey") String patientUserkey, Callback<MedicalBookEntity> callback);

    @FormUrlEncoded
    @POST("/d/questions/{question_id}/comments")
    void sendComment(@Header("a") String a, @Header("token") String token, @Path("question_id") int questionId, @Field("doctor_id") int doctorId, @Field("content") String content, Callback<CommentBean> callback);

    /**
     * 获取我的病历本的"其他资料"的详情页
     */
    @GET("/d/medical_record/items")
    void getIllNoteOthers(@Header("a") String a, @Header("token") String token, @Query("userkey") String userkey, @Query("type_id") Integer type_id, @Query("page") Integer page, @Query("per_page") Integer per_page, Callback<List<MedicalRecordItemBean>> response);

    /**
     * 找回密码 获取验证码
     */
    @FormUrlEncoded
    @POST("/forget_password/verify_code/send ")
    void obtainfindpwdCode(@Header("a") String a, @Field("mobile") String mobile, @Field("app_type") Integer appType, Callback<ErrorBean> response);

    /**
     * 找回密码 确定修改
     */
    @Multipart
    @PUT("/forget_password/password")
    void findPwd(@Header("a") String a, @Header("token") String token, @Part("mobile") String mobile, @Part("verify_code") String verifyCode, @Part("password") String password, Callback<String> callback);

    /**
     * V1.2预约详情
     */
    @GET("/d/phone_consulting_orders/{id}")
    void getOrderDetails(@Header("a") String a, @Header("token") String token, @Path("id") String id, @Query("doctor_id") int doctor_id, Callback<OrderDetailBean> response);

    /**
     * V1.2预约详情的接诊
     * 成功返回201,失败返回400
     */
    @FormUrlEncoded
    @POST("/d/phone_consulting_orders/{id}/accept")
    void postAcceptOrder(@Header("a") String a, @Header("token") String token, @Path("id") String id, @Field("doctor_id") int doctor_id, Callback<ErrorBean> response);

    /**
     * V1.2预约详情的需完善
     * 成功返回201,失败返回400
     */
    @FormUrlEncoded
    @POST("/d/phone_consulting_orders/{id}/return")
    void postReturnOrder(@Header("a") String a, @Header("token") String token, @Path("id") String id, @Field("doctor_id") int doctor_id, @Field("return_details") String return_details, @Field("return_items[]") List<String> list, Callback<ErrorBean> response);

    /**
     * V1.2 给客服留言
     */
    @FormUrlEncoded
    @POST("/d/phone_consulting_orders/{id}/message")
    void postMessageForOrder(@Header("a") String a, @Header("token") String token, @Path("id") String id, @Field("doctor_id") int doctor_id, @Field("message_item") int message_item, @Field("message_details") String message_details, Callback<ErrorBean> response);

    /**
     * V1.2 查询医生状态
     */
    @GET("/d/confirmed_status")
    void confirmedStatus(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int id, Callback<CheckBean> callback);

    /**
     * V1.2 获取电话咨询列表
     */
    @GET("/d/phone_consulting_orders")
    void phoneConsultingOrder(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int id, @Query("status") int status, @Query("per_page") int perPage, @Query("page") int page, Callback<ConsultListBean> callback);

    /**
     * v1.2预约详情的拒诊
     * 成功返回201,失败返回400
     */
    @FormUrlEncoded
    @POST("/d/phone_consulting_orders/{id}/refuse")
    void postRefuseDiagnose(@Header("a") String a, @Header("token") String token, @Path("id") String id, @Field("doctor_id") int doctor_id, @Field("refuse_item") String refuse_item, @Field("refuse_details") String refuse_details, Callback<ErrorBean> response);

    /**
     * v1.2 我的--消息中心
     */
    @GET("/d/doctor_notifications")
    void getNotificationCenter(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int id, @Query("page") int page, @Query("per_page") int perPage, CustomCallBack<List<NotificationCenterListBean>> callback);

    /**
     * 设置消息通知
     */
    @FormUrlEncoded
    @POST("/d/notification_settings")
    void postNotificationSetting(@Header("a") String a, @Header("token") String token, @Field("doctor_id") int doctor, @Field("has_message_notice") int has_message_notice, @Field("has_apply_notice") int has_apply_notice, @Field("has_user_comment_notice") int has_user_comment_notice, Callback<NotificationStateBean> response);

    /**
     * 获取消息通知状态
     */

    @GET("/d/notification_settings")
    void getNotificationSettingsStatus(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctor_id, Callback<NotificationStateBean> response);


    /**
     * V1.2预约设置
     */
    @FormUrlEncoded
    @POST("/d/consulting_settings")
    void postConsultingSettings(@Header("a") String a, @Header("token") String token, @Field("doctor_id") int doctor_id, @Field("status") int status, @Field("price") String price, @Field("phone_number") String phone_number, Callback<CanConsultBean> response);

    /**
     * V1.2获取预约设置页面信息
     */
    @GET("/d/consulting_settings")
    void getConsultingSettings(@Header("a") String a, @Header("token") String token, @Query("doctor_id") int doctor_id, Callback<ConsultingSettingBean> response);

    /**
     * V1.2添加时间
     */
    @POST("/d/doctors/{id}/consulting_time_settings")
    void postAddTime(@Header("a") String a, @Header("token") String token, @Path("id") int doctor_id, @Body JsonElement json, Callback<ErrorBean> response);

    /**
     * V1.2获取每天的时间
     */
    @GET("/d/doctors/{id}/consulting_time_settings")
    void getAddTimeSetting(@Header("a") String a, @Header("token") String token, @Path("id") int doctor_id, @Query("date") String date, Callback<AddTimeBean> response);

    /**
     * V1.2删除单项的时间区间
     */
    @DELETE("/d/doctors/{id}/consulting_time_settings/slice")
    void deleteTimeSetting(@Header("a") String a, @Header("token") String token, @Path("id") int doctor_id, @Query("slice") int slice, @Query("date") String date, Callback<ErrorBean> response);

    /**
     * V1.2 青牛直传图片
     */
    @Multipart
    @POST("/")
    void uploadImageQinniu(@Part("token") String token, @Part("file") TypedFile photoFile, Callback<UploadFileBean> callback);

    /**
     * v3.5 获取七牛上传图片的Token
     */
    @GET("/qiniu_uptoken")
    void getQiniuUpToken(@Header("a") String a, Callback<UptokenBean> callback);
}
