package wyqj.cancerprevent.doctorversion.adapter.mine;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;
import com.kaws.lib.common.utils.TimeUtil;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.IncomeRecordBean;

/**
 * Created by 杨才 on 2016/2/21.
 */
public class MyIncomeRecordAdapter extends BaseRecyclerViewAdapter<IncomeRecordBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incomerecord, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseRecyclerViewHolder<IncomeRecordBean> {
        private final ForegroundColorSpan span;
        TextView question;
        TextView money;
        TextView time;

        public ViewHolder(View childView) {
            super(childView);
            question = (TextView) childView.findViewById(R.id.tv_record_question);
            money = (TextView) childView.findViewById(R.id.tv_record_money);
            time = (TextView) childView.findViewById(R.id.tv_record_time);
            span = new ForegroundColorSpan(childView.getContext().getResources().getColor(R.color.but_bg));
        }

        @Override
        public void onBindViewHolder(IncomeRecordBean entity, int position) {

            String time = entity.createdAt;
            String transedtime = TimeUtil.getTranslateTime(time);
            this.time.setText(transedtime);
            int money = entity.amount;
            this.money.setText("+" + money);

            int action = entity.action;
            String content = entity.content;
            if (content != null) {
                switch (action) {
                    case 4:  //9-
                        SpannableStringBuilder builder4 = new SpannableStringBuilder("成功回答问题：" + content);
                        builder4.setSpan(span, 7, content.length() + 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        question.setText(builder4);
                        break;
                    case 5: //6-
                        SpannableStringBuilder builder5 = new SpannableStringBuilder("继续成为" + content + "的私人医生");
                        builder5.setSpan(span, 4, content.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        question.setText(builder5);
                        break;
                    case 7: //4-
                        SpannableStringBuilder builder7 = new SpannableStringBuilder("同意" + content + "成为我的患者");
                        builder7.setSpan(span, 2, content.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        question.setText(builder7);
                        break;
                    case 10://6-
                        SpannableStringBuilder builder10 = new SpannableStringBuilder("填写医生" + content + "的邀请码注册成功");
                        builder10.setSpan(span, 4, content.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        question.setText(builder10);
                        break;
//                case 12://4-
//                    SpannableStringBuilder builder12 = new SpannableStringBuilder("邀请" + content + "注册用户版成功");
//                    builder12.setSpan(span, 2, content.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    holder.question.setText(builder12);
//                    break;
                    case 13://9-
                        SpannableStringBuilder builder13 = new SpannableStringBuilder("一周成功回答了" + content + "个问题");
                        builder13.setSpan(span, 7, content.length() + 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        question.setText(builder13);
                        break;
                    case 14://7-
                        SpannableStringBuilder builder14 = new SpannableStringBuilder("成功签约" + content + "个患者并回答所有患者提问");
                        builder14.setSpan(span, 4, content.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        question.setText(builder14);
                        break;
                    case 15: //6-
                        SpannableStringBuilder builder15 = new SpannableStringBuilder("邀请医生" + content + "注册并通过审核认证");
                        builder15.setSpan(span, 4, content.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        question.setText(builder15);
                        break;
                    default:
                        break;
                }
            }

            switch (entity.status) {
                case 0: //已完成
                    SpannableStringBuilder builder16 = new SpannableStringBuilder("提现申请-" + "已完成");
                    builder16.setSpan(span, 5, "已完成".length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    question.setText(builder16);
                    break;
                case 1: //处理中
                    SpannableStringBuilder builder17 = new SpannableStringBuilder("提现申请-" + "处理中");
                    builder17.setSpan(span, 5, "处理中".length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    question.setText(builder17);
                    break;
                default:
                    break;
            }
        }
    }
}
