package www.tencent.com.superframe.loginmodule.presenter.imp;

import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.okhttplib.HttpInfo;
import com.okhttplib.callback.CallbackOk;

import org.simple.eventbus.EventBus;

import java.io.IOException;

import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.presenter.BasePresenter;
import www.tencent.com.superframe.globalconfig.BaseApplication;
import www.tencent.com.superframe.globalconfig.EventTag;
import www.tencent.com.superframe.loginmodule.bean.UserInfo;
import www.tencent.com.superframe.loginmodule.model.LoginModel;
import www.tencent.com.superframe.loginmodule.model.imp.LoginModelImp;
import www.tencent.com.superframe.loginmodule.presenter.LoginPresenter;
import www.tencent.com.superframe.loginmodule.view.view.LoginView;
import www.tencent.com.superframe.utils.StringUtils;
import www.tencent.com.superframe.utils.UIUtils;

/** 作者：王文彬 on 2017/4/24 14：28 邮箱：wwb199055@126.com */
public class LoginPresenterImp extends BasePresenter<LoginView, LoginModel>
    implements LoginPresenter {


  @Override
  public void validateCredentials(String username, String password) {
    if (StringUtils.isEmpty(username)) {
      v.showMsg("用户名不能为空");
      return;
    }
    if (StringUtils.isEmpty(password)) {
      v.showMsg("密码不能为空");
      return;
    }

    v.showLoading();
    model.login(
        username,
        password,
        new CallbackOk() {
          @Override
          public void onResponse(HttpInfo info) throws IOException {
            v.hideLoading();
            try {
              if (info.isSuccessful()) {
                UserInfo userInfo = JSON.parseObject(info.getRetDetail(), UserInfo.class);
                if (userInfo != null && userInfo.message.code == 101) {
                  v.showMsg("登陆成功");
                  BaseApplication.getApplication().setUser(userInfo);
                  BaseApplication.getApplication().initOkHttpHeaderMap();
                  v.loginSuccessful();
                  EventBus.getDefault().post(userInfo, EventTag.LoginSuccessful);
                } else if (userInfo != null) {
                  v.showMsg(userInfo.message.msg);
                } else {
                  v.showMsg("检查网络是否异常");
                }
              } else {
                v.showMsg("检查网络是否异常");
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });
  }

  @Override
  public void switchLineBackground(EditText mEditText, final View view) {
    mEditText.setOnFocusChangeListener(
        new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
              view.setBackgroundColor(UIUtils.getColor(R.color.brown_A67C58));
            } else {
              view.setBackgroundColor(UIUtils.getColor(R.color.gray_A3A3A3));
            }
          }
        });
  }

  @Override
  protected LoginModel createModel() {
    return new LoginModelImp();
  }
}
