package wyqj.cancerprevent.doctorversion.adapter.patient;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewAdapter;
import com.kaws.lib.common.baseadapter.BaseRecyclerViewHolder;
import com.kaws.lib.common.cache.ACache;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.fresco.Image;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.activity.chat.ChatActivity;
import wyqj.cancerprevent.doctorversion.bean.PatientBean;
import wyqj.cancerprevent.doctorversion.bean.PatientListBean;

/**
 * Created by 杨才 on 2016/2/24.
 */
public class PatientListAdapter extends BaseRecyclerViewAdapter<PatientListBean> {
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_patientlist, null);
        return new ViewHolder(view);
    }

    class ViewHolder extends BaseRecyclerViewHolder<PatientListBean> {
        private SimpleDraweeView avatar;
        private TextView name;

        public ViewHolder(View childView) {
            super(childView);
            avatar = (SimpleDraweeView) childView.findViewById(R.id.iv_patientlist_avatar);
            name = (TextView) childView.findViewById(R.id.tv_patientlist_name);
        }

        @Override
        public void onBindViewHolder(PatientListBean object, int position) {
            final PatientBean patient = object.patient;
            if (patient != null) {
                Image.displayRound(avatar, 10, Uri.parse(patient.avatarRrl));
                ACache.get(name.getContext()).put(patient.userkey, patient.avatarRrl, 5000);
                name.setText(patient.username);
                itemView.setOnClickListener(new PerfectClickListener() {
                    @Override
                    protected void onNoDoubleClick(View v) {
                        String patientUserkey = patient.userkey;
                        Intent intent = new Intent(v.getContext(), ChatActivity.class);
                        intent.putExtra("userId", patientUserkey);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
