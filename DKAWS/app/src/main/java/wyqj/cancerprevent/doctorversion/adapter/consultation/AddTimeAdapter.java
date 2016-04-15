package wyqj.cancerprevent.doctorversion.adapter.consultation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.kaws.lib.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import wyqj.cancerprevent.doctorversion.R;


/**
 * Created by xiaguangcheng on 16/3/25.
 */
public class AddTimeAdapter extends BaseAdapter {
    Context context;

    public void setListRaw(ArrayList<Map<String,Integer>> listRaw) {
        this.listRaw = listRaw;
        notifyDataSetChanged();
    }

    private ArrayList<Map<String,Integer>> listRaw;
    public AddTimeAdapter (Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return listRaw==null?0:listRaw.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =View.inflate(context, R.layout.item_timetable,null);
        final TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        final Map<String,Integer> hashMap =listRaw.get(position);
        int value=0;
        String time="";
        Set<Map.Entry<String, Integer>> entrySet = hashMap.entrySet();
        Iterator<Map.Entry<String, Integer>> iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            time=(String)(entry.getKey());
            tv_time.setText(time);
            value=(Integer)entry.getValue();
        }

        /**当前状态是未选择,点一次,表示选中当天生效*/
        if(value ==1){
            tv_time.setBackgroundResource(R.drawable.shape_checktime_default);
        }else if(value==2){
            /**当前状态是当天生效,点击一次,表示周循环*/
            tv_time.setBackgroundResource(R.drawable.shape_checktime_once);
        }else if(value==3){
            /**当前状态是周循环,点击一次表示取消选中*/
            tv_time.setBackgroundResource(R.drawable.shape_checktime_twice);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Set<Map.Entry<String, Integer>> entrySet = hashMap.entrySet();
                Iterator<Map.Entry<String, Integer>> iterator = entrySet.iterator();
                while (iterator.hasNext()){
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String time=(String)(entry.getKey());
                    int value=(Integer)entry.getValue();
                    /**当前状态是未选择,点一次,表示选中当天生效*/
                    if(value ==1){
                        tv_time.setBackgroundResource(R.drawable.shape_checktime_once);
                        hashMap.put(time,2);

                    }else if(value==2){
                        /**当前状态是当天生效,点击一次,表示周循环*/
                        tv_time.setBackgroundResource(R.drawable.shape_checktime_twice);
                        hashMap.put(time, 3);
                    }else if(value==3){
                        /**当前状态是周循环,点击一次表示取消选中*/
                        tv_time.setBackgroundResource(R.drawable.shape_checktime_default);
                        hashMap.put(time, 1);
                    }
                }
            }
        });
        return view;
    }


}
