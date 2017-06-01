package www.tencent.com.superframe.forgetpwdmodule.model.imp;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;

import java.util.HashMap;
import java.util.Map;

import www.tencent.com.superframe.base.view.activity.BaseActivity;
import www.tencent.com.superframe.forgetpwdmodule.model.ForgetPwdModel;
import www.tencent.com.superframe.globalconfig.Urls;

/** 作者：王文彬 on 2017/5/23 11：36 邮箱：wwb199055@126.com */
public class ForgetPwdModelImp implements ForgetPwdModel {

  @Override
  public void SaveData(String phoneNum, String code, String password, CallbackOk callbackOk) {
    Map<String, String> map = new HashMap<>();
    map.put("phoneNum", phoneNum);
    map.put("validCode", code);
    map.put("newPassword", password);
    OkHttpUtil.getDefault(BaseActivity.getThisActivity())
        .doPostAsync(
            HttpInfo.Builder().setUrl(Urls.ChangePassword).addParams(map).build(), callbackOk);
  }
}
