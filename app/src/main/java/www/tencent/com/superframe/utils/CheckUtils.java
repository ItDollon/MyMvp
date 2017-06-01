package www.tencent.com.superframe.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CheckUtils {

    public static final boolean isMobileFormat(String str_mobile) {
        if(TextUtils.isEmpty(str_mobile))
        {
            return  false;
        }

        Pattern p = Pattern.compile("^((13[0-9])|(147)|(171)|(177)|(15[^4,\\D])|(18[0,1,2,3,4,5-9]))\\d{8}$");
        Matcher m = p.matcher(str_mobile);
        return m.matches();

    }

    /**
     * 验证输入的邮箱格式是否符合
     *
     * @param email
     * @return 是否合法
     */
    public static boolean isEmailFormat(String email) {

        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

 }
