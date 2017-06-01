package www.tencent.com.superframe.registermodule.presenter.imp;

import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.okhttplib.HttpInfo;
import com.okhttplib.callback.CallbackOk;

import org.simple.eventbus.EventBus;

import java.io.IOException;

import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.bean.BaseJsonBean;
import www.tencent.com.superframe.base.presenter.BasePresenter;
import www.tencent.com.superframe.globalconfig.BaseApplication;
import www.tencent.com.superframe.globalconfig.EventTag;
import www.tencent.com.superframe.loginmodule.bean.UserInfo;
import www.tencent.com.superframe.registermodule.model.RegisterModel;
import www.tencent.com.superframe.registermodule.model.VerificationCodeModel;
import www.tencent.com.superframe.registermodule.model.imp.RegisterModelImp;
import www.tencent.com.superframe.registermodule.model.imp.VerificationCodeModelImp;
import www.tencent.com.superframe.registermodule.presenter.RegisterPresenter;
import www.tencent.com.superframe.registermodule.view.view.RegisterView;
import www.tencent.com.superframe.utils.StringUtils;
import www.tencent.com.superframe.utils.UIUtils;

/** 作者：王文彬 on 2017/5/19 15：09 邮箱：wwb199055@126.com */
public class RegisterPresenterImp extends BasePresenter<RegisterView, RegisterModel>
    implements RegisterPresenter {

  private VerificationCodeModel verificationCodeModel;
  private Handler mTimeHandler;
  private int mTime = 60;
  private ImageView mAgreeImg;

  public RegisterPresenterImp() {
    verificationCodeModel = new VerificationCodeModelImp();

    mTimeHandler =
        new Handler() {
          @Override
          public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mTime > 0) {
              if (mTimeHandler != null) {
                Message message = mTimeHandler.obtainMessage();
                message.what = 0;
                mTimeHandler.sendMessageDelayed(message, 1000);
                String txt = mTime-- + "s后重发";
                v.setVerificationCodeText(txt);
              }
            } else {
              v.setVerificationCodeTextEnable(true);
              String txt = "获取验证码";
              v.setVerificationCodeText(txt);
            }
          }
        };
  }

  @Override
  protected RegisterModel createModel() {
    return new RegisterModelImp();
  }

  @Override
  public void validateCode(String phoneNum) {
    if (StringUtils.isEmpty(phoneNum)) {
      v.showMsg("手机号码不能为空");
      return;
    }
    if (phoneNum.length() != 11) {
      v.showMsg("请输入正确的手机号码");
      return;
    }
    v.setVerificationCodeTextEnable(false);
    v.setVerificationCodeText("获取中");
    verificationCodeModel.verificationCode(
        phoneNum,
        VerificationCodeModelImp.registerType,
        new CallbackOk() {
          @Override
          public void onResponse(HttpInfo info) throws IOException {
            if (info.isSuccessful()) {
              BaseJsonBean baseJsonBean = JSON.parseObject(info.getRetDetail(), BaseJsonBean.class);
              //倒计时处理
              if (baseJsonBean != null && "200".equals(baseJsonBean.result)) {
                Message msg = mTimeHandler.obtainMessage();
                msg.what = 0;
                mTimeHandler.sendMessage(msg);
                mTime = 60;
                v.showMsg("发送验证码成功");
              } else if (baseJsonBean != null) {
                v.setVerificationCodeText("获取验证码");
                v.setVerificationCodeTextEnable(true);
                v.showMsg(baseJsonBean.message);
              } else {
                v.setVerificationCodeText("获取验证码");
                v.setVerificationCodeTextEnable(true);
                v.showMsg("请检查网络是否通畅");
              }
            } else {
              v.setVerificationCodeText("获取验证码");
              v.setVerificationCodeTextEnable(true);
              v.showMsg("发送验证码失败");
            }
          }
        });
  }

  @Override
  public void verificationCredentials(String phoneNum, String password, String code) {
    if (StringUtils.isEmpty(phoneNum)) {
      v.showMsg("手机号码不能为空");
      return;
    }
    if (phoneNum.length() != 11) {
      v.showMsg("请输入正确的手机号码");
      return;
    }
    if (StringUtils.isEmpty(password)) {
      v.showMsg("密码不能为空");
      return;
    }
    if (password.length() < 6) {
      v.showMsg("密码不能小于6位");
    }
    if (StringUtils.isEmpty(code)) {
      v.showMsg("验证码不能为空");
    }
    if (mAgreeImg != null) {
      boolean tag = (boolean) mAgreeImg.getTag();
      if (!tag) {
        v.showMsg("需要同意使用条款");
        return;
      }
    } else {
      v.showMsg("需要同意使用条款");
      return;
    }
    v.showLoading();

    model.register(
        phoneNum,
        password,
        code,
        new CallbackOk() {
          @Override
          public void onResponse(HttpInfo info) throws IOException {
            v.hideLoading();
            if (info.isSuccessful()) {
              UserInfo userInfo = JSON.parseObject(info.getRetDetail(), UserInfo.class);
              if (userInfo != null && "200".equals(userInfo.result)) {
                BaseApplication.getApplication().setUser(userInfo);
                BaseApplication.getApplication().initOkHttpHeaderMap();
                v.registerSuccessful();
                EventBus.getDefault().post(userInfo, EventTag.RegisterSuccessful);
              } else if (userInfo != null) {
                v.showMsg(userInfo.message.msg);
              } else {
                v.showMsg("请检查网络是否通畅");
              }
            } else {
              v.showMsg("请检查网络是否通畅");
            }
          }
        });
  }

  @Override
  public void switchEditTextBackground(
      EditText editText,
      final View view,
      final ImageView imageView,
      final int focus,
      final int unFocus) {
    editText.setOnFocusChangeListener(
        new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
              imageView.setImageResource(focus);
              view.setBackgroundColor(UIUtils.getColor(R.color.app_main));
            } else {
              imageView.setImageResource(unFocus);
              view.setBackgroundColor(UIUtils.getColor(R.color.gray_d7d7d7));
            }
          }
        });
  }

  @Override
  public void switchAgreeBackground(ImageView imageView) {
    this.mAgreeImg = imageView;
    Boolean tag = (Boolean) imageView.getTag();
    if (tag == null || tag == false) {
      imageView.setImageResource(R.mipmap.icon_registerd_agreed_selected);
      imageView.setTag(true);
    } else {
      imageView.setImageResource(R.mipmap.icon_registerd_agreed_unselected);
      imageView.setTag(false);
    }
  }

  @Override
  public void switchEyeBackground(final ImageView imageView, final EditText editText) {
    imageView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Boolean tag = (Boolean) editText.getTag();
            String password = editText.getText().toString().trim();
            int length = 0;
            if (!TextUtils.isEmpty(password)) {
              length = password.length();
            }
            if (tag != null && tag == true) {
              imageView.setImageResource(R.mipmap.icon_close_eye);
              editText.setTag(false);
              editText.setInputType(
                  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
              editText.setKeyListener(
                  DigitsKeyListener.getInstance(
                      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
            } else {
              imageView.setImageResource(R.mipmap.icon_open_eye);
              editText.setTag(true);
              editText.setInputType(
                  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
              editText.setKeyListener(
                  DigitsKeyListener.getInstance(
                      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
            }
            editText.setSelection(length);
          }
        });
  }

  @Override
  public void unbind() {
    super.unbind();
    if (mTimeHandler != null) {
      mTimeHandler.removeMessages(0);
    }
  }
}
