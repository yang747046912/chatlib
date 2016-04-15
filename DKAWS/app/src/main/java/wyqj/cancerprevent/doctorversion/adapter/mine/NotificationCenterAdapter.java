package wyqj.cancerprevent.doctorversion.adapter.mine;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;
import com.kaws.lib.common.utils.TimeUtil;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.MedicalRecordItemBean;
import wyqj.cancerprevent.doctorversion.bean.NotificationCenterListBean;

/**
 * Created by 景彬 on 2016/3/25.
 */
public class NotificationCenterAdapter extends BaseRecyclerViewAdapter<NotificationCenterListBean> {

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_notification_center, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseRecyclerViewHolder<NotificationCenterListBean> {
        TextView title;
        TextView time;
        TextView content;

        public ViewHolder(View childView) {
            super(childView);
            title = (TextView) childView.findViewById(R.id.tv_title);
            time = (TextView) childView.findViewById(R.id.tv_time);
            content = (TextView) childView.findViewById(R.id.tv_content);
        }

        @Override
        public void onBindViewHolder(final NotificationCenterListBean notificationCenterListBean, int position) {
            String time = null;
            String content = null;
            String title = null;
            if (notificationCenterListBean != null) {
                title = notificationCenterListBean.getTitle();
                content = notificationCenterListBean.getContent();
                time = notificationCenterListBean.getCreated_at();
            }
            String transedTime = TimeUtil.getTranslateTime(time);
            this.time.setText(transedTime);
            this.title.setText(title);
            this.content.setText(content);

        }
    }

}
