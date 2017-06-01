package www.tencent.com.superframe.registermodule.model;

import com.okhttplib.callback.CallbackOk;

import www.tencent.com.superframe.base.model.BaseModel;

/** 作者：王文彬 on 2017/5/19 13：31 邮箱：wwb199055@126.com */
public interface RegisterModel extends BaseModel {
  void register(String phoneNum, String password, String verificationCode, CallbackOk callback);
}
