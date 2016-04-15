package wyqj.cancerprevent.doctorversion.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.kaws.lib.common.event.PerfectClickListener;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.PopouAdapter;

/**
 * Created by 杨才 on 2016/2/3.
 */
public class ListViewPopup extends PopupWindow {

    private Context mContext;
    private ListView listView;

    public ListViewPopup(final Context mContext, final ISelecterLinstener selector, final String[] list) {
        this.mContext = mContext;
        View view = View.inflate(mContext, R.layout.list_popup, null);
        listView = (ListView) view.findViewById(R.id.mlv_popup);
        View blank = view.findViewById(R.id.blank);
        setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new ColorDrawable(0x555555));
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setOutsideTouchable(true);
        setFocusable(true);
        PopouAdapter adapter = new PopouAdapter(mContext, list);
        adapter.setLinstener(selector);
        listView.setAdapter(adapter);
        blank.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                ListViewPopup.this.dismiss();
            }
        });
    }

    public interface ISelecterLinstener {
        void onSelected(int id);
    }
}
