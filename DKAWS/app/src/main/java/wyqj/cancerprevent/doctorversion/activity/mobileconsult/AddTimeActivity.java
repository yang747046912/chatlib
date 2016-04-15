package wyqj.cancerprevent.doctorversion.activity.mobileconsult;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.common.widget.TipDialog;
import com.kaws.lib.fresco.Image;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.consultation.AddTimeAdapter;
import wyqj.cancerprevent.doctorversion.bean.AddTimeBean;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

/**
 * Created by xiaguangcheng on 16/3/25.
 */
public class AddTimeActivity extends LoadBaseActivity implements View.OnClickListener{
    public static String[] timeTable = {"00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30","14:00","14:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30","22:00","22:30","23:00","23:30"};
    /**
     * 保存时间按钮
     */
    private TextView save_date;
    /**
     * 删除时间按钮
     */
    private TextView delete_date;
    /**
     * 显示时间表
     */
    private GridView gv_add_time;
    /**
     * 前一天的控件
     */
    private TextView text_last;
    /**
     * 后一天的控件
     */
    private TextView text_next;
    /**
     * 显示选中的日期
     */
    private TextView text_show_date;

    /**存放每个时间段的状态*/
    private Map<String,Integer> hashMap;
    /**时间段列表的数据源*/
    private ArrayList<Map<String,Integer>> listTimeRaw;
    /**两周日期数据源*/
    private ArrayList<String> allWeekList;
    /**由于两周日期集合中的元素是"20150511 周一"这种形式,但是展示需要展示为"5月11号 周一"的形式.因此需要对该数组进行转换*/
    private ArrayList<String> showAllWeekList;

    /**用户选中的日期*/
    private int selectDayPosition;
    AddTimeAdapter addTimeAdapter;

    /**存储循环和当日的时间表,提交数据时用到*/
    List<Integer> weekList =new ArrayList<>();
    List<Integer> dayList=new ArrayList<>();
    /**用来暂存已选时间段的状态的集合,在判断是否需要提示对话框中用到*/
    List<Integer> tempWeekList;
    List<Integer> tempDayList;

    private final int DONOTHING=-1;
    private  final int LASTDAY=100;
    private  final int NEXTDAY=101;
    private  final int SAVEDAY=102;
    private  final int DELETEDAY=103;

    private HttpService httpService= HttpUtils.getInstance().getHttpService(HttpService.class);
    private SQuser sQuser=SQuser.getInstance();

    private ImageView btn_titlebar_back;

    /**获得今天的日期*/
    private String currentDay;
    /**今天在全日期集合中的位置,标记用户不能再选上一天*/
    private int currentDayPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setUpView();
    }

    private void setUpView() {
        listTimeRaw=new ArrayList<>();
        /**1表示没有选择的状态
         * 2表示选择了当前的状态
         * 3表示选择了周循环
         * */

        for(int i=0;i<timeTable.length;i++){
            hashMap=new HashMap<>();
            hashMap.put(timeTable[i],1);
            listTimeRaw.add(hashMap);
        }
        addTimeAdapter=new AddTimeAdapter(this);
        gv_add_time.setAdapter(addTimeAdapter);
        addTimeAdapter.setListRaw(listTimeRaw);
        initData(changeDate(allWeekList.get(selectDayPosition)));

    }

    /**
     * 重置时间表状态
     */
    public void clearData(){
        for(int i=0;i<listTimeRaw.size();i++){
            Map<String, Integer> stringIntegerMap = listTimeRaw.get(i);
            stringIntegerMap.put(timeTable[i],1);
        }
        addTimeAdapter.setListRaw(listTimeRaw);
    }

    /**
     *
     * @param list  集合元素为20160402....
     * @param list2 集合元素为 "4月2日 周二"
     * @return 集合元素为 "4月2日 周二"
     */
    public ArrayList<String> getTextShowAllWeekList(ArrayList<String> list,ArrayList<String> list2){
        DateFormat longFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMdd");

        for(int i=0;i<list.size();i++){
            String strDay=list.get(i).substring(0,8);
            try {
                Date date=simpleDateFormat.parse(strDay);
                String format = longFormat.format(date);
                String format1=format.substring(5,format.length()).replace("星期","周");
                int index=format1.indexOf(" ");
                String format2=format1.substring(0,index);
                int index1=format2.indexOf("周");
                String format3=format2.substring(0,index1)+" "+format2.substring(index1,format2.length());

                Log.e("format",format3);
                list2.add(format3);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list2;
    }

    /**
     * 获取该天已经选好的时间段
     * @param date 需要获取的某天时间段的日期 格式为 "2012-04-04"
     */
    private void initData(String date) {
        httpService.getAddTimeSetting(HttpHead.getHeader("GET"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, date, new CustomCallBack<AddTimeBean>() {
            @Override
            public void onSuccess(AddTimeBean addTimeBean) {
                showContentView();
                stopProgressDialog();
                if(addTimeBean!=null) {
                    /**该天被选中的周循环时间段*/
                    tempWeekList=addTimeBean.getWeek();
                    if (tempWeekList != null && tempWeekList.size() > 0) {
                        for (int i = 0; i < tempWeekList.size(); i++) {
                            Map<String, Integer> stringIntegerMap = listTimeRaw.get(tempWeekList.get(i));
                            /**3表示周循环*/
                            stringIntegerMap.put(AddTimeActivity.timeTable[tempWeekList.get(i)], 3);
                        }
                    }
                    tempDayList=addTimeBean.getDay();
                    if (tempDayList != null && tempDayList.size() > 0) {
                        for (int i = 0; i < tempDayList.size(); i++) {
                            Map<String, Integer> stringIntegerMap = listTimeRaw.get(tempDayList.get(i));
                            /**2表示只选中了当天*/
                            stringIntegerMap.put(AddTimeActivity.timeTable[tempDayList.get(i)], 2);
                        }
                    }
                    addTimeAdapter.setListRaw(listTimeRaw);
                }
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_addingtime);
        setTitle("增加时间");
        /**获取两周日期*/
        if(getIntent()!=null){
            allWeekList=getIntent().getStringArrayListExtra(Constants.ALLWEEKLIST);
            selectDayPosition=getIntent().getIntExtra(Constants.SELECTDAYPOSITION,-1);
            currentDay = getIntent().getStringExtra(Constants.CURRENTDAY);
            if(allWeekList==null||allWeekList.size()==0){
                throw new RuntimeException("allWeekList must not be null");
            }
            if(selectDayPosition==-1){
                throw  new RuntimeException("selectDayPosition must not be -1" );
            }
            currentDayPosition=allWeekList.indexOf(currentDay);
        }
        /**前一天*/
        text_last = (TextView) findViewById(R.id.text_last);
        text_last.setOnClickListener(this);
        /**后一天*/
        text_next = (TextView) findViewById(R.id.text_next);
        text_next.setOnClickListener(this);
        /**时间表*/
        gv_add_time = (GridView) findViewById(R.id.gv_add_time);
        /**保存选中*/
        save_date = (TextView) findViewById(R.id.save_date);
        save_date.setOnClickListener(this);
        /**删除选中*/
        delete_date = (TextView) findViewById(R.id.delete_date);
        delete_date.setOnClickListener(this);
        /**选中的当前日期*/
        text_show_date = (TextView) findViewById(R.id.text_show_date);
        showAllWeekList=new ArrayList<>();
        /**获得展示的日期集合*/
        showAllWeekList=getTextShowAllWeekList(allWeekList,showAllWeekList);
        if(allWeekList!=null){
            text_show_date.setText(showAllWeekList.get(selectDayPosition));
        }

        btn_titlebar_back= (ImageView) findViewById(R.id.btn_titlebar_back);
        btn_titlebar_back.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**点击前一天*/
            case R.id.text_last:
                /**先判断是否需要提交*/

                /**如果需要提交,就提交*/
                prepareDate(LASTDAY);
                break;
            /**点击后一天*/
            case R.id.text_next:
                /**先判断是否需要提交*/

                /**如果需要提交,就提交*/
                prepareDate(NEXTDAY);
                break;
            /**点击保存*/
            case R.id.save_date:
                prepareDate(SAVEDAY);
                break;
            /**点击清空*/
            case R.id.delete_date:
                /**传入空数组就好*/
                String date=changeDate(allWeekList.get(selectDayPosition));
                weekList.clear();
                dayList.clear();
                saveSelectData(weekList,dayList,date,DELETEDAY);


                break;
            case R.id.btn_titlebar_back:
                if(isTips()){
                    TipDialog tipDialog=new TipDialog(AddTimeActivity.this);
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
                            Intent intent= new Intent();
                            intent.putExtra("day",allWeekList.get(selectDayPosition));
                            setResult(Constants.ADDTIME_SAVE_TO_APPOINTSETTING_RESULT_CODE,intent);
                            finish();
                        }
                    });
                }else{
                    Intent intent= new Intent();
                    intent.putExtra("day",allWeekList.get(selectDayPosition));
                    setResult(Constants.ADDTIME_SAVE_TO_APPOINTSETTING_RESULT_CODE,intent);
                    finish();
                }

                break;
        }
    }

    /**
     * 判断退出本页面,是否需要提示.如果对该页面进行了修改,那么就提示
     * @return true 提示,false 不提示
     */
    public boolean isTips(){
        /**周循环集合是否相等*/
        boolean isWeek=false;
        /**当日是否相等*/
        boolean isDAY=false;
        prepareDate(DONOTHING);
        /**判断tempWeekList 和weekList是否相等,由于这两个数组都是有序唯一的,
         * 1:判断集合是否为null
         * 2:判断集合长度是否相等
         * 3:判断集合元素是否相等
         * */

        if(tempWeekList!=null&&tempWeekList.isEmpty()&&weekList.isEmpty()){
            /**两个集合长度都为0的情况*/
            isWeek=true;
        }else if(tempWeekList==null&&weekList.isEmpty()){
            /**一个为null,一个为0的情况*/
            isWeek=true;

        }else if(tempWeekList!=null&&(tempWeekList.size()!=weekList.size())){
            /**两个集合长度不一致的情况*/
            isWeek=false;

        }else if(tempWeekList!=null&&(tempWeekList.size()==weekList.size())){

            /**如果两个集合的长度不为0,并且长度相等*/
            for(int i=0;i<tempWeekList.size();i++){
                if(tempWeekList.get(i)!=weekList.get(i)){
                    isWeek=false;
                    break;
                }
                isWeek=true;
            }


        }

        /**判断tempDayList 和dayList是否相等*/
        if(tempDayList!=null&&tempDayList.isEmpty()&&dayList.isEmpty()){
            /**两个集合长度都为0的情况*/
            isDAY=true;

        }else if(tempDayList==null&&dayList.isEmpty()){
            /**一个为null,一个为0的情况*/
            isDAY=true;

        }else if(tempDayList!=null&&(tempDayList.size()!=dayList.size())){
            /**两个集合长度不一致的情况*/
            isDAY=false;

        }else if(tempDayList!=null&&(tempDayList.size()==dayList.size())){
            /**如果两个集合的长度不为0,并且长度相等*/
            for(int i=0;i<tempDayList.size();i++){
                if(tempDayList.get(i)!=dayList.get(i)){
                    isDAY=false;
                    break;
                }
                isDAY=true;
            }
        }

        if(isWeek&&isDAY){
            return false;
        }else{
            return true;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(isTips()){
                TipDialog tipDialog=new TipDialog(AddTimeActivity.this);
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
                        Intent intent= new Intent();
                        intent.putExtra("day", allWeekList.get(selectDayPosition));
                        setResult(Constants.ADDTIME_SAVE_TO_APPOINTSETTING_RESULT_CODE, intent);
                        finish();
                    }
                });
            }else{
                Intent intent= new Intent();
                intent.putExtra("day", allWeekList.get(selectDayPosition));
                setResult(Constants.ADDTIME_SAVE_TO_APPOINTSETTING_RESULT_CODE, intent);
                finish();
            }

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * @param flag 用来判断是什么地方提交的数据
     *             SAVEDAY 点击保存时提交
     *             NEXTDAY 点击后一天时提交
     *             LASTDAY 点击前一天时提交
     *             DELETEDAY 点击清空操作
     *             DONOTHING 点击返回按钮是否需要提示操作
     */
    public void prepareDate(int flag){

        dayList.clear();
        weekList.clear();

            /**获得服务器需要的时间格式字符串*/
            String date=changeDate(allWeekList.get(selectDayPosition));
            if(listTimeRaw!=null){
                for(int i=0;i<listTimeRaw.size();i++){
                    Map<String, Integer> stringIntegerMap = listTimeRaw.get(i);
                    Set<Map.Entry<String, Integer>> entrySet = stringIntegerMap.entrySet();
                    Iterator<Map.Entry<String, Integer>> iterator = entrySet.iterator();
                    /**该循环只走一次,因为每个map集合都只有一个键值对*/
                    while (iterator.hasNext()){
                        Map.Entry<String, Integer> next = iterator.next();
                        String key = next.getKey();
                        Integer value = next.getValue();
                        /**如果值为2,就是选中当日的时间段*/
                        if(value==2){
                            for(int position=0;position<AddTimeActivity.timeTable.length;position++){
                                if(key.equals(AddTimeActivity.timeTable[position])){
                                    dayList.add(position);
                                }
                            }
                            /**如果值为3,就是选中的周循环时间段*/
                        }else if(value==3){
                            for(int position=0;position<AddTimeActivity.timeTable.length;position++){
                                if(key.equals(AddTimeActivity.timeTable[position])){
                                    weekList.add(position);
                                }
                            }
                        }
                    }
                }
            }
            if(flag!=DONOTHING){
                saveSelectData(weekList,dayList,date,flag);
            }
    }

    class Test{
        private String date;

        public void setWeek(List<Integer> week) {
            this.week = week;
        }


        public void setDate(String date) {
            this.date = date;
        }


        public void setDay(List<Integer> day) {
            this.day = day;
        }

        private List<Integer> week;
        private List<Integer> day;
    }
    /**
     * 提交json到 服务器,用来保存医生选择的空闲可预约时间段
     * @param weekList 周循环的时间段 元素为时间表的位置 position
     * @param dayList  当日的时间段  元素为在时间表中的位置 position
     * @param date   当天的日期 格式为   "2016-04-07"
     * @param flag   用来判断是什么地方提交的数据
     *               SAVEDAY 点击保存时提交
     *               NEXTDAY 点击后一天时提交
     *               LASTDAY 点击前一天时提交
     */
    private void saveSelectData(List<Integer> weekList,List<Integer> dayList,String date, final int flag) {
        Gson gson =new Gson();
        Test test=new Test();
        test.setDate(date);
        test.setDay(dayList);
        test.setWeek(weekList);
        JsonElement jsonElement = gson.toJsonTree(test);
        /**如果点击保存或者删除的话,临时的周循环和当日时间段数组就更新为提交的数组.如果是删除,也就是空数组了*/
        tempDayList=dayList;
        tempWeekList=weekList;
        startProgressDialog();

        httpService.postAddTime(HttpHead.getHeader("POST"), sQuser.getUserInfo().token, sQuser.getUserInfo().doctorid, jsonElement, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {
                stopProgressDialog();
                if(flag==SAVEDAY){
                    /**点击保存时提交的数据,提交成功*/
                    ToastUtils.showToast(AddTimeActivity.this, "保存成功", 1500, 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent= new Intent();
                            intent.putExtra("day",allWeekList.get(selectDayPosition));
                            setResult(Constants.ADDTIME_SAVE_TO_APPOINTSETTING_RESULT_CODE,intent);
                            finish();
                        }
                    },1500);

                }else if(flag==NEXTDAY){
                    /**点击后一天提交的数据,提交成功*/
                    /**提交成功后,再访问网络,获得下一天有没有已经选择好了的数据*/
                    if(allWeekList!=null){
                        selectDayPosition++;
                        if(selectDayPosition>allWeekList.size()-1){
                            selectDayPosition--;
                            ToastUtils.showToast(AddTimeActivity.this,"不能再点击了,可选日期为前后一周的时间段",1500,3);
                            return;
                        }
                        if(selectDayPosition<allWeekList.size()&&selectDayPosition>-1){
                            text_show_date.setText(showAllWeekList.get(selectDayPosition));
                            clearData();
                            startProgressDialog();
                            initData(changeDate(allWeekList.get(selectDayPosition)));
                        }
                    }
                }else if(flag==LASTDAY){
                    /**点击前一天提交的数据,提交成功*/
                    if(allWeekList!=null){
                        selectDayPosition--;
                        if(selectDayPosition<currentDayPosition){
                            selectDayPosition++;
                            ToastUtils.showToast(AddTimeActivity.this,"昨天不可选",1500,3);
                            return;
                        }else{
                            if(selectDayPosition>-1&&selectDayPosition<allWeekList.size()){
                                text_show_date.setText(showAllWeekList.get(selectDayPosition));
                                clearData();
                                startProgressDialog();
                                initData(changeDate(allWeekList.get(selectDayPosition)));
                            }
                        }
                    }

                }else if(flag==DELETEDAY){
                    /**清空数据*/
                    clearData();
                    ToastUtils.showToast(AddTimeActivity.this,"清空时间成功",1500,1);
                }
            }

            @Override
            public void onFailure() {
                stopProgressDialog();
            }
        });
    }
}
