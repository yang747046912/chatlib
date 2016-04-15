package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import wyqj.cancerprevent.doctorversion.R;

public class AgreementActivity extends LoadBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        showContentView();
        setTitle("服务协议");
        TextView jtAgreement = getView(R.id.jt_agreement);
        jtAgreement.setText(getAssetsString(this, "agreement.txt"));
    }

    public String getAssetsString(Context context, String fileName) {
        StringBuffer sb = new StringBuffer();
        // 根据语言选择加载
        try {
            AssetManager am = context.getAssets();
            InputStream in = am.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                line += ("\n");
                sb.append(line);
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
