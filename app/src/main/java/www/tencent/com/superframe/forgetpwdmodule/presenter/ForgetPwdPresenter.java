package www.tencent.com.superframe.forgetpwdmodule.presenter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/** 作者：王文彬 on 2017/5/23 11：17 邮箱：wwb199055@126.com */
public interface ForgetPwdPresenter {
  void switchIconBackground(EditText mInputTxt, ImageView mImg, View line, int focus, int unFocus);

  void validateCode(String phoneNum);

  void switchEyeBackground(ImageView mImg, EditText mInputTxt);

  void finish(String phoneNum, String code, String password);
}
