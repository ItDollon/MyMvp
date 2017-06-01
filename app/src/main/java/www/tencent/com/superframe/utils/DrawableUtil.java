package www.tencent.com.superframe.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import www.tencent.com.superframe.globalconfig.GlobalConfig;

public class DrawableUtil {
  // tag 类日志名
  public static final String tag_ = "DrawableUtil";
  private static PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
  private static PaintFlagsDrawFilter pdf =
      new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
  /*
  *消除锯齿重要参数，两个一起用效果才好。最好开启activity的硬件加速，但是开启硬件加速 内存消耗就会增加。
  PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
  canvas.setDrawFilter(pfd);
  paint.setAntiAlias(true)；
  */

  /**
   * 以最省内存的方式读取本地资源的图片 hdpi文件夹下
   *
   * @param context
   * @param resId
   * @return
   */
  public static Bitmap readBitMap(Context context, int resId) {
    BitmapFactory.Options opt = new BitmapFactory.Options();
    opt.inPreferredConfig = Config.RGB_565;
    opt.inPurgeable = true;
    opt.inInputShareable = true;
    // 获取资源图片
    InputStream is = context.getResources().openRawResource(resId);
    return BitmapFactory.decodeStream(is, null, opt);
  }

  public static Bitmap readBIGBitMap(Context context, int resId) {
    BitmapFactory.Options opt = new BitmapFactory.Options();
    opt.inPreferredConfig = Config.ARGB_8888;
    opt.inPurgeable = true;
    opt.inInputShareable = true;
    // 获取资源图片
    InputStream is = context.getResources().openRawResource(resId);
    return BitmapFactory.decodeStream(is, null, opt);
  }

  //将assets文件中资源取出,并将图片从bitmap转换成drawable格式
  public static Bitmap getDrawableFromAssetFile(Context context, String fileName) {
    Bitmap image = null;
    BitmapDrawable drawable = null;
    try {
      AssetManager am = context.getAssets();
      InputStream is = am.open(fileName);
      image = BitmapFactory.decodeStream(is);
      drawable = new BitmapDrawable(context.getResources(), image);
      is.close();
    } catch (Exception e) {
    }
    return drawable.getBitmap();
  }

  /**
   * 获取圆角Bitmap图片
   *
   * @param bitmap ：原图片
   * @return：切圆角后的图片
   */
  public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
    float roundPx = 5;
    if (null == bitmap || bitmap.isRecycled()) {
      return null;
    }

    try {
      Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
      Canvas canvas = new Canvas(output);
      final int color = 0xff424242;
      final Paint paint = new Paint();
      final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
      final RectF rectF = new RectF(rect);
      paint.setAntiAlias(true);
      canvas.drawARGB(0, 0, 0, 0);
      paint.setColor(color);

      canvas.setDrawFilter(pdf);
      roundPx = bitmap.getWidth() / 10; //此处如除以2了  则就是圆图了  就不是圆角了
      float roundPy = bitmap.getHeight() / 2;
      canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
      paint.setXfermode(xfermode);
      canvas.drawBitmap(bitmap, rect, rect, paint);
      // bitmap.recycle();
      return output;
    } catch (Exception e) {
      Log.e(tag_, "DrawableUtil.getRoundedCornerBitmap(): " + e.getMessage());
      return null;
    }
  }

  /**
   * 获取圆形Bitmap图片
   *
   * @param bitmap
   */
  public static Bitmap toRoundBitmap(Bitmap bitmap) {
    if (bitmap != null) {
      int width = bitmap.getWidth();
      int height = bitmap.getHeight();
      int ovalLen = Math.min(width, height);
      Rect src =
          new Rect(
              (width - ovalLen) / 2,
              (height - ovalLen) / 2,
              (width - ovalLen) / 2 + ovalLen,
              (height - ovalLen) / 2 + ovalLen);
      Rect dst = new Rect(0, 0, ovalLen, ovalLen);
      Bitmap output = Bitmap.createBitmap(ovalLen, ovalLen, Config.ARGB_8888);
      Canvas canvas = new Canvas(output);
      Paint paint = new Paint();
      paint.setAntiAlias(true);
      canvas.setDrawFilter(pdf);
      canvas.drawOval(new RectF(0, 0, ovalLen, ovalLen), paint);
      paint.setXfermode(xfermode);
      canvas.drawBitmap(bitmap, src, dst, paint);
      return output;
    } else {
      return null;
    }
  }

  public static Bitmap decodeUriAsBitmap(Context cx, Uri uri) {
    Bitmap bitmap = null;
    try {
      bitmap = BitmapFactory.decodeStream(cx.getContentResolver().openInputStream(uri));
    } catch (FileNotFoundException e) {
      Log.e(tag_, "DrawableUtil.decodeUriAsBitmap(): " + e.getMessage());
      return null;
    }
    return bitmap;
  }

  public static void displayImgID(final ImageView img, final int id) {
    String globurl = "drawable://" + id;
    fadeInDisplay(img, readBitMap(img.getContext(), id));
  }

  public static void displayARGBImgID(final ImageView img, final int id) {
    String globurl = "drawable://" + id;
    fadeInDisplay(img, readBIGBitMap(img.getContext(), id));
  }

  //根据ID显示圆形图片
  public static void displayRoundImgID(final ImageView img, final int id) {
    img.setImageBitmap(toRoundBitmap(readBitMap(img.getContext(), id)));
  }

  //根据Resources  id
  /*
   * param
   * id
   * R.drawble.empty_Ava;
   * */
  public static void displayRoundedCornerImgID(
      final ImageView img, final int id, boolean isneedfadeanim) {
    final Context con = img.getContext();
    if (isneedfadeanim) fadeInDisplay(img, getRoundedCornerBitmap(readBitMap(con, id)));
    else img.setImageBitmap(getRoundedCornerBitmap(readBitMap(con, id)));
  }

  //将Bitmap保存到本地
  /*
   * @param  imgurl   不带file了开头的路径,如:  /strange/sdcard/asdasd.jpg
   *
   * */
  public static void savePictureToLocal(String imgurl, Bitmap bitmap) {
    FileOutputStream b = null;
    File file = new File(imgurl);
    file.getParentFile().mkdirs(); // 创建文件夹

    //对图片质量进行压缩,这样保存的图片就变小了
    int yasuolv = 60;
    try {
      b = new FileOutputStream(imgurl);
      bitmap.compress(Bitmap.CompressFormat.JPEG, yasuolv, b); // 把数据写入文件
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      try {
        b.flush();
        b.close();
        //bitmap.recycle();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  //重新保存图片  将文件大小变小  保存成功返回true
  public static boolean reSaveSamllImg(String input, String output) {
    Bitmap bitmap = null;
    InputStream in = null;
    try {
      in = new BufferedInputStream(new URL("file://" + input).openStream());
      ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 1;
      options.inTempStorage = new byte[5 * 1024];

      //允许系统在需要的时候回收内存
      options.inPurgeable = true;
      options.inInputShareable = true;
      //降低内存至一半,但是没有了透明度
      options.inPreferredConfig = Config.RGB_565;

      bitmap = BitmapFactory.decodeStream(in, null, options);
      in.close();
      dataStream.close();

      //准备执行保存步骤
      savePictureToLocal(output, bitmap);
      return true;
    } catch (OutOfMemoryError ex) {
      System.gc();
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /*
   *  @param imgurl 图片路径  /upload/2014-04-23/adsads324.jpg
   * */
  public static boolean iwsExistLocal(String imgurl) {
    File file = new File(imgurl);
    if (file.exists()) return true;
    else return false;
  }

  /**
   * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
   *
   * <p>A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
   *
   * <p>B.本地路径:url="file://mnt/sdcard/photo/image.png";
   *
   * <p>C.支持的图片格式 ,png, jpg,bmp,gif等等
   *
   * @param url
   * @return
   */
  public static Bitmap getLocalOrNetBitmap(String url) {
    Bitmap bitmap = null;
    InputStream in = null;
    try {
      in = new BufferedInputStream(new URL(url).openStream());
      ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 1;
      options.inTempStorage = new byte[5 * 1024];

      //允许系统在需要的时候回收内存
      options.inPurgeable = true;
      options.inInputShareable = true;
      //降低内存至一半,但是没有了透明度
      //options.inPreferredConfig=Bitmap.Config.RGB_565;

      bitmap = BitmapFactory.decodeStream(in, null, options);
      in.close();
      //in=null;
      dataStream.close();
      //dataStream=null;
      return bitmap;
    } catch (OutOfMemoryError ex) {
      System.gc();
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 根据图片信息的进行旋转
   *
   * @param input 输入路径图片
   * @param output 旋转后的图片
   */
  public static void rotateBmp(String input, String output) {
    Bitmap bMap;
    try {
      int degree = 0;
      degree = getPictureDegree(input);
      if (degree == 0) {
        Log.d("图像", "不需要旋转");
        return;
      }

      Bitmap bmp = getLocalOrNetBitmap("file://" + input, true);

      float zoom = 0.6f;
      //            if(bmp.getWidth()>1000)//宽带如果大于1000
      //                zoom=1000/bmp.getWidth();

      // 定义矩阵对象
      Matrix matrix = new Matrix();
      // 缩放原图
      matrix.postScale(zoom, zoom);
      // 向左旋转45度，参数为正则向右旋转
      matrix.postRotate(degree);
      //bmp.getWidth(), 500分别表示重绘后的位图宽高
      bMap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

      if (bmp != null) bmp.recycle();
      // 在画布上绘制旋转后的位图
      savePictureToLocal(output, bMap);
      Log.d("图像", "成功  zoom:" + zoom + " ");

      if (bMap != null) bMap.recycle();
    } catch (Exception e) {
      Log.d("图像", "失败");
      e.printStackTrace();
    }
  }

  /**
   * 读取图片需要旋转的角度
   *
   * @param path
   * @return 角度 如:90
   */
  public static int getPictureDegree(String path) {
    try {
      int degree = 0;

      ExifInterface exifInterface1 = new ExifInterface(path);

      int orientation =
          exifInterface1.getAttributeInt(
              ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          degree = 90;
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          degree = 180;
          break;
        case ExifInterface.ORIENTATION_ROTATE_270:
          degree = 270;
          break;
        default:
          degree = 0;
      }

      return degree;
    } catch (Exception e) {
      Log.d("获得图像旋转", "失败");
      e.printStackTrace();
    }
    return 0;
  }

  //同上,最后一个参数可选,配置是否返回高色彩的Bitmap
  public static Bitmap getLocalOrNetBitmap(String url, boolean isRGb_565) {
    Bitmap bitmap = null;
    InputStream in = null;
    try {
      in = new BufferedInputStream(new URL(url).openStream());
      ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inSampleSize = 1;
      options.inTempStorage = new byte[5 * 1024];

      //允许系统在需要的时候回收内存
      options.inPurgeable = true;
      options.inInputShareable = true;
      //降低内存至一半,但是没有了透明度
      if (isRGb_565) options.inPreferredConfig = Config.RGB_565;

      bitmap = BitmapFactory.decodeStream(in, null, options);
      in.close();
      //in=null;
      dataStream.close();
      //dataStream=null;
      return bitmap;
    } catch (OutOfMemoryError ex) {
      System.gc();
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  //渐变动画效果显示
  private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable();

  public static void fadeInDisplay(ImageView imageView, Bitmap bitmap) {
    final TransitionDrawable transitionDrawable =
        new TransitionDrawable(
            new Drawable[] {
              TRANSPARENT_DRAWABLE, new BitmapDrawable(imageView.getResources(), bitmap)
            });
    imageView.setImageDrawable(transitionDrawable);
    transitionDrawable.startTransition(500);
  }

  //保存Bitmap到图库,返回路径
  public static String saveImageToGallery(Context context, Bitmap bmp) {
    // 首先保存图片
    File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
    if (!appDir.exists()) {
      appDir.mkdir();
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
      MediaStore.Images.Media.insertImage(
          context.getContentResolver(), file.getAbsolutePath(), fileName, null);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    String path = appDir + fileName;
    // 最后通知图库更新
    context.sendBroadcast(
        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    return path;
  }

  //保存Bitmap到图库,但是不返回路径
  public static void saveImageToGalleryNoPath(Context context, Bitmap bitmap) {
    MediaStore.Images.Media.insertImage(
        context.getContentResolver(),
        bitmap,
        "title",
        "description"); //title decription同样需要插入数据库,这两个字段没多大用处
  }

  /*
   * 给View截图
   * ::但是不支持SurfaceView
   * */
  public static Bitmap convertViewToBitmap(View view) {
    view.setDrawingCacheEnabled(true);
    view.buildDrawingCache();
    //上面2行必须加入，如果不加如view.getDrawingCache()返回null
    Bitmap bitmap = view.getDrawingCache();
    return bitmap;
  }

  public static void createPath(String path) {
    File file = new File(path);
    if (!file.exists()) {
      file.mkdir();
    }
  }

  //传递两个参数即可 第一个是bitmap  第二个是目录名称,第二个参数为空时  保存在SD卡上
  public static void saveMyBitmap(Bitmap bmp, String directory, String filename)
      throws IOException {
    String direc = "";
    if (directory.length() < 1) {
      direc = GlobalConfig.getInstance().sdRootPath + "/";
    } else {
      direc = GlobalConfig.getInstance().sdRootPath + "/" + directory + "/";
      createPath(direc);
    }
    File f = null;
    if (filename.length() < 1) {
      try {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss_");
        filename = "__" + format.format(date);
      } catch (Exception e) {
        filename = "未知";
      }
      f = new File(direc + filename + ".png");
    } else {
      f = new File(direc + filename + ".png");
    }
    f.createNewFile();
    FileOutputStream fOut = null;
    try {
      fOut = new FileOutputStream(f);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    //Bitmap bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
    bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
    try {
      fOut.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      fOut.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
