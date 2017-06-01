package www.tencent.com.superframe.forgetpwdmodule.view.view;

import www.tencent.com.superframe.base.view.view.BaseView;

/** 作者：王文彬 on 2017/5/23 10：49 邮箱：wwb199055@126.com */
public interface ForgetPwdView extends BaseView {
  void showMsg(String msg);

  void changePwdSuccessful();

  void showLoading();

  void hideLoading();

  void setValidateCodeText(String code);

  void setValidateCodeTextEnable(boolean b);
}
