package wyqj.cancerprevent.doctorversion.adapter.square;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;
import com.kaws.lib.common.utils.TimeUtil;
import com.kaws.lib.fresco.Image;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.PatientBean;
import wyqj.cancerprevent.doctorversion.bean.QuestionDetailBean;

/**
 * Created by 杨才 on 2016/2/3.
 */
public class QuestionDetailAdapter extends BaseRecyclerViewAdapter<QuestionDetailBean.comments> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v =  View.inflate(parent.getContext(), R.layout.item_questiondetail_answer, null);
        return new ViewHolder(v);
    }

    class ViewHolder extends BaseRecyclerViewHolder<QuestionDetailBean.comments> {
        SimpleDraweeView header;
        TextView name;
        TextView time;
        TextView reply;
        LinearLayout ll_layout;

        public ViewHolder(View childView) {
            super(childView);
            header = (SimpleDraweeView) childView.findViewById(R.id.iv_itemdetail_header);
            name = (TextView) childView.findViewById(R.id.tv_itemdetail_name);
            time = (TextView) childView.findViewById(R.id.tv_itemdetail_time);
            reply = (TextView) childView.findViewById(R.id.tv_itemdetail_reply);
            ll_layout = (LinearLayout) childView.findViewById(R.id.ll_layout);
        }

        @Override
        public void onBindViewHolder(QuestionDetailBean.comments object, int position) {
            QuestionDetailBean.Doctor doctorInfo = object.doctor;
            PatientBean userInfo = object.user;
            if (object.isDoctor) {
                if (doctorInfo != null) {
                    Image.displayRound(header, 5, Uri.parse(doctorInfo.avatarUrl));
                    name.setText(doctorInfo.name);
                }
            } else {
                if (userInfo != null) {
                    Image.displayRound(header, 5, Uri.parse(userInfo.avatarRrl));
                    name.setText(userInfo.userkey);
                }
            }
            reply.setText(object.content);

            String time = object.createdAt;
            String transtime = TimeUtil.getTranslateTime(time);
            this.time.setText(transtime);
//            /**解决文章太长，引起的下拉bug*/
//            if (commentList.size() == 1 && TextUtils.isEmpty(commentList.get(0).content) && commentList.get(0).info == null) {
//                childView.setVisibility(View.GONE);
//            }
        }
    }
}
