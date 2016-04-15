package wyqj.cancerprevent.doctorversion.adapter.consultation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.R;

/**
 * Created by xiaguangcheng on 16/3/24.
 */
public class SelectPerfectReasonAdapter extends BaseAdapter {

    /**六个需要完善的信息*/
    private ArrayList<String> rawList;
    private Context context;
    private boolean isSingle;
    private GridView gv;
    public void setSelectReaseonListener(SelectReaseonListener selectReaseonListener) {
        this.selectReaseonListener = selectReaseonListener;
    }

    private SelectReaseonListener selectReaseonListener;

    /**
     *
     * @param context
     * @param isSingle false 多选,true,单选
     */
    public SelectPerfectReasonAdapter(Context context,boolean isSingle,GridView gv){
        this.context=context;
        this.isSingle=isSingle;
        this.gv=gv;
    }

    @Override
    public int getCount() {
        return rawList==null?0:rawList.size();
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
        View view =View.inflate(context, R.layout.item_perfect_reason,null);
        CheckBox checkBox= (CheckBox) view.findViewById(R.id.item_reason);
        checkBox.setText(rawList.get(position));
        if(isSingle){
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSingle){
                        for(int i=0;i<rawList.size();i++){
                            final CheckBox cb = (CheckBox) gv.getChildAt(i).findViewById(R.id.item_reason);
                            if(i==position){
                                selectReaseonListener.selectReason(position, true);
                                cb.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        cb.setChecked(true);
                                    }
                                },50);
                            }else{
                                cb.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        cb.setChecked(false);
                                    }
                                },50);
                            }
                        }
                    }
                }
            });
        }else{
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectReaseonListener.selectReason(position, isChecked);

                }
            });
        }


        return view;
    }
    /**设置数据源*/
    public void setRawList(ArrayList<String> rawList) {
        this.rawList = rawList;
        notifyDataSetChanged();
    }

    /**当医生选择患者所要完善当信息时当回调*/
    public interface SelectReaseonListener{
        /**
         *
         * @param position 医生所选择的当前索引
         * @param isChecked 医生所选择的当前索引的是true还是false.true表示选中,false 表示未选中.当处于单选模式时,ischecked始终是true.
         */
        void selectReason(int position,boolean isChecked);
    }
}
