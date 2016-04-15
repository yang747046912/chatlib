package wyqj.cancerprevent.doctorversion.headview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kaws.lib.bigimage.ViewBigImageActivity;
import com.kaws.lib.common.utils.DensityUtil;
import com.kaws.lib.common.utils.ScreenSizeUtil;
import com.kaws.lib.fresco.Image;

import java.util.ArrayList;

import wyqj.cancerprevent.doctorversion.constant.Constants;
import wyqj.cancerprevent.doctorversion.R;
import wyqj.cancerprevent.doctorversion.adapter.PicGridAdapter;
import wyqj.cancerprevent.doctorversion.bean.QuestionDetailBean;

/**
 * Created by 杨才 on 2016/2/3.
 */
public class QuestionDetailHead extends BaseHead<QuestionDetailBean> {
    private Context mContex;
    private TextView tvDetailContain;
    private TextView tvDetailKind;
    private TextView tvDetailName;
    private TextView tvDetailTitle;
    private SimpleDraweeView ivDetailTouxiang;
    private SimpleDraweeView ivDetailPic;
    private GridView gvDetailPic;
    private LinearLayout ll_image;
    private String username;
    private String avatarUrl;
    private String patientUserkey;
    private String kind;
    private ArrayList<String> images = new ArrayList<>();

    public QuestionDetailHead(Context mContex) {
        this.mContex = mContex;
    }


    @Override
    public View getHeadView(int layoutID) {
        View headview = View.inflate(mContex, layoutID, null);
        tvDetailContain = (TextView) headview.findViewById(R.id.tv_detail_contain);
        tvDetailKind = (TextView) headview.findViewById(R.id.tv_detail_kind);
        tvDetailName = (TextView) headview.findViewById(R.id.tv_detail_name);
        tvDetailTitle = (TextView) headview.findViewById(R.id.tv_detail_title);
        ivDetailTouxiang = (SimpleDraweeView) headview.findViewById(R.id.iv_detail_touxiang);
        ivDetailPic = (SimpleDraweeView) headview.findViewById(R.id.iv_detail_pic);
        gvDetailPic = (GridView) headview.findViewById(R.id.gv_detail_pic);
        ll_image = (LinearLayout) headview.findViewById(R.id.ll_image);
        return headview;
    }

    @Override
    public void setUpView(QuestionDetailBean questionDetailEntity) {
        if (questionDetailEntity.question != null) {
            String content = questionDetailEntity.question.content;
            if (questionDetailEntity.question.questioner != null) {
                username = questionDetailEntity.question.questioner.username;
                avatarUrl = questionDetailEntity.question.questioner.avatarUrl;
            }
            patientUserkey = questionDetailEntity.question.questioner.userkey;
            String title = questionDetailEntity.question.title;
            if (questionDetailEntity.question.diseasedState != null) {
                kind = questionDetailEntity.question.diseasedState.name;
            }
            tvDetailContain.setText(content);
            tvDetailName.setText(username);
            tvDetailKind.setText(kind);
            tvDetailTitle.setText(title);
            Image.displayRound(ivDetailTouxiang, Constants.headRadiusPixels, Uri.parse(avatarUrl));
            int length = questionDetailEntity.question.images.size();
            images.clear();
            for (int i = 0; i < length; i++) {
                images.add(questionDetailEntity.question.images.get(i).url);
            }

            if (images.size() == 1) {
                ivDetailPic.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = ivDetailPic.getLayoutParams();
                params.height = ScreenSizeUtil.getScreenWidth(mContex) * 2 / 3;
                params.width = ScreenSizeUtil.getScreenWidth(mContex) * 2 / 3;
                ivDetailPic.setLayoutParams(params);
                Image.displayImage(ivDetailPic, Uri.parse(images.get(0)));
            } else if (images.size() == 4) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((ScreenSizeUtil.getScreenWidth(mContex) - DensityUtil.dip2px(mContex, 30)) * 2 / 3 + DensityUtil.dip2px(mContex, 4), ScreenSizeUtil.getScreenWidth(mContex) * 2 / 3);
                lp.setMargins(DensityUtil.dip2px(mContex, 15), 0, 0, 0);
                gvDetailPic.setLayoutParams(lp);
                gvDetailPic.setHorizontalSpacing(DensityUtil.dip2px(mContex, 4));
                gvDetailPic.setNumColumns(2);
                PicGridAdapter picGridAdapter = new PicGridAdapter(mContex, images);
                gvDetailPic.setAdapter(picGridAdapter);
            } else if (images.size() > 1) {
                PicGridAdapter picGridAdapter = new PicGridAdapter(mContex, images);
                gvDetailPic.setAdapter(picGridAdapter);
            } else {
                ll_image.setVisibility(View.GONE);
                ivDetailPic.setVisibility(View.GONE);
            }

            gvDetailPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("code", i);
                    bundle.putInt("selet", 2);
                    bundle.putStringArrayList("imageuri", images);
                    Intent intent = new Intent(mContex, ViewBigImageActivity.class);
                    intent.putExtras(bundle);
                    mContex.startActivity(intent);
                }
            });
            ivDetailPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("code", 0);
                    bundle.putInt("selet", 1);
                    bundle.putStringArrayList("imageuri", images);
                    Intent intent = new Intent(mContex, ViewBigImageActivity.class);
                    intent.putExtras(bundle);
                    mContex.startActivity(intent);
                }
            });
        }
    }

}
