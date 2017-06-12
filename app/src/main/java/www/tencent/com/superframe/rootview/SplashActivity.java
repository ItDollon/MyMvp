package www.tencent.com.superframe.rootview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.zhy.autolayout.AutoLayoutActivity;

import www.tencent.com.superframe.loginmodule.view.activity.LoginActivity;
import www.tencent.com.superframe.utils.ConstantUtils;
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
          }
        }
      };

  protected void gotoLoginAct() {
    startActivity(new Intent(mActivity, LoginActivity.class));
    finish();
  }

  protected void gotoGuideAct() {
    startActivity(new Intent(mActivity, GuideActivity.class));
    SpUtil.setBoolean(ConstantUtils.IS_FIRST_OPEN, true);
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
    gotoWhere();
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
}
