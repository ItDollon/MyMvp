package www.tencent.com.superframe.loginmodule.model.imp;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;

import java.util.HashMap;
import java.util.Map;

import www.tencent.com.superframe.base.view.activity.BaseActivity;
import www.tencent.com.superframe.globalconfig.Urls;
import www.tencent.com.superframe.loginmodule.model.LoginModel;

/** 作者：王文彬 on 2017/4/21 19：07 邮箱：wwb199055@126.com */
public class LoginModelImp implements LoginModel {

  @Override
  public void login(String username, String password, CallbackOk callbackOk) {
    Map<String, String> map = new HashMap<>();
    map.put("loginName", username);
    map.put("password", password);
    map.put("deviceNum", "190e35f7e0751a1b6c4");
    OkHttpUtil.getDefault(BaseActivity.getThisActivity())
        .doPostAsync(HttpInfo.Builder().setUrl(Urls.LoginUrl).addParams(map).build(), callbackOk);
  }
}
