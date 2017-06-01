package www.tencent.com.superframe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import www.tencent.com.superframe.globalconfig.BaseApplication;

/**
 * ClassName：SpUtil
 * <p>
 * Fuction：SharedPreferences工具
 */
public class SpUtil {

    public static final String SP_XML = "info_cache";

    public static SharedPreferences getSharedPreferences() {
        return BaseApplication.getApplication().getSharedPreferences(SP_XML,
                Context.MODE_PRIVATE);
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, "");
    }

    public static void setString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    public static void setBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public static int getInt(String key) {
        return getSharedPreferences().getInt(key, -1);
    }

    public static int getInt(String key, int value) {
        return getSharedPreferences().getInt(key, value);
    }

    public static void setInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    public static long getLong(String key) {
        return getSharedPreferences().getLong(key, -1);
    }

    public static void setLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).apply();
    }

    public static void remove(String key) {
        getSharedPreferences().edit().remove(key).apply();
    }

    public static void removeAll() {
        getSharedPreferences().edit().clear().apply();
    }


    /**
     * 保存map集合
     *
     * @param key
     * @param mMap
     */
    public static void setMap(String key, Map<String, String> mMap) {
        JSONObject object = new JSONObject();
        Iterator<Entry<String, String>> iterator = mMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            String key_ = entry.getKey();
            String value = entry.getValue();
            try {
                object.put(key_, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setString(key, object.toString());
    }

    public static Map<String, String> getMap(String key) {
        String result = getString(key);
        Map<String, String> hashMap = new HashMap<String, String>();
        String[] split = result.replace("{", "").replace("}", "").split(",");
        for (int i = 0; i < split.length; i++) {
            String string = split[i];
            String[] split2 = string.split(":");
            hashMap.put(split2[0], split2[1]);
        }

        return hashMap;

    }

    public static void setList(String key, List<String> list) {
        JSONObject object = new JSONObject();
        for (int i = 0; i < list.size(); i++) {
            try {
                object.put(i + "", list.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setString(key, object.toString());
    }

    public static List<String> getList(String key) {
        List<String> arrayList = new ArrayList<String>();
        try {
            String result = getString(key);
            String[] split = result.split(",");
            JSONObject jsonObject = new JSONObject(result);
            for (int i = 0; i < split.length; i++) {
                String string = jsonObject.getString("" + i);
                arrayList.add(string);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;

    }
}
