package www.tencent.com.superframe.globalconfig;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import www.tencent.com.superframe.utils.DeviceInfoUtils;
import www.tencent.com.superframe.utils.FileUtil;


public class GlobalConfig {

  private static String projectName = BaseApplication.PROJECT_NAME; //项目制定文件目录
  public static DeviceInfoUtils deviceInfo; // 设备信息
  public String sdPath; // 用于存放日志，缓存等到外置SD卡上
  public String sdRootPath; // 外置存储根目录
  public String dataPath; // 用户存放数据等到私有目录下
  public String imagePath; // 图像目录
  public String sdcachePath; // 缓存目录
  public String sdlogPath; // 日志目录
  public String databasePath; // 数据库目录
  public String dataFileName = projectName + ".db"; // 数据库文件名称
  //屏幕宽度高度
  public int screen_hei = 1920;
  public int screen_wid = 1080;

  private static GlobalConfig globalInstance; // 唯一配置实例
  /** 获取全局唯一实例 */
  public static GlobalConfig getInstance() {
    if (globalInstance == null) {
      globalInstance = new GlobalConfig();
    }
    return globalInstance;
  }

  public static void init(Context context) {
    getInstance().initThis(context);
  }

  /** 加载配置信息 */
  public void initThis(Context context) {

    // 加载设备信息
    screen_wid = getScreenRect().width();
    screen_hei = getScreenRect().height();
    deviceInfo = new DeviceInfoUtils();

    deviceInfo.initDeviceInfo(context);
    // 初始化文件目录
    String fileSeparator = System.getProperty("file.separator");
    //sd卡目录
    sdPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    //wyt根目录
    sdRootPath = sdPath + fileSeparator + projectName + fileSeparator;
    //普通数据
    dataPath = sdRootPath + "data" + fileSeparator;
    //数据库
    databasePath = sdRootPath + "db" + fileSeparator;
    //图片
    imagePath = sdRootPath + ".image" + fileSeparator;
    //缓存
    sdcachePath = sdRootPath + "cache" + fileSeparator;
    //日志
    sdlogPath = sdRootPath + ".log" + fileSeparator;
    //初始化数据库，数据，图片文件夹,日志文件夹
    checkAndCreatePrivateDirectory();
    //初始化缓存文件夹
    checkAndCreateSdDirectory();
    return;
  }

  /** 检查并创建私有文件夹 */
  private void checkAndCreatePrivateDirectory() {
    // 创建私有文件夹
    String[] path = {sdRootPath, databasePath, dataPath, imagePath, sdcachePath, sdlogPath};
    //这里创建目录要先创建sd/layou/  再创建sd/layou/image/  否则创建失败
    for (String x : path) {
      File fi = new File(x);
      if (!fi.exists()) {
        fi.mkdir();
      }
    }
  }

  /** 每次启动需要删除的旧文件夹 */
  private void checkAndCreateSdDirectory() {

    if ((!TextUtils.isEmpty(sdPath) && new File(sdPath).canRead())) {

      String[] path = {sdcachePath, imagePath};
      for (String x : path) {
        File fi = new File(x);
        if (!fi.exists()) {
          fi.mkdir();
        } else {
          FileUtil.DeleteFile(x);
          fi.mkdir();
        }
      }
      // 禁止系统Media搜索程序目录;
      String nomediapath = imagePath + ".nomedia";
      File nomedia = new File(nomediapath);
      try {
        if (!nomedia.exists()) nomedia.createNewFile();
      } catch (IOException e) {
      }
    }
  }

  /** 程序退出清空缓存数据 */
  public void clearAllFile() {
    FileUtil.DeleteFile(sdcachePath);
    FileUtil.DeleteFile(imagePath);
  }

  /** 获取屏幕区域 */
  public static Rect getScreenRect() {
    DisplayMetrics displayMetric = Resources.getSystem().getDisplayMetrics();
    Rect rect = new Rect(0, 0, displayMetric.widthPixels, displayMetric.heightPixels);
    Log.d("initImageLoader() 宽高 :", rect.width() + " " + rect.height());
    return rect;
  }
}
