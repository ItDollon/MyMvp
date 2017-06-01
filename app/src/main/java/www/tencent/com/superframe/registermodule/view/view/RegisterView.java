package www.tencent.com.superframe.registermodule.view.view;

import www.tencent.com.superframe.base.view.view.BaseView;

/** 作者：王文彬 on 2017/5/19 09：48 邮箱：wwb199055@126.com */
public interface RegisterView extends BaseView {

  void showMsg(String msg);

  void registerSuccessful();

  void setVerificationCodeText(String txt);

  void setVerificationCodeTextEnable(boolean enable);

  void showLoading();

  void hideLoading();
}
