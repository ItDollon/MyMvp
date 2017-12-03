package www.tencent.com.superframe.rootview;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissonItem;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.globalconfig.GlobalConfig;
import www.tencent.com.superframe.loginmodule.view.activity.LoginActivity;
import www.tencent.com.superframe.utils.ConstantUtils;
import www.tencent.com.superframe.utils.LogUtils;
import www.tencent.com.superframe.utils.SpUtil;

public class SplashActivity extends AutoLayoutActivity {

  public SplashActivity mActivity;
  private final int GOTO_GUIDE_UI = 1;
  private final int GOTO_LOGIN_UI = 2;
  private final int GOTO_HOME_UI = 3;

  private Handler handler =
      new Handler() {
        @Override
        public void handleMessage(Message msg) {
          switch (msg.what) {
            case GOTO_GUIDE_UI:
              gotoGuideAct();
              break;
            case GOTO_LOGIN_UI:
              gotoLoginAct();
              //gotoHomeAct();
              break;
            case GOTO_HOME_UI:
              gotoHomeAct();
              break;
            default:
              break;
          }
        }
      };

  protected void gotoLoginAct() {
    startActivity(new Intent(mActivity, LoginActivity.class));
    finish();
  }

  protected void gotoGuideAct() {
    SpUtil.setBoolean(ConstantUtils.IS_FIRST_OPEN, true);
    startActivity(new Intent(mActivity, GuideActivity.class));
    finish();
  }

  protected void gotoHomeAct() {
    startActivity(new Intent(mActivity, HomeActivity.class));
    finish();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    mActivity = this;
    //6.0获取动态权限
    List<PermissonItem> permissionItems = new ArrayList<>();
    permissionItems.add(
        new PermissonItem(
            Manifest.permission.READ_PHONE_STATE, "手机状态", R.drawable.permission_ic_phone));
    HiPermission.create(mActivity)
        .title("亲爱的用户")
        .permissions(permissionItems)
        .msg("为了更好的使用，开启这些权限吧")
        .style(R.style.PermissionBlueStyle)
        .checkMutiPermission(
            new PermissionCallback() {
              @Override
              public void onClose() {
                LogUtils.e("onClose");
              }

              @Override
              public void onFinish() {
                LogUtils.e("完成");
                GlobalConfig.init(mActivity);
                gotoWhere();
              }

              @Override
              public void onDeny(String permission, int position) {
                LogUtils.e("拒绝");
              }

              @Override
              public void onGuarantee(String permission, int position) {
                LogUtils.e("onGuarantee");
              }
            });
  }

  private void gotoWhere() {
    boolean isFirstOpen = SpUtil.getBoolean(ConstantUtils.IS_FIRST_OPEN);
    int goType = SpUtil.getInt(ConstantUtils.GO_TYPE);
    if (!isFirstOpen && goType == -1) {
      handler.sendEmptyMessageDelayed(GOTO_GUIDE_UI, 500);
    } else if (isFirstOpen && goType == -1) {
      handler.sendEmptyMessageDelayed(GOTO_LOGIN_UI, 500);
    } else {
      handler.sendEmptyMessageDelayed(GOTO_HOME_UI, 500);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (handler != null) {
      handler.removeMessages(GOTO_GUIDE_UI);
      handler.removeMessages(GOTO_LOGIN_UI);
      handler.removeMessages(GOTO_HOME_UI);
    }
  }
}
