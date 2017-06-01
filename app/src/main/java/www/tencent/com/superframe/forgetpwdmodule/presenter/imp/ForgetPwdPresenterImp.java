package www.tencent.com.superframe.forgetpwdmodule.presenter.imp;

import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.okhttplib.HttpInfo;
import com.okhttplib.callback.CallbackOk;

import java.io.IOException;

import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.bean.BaseJsonBean;
import www.tencent.com.superframe.base.presenter.BasePresenter;
import www.tencent.com.superframe.forgetpwdmodule.model.ForgetPwdModel;
import www.tencent.com.superframe.forgetpwdmodule.model.imp.ForgetPwdModelImp;
import www.tencent.com.superframe.forgetpwdmodule.presenter.ForgetPwdPresenter;
import www.tencent.com.superframe.forgetpwdmodule.view.view.ForgetPwdView;
import www.tencent.com.superframe.registermodule.model.VerificationCodeModel;
import www.tencent.com.superframe.registermodule.model.imp.VerificationCodeModelImp;
import www.tencent.com.superframe.utils.StringUtils;
import www.tencent.com.superframe.utils.UIUtils;

/** 作者：王文彬 on 2017/5/23 13：31 邮箱：wwb199055@126.com */
public class ForgetPwdPresenterImp extends BasePresenter<ForgetPwdView, ForgetPwdModel>
    implements ForgetPwdPresenter {

  private VerificationCodeModel codeModel;
  private int mTime = 60;
  private Handler mTimeHandler;

  public ForgetPwdPresenterImp() {
    codeModel = new VerificationCodeModelImp();
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
                v.setValidateCodeText(txt);
              }
            }
          }
        };
  }

  @Override
  public void switchIconBackground(
      EditText mInputTxt,
      final ImageView mImg,
      final View line,
      final int focus,
      final int unFocus) {
    mInputTxt.setOnFocusChangeListener(
        new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
              mImg.setImageResource(focus);
              line.setBackgroundColor(UIUtils.getColor(R.color.app_main));
            } else {
              mImg.setImageResource(unFocus);
              line.setBackgroundColor(UIUtils.getColor(R.color.gray_d7d7d7));
            }
          }
        });
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
    v.setValidateCodeTextEnable(false);

    codeModel.verificationCode(
        phoneNum,
        VerificationCodeModelImp.unRegisterType,
        new CallbackOk() {
          @Override
          public void onResponse(HttpInfo info) throws IOException {
            if (info.isSuccessful()) {
              BaseJsonBean jsonBean = JSON.parseObject(info.getRetDetail(), BaseJsonBean.class);
              //倒计时处理
              if (jsonBean != null && "200".equals(jsonBean.result)) {
                v.showMsg("发送验证码成功");
                Message message = mTimeHandler.obtainMessage();
                message.what = 0;
                mTimeHandler.sendMessage(message);
                mTime = 60;
              } else if (jsonBean != null) {
                v.showMsg(jsonBean.message);
              } else {
                v.showMsg("网络链接错误");
              }
            } else {
              v.setValidateCodeText("获取验证码");
              v.setValidateCodeTextEnable(true);
            }
          }
        });
  }

  @Override
  public void switchEyeBackground(final ImageView mImg, final EditText mInputTxt) {
    mImg.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Boolean tag = (Boolean) mInputTxt.getTag();
            String txt = mInputTxt.getText().toString().trim();
            int length = 0;
            if (!TextUtils.isEmpty(txt)) {
              length = txt.length();
            }
            if (tag != null && tag == true) {
              mImg.setImageResource(R.mipmap.icon_close_eye);
              mInputTxt.setTag(false);
              mInputTxt.setInputType(
                  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
              mImg.setImageResource(R.mipmap.icon_open_eye);
              mInputTxt.setTag(true);
              mInputTxt.setInputType(
                  InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            mInputTxt.setSelection(length);
          }
        });
  }

  @Override
  public void finish(String phoneNum, String code, String password) {
    if (StringUtils.isEmpty(phoneNum)) {
      v.showMsg("手机号码不能为空");
      return;
    }
    if (phoneNum.length() != 11) {
      v.showMsg("请输入正确的手机号");
      return;
    }
    if (StringUtils.isEmpty(code)) {
      v.showMsg("验证码不能为空");
      return;
    }
    if (StringUtils.isEmpty(password)) {
      v.showMsg("密码不能为空");
      return;
    }
    if (password.length() < 6) {
      v.showMsg("密码不能小于6位");
      return;
    }

    v.showLoading();
    model.SaveData(
        phoneNum,
        code,
        password,
        new CallbackOk() {
          @Override
          public void onResponse(HttpInfo info) throws IOException {
            v.hideLoading();
            if (info.isSuccessful()) {
              BaseJsonBean jsonBean = JSON.parseObject(info.getRetDetail(), BaseJsonBean.class);
              if ("200".equals(jsonBean.result)) {
                v.changePwdSuccessful();
                v.showMsg(jsonBean.message);
              } else {
                v.showMsg(jsonBean.message);
              }
            } else {
              v.showMsg("找回密码失败，请检查网络");
            }
          }
        });
  }

  @Override
  protected ForgetPwdModel createModel() {
    return new ForgetPwdModelImp();
  }

  @Override
  public void unbind() {
    super.unbind();
    if (mTimeHandler != null) {
      mTimeHandler.removeMessages(0);
    }
  }
}
