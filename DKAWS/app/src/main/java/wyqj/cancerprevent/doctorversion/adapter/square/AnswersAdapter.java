package wyqj.cancerprevent.doctorversion.adapter.square;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.TimeUtil;
import com.kaws.lib.fresco.Image;

import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.square.QuestionDetailActivity;
import wyqj.cancerprevent.doctorversion.bean.QuestionBean;

/**
 * Created by 杨才 on 2016/2/3.
 */
public class AnswersAdapter extends BaseRecyclerViewAdapter<QuestionBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_consult_square, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseRecyclerViewHolder<QuestionBean> {
        TextView title;
        TextView time;
        TextView money;
        SimpleDraweeView touxiang;
        TextView name;
        TextView kindofdisease;

        public ViewHolder(View childView) {
            super(childView);
            touxiang = (SimpleDraweeView) childView.findViewById(R.id.iv_item_consult_square_touxiang);
            kindofdisease = (TextView) childView.findViewById(R.id.tv_item_consult_square_kindofdisease);
            money = (TextView) childView.findViewById(R.id.tv_item_consult_square_money);
            name = (TextView) childView.findViewById(R.id.tv_item_consult_square_name);
            time = (TextView) childView.findViewById(R.id.tv_item_consult_square_time);
            title = (TextView) childView.findViewById(R.id.tv_item_consult_square_title);
        }

        @Override
        public void onBindViewHolder(final QuestionBean questionEntity, int position) {

            String iamgeUrl = null;
            if (questionEntity.user != null && questionEntity.user.avatarUrl != null) {
                iamgeUrl = questionEntity.user.avatarUrl;
            }
            Image.displayRound(touxiang, 5, Uri.parse(iamgeUrl == null ? "" : iamgeUrl));
            if (questionEntity.diseaseState != null) {
                kindofdisease.setText(questionEntity.diseaseState.diseaseName);
            }
            money.setText(questionEntity.price + "");
            if (questionEntity.user != null && questionEntity.user.username != null) {
                name.setText(questionEntity.user.username);
            }
            title.setText(questionEntity.title);

            String time = questionEntity.latestCommentTime;
            String transtime = TimeUtil.getTranslateTime(time);
            this.time.setText(transtime);
            itemView.setOnClickListener(new PerfectClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    Intent intent = new Intent(v.getContext(), QuestionDetailActivity.class);
                    intent.putExtra(Constants.QUESTION_ID, questionEntity.id);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
