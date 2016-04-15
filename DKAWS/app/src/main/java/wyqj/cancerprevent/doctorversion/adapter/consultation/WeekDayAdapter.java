package wyqj.cancerprevent.doctorversion.adapter.consultation;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import wyqj.cancerprevent.doctorversion.R;

/**
 * Created by xiaguangcheng on 16/3/24.
 */
public class WeekDayAdapter extends BaseAdapter {
    private Context context;
    private GridView gridView;

    public void setCurrentClickDay(String currentClickDay) {
        this.currentClickDay = currentClickDay;
    }

    private String currentClickDay;
    private ClickReturnDate clickReturnDate;
    private int currenDaySeconds;
    private ArrayList<String> listRaw;
    private long todayMillSeconds;

    private ArrayList<String> tempList;
    /**设置医生已经选中的日期的红点标记*/
    public void setListSelected(List<String> listSelected) {
        this.listSelected = listSelected;
        notifyDataSetChanged();
    }

    private List<String> listSelected;

    public void setListRaw(ArrayList<String> listRaw) {
        this.listRaw = listRaw;
        this.tempList=listRaw;
        notifyDataSetChanged();
    }

    public void setClickReturnDate(ClickReturnDate clickReturnDate) {
        this.clickReturnDate = clickReturnDate;
    }


    public interface ClickReturnDate{
        /**
         *
         * @param date 医生点击具体当某个日期时,会将这个日期值返回.格式为:"20150302 一"
         */
        void getDate(String date);
    }

    public WeekDayAdapter(Context context,GridView gridView,String currentClickDay,long todayMillSeconds){
        this.context=context;
        this.gridView=gridView;
        this.currentClickDay=currentClickDay;
        this.todayMillSeconds=todayMillSeconds;
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context, R.layout.item_weekday,null);
        TextView text_week = (TextView) view.findViewById(R.id.text_week);
        final Button text_date= (Button) view.findViewById(R.id.text_date);

        String s=listRaw.get(position);
        String weekday=s.substring(s.length() - 1, s.length());
        text_week.setText(weekday);
        String date=s.substring(6, 8);
        text_date.setText(date);

        String someDay= listRaw.get(position);
        Drawable drawable=context.getResources().getDrawable(R.drawable.shape_reddot);
        if(listSelected!=null){
            for(int i=0;i<listSelected.size();i++){
                /**如果已经定制了某天有空闲时间,那么某天就显示出小红点*/
                /**这两个集合日期的格式都是 20140406*/

                if((listSelected.get(i).replaceAll("-","")).equals(listRaw.get(position).substring(0,8))){
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    text_date.setCompoundDrawables(null,null,null,drawable);
                    break;
                }else{
                    text_date.setCompoundDrawables(null,null,null,null);
                }
            }
        }

        final int year=Integer.valueOf(someDay.substring(0, 4));
        final int month=Integer.valueOf(someDay.substring(4, 6));
        final int day =Integer.valueOf(someDay.substring(6,8));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();

        /**这里设置的时候,month需要减1*/
        calendar.set(year, month-1, day);
        calendar.set(Calendar.MILLISECOND,0);
        final long somedaySeconds =calendar.getTime().getTime();
        calendar=Calendar.getInstance();
        String currday=dateFormat.format(calendar.getTime());

        if(somedaySeconds<todayMillSeconds){
            /**所有小于当前毫秒值的天,都不能点击*/
            text_date.setTextColor(Color.parseColor("#999999"));
        }else{
            text_date.setTextColor(context.getResources().getColor(R.color.selector_choose_date_color));
            text_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**触发的回调方法*/
                    clickReturnDate.getDate(listRaw.get(position));
                    /**当医生点击时,当前日期修改为医生点击的日期*/
                    currentClickDay = listRaw.get(position);
                    for (int i = 0; i < listRaw.size(); i++) {
                        final Button text_date = (Button) gridView.getChildAt(i).findViewById(R.id.text_date);
                        if (i != position) {
                            text_date.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    text_date.setSelected(false);
                                }
                            }, 50);
                        }
                    }
                    text_date.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            text_date.setSelected(true);
                        }
                    }, 50);
                }
            });
        }
            /**默认选中当前日期*/
        if(!TextUtils.isEmpty(currentClickDay)&&currentClickDay.equals(listRaw.get(position))){
            text_date.setSelected(true);
        }
        return view;
    }
}
