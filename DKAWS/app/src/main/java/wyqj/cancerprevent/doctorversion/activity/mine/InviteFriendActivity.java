package wyqj.cancerprevent.doctorversion.activity.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.common.base.LoadBaseActivity;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.widget.LoadDialog;
import com.kaws.lib.fresco.Image;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.bean.UserBean;
import wyqj.cancerprevent.doctorversion.utils.SQuser;

public class InviteFriendActivity extends LoadBaseActivity {

    private Button btnInvitefriendShareFriendcircle;
    private Button btnInvitefriendShareSina;
    private Button btnInvitefriendShareWeixin;
    private Button btnInvitefriendShareQqspace;
    private TextView tvInvitefriendName;
    private TextView tvInvitefriendGrade;
    private TextView tvInvitefriendHospital;
    private TextView tvInvitefriendInvitecode;
    private TextView tvInvitefriendOffice;
    private SimpleDraweeView ivInvitefriendTouxiang;
    private String shareUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        showContentView();
        setTitle("邀请好友");
        initView();
        setUpView();
        Config.dialog = LoadDialog.buildDialog(this);
    }

    private void initView() {
        btnInvitefriendShareFriendcircle = (Button) findViewById(R.id.btn_invitefriend_share_friendcircle);
        btnInvitefriendShareSina = (Button) findViewById(R.id.btn_invitefriend_share_sina);
        btnInvitefriendShareWeixin = (Button) findViewById(R.id.btn_invitefriend_share_weixin);
        btnInvitefriendShareQqspace = (Button) findViewById(R.id.btn_invitefriend_share_qqspace);

        tvInvitefriendName = (TextView) findViewById(R.id.tv_invitefriend_name);
        tvInvitefriendGrade = (TextView) findViewById(R.id.tv_invitefriend_grade);
        tvInvitefriendHospital = (TextView) findViewById(R.id.tv_invitefriend_hospital);
        tvInvitefriendInvitecode = (TextView) findViewById(R.id.tv_invitefriend_invitecode);
        tvInvitefriendOffice = (TextView) findViewById(R.id.tv_invitefriend_office);
        ivInvitefriendTouxiang = getView(R.id.iv_invitefriend_touxiang);
    }

    private void setUpView() {
        UserBean.Doctor doctor = SQuser.getInstance().getUserInfo().info;
        if (doctor != null) {
            tvInvitefriendName.setText(doctor.name);
            tvInvitefriendGrade.setText(" - " + doctor.degree.name);
            tvInvitefriendHospital.setText(doctor.hospitalName);
            tvInvitefriendInvitecode.setText(doctor.invitationCode);
            tvInvitefriendOffice.setText(doctor.department);
            Image.displayRound(ivInvitefriendTouxiang, 10, Uri.parse(doctor.avatarUrl));
            shareUrl = doctor.shareLink;
            btnInvitefriendShareQqspace.setOnClickListener(listener);
            btnInvitefriendShareFriendcircle.setOnClickListener(listener);
            btnInvitefriendShareSina.setOnClickListener(listener);
            btnInvitefriendShareWeixin.setOnClickListener(listener);
        }
    }

    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(View v) {
            UMImage image = new UMImage(InviteFriendActivity.this, R.drawable.shareimage);
            SHARE_MEDIA shareMedia = null;
            switch (v.getId()) {
                case R.id.btn_invitefriend_share_qqspace:
                    shareMedia = SHARE_MEDIA.QZONE;
                    break;
                case R.id.btn_invitefriend_share_weixin:
                    shareMedia = SHARE_MEDIA.WEIXIN;
                    break;
                case R.id.btn_invitefriend_share_sina:
                    shareMedia = SHARE_MEDIA.SINA;
                    break;
                case R.id.btn_invitefriend_share_friendcircle:
                    shareMedia = SHARE_MEDIA.WEIXIN_CIRCLE;
                    break;
                default:
                    break;
            }
            new ShareAction(InviteFriendActivity.this).setPlatform(shareMedia).setCallback(umShareListener).withText("欢迎加入抗癌卫士医生平台！").withTitle("欢迎加入抗癌卫士医生平台！").withTargetUrl(shareUrl).withMedia(image).share();
        }
    };
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(InviteFriendActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(InviteFriendActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(InviteFriendActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
