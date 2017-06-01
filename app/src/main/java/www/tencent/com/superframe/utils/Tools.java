package www.tencent.com.superframe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

public class Tools {

    private static NetworkInfo mobNetInfo;

    private static NetworkInfo wifiNetInfo;



    /**
     * 判断网络状态
     */
    @SuppressWarnings("static-access")
    public static boolean getNetState(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            return false;
        }
        return true;
    }

    /**
     * 判断网络状态的方法，这有三中状态 0 标识没有网络 1 标识 是移动网络 2 标识是wifi
     *
     * @param context
     * @return
     */
    public static int isNetState(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobNetInfo.isConnected()) {
            return 1;
        } else if (wifiNetInfo.isConnected()) {
            return 2;
        } else {
            return 0;
        }

    }

    public static String isNetStateStr(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mobNetInfo.isConnected()) {
            mobNetInfo.getTypeName();
            return "4G";
        } else if (wifiNetInfo.isConnected()) {
            return "wifi";
        } else {
            return "无网";
        }

    }

    /**
     * 移动数据的时候获取网络ip
     *
     * @return
     */
    public static String getPhoneIp() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr
                            .nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "127.0.0.1";
    }

    /**
     * 手机获取ip地址
     *
     * @param mContext
     * @return
     */
    public static String getAppNetIp(Context mContext) {
        int netState = isNetState(mContext);
        switch (netState) {
            case 0:
                return "127.0.0.1";
            case 1:
                return getPhoneIp();
            case 2:
                return getWiFiIp(mContext);
        }
        return "127.0.0.1";

    }

    /**
     * WIFI环境中获取ip
     *
     * @param context
     * @return
     */
    public static String getWiFiIp(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (wifiManager.isWifiEnabled()) {
            // wifiManager.setWifiEnabled(true);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return intToIp(ipAddress);
        }
        return "127.0.0.1";
    }

    private static String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

    /**
     * 获取手机制造商
     *
     * @return
     */
    public static String getVendor() {
        return Build.BRAND;
    }

    /**
     * 获取系统版本
     *
     * @return
     */
    public static String getBuildVERSION() {
        return Build.VERSION.RELEASE;
    }



    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    State state = info[i].getState();
                    if (state == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是WIFi网络
     *
     * @param context
     * @return
     */
    public static boolean isWifiNet(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    public static boolean IsContainsDate(String startDate, String endDate,
                                         String curDate) {

        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)
                || TextUtils.isEmpty(curDate))
            return false;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(startDate);
            Date dt2 = df.parse(endDate);
            Date dt3 = df.parse(curDate);
            long d1 = dt1.getTime();
            long d2 = dt2.getTime();
            long d3 = dt3.getTime();
            if (d1 <= d3 && d3 <= d2) {
                return true;
            }
        } catch (ParseException e) {
            return false;
        }
        return false;

    }



    /**
     * 将str关键边红
     *
     * @return
     */
    public static String HandlingText(String oldStr, String key) {
        if (TextUtils.isEmpty(oldStr) || TextUtils.isEmpty(key)) {
            return "";
        }
        String[] str = oldStr.split(key);
        String tempStr = "";
        for (int i = 0; i < str.length; i++) {
            if (i < str.length - 1) {
                tempStr = (tempStr + str[i] + "<font color='red'>" + key + "</font>");
            } else {
                tempStr = (tempStr + str[i]);
            }
        }
        return tempStr;
    }

    /**
     * 给url的添加参数 shareType: 1:weixi 2:pengyouquan 3:weibo 4：qq
     */
    public static final int FoundUrl = 0x001;
    public static final int shareUrl = 0x002;


    /**
     * 检查设备imei号
     *
     * @return
     */
    public static void checkIMEI(Context context) {
        String imei = getIMEI(context);
        boolean ischanged = false;
        if (null == imei || imei.length() < 1) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId() + "_openbook";
            ischanged = true;
        } else if (imei.equals("")) {
            imei = getUUID();
        }
        if (imei == null || imei.equals("") || imei.startsWith("null")
                || imei.startsWith("0000") || imei.startsWith("0049")
                || imei.trim().equals("_openbook")) {
            imei = getUUID() + "_openbook";
            ischanged = true;
        }
        if (ischanged) {
            SpUtil.setString(ConstantUtils.KEY_IMEI, imei);
        }
        System.out.println("imei = " + imei);
    }

    public static String getIMEI(Context context) {
        return SpUtil.getString(ConstantUtils.KEY_IMEI);
    }


    /*
     * 自己生成UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "0";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return versionName;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }


    public static String getTimeStr(int str) {
        if (str >= 1 && str <= 3) {
            return "s1-3";
        } else if (str >= 4 && str <= 10) {
            return "s4-10";
        } else if (str >= 11 && str <= 30) {
            return "s11-30";
        } else if (str >= 31 && str <= 60) {
            return "s31-60";
        } else if (str >= 61 && str <= 180) {
            return "m1-3";
        } else if (str >= 181 && str <= 600) {
            return "m4-10";
        } else if (str >= 601 && str <= 1800) {
            return "m10-30";
        } else {
            return "m30over";
        }

    }

    public static String getEditTextString(EditText mEditext) {
        return mEditext.getText().toString().trim();
    }

    public static String getString(TextView tv) {
        return tv.getText().toString().trim();
    }



    /**
     * 判断是否登陆
     *
     * @return
     */
/*    public static boolean isLogined() {
        if (BaseApplication.getApplication().getUser() != null) {
            return true;
        }
        return false;
//        return true;
    }*/
    public static boolean isEmpty(TextView tv) {
        return  TextUtils.isEmpty(getString(tv));
    }

    public static long lastClickTime =0;

    /**
     * 返回true 可以点击
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long truetimeD = time - lastClickTime;
        lastClickTime = time;
        return truetimeD > 2000;
    }

}
