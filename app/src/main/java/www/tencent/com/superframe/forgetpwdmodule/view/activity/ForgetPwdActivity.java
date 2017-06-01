package www.tencent.com.superframe.forgetpwdmodule.view.activity;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.view.activity.BaseMvpActivity;
import www.tencent.com.superframe.forgetpwdmodule.presenter.imp.ForgetPwdPresenterImp;
import www.tencent.com.superframe.forgetpwdmodule.view.view.ForgetPwdView;
import www.tencent.com.superframe.utils.ProgressDialogUtils;
import www.tencent.com.superframe.utils.Tools;
import www.tencent.com.superframe.utils.UIUtils;

/**
 * 作者：王文彬 on 2017/5/23 10：09 邮箱：wwb199055@126.com
 */
public class ForgetPwdActivity extends BaseMvpActivity<ForgetPwdPresenterImp, ForgetPwdView>
    implements ForgetPwdView {

  private ProgressDialog dialog;

  @BindView(R.id.iv_forget_phone_img)
  ImageView mPhoneImg;

  @BindView(R.id.et_forget_phone_input)
  EditText mPhoneInput;

  @BindView(R.id.v_line_one)
  View mLineOne;

  @BindView(R.id.iv_code)
  ImageView mCodeImg;

  @BindView(R.id.tv_code_txt)
  TextView mCodeTxt;

  @BindView(R.id.et_code_input)
  EditText mCodeInput;

  @BindView(R.id.v_line_two)
  View mLineTwo;

  @BindView(R.id.iv_forget_password_img)
  ImageView mPasswordImg;

  @BindView(R.id.et_password_input)
  EditText mPasswordInput;

  @BindView(R.id.iv_forget_eye)
  ImageView mForgetEye;

  @BindView(R.id.v_line_three)
  View mLineThree;

  @BindView(R.id.btn_finish)
  Button mFinish;

  @Override
  protected int initLayout() {
    return R.layout.activity_forgetpwd;
  }

  @Override
  protected boolean needHeader() {
    return false;
  }

  @Override
  protected void initView() {
    super.initView();
    dialog = ProgressDialogUtils.getInstance().getProgressDialog(this);
    presenter.switchIconBackground(
        mPhoneInput,
        mPhoneImg,
        mLineOne,
        R.mipmap.icon_login_phone_focus,
        R.mipmap.icon_login_phone_unfocus);
    presenter.switchIconBackground(
        mCodeInput,
        mCodeImg,
        mLineTwo,
        R.mipmap.icon_registered_code_focus,
        R.mipmap.icon_registered_code_unfocus);
    presenter.switchIconBackground(
        mPasswordInput,
        mPasswordImg,
        mLineThree,
        R.mipmap.icon_login_password_focus,
        R.mipmap.icon_login_password_unfocus);

    presenter.switchEyeBackground(mForgetEye, mPasswordInput);
  }

  @Override
  protected ForgetPwdPresenterImp getBasePresenter() {
    return new ForgetPwdPresenterImp();
  }

  @Override
  public void showMsg(String msg) {
    UIUtils.showToastSafe(msg);
  }

  @Override
  public void changePwdSuccessful() {
    finish();
  }

  @Override
  public void showLoading() {
    dialog.show();
    dialog.setMessage("请稍后...");
  }

  @Override
  public void hideLoading() {
    dialog.dismiss();
  }

  @Override
  public void setValidateCodeText(String code) {
    mCodeTxt.setText(code);
  }

  @Override
  public void setValidateCodeTextEnable(boolean b) {
    mCodeTxt.setEnabled(b);
  }

  @OnClick({R.id.tv_code_txt, R.id.btn_finish})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_code_txt:
        presenter.validateCode(Tools.getEditTextString(mPhoneInput));
        break;
      case R.id.btn_finish:
        if (Tools.isFastDoubleClick()) {
          presenter.finish(
              Tools.getEditTextString(mPhoneInput),
              Tools.getEditTextString(mCodeInput),
              Tools.getEditTextString(mPasswordInput));
        }
        break;
    }
  }
}
