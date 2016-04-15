package wyqj.cancerprevent.doctorversion.activity.mobileconsult;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.consultation.ShowSelectedHourAdapter;
import wyqj.cancerprevent.doctorversion.adapter.consultation.WeekDayAdapter;
import wyqj.cancerprevent.doctorversion.bean.AddTimeBean;
import wyqj.cancerprevent.doctorversion.bean.CanConsultBean;
import wyqj.cancerprevent.doctorversion.bean.ConsultingSettingBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

/**
 * Created by xiaguangcheng on 16/3/24.
 * 将时间表中的每一个标签作为一个对象,更加方便. 比如将00:30看作一个对象,
 * String time="00:30"
 * int status=2  表示当日选中
 * 然后将该对象放在list集合中,作为数据
 */
public class AppointSettingActivity extends LoadBaseActivity implements View.OnClickListener{

    /**某天被选择的可预约时间*/
    private GridView gv_select_hour;

    /**一周的某天*/
    private GridView gv_week;

    /**下周上周当切换按钮*/
    private TextView text_weekbtn;

    /**星期日期的适配器*/
    private WeekDayAdapter weekDayAdapter;

    /**某年某月的显示框*/
    private TextView text_year_month;

    private TextView btn_add_time;
    private TextView btn_clear_time;

    /**获取今天凌晨的毫秒值*/
    long todayMillSeconds;

    /**预约开关按钮*/
    private ImageView image_switch;

    /**保存按钮*/
    private Button btn_titlebar_submit;

    /**价格和电话*/
    private EditText edit_price;
    private EditText edit_phone;

    /**用户已经设置过的日期字符串 2014-06-05*/
    List<String> selectedDateList =new ArrayList<>();

    ArrayList<String> lastWeekList = new ArrayList<>();
    ArrayList<String> nextWeekList=new ArrayList<>();
    ArrayList<String> allWeekList=new ArrayList<>();
    private static final int FIRST_DAY = Calendar.MONDAY;
    private String currentDay;
    /**0 close,1 open 开关按钮的状态*/
    private int status =-1;
    /**存放每个时间段的状态*/
    private Map<String,Integer> hashMap;
    /**时间段列表的数据源*/
    private ArrayList<Map<String,Integer>> listTimeRaw=new ArrayList<>();

    private HttpService httpService= HttpUtils.getInstance().getHttpService(HttpService.class);
    private SQuser sQuser=SQuser.getInstance();

    private ShowSelectedHourAdapter showSelectedHourAdapter;

    /**全日期集合中的选中某天的位置,初始化为今天的位置,当点击某天时,就改为某天的位置*/
    private int selectDayPosition;

    /**暂时保存电话和价格*/
    private String tempPhone;
    private String tempPrice;

    /**左上角的关闭按钮*/
    private ImageView btn_titlebar_back;
    /**记录开关是否被改变*/
    private boolean ischange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setUpView();
    }


    /**
     * 当点击删除某个时间之后,再调用该方法,listTimeRaw中的时间表个数超过了48个!!!!!
     */
    /**
     *
     * @param isNew  由于在ShowSelectedHourAdapter 传入的listTimeRaw,作为数据源,并且对该集合进行了删改,因此在切换到
     *               其他的日期时,是否需要创建全新的listimeRaw.
     */
    public void clearCache(boolean isNew) {

            listTimeRaw.clear();
            for(int i=0;i<AddTimeActivity.timeTable.length;i++){
                hashMap=new HashMap<>();
                /**1表示未选中*/
                hashMap.put(AddTimeActivity.timeTable[i],1);
                listTimeRaw.add(hashMap);
            }


    }
    private void setUpView() {
        /**
         * 获得位置和时间段对应
         */
        for(int i=0;i<AddTimeActivity.timeTable.length;i++){
            hashMap=new HashMap<>();
            /**1表示未选中*/
            hashMap.put(AddTimeActivity.timeTable[i],1);
            listTimeRaw.add(hashMap);
        }
        getTwoWeeksOfThisDay();
        todayMillSeconds=getMorningTs();
        weekDayAdapter=new WeekDayAdapter(this,gv_week,currentDay,todayMillSeconds);
        gv_week.setAdapter(weekDayAdapter);
        weekDayAdapter.setListRaw(lastWeekList);
        text_year_month.setText(lastWeekList.get(0).substring(0,4)+"."+lastWeekList.get(0).substring(4,6));

        /**某天的时间段*/
         showSelectedHourAdapter=new ShowSelectedHourAdapter(AppointSettingActivity.this);
        gv_select_hour.setAdapter(showSelectedHourAdapter);

        /**初始化选择当前日期*/
        selectDayPosition = allWeekList.indexOf(currentDay);
        /**当医生将该天的时间段都删除后,那么就会将小红点隐藏*/
        showSelectedHourAdapter.setDeleteTip(new ShowSelectedHourAdapter.DeleteTip() {
            @Override
            public void setReddotInvisiable(String date) {

                if(selectedDateList!=null&&selectedDateList.contains(date)){
                    selectedDateList.remove(date);
                    weekDayAdapter.setListSelected(selectedDateList);
                }
            }
        });
        /**当医生点击某天时,回调得到该天的可预约时间段*/
        weekDayAdapter.setClickReturnDate(new WeekDayAdapter.ClickReturnDate() {
            @Override
            public void getDate(String date) {
                /**获得用户点击的日期在全日期集合中的位置*/
                selectDayPosition = allWeekList.indexOf(date);
                String year = date.substring(0, 4);
                String month = date.substring(4, 6);
                String day = date.substring(6, 8);
                String newDay = year + "-" + month + "-" + day;
                /**如果这一天可以被点击,并且点击有医生已经选中的时间设置,那么就显示这天时间设置的详情*/

                if (selectedDateList.contains(newDay)) {
                    /**如果服务器返回的有数据的日期集合包含用户点击的话,就访问网络,否则就不访问*/
                    btn_clear_time.setVisibility(View.VISIBLE);
                    getSomeDayDetails(newDay);
                } else {
                    /**当该天没有设置可预约时间时*/
                    if (showSelectedHourAdapter != null) {
                        clearCache(false);
                        btn_clear_time.setVisibility(View.GONE);
                        showSelectedHourAdapter.notifyDataSetChanged();

                    }
                }

            }
        });

        /**初始化预约设置页面的数据*/
        initDate(currentDay);
    }

    /**
     * 获得某天的已经选中保存在服务器的时间段
     * @param day 传入某天的字符串 "2016-04-07"
     */
    private void getSomeDayDetails(String day) {
        startProgressDialog();
        httpService.getAddTimeSetting(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, day, new CustomCallBack<AddTimeBean>() {
            @Override
            public void onSuccess(AddTimeBean addTimeBean) {
                stopProgressDialog();
                /**先清空集合中的状态缓存*/
                clearCache(true);
                if(addTimeBean!=null){
                    /**该天被选中的周循环时间段*/

                    List<Integer> week=addTimeBean.getWeek();
                    if(week!=null&&week.size()>0){
                        for(int i=0;i<week.size();i++){
                            Map<String, Integer> stringIntegerMap = listTimeRaw.get(week.get(i));
                            /**3表示周循环*/
                            stringIntegerMap.put(AddTimeActivity.timeTable[week.get(i)],3);
                        }
                    }

                    List<Integer> day1 = addTimeBean.getDay();
                    if(day1!=null&&day1.size()>0){
                        for(int i=0;i<day1.size();i++){
                            Map<String, Integer> stringIntegerMap = listTimeRaw.get(day1.get(i));
                            /**2表示只选中了当天*/
                            stringIntegerMap.put(AddTimeActivity.timeTable[day1.get(i)],2);
                        }
                    }
                    showSelectedHourAdapter.setListRaw(listTimeRaw,addTimeBean.getDate());
                }
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }
    /**
     *
     * @param date 传入一个字符串 20160407 转换为 2016-04-07
     * @return
     */
    public String changeDate(String date){
        String year=date.substring(0, 4);
        String month=date.substring(4, 6);
        String day=date.substring(6,8);
        date=year+"-"+month+"-"+day;
        return date;
    }
    /**
     * 初始化预约设置页面的数据
     * 日期为 20140504 ....
     */
    private void initDate(final String day) {
        httpService.getConsultingSettings(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, new CustomCallBack<ConsultingSettingBean>() {
            @Override
            public void onSuccess(ConsultingSettingBean consultingSettingBean) {
                showContentView();
                stopProgressDialog();
                if(consultingSettingBean!=null){
                    image_switch.setSelected(consultingSettingBean.isCan_consult());
                    if(consultingSettingBean.isCan_consult()){
                        status=1;
                        ischange=true;
                    }else {
                        status=0;
                        ischange=false;
                    }
                    tempPhone=consultingSettingBean.getPhone_number();
                    tempPrice=consultingSettingBean.getPrice();
                    edit_price.setText(tempPrice);
                    edit_phone.setText(tempPhone);
                    selectedDateList=consultingSettingBean.getDates();
                    /**设置需要被点击的那一天*/
                    weekDayAdapter.setCurrentClickDay(day);
                    if(lastWeekList.contains(day)){
                        text_weekbtn.setText("下周");
                        weekDayAdapter.setListRaw(lastWeekList);
                        text_year_month.setText(lastWeekList.get(0).substring(0,4)+"."+lastWeekList.get(0).substring(4,6));

                    }else{
                        text_weekbtn.setText("上周");
                        weekDayAdapter.setListRaw(nextWeekList);
                        text_year_month.setText(nextWeekList.get(0).substring(0,4)+"."+nextWeekList.get(0).substring(4,6));
                    }

                    selectDayPosition = allWeekList.indexOf(day);
                    weekDayAdapter.setListSelected(selectedDateList);
                    if(selectedDateList.contains(changeDate(day))){
                        /**如果服务器返回的有数据的日期集合包含用户点击的话,就访问网络,否则就不访问*/
                        btn_clear_time.setVisibility(View.VISIBLE);
                        getSomeDayDetails(changeDate(day));
                    }else{
                        /**不访问服务器,如果选择的时间段,被医生清空了,那么就需要手动设置*/
                        btn_clear_time.setVisibility(View.GONE);
                        clearCache(true);
                        showSelectedHourAdapter.setListRaw(listTimeRaw,day);
                        showSelectedHourAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }


    public  long getMorningTs(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = format.parse(format.format(new Date()));
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**获取此时此刻本周以及下周的具体日期,格式为20160324 周四*/
    private  void getTwoWeeksOfThisDay() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd EE");
        currentDay=dateFormat.format(calendar.getTime()).replace("星期","周");

        while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
            /**将当前日期向后推迟一天
             * 参数1为Calendar.DATE_OF_MONTH时,参数2若为6,那么就是将当前月份向前增加6个月
             * */
            calendar.add(Calendar.DATE, -1);
        }
        for (int i = 0; i < 14; i++) {

            if(i>6){
                nextWeekList.add(dateFormat.format(calendar.getTime()).replace("星期", "周"));

            }else{
                lastWeekList.add(dateFormat.format(calendar.getTime()).replace("星期","周"));
            }
            allWeekList.add(dateFormat.format(calendar.getTime()).replace("星期", "周"));
            /**将当前日期增加一天*/
            calendar.add(Calendar.DATE, 1);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_appointsetting);
        setTitle("预约设置");
        gv_week= (GridView) findViewById(R.id.gv_week);
        gv_select_hour= (GridView) findViewById(R.id.gv_select_hour);
        text_weekbtn= (TextView) findViewById(R.id.text_weekbtn);
        text_weekbtn.setOnClickListener(this);
        text_year_month= (TextView) findViewById(R.id.text_year_month);
        btn_add_time= (TextView) findViewById(R.id.btn_add_time);
        btn_add_time.setOnClickListener(this);
        btn_clear_time= (TextView) findViewById(R.id.btn_clear_time);
        btn_clear_time.setOnClickListener(this);
        image_switch= (ImageView) findViewById(R.id.image_switch);
        btn_titlebar_back= (ImageView) findViewById(R.id.btn_titlebar_back);
        /**预约开关的点击事件*/
        image_switch.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

                /**如果选中的话,点击就改为0,如果没选中,点击就改为1*/
                if(image_switch.isSelected()){
                    status=0;
                    image_switch.setSelected(false);
                }else{
                    status=1;
                    image_switch.setSelected(true);
                }
            }
        });
        btn_titlebar_submit= (Button) findViewById(R.id.btn_titlebar_submit);
        btn_titlebar_submit.setVisibility(View.VISIBLE);
        btn_titlebar_submit.setText("保存");

        edit_phone= (EditText) findViewById(R.id.edit_phone);
        edit_price= (EditText) findViewById(R.id.edit_price);

        /**关闭保存*/
        btn_titlebar_back.setOnClickListener(this);
        /**保存按钮,提交数据*/
        btn_titlebar_submit.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                String price=edit_price.getText().toString();
                try {
                    /**price不能为空,并且price的值不能小于50*/
                    if(!TextUtils.isEmpty(price)&&!(Integer.valueOf(price)<50)){

                    }else{
                        ToastUtils.showToast(AppointSettingActivity.this,"价格不能低于50元",2000,3);
                        return;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }

                String phone=edit_phone.getText().toString();
                if(!TextUtils.isEmpty(phone)){
                    if(phone.length()<11){
                        ToastUtils.showToast(AppointSettingActivity.this,"若是固定电话请输入区号",2000,3);
                        return;
                    }else {

                    }
                }else{
                    ToastUtils.showToast(AppointSettingActivity.this, "电话不能为空", 2000, 3);
                    return;
                }

                saveData(price,phone);
            }
        });


    }
    /**保存本页修改后的内容*/
    private void saveData(String price,String phone_num) {
        tempPhone=phone_num;
        tempPrice=price;
        ischange=(status!=0);

        startProgressDialog();
        httpService.postConsultingSettings(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, status, price, phone_num, new CustomCallBack<CanConsultBean>() {
            @Override
            public void onSuccess(CanConsultBean canConsultBean) {
                stopProgressDialog();
                ToastUtils.showToast(AppointSettingActivity.this, "保存信息成功", 1000, 1);
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }

    /**
     * 判断开启预约按钮状态是否被改变
     * @param rawState 原始状态
     * @param status 当前状态
     * @return 如果原始状态和当前状态一致,就返回false,如果原始状态和当前状态不一致,就返回true
     */
    public boolean getImageSwitchIsChange(boolean rawState,int status){
        if((status==0)&&!rawState){
            return false;
        }else if((status==1)&&rawState){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 判读当用户离开预约设置页面时,是否提示保存
     * @return 当为true时,弹出提示,当为false,不弹出提示
     */
    public boolean getIsTip(){
        String phone=edit_phone.getText().toString();
        String price=edit_price.getText().toString();
        if(getImageSwitchIsChange(ischange,status)||(TextUtils.isEmpty(tempPrice)&&!TextUtils.isEmpty(price))||(TextUtils.isEmpty(tempPhone)&&!TextUtils.isEmpty(phone))||(!TextUtils.isEmpty(tempPhone)&&!tempPhone.equals(phone))||(!TextUtils.isEmpty(tempPrice)&&!tempPrice.equals(price))){
            return true;
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_weekbtn:
                if("上周".equals(text_weekbtn.getText().toString())){
                    text_weekbtn.setText("下周");
                    weekDayAdapter.setListRaw(lastWeekList);
                    text_year_month.setText(lastWeekList.get(0).substring(0,4)+"."+lastWeekList.get(0).substring(4,6));
                }else{
                    text_weekbtn.setText("上周");
                    weekDayAdapter.setListRaw(nextWeekList);
                    text_year_month.setText(nextWeekList.get(0).substring(0,4)+"."+nextWeekList.get(0).substring(4,6));
                }
                break;
            /**添加时间*/
            case R.id.btn_add_time:

                Intent intent =new Intent(this,AddTimeActivity.class);
                intent.putStringArrayListExtra(Constants.ALLWEEKLIST,allWeekList);
                intent.putExtra(Constants.SELECTDAYPOSITION,selectDayPosition);
                intent.putExtra(Constants.CURRENTDAY,currentDay);
                startActivityForResult(intent, Constants.APPOINTSETTING_TO_ADDTIME_REQUEST_CODE);
                break;
            /**清除时间 当该天没有可预约时间段时,该按钮隐藏*/
            case R.id.btn_clear_time:
                if("清除时间".equals(btn_clear_time.getText().toString())){
                    showSelectedHourAdapter.setIsOpen(true);
                    btn_clear_time.setText("取消");

                }else{
                    showSelectedHourAdapter.setIsOpen(false);
                    btn_clear_time.setText("清除时间");
                }

                break;

            case R.id.btn_titlebar_back:
                if(getIsTip()){
                    TipDialog tipDialog=new TipDialog(AppointSettingActivity.this);
                    tipDialog.show();
                    tipDialog.setTitle("信息未保存");
                    tipDialog.setMessage("退出将丢失已更改的信息");
                    tipDialog.setNegative("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    tipDialog.setPositive("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                }else{
                    finish();
                }

                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(getIsTip()){
                TipDialog tipDialog=new TipDialog(AppointSettingActivity.this);
                tipDialog.show();
                tipDialog.setTitle("信息未保存");
                tipDialog.setMessage("退出将丢失已更改的信息");
                tipDialog.setNegative("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                tipDialog.setPositive("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.APPOINTSETTING_TO_ADDTIME_REQUEST_CODE && resultCode == Constants.ADDTIME_IMAGE_TO_APPOINTSETTING_RESULT_CODE) {
            /**返回按钮*/
            if(data!=null){
                String day=data.getStringExtra("day");
                startProgressDialog();
                initDate(day);
            }

        } else if (requestCode == Constants.APPOINTSETTING_TO_ADDTIME_REQUEST_CODE && resultCode == Constants.ADDTIME_SAVE_TO_APPOINTSETTING_RESULT_CODE) {
            /**保存返回*/
            if(data!=null){
                String day=data.getStringExtra("day");
                startProgressDialog();
                initDate(day);
            }

        } else if (requestCode == Constants.APPOINTSETTING_TO_ADDTIME_REQUEST_CODE && resultCode == Constants.ADDTIME_REBACK_TO_APPOINTSETTING_RESULT_CODE) {
            /**取消返回*/
            if(data!=null){
                String day=data.getStringExtra("day");
                startProgressDialog();
                initDate(day);
            }
        }
    }
}
