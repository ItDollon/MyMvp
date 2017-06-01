package www.tencent.com.superframe.loginmodule.model;

import com.okhttplib.callback.CallbackOk;

import www.tencent.com.superframe.base.model.BaseModel;

/**
 * 作者：王文彬 on 2017/4/21 19：04
 * 邮箱：wwb199055@126.com
 */

public interface LoginModel extends BaseModel {

    void login(String username, String password, CallbackOk callbackOk);


}
