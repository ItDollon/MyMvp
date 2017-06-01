package www.tencent.com.superframe.loginmodule.view.view;

import www.tencent.com.superframe.base.view.view.BaseView;

/** 作者：王文彬 on 2017/4/21 20：00 邮箱：wwb199055@126.com */
public interface LoginView extends BaseView {

  void showMsg(String msg);

  void loginSuccessful();

  void showLoading();

  void hideLoading();
}
