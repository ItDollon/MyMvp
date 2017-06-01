package www.tencent.com.superframe.registermodule.presenter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/** 作者：王文彬 on 2017/5/19 11：45 邮箱：wwb199055@126.com */
public interface RegisterPresenter {
  void validateCode(String phoneNum);

  void verificationCredentials(String phoneNum, String password, String code);

  void switchEditTextBackground(
      EditText editText, View view, ImageView imageView, int focus, int unFocus);

  void switchAgreeBackground(ImageView imageView);

  void switchEyeBackground(ImageView imageView, EditText editText);
}
