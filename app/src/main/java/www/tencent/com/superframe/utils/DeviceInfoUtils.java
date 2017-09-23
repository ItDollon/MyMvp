package www.tencent.com.superframe.utils;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DeviceInfoUtils {

  private final String TAG = "DeviceInfo";

  private String deviceIMEI;
  private String deviceIMSI;
  public String firmware;
  public String language;
  public String deviceId;
  public int netType = -1;
  public String resolution;
  public String sdkVersion;
  public String simId;

  public String deviceDetailsstr = "";
  public String cupdetails = "";
  public int screenWidth;
  public int screenHeight;
  public float density;
  public long LIMIT_MEMORY_MB = 0; //常规应用最大内存限制

  public void initDeviceInfo(Context context) {
    TelephonyManager tmManager =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    deviceIMEI = tmManager.getDeviceId();
    //firmware = tmManager.getDeviceSoftwareVersion();
    netType = tmManager.getNetworkType();
    deviceIMSI = tmManager.getSubscriberId();
    DisplayMetrics displayMetric = context.getResources().getDisplayMetrics();
    displayMetric = context.getResources().getDisplayMetrics();
    screenWidth = displayMetric.widthPixels;
    screenHeight = displayMetric.heightPixels;
    density = displayMetric.density;
    resolution = String.valueOf(screenWidth) + " * " + String.valueOf(screenHeight);
    firmware = Build.VERSION.RELEASE;
    sdkVersion = Build.VERSION.SDK;
    deviceId = Build.MODEL;
    Log.d(TAG + "屏幕尺寸:", screenWidth + "  " + screenHeight);
    initMemoryData();
    deviceDetailsstr =
        "品牌: " + Build.BRAND + "型号: " + Build.MODEL + "版本: Android " + Build.VERSION.RELEASE;
  }

  /**
   * 获取手机CPU信息
   *
   * @return
   */
  public String[] getCpuInfo() {
    String str1 = "/proc/cpuinfo";
    String str2 = "";
    String[] cpuInfo = {"", ""};
    String[] arrayOfString;
    try {
      FileReader fr = new FileReader(str1);
      BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
      str2 = localBufferedReader.readLine();
      arrayOfString = str2.split("\\s+");
      for (int i = 2; i < arrayOfString.length; i++) {
        cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
      }
      str2 = localBufferedReader.readLine();
      arrayOfString = str2.split("\\s+");
      cpuInfo[1] += arrayOfString[2];
      localBufferedReader.close();
    } catch (IOException e) {
    }
    cupdetails = "CPU型号 " + cpuInfo[0] + "\n" + "CPU频率: " + cpuInfo[1] + "\n";
    return cpuInfo;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public void initMemoryData() {

    //ActivityManager 内存小的手机 容易崩溃
    //        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(context.ACTIVITY_SERVICE);
    //        Log.d(TAG + "最大内存:", "常规应用最大内存限制:" + am.getMemoryClass() + "MB 流氓应用最大内存限制:" + am.getLargeMemoryClass() + "MB");
    //

    Runtime rt = Runtime.getRuntime();
    LIMIT_MEMORY_MB = rt.maxMemory() / 1024 / 1024;
    Log.d(TAG, "当前应用最大内存限制:" + LIMIT_MEMORY_MB + "MB");
  }
}
