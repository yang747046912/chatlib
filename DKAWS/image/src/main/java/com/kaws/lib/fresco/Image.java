package com.kaws.lib.fresco;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.soloader.SoLoader;
import com.kaws.lib.common.utils.DebugUtil;
import com.kaws.lib.common.utils.ScreenSizeUtil;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by 杨才 on 2016/1/14.
 */
public class Image {

    private static Context context;

    public static void initFresco(final Context context) {
        Image.context = context;
        File cacheFile = new File(context.getApplicationContext().getCacheDir().getAbsolutePath(), "HttpCache");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(cacheFile, cacheSize);
        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(context.getApplicationContext(), client)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(context.getApplicationContext(), config);
        try {
            SoLoader.init(context, 0);
            SoLoader.loadLibrary("webp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static boolean isImageDownloaded(Uri loadUri) {
        if (loadUri == null) {
            return false;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
        return ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey) || ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey);
    }

    //return file or null
    public static File getCachedImageOnDisk(Uri loadUri) {
        File localFile = null;
        if (loadUri != null) {
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }


    public static void load(SimpleDraweeView draweeView, float cornersRadius, Uri uri, Boolean isCircle, int placeholder) {
        RoundingParams params;
        if (!isCircle) {
            params = RoundingParams.fromCornersRadius(cornersRadius);
            params.setRoundAsCircle(isCircle);
        } else {
            params = RoundingParams.asCircle();
        }
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)

                .setAutoRotateEnabled(true)
                .build();
        GenericDraweeHierarchy round = new GenericDraweeHierarchyBuilder(context.getResources())
                .setRoundingParams(params)
                .setPlaceholderImage(context.getResources().getDrawable(placeholder), ScalingUtils.ScaleType.FOCUS_CROP)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .setUri(uri)
                .build();
        draweeView.setHierarchy(round);
        draweeView.setController(controller);
    }

    /**
     * 加载圆角图片
     */
    public static void displayRound(SimpleDraweeView draweeView, float cornersRadius, Uri uri) {
        load(draweeView, cornersRadius, uri, false, R.drawable.shape_placeholder_default);
    }

    /**
     * 加载圆角图片
     */
    public static void displayRound(SimpleDraweeView draweeView, float cornersRadius, Uri uri, int defauleDrawablle) {
        load(draweeView, cornersRadius, uri, false, defauleDrawablle);
    }

    /**
     * 加载头像
     */
    public static void displayCricle(SimpleDraweeView draweeView, Uri uri) {
        load(draweeView, 0, uri, true, R.drawable.shape_placeholder_default);
    }

    /**
     * 加载头像
     */
    public static void displayCricle(SimpleDraweeView draweeView, Uri uri, int defauleDrawablle) {
        load(draweeView, 0, uri, true, defauleDrawablle);
    }

    /**
     * 加载正常图
     */
    public static void displayImage(SimpleDraweeView draweeView, Uri uri) {
        load(draweeView, 0, uri, false, R.drawable.shape_placeholder_default);
    }

    /**
     * 加载正常图
     */
    public static void displayImage(SimpleDraweeView draweeView, Uri uri, int defauleDrawablle) {
        load(draweeView, 0, uri, false, defauleDrawablle);
    }

    /**
     * @param context 上下文
     * @param uri     下载文件的Uri
     * @param path    下载文件保存的全路径
     */
    public static void loadImage(Context context, final Uri uri, final String path) {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRequestPriority(Priority.HIGH)
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();
        imagePipeline.prefetchToDiskCache(imageRequest, context);
    }


    public static void displayChatImage(final SimpleDraweeView draweeView, Uri uri, int defauleDrawablle, final ILoadProgress iLoadProgress) {
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(
                    String id,
                    @Nullable ImageInfo imageInfo,
                    @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int screenSize = ScreenSizeUtil.getScreenWidth(context);
                int bWidth = imageInfo.getWidth();
                int bHeight = imageInfo.getHeight();
                if (bWidth > screenSize * 3 / 8) {
                    bWidth = screenSize * 3 / 8;
                } else if (bWidth < screenSize / 4) {
                    bWidth = screenSize / 4;
                }
                if (bHeight > screenSize * 3 / 8) {
                    bHeight = screenSize * 3 / 8;
                } else if (bHeight < screenSize / 4) {
                    bHeight = screenSize / 4;
                }
                ViewGroup.LayoutParams params = draweeView.getLayoutParams();
                params.width = bWidth;
                params.height = bHeight;
                draweeView.setLayoutParams(params);
                if (iLoadProgress != null) {
                    iLoadProgress.onLoadSuccess();
                }
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                DebugUtil.debug("----->", "Intermediate image received");
                if (iLoadProgress != null) {
                    iLoadProgress.onLoadFailure();
                }
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                DebugUtil.debug("----->", "Error loading " + id);
                DebugUtil.debug("----->", "Error loading " + throwable.toString());
                if (iLoadProgress != null) {
                    iLoadProgress.onLoadFailure();
                }
            }
        };
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(screenWidth / 4, screenWidth / 4))
                .setAutoRotateEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setControllerListener(controllerListener)
                .setImageRequest(request)
                .build();
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(context.getResources())
                .setProgressBarImage(new CoustomProgressBar(iLoadProgress))
                .setPlaceholderImage(context.getResources().getDrawable(defauleDrawablle), ScalingUtils.ScaleType.FOCUS_CROP)
                .build();
        draweeView.setHierarchy(hierarchy);
        draweeView.setController(controller);
    }

    public interface ILoadProgress {
        public void onProgressChange(int level);

        public void onLoadSuccess();

        public void onLoadFailure();
    }

    static class CoustomProgressBar extends Drawable {
        private ILoadProgress iLoadProgress;

        public CoustomProgressBar(ILoadProgress iLoadProgress) {
            this.iLoadProgress = iLoadProgress;
        }

        @Override
        protected boolean onLevelChange(int level) {
            DebugUtil.debug(" ------ > ", "level : " + level);
            if (iLoadProgress != null) {
                iLoadProgress.onProgressChange(level / 100);
            }
            invalidateSelf();
            return true;
        }

        @Override
        public void draw(Canvas canvas) {

        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }
}
