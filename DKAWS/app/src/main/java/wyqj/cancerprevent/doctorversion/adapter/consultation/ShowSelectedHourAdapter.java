package wyqj.cancerprevent.doctorversion.adapter.consultation;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.common.utils.ToastUtils;
import com.kaws.lib.http.HttpHead;
import com.kaws.lib.http.HttpUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.mobileconsult.AddTimeActivity;
import wyqj.cancerprevent.doctorversion.activity.mobileconsult.AppointSettingActivity;
import wyqj.cancerprevent.doctorversion.bean.ErrorBean;
import wyqj.cancerprevent.doctorversion.http.CustomCallBack;
import wyqj.cancerprevent.doctorversion.http.HttpService;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

/**
 * Created by xiaguangcheng on 16/4/8.
 */
public class ShowSelectedHourAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<Map<String, Integer>> listRaw;

    private ArrayList<Map<String,Integer>> tempList=new ArrayList<>();

    /**该天的日期   "2016-04-07*/
    private String day;


    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
        notifyDataSetChanged();
    }

    /**用来决定是否显示删除按钮*/

    private boolean isOpen;
    public ShowSelectedHourAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return listRaw==null?0:getSize(listRaw);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view =View.inflate(context, R.layout.item_delete_hour,null);
        ImageView hour_delete= (ImageView) view.findViewById(R.id.hour_delete);
        final TextView hour_show= (TextView) view.findViewById(R.id.hour_show);
        TextView hour_week= (TextView) view.findViewById(R.id.hour_week);
            Map<String, Integer> stringIntegerMap = tempList.get(position);
            Set<Map.Entry<String, Integer>> entries = stringIntegerMap.entrySet();
            Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Integer> next = iterator.next();
                String key = next.getKey();
                Integer value = next.getValue();
                if(value==3){
                    hour_week.setVisibility(View.VISIBLE);
                    hour_week.setText("每周");
                    hour_show.setText(key);
                }else if(value==2){
                    hour_show.setText(key);
                    hour_week.setVisibility(View.INVISIBLE);
                }
            }

        if(isOpen){
            hour_delete.setVisibility(View.VISIBLE);
            hour_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0;i< AddTimeActivity.timeTable.length;i++){
                        /**获取删除的时间段在整体时间段中的位置*/
                        if((hour_show.getText().toString()).equals(AddTimeActivity.timeTable[i])){
                            deleteThisTime(position,i,hour_show.getText().toString());
                        }
                    }
                }
            });
        }else{
            hour_delete.setVisibility(View.GONE);

        }
        return view;
    }

    /**
     *
     * @param position 在页面中显示的位置.显示的位置是templist集合中的位置
     * @param i  在时间表中的位置,从0开始  时间表的位置是timetable数组中的位置
     */
    private void deleteThisTime(final int position, final int i,final String tempTime) {
        final AppointSettingActivity appointSettingActivity=(AppointSettingActivity)context;
        appointSettingActivity.startProgressDialog();
        HttpUtils.getInstance().getHttpService(HttpService.class).deleteTimeSetting(HttpHead.getHeader("DELETE"), SQuser.getInstance().getUserInfo().token, SQuser.getInstance().getUserInfo().doctorid, i, day, new CustomCallBack<ErrorBean>() {
            @Override
            public void onSuccess(ErrorBean errorBean) {

                /**这个删除的是原始数据,也就是全部时间的map*/
                /**无论是i还是position,都无法锁定listraw中的时间位置,需要找到删除时间在listraw中的时间位置*/
                int index=-1;
                for(int a=0;a<listRaw.size();a++){
                    Map<String, Integer> remove = listRaw.get(a);
                    Set<Map.Entry<String, Integer>> entries = remove.entrySet();
                    Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
                    while (iterator.hasNext()){
                        Map.Entry<String, Integer> next = iterator.next();
                        String key = next.getKey();

                        if(tempTime!=null&&tempTime.equals(key)){
                            index=a;
                            break;
                        }
                    }
                }
                if(index!=-1){
                    listRaw.remove(index);
                }
                /**如果医生将该天的可用时间删除为空,就将红点隐藏*/
                if(getSize(listRaw)<1){
                    deleteTip.setReddotInvisiable(day);
                }
                notifyDataSetChanged();
                appointSettingActivity.stopProgressDialog();
                ToastUtils.showToast(context, "删除成功", 1000, 1);


            }

            @Override
            public void onFailure() {

            }
        });
    }

    public int getSize(ArrayList<Map<String, Integer>> listRaw){
        int number=0;
        tempList.clear();
        for(int i=0;i<listRaw.size();i++){
            Map<String, Integer> stringIntegerMap = listRaw.get(i);
            Set<Map.Entry<String, Integer>> entries = stringIntegerMap.entrySet();
            Iterator<Map.Entry<String, Integer>> iterator = entries.iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Integer> next = iterator.next();
                String key = next.getKey();
                Integer value = next.getValue();
                if(value>1){
                    tempList.add(stringIntegerMap);
                    number++;
                }
            }
        }
        return number;
    }

    public DeleteTip deleteTip;

    public void setDeleteTip(DeleteTip deleteTip) {
        this.deleteTip = deleteTip;
    }

    public interface DeleteTip{
        void setReddotInvisiable(String date);
    }
    public void setListRaw(ArrayList<Map<String, Integer>> listRaw,String day) {
        this.listRaw = listRaw;
        this.day=day;

        notifyDataSetChanged();
    }
}
