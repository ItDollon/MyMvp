package www.tencent.com.superframe.registermodule.model.imp;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;

import java.util.HashMap;
import java.util.Map;

import www.tencent.com.superframe.base.view.activity.BaseActivity;
import www.tencent.com.superframe.globalconfig.Urls;
import www.tencent.com.superframe.registermodule.model.RegisterModel;

/** 作者：王文彬 on 2017/5/19 13：46 邮箱：wwb199055@126.com */
public class RegisterModelImp implements RegisterModel {
  @Override
  public void register(
      String userName, String password, String verificationCode, CallbackOk callback) {
    Map<String, String> map = new HashMap<>();
    map.put("userName", userName);
    map.put("passWord", password);
    map.put("validCode", verificationCode);
    OkHttpUtil.getDefault(BaseActivity.getThisActivity())
        .doPostAsync(HttpInfo.Builder().setUrl(Urls.RegisterUrl).addParams(map).build(), callback);
  }
}
