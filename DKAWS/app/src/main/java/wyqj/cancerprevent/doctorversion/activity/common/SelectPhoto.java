package wyqj.cancerprevent.doctorversion.activity.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.kaws.lib.bigimage.ViewBigImageActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.common.utils.image.CallBack_CropImage;
import com.kaws.lib.common.utils.image.CropHelper;

import java.io.File;
import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.activity.mobileconsult.CustomerServiceMessageActivity;
import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;

public class SelectPhoto extends FragmentActivity {
    private Button viewBigPhoto;
    private Button cancel;
    private Button takePhoto;
    private Button pickPhoto;
    private CropHelper cropHelper;
    private String urlOfPhoto = null;
    private boolean isNeedCrop;
    /**订单详情 联系客服 */
    private boolean strFlag;
    /**订单的id*/
    private String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = d.getWidth();    //宽度设置为屏幕的0.8
        getWindow().setAttributes(p);     //设置生效
        getWindow().setGravity(Gravity.BOTTOM);       //设置靠右对齐
        initView();
        isNeedCrop = getIntent().getBooleanExtra(Constants.IS_NEED_CROP, true);
    }

    private void initView() {
        cropHelper = new CropHelper();
        viewBigPhoto = (Button) findViewById(R.id.btn_check_big_photo);
        pickPhoto = (Button) findViewById(R.id.btn_pick_photo);
        takePhoto = (Button) findViewById(R.id.btn_take_photo);
        cancel = (Button) findViewById(R.id.btn_cancel);
        if (getIntent() != null) {
            urlOfPhoto = getIntent().getStringExtra(Constants.URL_OF_PHOTO);
            /**预约详情 联系客服 的标记*/
            strFlag=getIntent().getBooleanExtra(Constants.ORDERDETAIL_TO_SELECTPHOTO,false);
            /**订单号*/
            orderId=getIntent().getStringExtra(Constants.ORDER_ID);
        }
        if (TextUtils.isEmpty(urlOfPhoto)) {
            viewBigPhoto.setVisibility(View.GONE);
        }
        /**如果是预约详情*/
        if(strFlag){
            viewBigPhoto.setVisibility(View.GONE);
            pickPhoto.setText("给客服留言");
            takePhoto.setText("呼叫客服400-101-1510");
        }
        viewBigPhoto.setOnClickListener(listener);
        pickPhoto.setOnClickListener(listener);
        takePhoto.setOnClickListener(listener);
        cancel.setOnClickListener(listener);
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_check_big_photo://查看网络大图
                    showBigPhoto();
                    finish();
                    break;
                case R.id.btn_take_photo:
                    if(strFlag){
                        /**如果是呼叫客服*/
                        Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+"4001011510"));
                        startActivity(intent);
                        finish();

                    }else{
                        cropHelper.startTakePhoto(SelectPhoto.this, isNeedCrop);
                    }
                    break;
                case R.id.btn_pick_photo:
                    if(strFlag){
                        /**给客服留言*/
                        Intent in=new Intent(SelectPhoto.this,CustomerServiceMessageActivity.class);
                        in.putExtra(Constants.ORDER_ID,orderId);
                        startActivityForResult(in,Constants.SELECTPHOTO_TO_CUSTOMMESSAGE_REQUEST_CODE);
                    }else{
                        cropHelper.startImagePick(SelectPhoto.this, isNeedCrop);
                    }
                    break;
                case R.id.btn_cancel:
                    onBackPressed();
                    break;
                default:
                    break;
            }
        }
    };

    private CallBack_CropImage callBack_cropImage = new CallBack_CropImage() {
        @Override
        public void finishImageCrop(String path, Uri uri, Bitmap cropedBitmap, File fileBitmap) {
            DebugUtil.debug("--->", "url :" + path);
            Intent data = new Intent();
            data.putExtra(Constants.SELECT_PHOTO, path);
            setResult(RESULT_OK, data);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**联系客服*/
        if(requestCode==Constants.SELECTPHOTO_TO_CUSTOMMESSAGE_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }else{
                finish();
            }
            return;
        }

        if (resultCode != RESULT_OK) {
            return;
        }

        cropHelper.handleRequestCode(requestCode, callBack_cropImage, data, SelectPhoto.this);
    }

    private void showBigPhoto() {
        Bundle bundle = new Bundle();
        bundle.putInt("selet", 1);
        bundle.putInt("code", 0);
        bundle.putBoolean("isLocal", false);
        ArrayList<String> imageuri = new ArrayList<>();
        imageuri.add(urlOfPhoto);
        bundle.putStringArrayList("imageuri", imageuri);
        Intent intent = new Intent(SelectPhoto.this, ViewBigImageActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
