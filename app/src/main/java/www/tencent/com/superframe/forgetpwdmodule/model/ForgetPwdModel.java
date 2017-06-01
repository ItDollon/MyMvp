package www.tencent.com.superframe.forgetpwdmodule.model;

import com.okhttplib.callback.CallbackOk;

import www.tencent.com.superframe.base.model.BaseModel;

/** 作者：王文彬 on 2017/5/23 11：33 邮箱：wwb199055@126.com */
public interface ForgetPwdModel extends BaseModel {
  void SaveData(String phoneNum, String code, String password, CallbackOk callbackOk);
}
