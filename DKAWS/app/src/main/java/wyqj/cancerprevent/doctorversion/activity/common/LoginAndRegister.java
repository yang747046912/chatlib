package wyqj.cancerprevent.doctorversion.activity.common;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kaws.lib.common.base.BaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.ActivityStackUtil;

import wyqj.cancerprevent.doctorversion.R;

public class LoginAndRegister extends BaseActivity {

    private TextView registe;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        registe = getView(R.id.tv_login_register_register);
        registe.setOnClickListener(listener);
        login = getView(R.id.tv_login_register_login);
        login.setOnClickListener(listener);
        ActivityStackUtil.getInstance().add(this);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_login_register_register:
                    openActivity(Register.class);
                    break;
                case R.id.tv_login_register_login:
                    openActivity(Login.class);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        ActivityStackUtil.getInstance().remove(this);
        super.onDestroy();
    }
}
