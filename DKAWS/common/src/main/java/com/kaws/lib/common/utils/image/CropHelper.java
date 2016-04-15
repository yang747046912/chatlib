package com.kaws.lib.common.utils.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.kaws.lib.common.utils.FileUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名称：github12
 * 类名称：CropHelper
 * 创建人：xiaguangcheng
 * 修改人：xiaguangcheng
 * 修改时间：2016/1/29 14:41
 * 修改备注：功能1：选择图库图片
 * 功能2：剪裁图片
 */

public class CropHelper {
    Bitmap newFaceImage;
    private Uri origUri;
    private String theLarge;
    private String protraitPath;
    private boolean isNeedCrop;
    private File protraitFile;
    private Uri cropUri;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/kaws/Portrait/";


    public void handleRequestCode(int requestCode, CallBack_CropImage cropImage, Intent intent, Activity context) {
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                if (isNeedCrop) {
                    startActionCrop(origUri, cropImage, context);// 拍照后裁剪
                } else {
                    this.getUploadTempFile(cropImage, context, origUri);
                    doLastCrop(cropImage, context);
                }
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                if (isNeedCrop) {
                    startActionCrop(intent.getData(), cropImage, context);// 选图后裁剪
                } else {
                    this.getUploadTempFile(cropImage, context, intent.getData());
                    doLastCrop(cropImage, context);
                }
                break;
            case UCrop.REQUEST_CROP:
                doLastCrop(cropImage, context);
                break;
        }
    }

    private void doLastCrop(CallBack_CropImage cropImage, Activity context) {
        if (!TextUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            cropImage.finishImageCrop(protraitPath, cropUri, newFaceImage, protraitFile);
        } else {
            Toast.makeText(context, "图像不存在，上传失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择图片裁剪
     */
    public void startImagePick(Activity activity, boolean isNeedCrop) {
        this.isNeedCrop = isNeedCrop;
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            activity.startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            activity.startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    public void startTakePhoto(Activity context, boolean isNeedCrop) {
        this.isNeedCrop = isNeedCrop;
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/kaws/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }
        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(savePath)) {
            Toast.makeText(context, "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "kaws_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;
        theLarge = savePath + fileName;// 该照片的绝对路径
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    private String thePath;

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(CallBack_CropImage cropImage, Activity context, Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            Toast.makeText(context, "无法保存上传的头像，请检查SD卡是否挂载", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (uri != null) {
            thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);
            // 如果是标准Uri
            if (TextUtils.isEmpty(thePath)) {
                thePath = ImageUtils.getAbsoluteImagePath(context, uri);
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String ext = FileUtil.getFileFormat(thePath);
        ext = TextUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "kaws_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(protraitFile.getAbsolutePath());
            newFaceImage = decodeUriAsBitmap(context, uri);
            newFaceImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data, CallBack_CropImage cropImage, Activity context) {

        UCrop.of(data, getUploadTempFile(cropImage, context, data))
                .withAspectRatio(1, 1)
                .start(context);
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
