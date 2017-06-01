package www.tencent.com.superframe.utils;

import java.text.DecimalFormat;


public class FormatUtils {


    //double转String  保留两位小数
    public static final String getDoubleFooter(double d1) {
        d1 += 0.0;
        DecimalFormat df = new DecimalFormat("######0.00");
        String str = "" + df.format(d1);
        return str;
    }
}
