package www.tencent.com.superframe.loginmodule.presenter;

import android.view.View;
import android.widget.EditText;

/** 作者：王文彬 on 2017/4/21 19：09 邮箱：wwb199055@126.com */
public interface LoginPresenter {

  void validateCredentials(String username, String password); //验证用户名密码

  void switchLineBackground(EditText mEditText, View view); //修改线的颜色
}
