package com.kaws.lib.common.utils.image;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

/**
 * 项目名称：github12
 * 类名称：CallBack_CropImage
 * 创建人：xiaguangcheng
 * 修改人：xiaguangcheng
 * 修改时间：2016/1/29 14:43
 * 修改备注：
 */

public interface CallBack_CropImage {
    /**
     *
     * @param path 裁剪后图像的路径
     * @param uri  裁剪后图像的uri
     * @param cropedBitmap 裁剪后图片的bitmap
     */
    void finishImageCrop(String path, Uri uri, Bitmap cropedBitmap, File fileBitmap);

}
