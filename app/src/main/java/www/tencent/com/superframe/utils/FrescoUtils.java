package www.tencent.com.superframe.utils;

import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;


public class FrescoUtils {

    //全民的在线图片尾部追加 这个可以 实现修圆
    private static final String GIF2JPGEND = "?imageView2/format/jpg";

    /**
     * 设置加载中的图
     *
     * @param view
     * @param resId
     */
    public static void setLodingImgRes(SimpleDraweeView view, int resId) {

        view.getHierarchy().setFadeDuration(0);
        view.getHierarchy().setPlaceholderImage(resId);
    }

    /**
     * 设置出错默认图
     *
     * @param view
     * @param resId
     */
    public static void setFaildImgRes(SimpleDraweeView view, int resId) {

        view.getHierarchy().setFadeDuration(0);
        view.getHierarchy().setFailureImage(view.getResources().getDrawable(resId));
    }


    /**
     * 设置加载中的图和出错的图一致  一次代替上面两个方法
     *
     * @param view
     * @param resId
     */
    public static void setDefaultImgRes(SimpleDraweeView view, int resId) {

        view.getHierarchy().setFadeDuration(0);
        view.getHierarchy().setPlaceholderImage(resId);
        view.getHierarchy().setFailureImage(view.getResources().getDrawable(resId));
    }

    /**
     * 给imgview实现圆形功能 但是不带包边
     *
     * @param view
     */
    public static void setViewRound(SimpleDraweeView view) {

        RoundingParams roundingParams = new RoundingParams();

        roundingParams.setRoundAsCircle(true);
        roundingParams.setRoundingMethod(RoundingParams.RoundingMethod.BITMAP_ONLY);
        view.getHierarchy().setRoundingParams(roundingParams);
    }

    /**
     * 给imgview实现圆形功能 并带有包边
     *
     * @param view     SimpleDraweeView
     * @param borderDP border跨度
     * @param colRes   颜色资源
     */
    public static void setViewRound(SimpleDraweeView view, float borderDP, int colRes) {

        float border_width = dip2px(borderDP);
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        roundingParams.setBorder(colRes, border_width);
        roundingParams.setPadding(border_width);//不设置内边距 不会显示出边框

        //BITMAP_ONLY 是采用BitmapShader的方式 会造成内存抖动 注意  大图注意
        roundingParams.setRoundingMethod(RoundingParams.RoundingMethod.BITMAP_ONLY);
        view.getHierarchy().setRoundingParams(roundingParams);
        view.setAspectRatio(16.0f / 9);
    }

    /**
     * 关掉 view设置bitmap时的动画
     *
     * @param view
     */
    public static void closeAnim(SimpleDraweeView view) {

        view.getHierarchy().setFadeDuration(0);
    }


    /**
     * 关掉 view设置bitmap时的动画
     *
     * @param view
     */
    public static void setAspectRatio(SimpleDraweeView view, float ratio) {

        view.setAspectRatio(ratio);
    }


    /**
     * 给imgview实现圆形功能
     *
     * @param view
     */
    public static void setViewCorner(SimpleDraweeView view) {

        RoundingParams cornerParams = RoundingParams.fromCornersRadius(7f);
        cornerParams.setOverlayColor(android.R.color.white);

        //BITMAP_ONLY 是采用BitmapShader的方式 会造成内存抖动 注意!!!  大图注意!!!
        cornerParams.setRoundingMethod(RoundingParams.RoundingMethod.BITMAP_ONLY);
        cornerParams.setCornersRadius(dip2px(9));
        view.getHierarchy().setRoundingParams(cornerParams);
    }

    public static float dip2px(float dipValue) {

        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (dipValue * scale + 0.5f);
    }


    public static void displayUrl(SimpleDraweeView view, String url) {

        if (TextUtils.isEmpty(url)) {
            view.setImageURI(Uri.parse(""));
        }
        else {
            view.setImageURI(Uri.parse(url));
        }
    }

    public static void displayLocalUrl(SimpleDraweeView view, String url) {

        if (TextUtils.isEmpty(url)) {
            view.setImageURI((Uri.parse("")));
        }
        else {
            view.setImageURI(Uri.fromFile(new File(url)));
        }
    }


    //让fresco主动把图片缓存下来
    public static void gotoCacheImg(String url) {

        if (TextUtils.isEmpty(url))
            return;


        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                //.setPostprocessor(postprocessor)
                .build();

        Fresco.getImagePipeline().fetchDecodedImage(imageRequest, null);
    }


    public static void fetchToBitmapCache(String url) {

        if (!TextUtils.isEmpty(url)) {
            ImageRequest imageRequest = ImageRequest.fromUri(url);
            Fresco.getImagePipeline().prefetchToBitmapCache(imageRequest, null);
        }
    }


    /**
     * 展示在线地址的头像 自动把服务器的gif转成jpg下载下来
     * @param imageView
     * @param url
     */
    public static void displayAvatar(final SimpleDraweeView imageView, String url) {

        if (TextUtils.isEmpty(url)) {
            imageView.setImageURI(Uri.parse(""));
        }
        else {
            imageView.setImageURI(Uri.parse(url + GIF2JPGEND));
        }
    }

    /**
     * fresco载入图片的监听的方法
     * @param view
     * @param url
     * @param listener
     */
    public static void displayUrlAndListen(SimpleDraweeView view, String url, final ImageLoadListener listener) {

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>()
        {
            @Override
            public void onFailure(String id, Throwable throwable) {

                super.onFailure(id, throwable);
                if(listener!=null)
                {
                    listener.onError();
                }
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {

                super.onFinalImageSet(id, imageInfo, animatable);
                if(listener!=null)
                {
                    listener.onSucceed();
                }
            }
        };

        Uri uri;
        if (TextUtils.isEmpty(url)) {
            uri= Uri.parse("");
        }
        else {
            uri= Uri.parse(url);
        }

        DraweeController controller
                =
                Fresco.newDraweeControllerBuilder()
                        .setControllerListener(controllerListener)
                        .setUri(uri)
                        .build();
        view.setController(controller);
    }

    public interface ImageLoadListener
    {
        void onSucceed();
        void onError();

    }

}
