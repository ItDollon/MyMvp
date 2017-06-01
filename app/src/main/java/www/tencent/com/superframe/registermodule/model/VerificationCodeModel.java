package www.tencent.com.superframe.registermodule.model;

import com.okhttplib.callback.CallbackOk;

import www.tencent.com.superframe.base.model.BaseModel;

/** 作者：王文彬 on 2017/5/19 15：20 邮箱：wwb199055@126.com */
public interface VerificationCodeModel extends BaseModel {
  /**
   * @param phoneNum
   * @param isRegister 必须使用VerificationCodeModelImp中的常量，不能乱使用
   * @param callback
   */
  void verificationCode(String phoneNum, String isRegister, CallbackOk callback);
}
