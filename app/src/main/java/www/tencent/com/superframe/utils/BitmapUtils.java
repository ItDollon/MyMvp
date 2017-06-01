package www.tencent.com.superframe.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 作者：王文彬 on 2017/4/1 15：30
 * 邮箱：wwb199055@126.com
 */

public class BitmapUtils {


    /**
     * 根据传入比例进行大小缩放
     *
     * @param bitmap
     * @param widthRatio  宽度比例，缩小就比1小，放大就比1大
     * @param heightRatio
     * @return
     */
    public static Bitmap scaleRatioBitmap(Bitmap bitmap, float widthRatio, float heightRatio) {
        Matrix matrix = new Matrix();
        matrix.postScale(widthRatio, heightRatio);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    /**
     * 获取一个bitmap在内存中所占的大小
     *
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
        int size = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            size = bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            size = bitmap.getByteCount();
        } else {
            size = bitmap.getRowBytes() * bitmap.getHeight();
        }
        return size;
    }


    /**
     * 从assets文件夹中根据文件名得到一个Bitmap
     *
     * @param fileName
     * @return
     */
    public static Bitmap getBitmapFromAssets(Context context, String fileName) {
        try {
            //可以直接使用context.getResources().getAssets().open(fileName);得到一个InputStream再用BufferedInputStream通过缓冲区获得字符数组
            AssetFileDescriptor descriptor = context.getResources().getAssets().openFd(fileName);//此处获得文件描述之后可以得到FileInputStream，然后使用NIO得到Channel
            long fileSize = descriptor.getLength();
            //注意这个地方如果文件大小太大，在decodeStream需要设置参数进行裁剪
            Bitmap bitmap = BitmapFactory.decodeStream(context.getResources().getAssets().open(fileName));
            //注意，AssetFileDescriptor只能用来获取文件的大小，不能用来获取inputStream，用FileDescriptor获取的输入流BitmapFactory.decodeStream不能识别
            //Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据传入宽度进行大小缩放
     *
     * @param oldBitmap
     * @param newWidth  宽度
     * @return
     */
    public static Bitmap scaleWidthBitmap(Bitmap oldBitmap, int newWidth) {
        int oldWidth = oldBitmap.getWidth();
        int oldHeight = oldBitmap.getHeight();
        float scale = (float) oldHeight / oldWidth;
        int newHeight = Math.round(newWidth * scale);
        Bitmap newBmp = Bitmap.createScaledBitmap(oldBitmap, newWidth, newHeight, false);
        oldBitmap.recycle();
        return newBmp;

    }


    /**
     * 根据传入url转换成bitmap
     *
     * @param url
     * @return
     */
    public final static Bitmap getUrlToBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;

        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 旋转Bitmap
     *
     * @param bitmap
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap bitmap, float rotateDegree) {
        if (bitmap == null)
            return null;
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return rotaBitmap;
    }


    /**
     * 根据传入长宽度进行大小缩放图片
     *
     * @param bitmap
     * @param width
     * @param height
     * @param keep   keep为true，保持长宽比
     */
    public static Bitmap scaleHeightAndWidthBitmap(Bitmap bitmap, int width, int height, boolean keep) {
        // 获得图片的宽高
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) width) / w;
        float scaleHeight = ((float) height) / h;
        if (keep) {
            float s = Math.max(scaleWidth, scaleHeight);
            scaleWidth = s;
            scaleHeight = s;
        }
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newBitmap;
    }

    /**
     * 根据传入最大宽度度进行大小缩放图片
     *
     * @param bitmap
     * @param maxWidth
     * @return
     */
    public static Bitmap scaleMaxWidthBitmap(Bitmap bitmap, int maxWidth) {
        // 获得图片的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = 1;
        float scaleHeight = 1;
        if (width > height) {
            scaleWidth = ((float) maxWidth) / width;
            scaleHeight = scaleWidth;
        } else {
            scaleHeight = scaleWidth = ((float) maxWidth) / height;
        }
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }

    /**
     * 根据传入示例图片进行大小缩放图片
     *
     * @param orgfile
     * @param descFile
     * @param sampleFileSize
     * @return
     */
    public static File scaleFileBitmap(File orgfile, File descFile, int sampleFileSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleFileSize;
        Bitmap bitmap = BitmapFactory.decodeFile(orgfile.getAbsolutePath(), options);

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(descFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bitmap.recycle();
        }

        return descFile;
    }

    /**
     * 根据传入示例图片,最大宽高进行大小缩放图片
     *
     * @param orifile
     * @param descFile
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static File scaleMaxWithHeightBitmap(File orifile, File descFile, int maxWidth, int maxHeight) {
        Bitmap bitmap = getFileToBitmap(orifile.getAbsolutePath(), maxWidth, maxHeight);
        saveBitmap(bitmap, descFile);
        return descFile;
    }


    /**
     * 获得示例文件的大小
     *
     * @param file
     * @param maxsize
     * @return
     */
    public static int getSampleFileSize(File file, int maxsize) {
        int initialSize = (int) ((float) (file.length() / 1000) / (float) maxsize + 0.5);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * 将bitmap转换成数组
     */
    public static byte[] bitmapToByte(Bitmap b) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * 将数组转换成Bitmap
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * 把bitmap转换成Base64编码String
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        return Base64.encodeToString(bitmapToByte(bitmap), Base64.DEFAULT);
    }

    /**
     * 将Drawable转换成Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * 将Bitmap转换成Drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }

    /**
     *
     */
    public static Bitmap scaleImageTo(Bitmap bitmap, int newWidth, int newHeight) {
        return scaleImage(bitmap, (float) newWidth / bitmap.getWidth(), (float) newHeight / bitmap.getHeight());
    }

    /**
     * scaleHeightAndWidthBitmap image
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }

    public static Bitmap toRoundCorner(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xff424242);
        // paint.setColor(Color.TRANSPARENT);
        canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 120;
        int newHeight = 120;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
        if (needRecycle)
            bitMap.recycle();
        return newBitMap;
    }


    /**
     * 压缩并保存图片到文件
     **/
    public static boolean saveBitmap(Bitmap bitmap, File file) {
        if (bitmap == null)
            return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 压缩图片并保存到文件里面  format 是图片格式  quelity是压缩的程度 file是压缩之后放到这个file里面  bitmap就是被压缩的图片
     *
     * @param bitmap
     * @param file
     * @param quality 0-100
     * @param format  jpg,png
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, File file, int quality, String format) {
        if (bitmap == null)
            return false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if ("jpg".equals(format)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
            }
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean saveBitmap(Bitmap bitmap, String absPath) {
        return saveBitmap(bitmap, new File(absPath));
    }

    /**
     * 计算图片的缩放值 如果图片的原始高度或者宽度大与我们期望的宽度和高度，我们需要计算出缩放比例的数值。否则就不缩放。
     * heightRatio是图片原始高度与压缩后高度的倍数， widthRatio是图片原始宽度与压缩后宽度的倍数。
     * inSampleSize就是缩放值 ，取heightRatio与widthRatio中最小的值。
     * inSampleSize为1表示宽度和高度不缩放，为2表示压缩后的宽度与高度为原来的1/2(图片为原1/4)。
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions(尺寸) larger than or equal to
            // the requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getFileToBitmap(String filePath, int w, int h) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        // 该值设为true那么将不返回实际的bitmap不给其分配内存空间而里面只包括一些解码边界信息即图片大小信息
        options.inJustDecodeBounds = true;// inJustDecodeBounds设置为true，可以不把图片读到内存中,但依然可以计算出图片的大小
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, w, h);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;// 重新读入图片，注意这次要把options.inJustDecodeBounds
        // 设为 false
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);// BitmapFactory.decodeFile()按指定大小取得图片缩略图
        return bitmap;
    }


    /**
     * 从手机中取出Bitmap
     *
     * @param saveTo
     * @param aspectX
     * @param aspectY
     * @param outputX
     * @param outputY
     * @param returnData
     * @return
     */
    public static Intent getImagePickIntent(Uri saveTo, int aspectX, int aspectY, int outputX, int outputY,
                                            boolean returnData) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("output", saveTo);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scaleHeightAndWidthBitmap", true);
        intent.putExtra("return-data", returnData);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        return intent;
    }

    public static Intent getImagePickIntent(Uri uriFrom, Uri uriTo, int aspectX, int aspectY, int outputX,
                                            int outputY, boolean returnData) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uriFrom, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("output", uriTo);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scaleHeightAndWidthBitmap", true);
        intent.putExtra("return-data", returnData);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        return intent;
    }

    public static Intent getIntent(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public static Bitmap scaleRGBBitmap(Bitmap bitmap) {
        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bitmap, 0, 0, paint);
        return bmpGrayScale;
    }

    /**
     * 图片锐化（拉普拉斯变换）
     *
     * @param bp
     * @return
     */
    public static Bitmap getBitmapAmeliorate(Bitmap bp) {
        long start = System.currentTimeMillis();
        // 拉普拉斯矩阵
        int[] laplacian = new int[]{-1, -1, -1, -1, 9, -1, -1, -1, -1};

        int width = bp.getWidth();
        int height = bp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int idx = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        bp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + n) * width + k + m];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        return bitmap;
    }


    public static Bitmap changeBitmapToGrey(Bitmap bitmap) {

        int threashold = 80;
        int width = bitmap.getWidth();
        // int height = bm.getHeight();
        // convert image into gray
        Paint p = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        p.setColorFilter(filter);
        // binaryzation
        int[] colors = new int[bitmap.getWidth()];
        for (int i = 0; i < bitmap.getHeight(); i++) {
            bitmap.getPixels(colors, 0, width, 0, i, width, 1);
            for (int j = 0; j < bitmap.getWidth(); j++) {
                int red = Color.red(colors[j]);
                if (red > threashold) {
                    red = 255;
                } else {
                    red = 0;
                }
                colors[j] = Color.argb(255, red, red, red);
            }
            bitmap.setPixels(colors, 0, width, 0, i, width, 1);
        }
        return bitmap;

    }


    //以二值图像格式保存纯色信息的二值化简单彩色图像
    public static Bitmap getBitmapBinarization(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int area = width * height;
        int gray[][] = new int[width][height];
        int average = 0;// 灰度平均值
        int graysum = 0;
        int graymean = 0;
        int grayfrontmean = 0;
        int graybackmean = 0;
        int pixelGray;
        int front = 0;
        int back = 0;
        int[] pix = new int[width * height];
        bitmap.getPixels(pix, 0, width, 0, 0, width, height);
        for (int i = 1; i < width; i++) { // 不算边界行和列，为避免越界
            for (int j = 1; j < height; j++) {
                int x = j * width + i;
                int r = (pix[x] >> 16) & 0xff;
                int g = (pix[x] >> 8) & 0xff;
                int b = pix[x] & 0xff;
                pixelGray = (int) (0.3 * r + 0.59 * g + 0.11 * b);// 计算每个坐标点的灰度
                gray[i][j] = (pixelGray << 16) + (pixelGray << 8) + (pixelGray);
                graysum += pixelGray;
            }
        }
        graymean = (int) (graysum / area);// 整个图的灰度平均值
        average = graymean;
        for (int i = 0; i < width; i++) // 计算整个图的二值化阈值
        {
            for (int j = 0; j < height; j++) {
                if (((gray[i][j]) & (0x0000ff)) < graymean) {
                    graybackmean += ((gray[i][j]) & (0x0000ff));
                    back++;
                } else {
                    grayfrontmean += ((gray[i][j]) & (0x0000ff));
                    front++;
                }
            }
        }
        int frontvalue = (int) (grayfrontmean / front);// 前景中心
        int backvalue = (int) (graybackmean / back);// 背景中心
        float G[] = new float[frontvalue - backvalue + 1];// 方差数组
        int s = 0;
        for (int i1 = backvalue; i1 < frontvalue + 1; i1++)// 以前景中心和背景中心为区间采用大津法算法（OTSU算法）
        {
            back = 0;
            front = 0;
            grayfrontmean = 0;
            graybackmean = 0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (((gray[i][j]) & (0x0000ff)) < (i1 + 1)) {
                        graybackmean += ((gray[i][j]) & (0x0000ff));
                        back++;
                    } else {
                        grayfrontmean += ((gray[i][j]) & (0x0000ff));
                        front++;
                    }
                }
            }
            grayfrontmean = (int) (grayfrontmean / front);
            graybackmean = (int) (graybackmean / back);
            G[s] = (((float) back / area) * (graybackmean - average) * (graybackmean - average) + ((float) front / area)
                    * (grayfrontmean - average) * (grayfrontmean - average));
            s++;
        }
        float max = G[0];
        int index = 0;
        for (int i = 1; i < frontvalue - backvalue + 1; i++) {
            if (max < G[i]) {
                max = G[i];
                index = i;
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int in = j * width + i;
                if (((gray[i][j]) & (0x0000ff)) < (index + backvalue)) {
                    pix[in] = Color.rgb(0, 0, 0);
                } else {
                    pix[in] = Color.rgb(255, 255, 255);
                }
            }
        }

        Bitmap temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        temp.setPixels(pix, 0, width, 0, 0, width, height);
        return temp;
    }

    public static Bitmap getBitmapRoundCorner(Bitmap bitmap, float roundPx) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    /**
     * 简单计算清晰度是否可识别
     *
     * @param bitmap
     * @return
     */
    public static boolean getBitmapClarity(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();// 得到图片的长宽

        int pixel, pixel1;// pixel1是当前像素点颜色 pixel是相邻的像素点颜色
        // 拉普拉斯模板
        // int[] Laplacian = { -1, -1, -1, -1, 9, -1, -1, -1, -1 };//
        int[] Laplacian = {-1, 3, -1};//
        int count = 0;
        for (int x = 1; x < width - 1; x++) {
            for (int y = 1; y < height - 1; y++) {// 循环图片的每个像素点
                int r = 0, g = 0, b = 0;
                int Index = 0;
                pixel1 = bitmap.getPixel(x, y);
                // for (int col = -1; col <= 1; col++) {
                for (int row = -1; row <= 1; row++) {// 循环每个像素相邻的8个像素
                    pixel = bitmap.getPixel(x + row, y);// 得到相邻像素的颜色
                    r += Color.red(pixel) * Laplacian[Index];
                    g += Color.green(pixel) * Laplacian[Index];
                    b += Color.blue(pixel) * Laplacian[Index];
                    Index++;
                }
                // }
                r = Math.abs(Color.red(pixel1) - r) + Math.abs(Color.green(pixel1) - g)
                        + Math.abs(Color.blue(pixel1) - b);// 得到相邻像素RGB的平均值
                // 与当前像素RGB值相减
                if (r > 100)
                    count++;// 绝对值超过120我算它是可识别的 120是我随意定的一个值
            }
        }
        int percent = count * 100 / (width * height);
        if (percent < 2)// 简单统计出来的数据
            return false;
        else
            return true;
    }

    public static int[] getBitmapSize(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int[] size = new int[2];
        size[0] = options.outWidth;
        size[1] = options.outHeight;
        return size;
    }

    @SuppressLint("NewApi")
    public static Bitmap readRegionBitmap(ContentResolver resolver, Uri inUri, int max, Rect roundedTrueCrop) {

        float width = roundedTrueCrop.right - roundedTrueCrop.left;
        float height = roundedTrueCrop.bottom - roundedTrueCrop.top;

        float initialSize = Math.max(width, height) / (float) max;
        int inSampleSize;
        if (initialSize <= 8) {
            inSampleSize = 1;
            while (inSampleSize < initialSize) {
                inSampleSize <<= 1;
            }
        } else {
            inSampleSize = ((int) initialSize + 7) / 8 * 8;
        }


        BitmapRegionDecoder decoder = null;
        InputStream is = null;
        try {
            is = resolver.openInputStream(inUri);
            decoder = BitmapRegionDecoder.newInstance(is, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Bitmap bitmap = null;
        if (decoder != null) {
            // Do region decoding to get crop bitmap
            BitmapFactory.Options options = new BitmapFactory.Options();
            if (Build.VERSION.SDK_INT > 10) {
                options.inMutable = true;
            }
            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            bitmap = decoder.decodeRegion(roundedTrueCrop, options);
            decoder.recycle();
        }

        return bitmap;
    }


    /**
     * 获取系统总内存,返回单位为kb
     *
     * @return
     */
    public static long getPhoneTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue();// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return initial_memory;
    }

    /**
     * 获取系统当前可用内存
     *
     * @return
     */
    private long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;// 将获取的内存大小规格化
    }


    public static Bitmap base64ToBitmap(String sourceBase64) {
        if (TextUtils.isEmpty(sourceBase64))
            sourceBase64 = "/9j/4AAQSkZJRgABAQEAYABgAAD//gA7Q1JFQVRPUjogZ2QtanBlZyB2MS4wICh1c2luZyBJSkcgSlBFRyB2ODApLCBxdWFsaXR5ID0gO" +
                    "TAK/9sAQwADAgIDAgIDAwMDBAMDBAUIBQUEBAUKBwcGCAwKDAwLCgsLDQ4SEA0OEQ4LCxAWEBETFBUVFQwPFxgWFBgSFBUU/9sAQwEDBAQFBAUJB" +
                    "QUJFA0LDRQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQU/8AAEQgAMgAyAwEiAAIRAQMRAf/EAB8AAAEFA" +
                    "QEBAQEBAAAAAAAAAAABAgMEBQYHCAkKC//EALUQAAIBAwMCBAMFBQQEAAABfQECAwAEEQUSITFBBhNRYQcicRQygZGhCCNCscEVUtHwJDNicoIJC" +
                    "hYXGBkaJSYnKCkqNDU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6g4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ub" +
                    "rCw8TFxsfIycrS09TV1tfY2drh4uPk5ebn6Onq8fLz9PX29/j5" + "+v/EAB8BAAMBAQEBAQEBAQEAAAAAAAABAgMEBQYHCAkKC//EALURAAIBAg" +
                    "QEAwQHBQQEAAECdwABAgMRBAUhMQYSQVEHYXETIjKBCBRCkaGxwQkjM1LwFWJy0QoWJDThJfEXGBkaJicoKSo1Njc4OTpDREVGR0hJSlNUVVZXWFl" +
                    "aY2RlZmdoaWpzdHV2d3h5eoKDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uLj5OXm5" +
                    "+jp6vLz9PX29/j5+v/aAAwDAQACEQMRAD8A+ePjz8EdT8B61pGr6PqU/jDwzqKpDaanbxq4dHYhPmjJVjyMsMZPasjxRc6lqWgy" +
                    "+HPElnqqGxgk3LFrEFu8HyBiCk0eF3hVXCuSxjKkhsqPYLgXXxKk8RQTapq0PgrwwhVoNE0W6srG1WJlSGKw2TOTskbeQYw5CbjxlR4B4h+H2nak" +
                    "17LLqcOoXlvE4kKW7x" + "+faxosu9QUeTezlg8v3d24Egcn5zCUKSqXe8Vpp31+9Htzp8q5orcwdH0LRtKna/0nTT4mSIrFqdpe2aXAhBJMog8l" +
                    "mG7AUo42qNzBjnFfQ/7NPxqT4aeOZtHj8GW9l4f1i3gV7PRboX0qJJD5hCSC4keHYBJI/wAzdEVkjJJHi2rvo1hpnhtdITTtVZFOpQxyf6VHaPI6" +
                    "s0MUBLhmEafLHcsd2MDcTsXX8A/Dd/G3xlTQ/Amm6rrl3cJPrIsbqzjiWNjF5kUhkhVViRmYIRlUUkAkYFezdSad/wAzBx5U10P0m1D4h+GLPSho" +
                    "+p6lb20t3OLCBblNrzuyKWUZA+6WJ3cdfpWzHo/g26uoovD1+LzXJYWE0mnyRXOSxJy4bIVSWJbZgnA3ZwCPlHSP+CfHjq906yfxb4k8OaPfC6" +
                    "+0TX+nNcTag8eQSpKgRK52qu5c8DOTk5" + "+tPAvgrUfBngeax8OX+kXMkLRw29pdCW2SMDILBgkjFh2zwe5HWvnsPRqYKpONOLk5ybvokktl1O" +
                    "GK5L2ZEvwL8UbR/pugjjo2nx5/Hg/zP1oqdrTx8zEhLJgTncuoxYP0y9FexbEdo/18ivaT7nmOv/tH/DyWK60LTNW0zxvql7GzHR7C9tmEqkFTHvkI" +
                    "Rs4JIJOAeeMV8d61pt74/uoNS8L6lP4e0aS9Nu" + "+mo9pfjTFkRU32ZEhleMyx7tkUIJCH95kfL9LfH79lrV/HmqadeeBJPDfh/TtLhiaLw7Ppv" +
                    "2S3eZXkZ3823ZZI0KSkFQo3FACTmvOtc/YC1m90+yuYfHFheeLdQQ2V3YXmmi8sAzn91Das53wqPueYDvAOV29K5aEqVPVPfS1j1ZNtbanK/DD9mxf" +
                    "iP4as9S" + "+JPxAXRdPhg+wafZzaY1xO8aFo0MlshjEDKzSjMkkkjNk/Lzu+qf2eNO8CfBjR/E3hjwVr1t4i8UX16l2s1xbtZvcRBAqWgMhJxGd7" +
                    "hNx++230rZ+FfwQH7NWh6LYXMFv4jtbNfOubyyst50+8bO+S3RyzBF3MqtkNtLZxnFcV8XvD3hv4jeINYl8O3iXMyGG9ku442QNIqEMpBAI" +
                    "+8Dkdwa9j2XPBxvZtbq2nnY8z2t5a6r+vmemeNvh98UtavytodQjtwoObFkhRe+0E5LY9eKi8NfDLx34fgspbq414SJI7OwkE/BI6oynPTpkV5r" +
                    "4D8bfELwlFHbW/iPUkt0GFill86MD2DZxXsWh/FPxjeoFudV8wnv5CKf0UV8w" + "+Hq6rOtDFS17vT8j0vrvucjhG3od1F8LNQuokmjv4hHI" +
                    "odQ9qynB5GR2NFZH/AAlniD/oK3H/AH0aK9z6pV/n/M832q7I+VfB/wC0j418dwWNlY/D/UtT1LWpFls9ZXS5IdF0+OUj5JZt26Uxry0ufvZwp" +
                    "4z2uleH9S8I/Gr4d" + "+Kte10+MJLbUpINTdB9mstIie3kRZIYFIU4kaPczEnGTgEZHQaHrOr2PgTQ9P17Um1XUrWzjhu7uZzCk0gUBmC43HJ5" +
                    "+7gjoa4X4g+JLXVvCl29heWl5ZmAtIyyqbcKcgEKrEuCysArHGVNfLyxijO9JXtY99UHNWlomfc" +
                    "+grbQ6y0cxVZuQgY9T7fhXO/ET4P+Gb433iBY10rUvs0kbywkJFNuGB5i9Cd205GD9a/PL4J/8FGNL8MXbeA/ii1xJYQEQ2PiWAAzW6g/Ksqg5" +
                    "IHQNnOOvSvrW51GP4waPa32g" + "+LoPFemrFmJrW5Vvl/2lByD7kZNfY0nzJPa581Om6cnF9DzXT4du5NwYxsVIBzgg4IrqdNmitgDLII1x" +
                    "yWOK4a5+D9vpGqXF3barqehTzPvmW3kBjdvUxurKD7gDNfM/wC2H4R8WaJaWeo6X498QajFM4t/7LtLM+WqnO5naIj6YIPXtXXGSjuXG8nZH1" +
                    "vdftOfC7Tbma0uPF" + "+lpPbuYpF+0rwynBH5iivyEn+D/iITybdH1IruOD9gn6Z/3aKPax7Mr2Mv6Z+rmgRJd61C06LMwcnMg3H171+bvw" +
                    "Yu57Gy+KX2aaS322HHlOVx++X0oor86yr+BV9V+Z9Ri/41P5nmv2eKW21CR40eQSjDsoJH41pfB/xPrHh7xtpy6Vq19pivMN4s7l4g312kZoo" +
                    "r76Pwo+aqfE/mfsx8CtZ1DXvD9v8A2nfXOo/u1/4" + "+5ml7f7RNe1W3hLQ7mUNNo2nytnq9rGT+ooopy2Odbmqmg6YEX/iXWnT/AJ4L/hRRRWQz/9k=";
        byte[] sourceBytes = Base64.decode(sourceBase64.getBytes(), Base64.DEFAULT);
        if (sourceBytes == null || sourceBytes.length == 0) return null;
        Bitmap bitmap = BitmapFactory.decodeByteArray(sourceBytes, 0, sourceBytes.length);
        if (bitmap == null) return null;
        if (bitmap.getHeight() < 2) return null;
        return bitmap;
    }

    public static Bitmap tiny2BlurBitmap(Context context, Bitmap tinyBitmap) {
        if (tinyBitmap == null || tinyBitmap.getWidth() < 2) return null;
        //将25*25的小bitmap先放大，再进行高斯模糊,放大后的宽度是300像素
        int scaleRatio = 300 / tinyBitmap.getWidth();
        Bitmap bigBitmap = BitmapUtils.scaleRatioBitmap(tinyBitmap, scaleRatio, scaleRatio);
        tinyBitmap.recycle();//微型图片已经没用了，回收掉
        //将300宽度的大图进行高斯模糊，模糊半径为8
        Bitmap blurBitmap = BitmapUtils.fastblur(context, bigBitmap, 8);
        bigBitmap.recycle();//扩大到300像素宽图片已经没用了，回收掉
        return blurBitmap;
    }

    public static Bitmap base64ToBlurBitmap(Context context, String base64) {
        if (context == null || TextUtils.isEmpty(base64)) return null;
        Bitmap tinyBitmap = base64ToBitmap(base64);
        Bitmap blurBitmap = tiny2BlurBitmap(context, tinyBitmap);
        return blurBitmap;
    }

    @SuppressLint("NewApi")
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

}

