package wyqj.cancerprevent.doctorversion.activity.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import wyqj.cancerprevent.doctorversion.activity.common.Login;

/**
 * Created by 杨才 on 2016/2/26.
 */
public class EMchatCreat {
    public static void Oncreat(Bundle savedInstanceState, Activity context) {
        if (savedInstanceState != null && savedInstanceState.getBoolean(EMchatConstant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            EaseUIHelper.getInstance().logout(true, null);
            context.finish();
            context.startActivity(new Intent(context, Login.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean(EMchatConstant.ACCOUNT_CONFLICT, false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            context.finish();
            context.startActivity(new Intent(context, Login.class));
            return;
        }
    }

    public static void saveSate(Bundle outState, boolean isConflict, boolean isCurrentAccountRemoved) {
        outState.putBoolean(EMchatConstant.ACCOUNT_CONFLICT, isConflict);
        outState.putBoolean(EMchatConstant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
    }
}
