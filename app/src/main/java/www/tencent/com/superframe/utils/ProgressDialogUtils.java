package www.tencent.com.superframe.utils;

import android.app.ProgressDialog;
import android.content.Context;


/**
 * 系统进度框的工具类， 扩展：修改 progressDialog
 *
 * @author
 */
public class ProgressDialogUtils {

    private static ProgressDialogUtils instance;
    private ProgressDialog progressDialog;

    private ProgressDialogUtils() {
    }

    public static synchronized ProgressDialogUtils getInstance() {
        if (instance == null) {
            instance = new ProgressDialogUtils();
        }
        return instance;
    }

    public ProgressDialog getProgressDialog(Context mContext) {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("请稍等...");
        return progressDialog;

    }


}
