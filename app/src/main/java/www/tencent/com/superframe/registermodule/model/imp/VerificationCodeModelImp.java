package www.tencent.com.superframe.registermodule.model.imp;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;

import java.util.HashMap;
import java.util.Map;

import www.tencent.com.superframe.base.view.activity.BaseActivity;
import www.tencent.com.superframe.globalconfig.Urls;
import www.tencent.com.superframe.registermodule.model.VerificationCodeModel;

/** 作者：王文彬 on 2017/5/19 15：22 邮箱：wwb199055@126.com */
public class VerificationCodeModelImp implements VerificationCodeModel {
  public static String registerType = "1"; //注册界面使用
  public static String unRegisterType = "0";

  @Override
  public void verificationCode(String phoneNum, String isRegister, CallbackOk callback) {
    Map<String, String> map = new HashMap<>();
    map.put("phoneNum", phoneNum);
    map.put("isRegister", isRegister);
    OkHttpUtil.getDefault(BaseActivity.getThisActivity())
        .doPostAsync(
            HttpInfo.Builder().setUrl(Urls.VerificationCodeUrl).addParams(map).build(), callback);
  }
}
