package www.tencent.com.superframe.globalconfig;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.annotation.Encoding;
import com.okhttplib.cookie.PersistentCookieJar;
import com.okhttplib.cookie.cache.SetCookieCache;
import com.okhttplib.cookie.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import www.tencent.com.superframe.loginmodule.bean.UserInfo;
import www.tencent.com.superframe.utils.ConstantUtils;
import www.tencent.com.superframe.utils.SpUtil;
import www.tencent.com.superframe.utils.Tools;

/** 作者：王文彬 on 2017/4/1 10：46 邮箱：wwb199055@126.com */
public class BaseApplication extends Application {

  public static final String PROJECT_NAME = "super";
  private static BaseApplication application = null;
  public static Map<String, String> headerMap;
  private UserInfo userInfo;
  private static int mMainThreadId = -1;
  private static Thread mMainThread;
  private static Handler mMainThreadHandler;
  private static Looper mMainLooper;
  public static String versionName = "";

  @Override
  public void onCreate() {
    super.onCreate();
    application = this;
    mMainThreadId = Process.myTid();
    mMainThread = Thread.currentThread();
    mMainThreadHandler = new Handler();
    mMainLooper = getMainLooper();
    CrashHandler.getInstance().init(this);
    versionName = Tools.getVersionName(this);
    GlobalConfig.init(this);
    initOkHttp();
    intFresco();
    initOkHttpHeaderMap();
  }

  public static BaseApplication getApplication() {
    return application;
  }

  private void intFresco() {

    ImagePipelineConfig config =
        ImagePipelineConfig.newBuilder(this).setBitmapsConfig(Bitmap.Config.RGB_565).build();
    Fresco.initialize(this, config);
  }

  private void initOkHttp() {

    String downloadFileDir =
        Environment.getExternalStorageDirectory().getPath() + "/okHttp_download/";
    String cacheDir = Environment.getExternalStorageDirectory().getPath();
    if (getExternalCacheDir() != null) {
      //缓存目录，APP卸载后会自动删除缓存数据
      cacheDir = getExternalCacheDir().getPath();
    }

    OkHttpUtil.init(this)
        .setConnectTimeout(15) //连接超时时间
        .setWriteTimeout(30) //写超时时间
        .setReadTimeout(30) //读超时时间
        .setMaxCacheSize(10 * 1024 * 1024) //缓存空间大小
        .setCacheType(CacheType.NETWORK_THEN_CACHE) //缓存类型
        .setHttpLogTAG("HttpLog") //设置请求日志标识
        .setIsGzip(false) //Gzip压缩，需要服务端支持
        .setShowHttpLog(true) //显示请求日志
        .setShowLifecycleLog(false) //显示Activity销毁日志
        .setRetryOnConnectionFailure(false) //失败后不自动重连
        .setCachedDir(new File(cacheDir, "okHttp_cache")) //缓存目录
        .setDownloadFileDir(downloadFileDir) //文件下载保存目录
        .setResponseEncoding(Encoding.UTF_8) //设置全局的服务器响应编码
        .addResultInterceptor(HttpInterceptor.ResultInterceptor) //请求结果拦截器
        .addExceptionInterceptor(HttpInterceptor.ExceptionInterceptor) //请求链路异常拦截器
        .setCookieJar(
            new PersistentCookieJar(
                new SetCookieCache(), new SharedPrefsCookiePersistor(this))) //持久化cookie
        .build();
  }

  public void initOkHttpHeaderMap() {
    headerMap = new HashMap<>();
    headerMap.put("deviceNum", "190e35f7e0751a1b6c4");
    headerMap.put("appTokenId", getAppToken());
    headerMap.put("roleId", getRoleId());
  }

  public static Map<String, String> getHeaderMap() {
    return headerMap;
  }

  public static int getMainThreadId() {
    return mMainThreadId;
  }

  public static Thread getMainThread() {
    return mMainThread;
  }

  public static Looper getMainThreadLooper() {
    return mMainLooper;
  }

  public static Handler getMainThreadHandler() {
    return mMainThreadHandler;
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
  }

  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
  }

  //保存bean
  public void setUser(UserInfo userInfo) {
    this.userInfo = null;
    this.userInfo = userInfo;
    if (userInfo != null) {
      String userString = JSON.toJSONString(userInfo);
      SpUtil.setString(ConstantUtils.APP_USER_DATA, userString);
    } else {
      SpUtil.remove(ConstantUtils.APP_USER_DATA);
    }
  }

  public UserInfo getUser() {
    if (userInfo == null) {
      String userString = SpUtil.getString(ConstantUtils.APP_USER_DATA);
      if (!TextUtils.isEmpty(userString)) {
        userInfo = JSON.parseObject(userString, UserInfo.class);
      }
    }
    return userInfo;
  }

  private String getAppToken() {
    String appToken = "";
    UserInfo userInfo = getUser();
    if (userInfo != null && userInfo.appTokenId != null) {
      appToken = userInfo.appTokenId;
    }
    return appToken;
  }

  private String getRoleId() {
    String roleId = "";
    UserInfo userInfo = getUser();
    if (userInfo != null && userInfo.roleId != null) {
      roleId = userInfo.roleId;
    }
    return roleId;
  }
}
