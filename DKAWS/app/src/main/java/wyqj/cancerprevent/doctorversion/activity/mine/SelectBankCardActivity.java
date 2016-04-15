package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kaws.lib.common.base.LoadBaseActivity;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.constant.Constants;

public class SelectBankCardActivity extends LoadBaseActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank_card);
        showContentView();
        setTitle("选择发卡银行");
        initView();
        setUpView();
    }

    private void setUpView() {
        listView.setAdapter(new BankAdapter(Constants.banks, Constants.resIds));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                intent.putExtra(Constants.SELECT_BANK_ICON, Constants.resIds[position]);
                intent.putExtra(Constants.SELECT_BANK_NAME, Constants.banks[position]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initView() {
        listView = (ListView) this.findViewById(R.id.list);
    }

    public class BankAdapter extends BaseAdapter {

        private String[] banks;
        private int[] resIds;

        public BankAdapter(String[] banks, int[] resIds) {
            this.banks = banks;
            this.resIds = resIds;
        }

        public int getCount() {
            return banks.length;
        }

        public View getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;
            if (convertView != null) {
                view = convertView;
            } else {
                view = View.inflate(SelectBankCardActivity.this, R.layout.item_select_bankcard, null);
            }
            ImageView bankIcon = (ImageView) view.findViewById(R.id.img_selectbank_icon);
            TextView bankName = (TextView) view.findViewById(R.id.tv_selectbank_name);
            bankIcon.setImageResource(resIds[position]);
            bankName.setText(banks[position]);

            return view;
        }
    }
}
