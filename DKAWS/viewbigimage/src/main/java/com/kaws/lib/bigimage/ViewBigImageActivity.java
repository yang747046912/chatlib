package com.kaws.lib.bigimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.kaws.lib.bigimage.widget.photodraweeview.OnPhotoTapListener;
import com.kaws.lib.bigimage.widget.photodraweeview.PhotoDraweeView;
import com.kaws.lib.common.event.PerfectClickListener;
import com.kaws.lib.common.utils.FileUtil;
import com.kaws.lib.common.utils.Md5Util;
import com.kaws.lib.fresco.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 用于查看大图
 * 可查看 本地图片
 * 网络图片
 * drable 图片
 *
 * @author lenovo
 */
public class ViewBigImageActivity extends FragmentActivity implements OnPageChangeListener {

    /*保存图片*/
    private TextView txt_save_image_big_activity;
    // 接收传过来的uri地址
    List<String> imageuri;
    // 接收穿过来当前选择的图片的数量
    int code;
    // 用于判断是头像还是文章图片
    int selet;
    // 接收传值
    Bundle bundle;

    /**
     * 用于管理图片的滑动
     */
    ViewPager very_image_viewpager;
    /*当前页数*/
    private int page;

    /**
     * 显示当前图片的页数
     */
    TextView very_image_viewpager_text;
    /**
     * 用于判断是否是加载本地图片
     */
    private boolean isLocal;

    ViewPagerAdapter adapter;

    /**
     * 本应用图片的id
     */
    private int imageId;
    /**
     * 是否是本应用中的图片
     */
    private boolean isApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cntican_cancer_com_new_item_very_image);
        getView();
    }

    private void saveFile(File f) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "抗癌卫士");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        File[] flies = appDir.listFiles();
        for (File file : flies) {
            if (Md5Util.isSameMd5(f, file)) {
                Toast.makeText(ViewBigImageActivity.this, "文件已经存在", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        if (FileUtil.copy(f, file)) {
            Toast.makeText(ViewBigImageActivity.this, "保存成功", Toast.LENGTH_LONG).show();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsoluteFile())));
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "抗癌卫士");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsoluteFile())));
    }

    /*
     * 接收控件
     */
    private void getView() {
        /************************* 接收控件 ***********************/
        very_image_viewpager_text = (TextView) findViewById(R.id.very_image_viewpager_text);
        txt_save_image_big_activity = (TextView) findViewById(R.id.txt_save_image_big_activity);
        very_image_viewpager = (ViewPager) findViewById(R.id.very_image_viewpager);
        txt_save_image_big_activity.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (isApp) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
                    if (bitmap != null) {
                        saveImageToGallery(ViewBigImageActivity.this, bitmap);
                        Toast.makeText(ViewBigImageActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    File file = Image.getCachedImageOnDisk(Uri.parse(imageuri.get(page)));
                    saveFile(file);
                }
            }
        });
        /************************* 接收传值 ***********************/
        bundle = getIntent().getExtras();
        code = bundle.getInt("code");
        selet = bundle.getInt("selet");
        isLocal = bundle.getBoolean("isLocal", false);
        imageuri = bundle.getStringArrayList("imageuri");
        /**是否是本应用中的图片*/
        isApp = bundle.getBoolean("isApp", false);
        /**本应用图片的id*/
        imageId = bundle.getInt("id", 0);
        /*
         * 给viewpager设置适配器
		 */
        if (isApp) {
            MyPageAdapter myPageAdapter = new MyPageAdapter();
            very_image_viewpager.setAdapter(myPageAdapter);
            very_image_viewpager.setEnabled(false);
        } else {
            adapter = new ViewPagerAdapter();
            very_image_viewpager.setAdapter(adapter);
            very_image_viewpager.setCurrentItem(code);
            page = code;
            very_image_viewpager.setOnPageChangeListener(this);
            very_image_viewpager.setEnabled(false);
            // 设定当前的页数和总页数
            if (selet == 2) {
                very_image_viewpager_text.setText((code + 1) + "/" + imageuri.size());
            }
        }
    }

    /**
     * 本应用图片适配器
     */

    class MyPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.viewpager_very_image, container, false);
            final PhotoDraweeView zoom_image_view = (PhotoDraweeView) view.findViewById(R.id.zoom_image_view);
            ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading);
            spinner.setVisibility(View.GONE);
            PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
            controllerBuilder.setUri(Uri.parse("res://" + imageId));
            controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    zoom_image_view.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            zoom_image_view.setController(controllerBuilder.build());
            zoom_image_view.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    /**
     * ViewPager的适配器
     *
     * @author guolin
     */
    class ViewPagerAdapter extends PagerAdapter {

        LayoutInflater inflater;

        ViewPagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.viewpager_very_image,
                    container, false);
            final PhotoDraweeView zoom_image_view = (PhotoDraweeView) view.findViewById(R.id.zoom_image_view);
            final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading);
            // 保存网络图片的路径
            String adapter_image_Entity = (String) getItem(position);
            //TODO
            String imageUrl;
            if (isLocal) {
                imageUrl = "file://" + adapter_image_Entity;
                txt_save_image_big_activity.setVisibility(View.GONE);
            } else {
                imageUrl = adapter_image_Entity;
            }
            PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
            controllerBuilder.setUri(Uri.parse(imageUrl));
            controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    zoom_image_view.update(imageInfo.getWidth(), imageInfo.getHeight());
                    spinner.setVisibility(View.GONE);
                }
            });
            zoom_image_view.setController(controllerBuilder.build());
            zoom_image_view.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });
            container.addView(view, 0);
            return view;
        }

        @Override
        public int getCount() {
            if (imageuri == null || imageuri.size() == 0) {
                return 0;
            }
            return imageuri.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        public Object getItem(int position) {
            return imageuri.get(position);
        }
    }

    /*
     * 下面是对Viewpager的监听
     */
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    /*
     * 本方法主要监听viewpager滑动的时候的操作
     */
    @Override
    public void onPageSelected(int arg0) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
        very_image_viewpager_text.setText((arg0 + 1) + "/" + imageuri.size());
        page = arg0;
    }


}
