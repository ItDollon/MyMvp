package www.tencent.com.superframe.registermodule.view.activity;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.view.activity.BaseMvpActivity;
import www.tencent.com.superframe.globalconfig.EventTag;
import www.tencent.com.superframe.loginmodule.view.activity.LoginActivity;
import www.tencent.com.superframe.registermodule.presenter.imp.RegisterPresenterImp;
import www.tencent.com.superframe.registermodule.view.view.RegisterView;
import www.tencent.com.superframe.utils.ProgressDialogUtils;
import www.tencent.com.superframe.utils.Tools;
import www.tencent.com.superframe.utils.UIUtils;



/** 作者：王文彬 on 2017/5/18 17：10 邮箱：wwb199055@126.com */
public class RegisterActivity extends BaseMvpActivity<RegisterPresenterImp, RegisterView>
    implements RegisterView {

  @BindView(R.id.iv_back)
  ImageView mImgBack;

  @BindView(R.id.iv_phone)
  ImageView mImgPhone;

  @BindView(R.id.et_account)
  EditText mTxtAccount;

  @BindView(R.id.v_line_one)
  View mLineOne;

  @BindView(R.id.iv_password)
  ImageView mImgPassword;

  @BindView(R.id.et_password)
  EditText mTxtPassword;

  @BindView(R.id.iv_eye)
  ImageView mImgEye;

  @BindView(R.id.v_line_two)
  View mLineTwo;

  @BindView(R.id.iv_code)
  ImageView mImgCode;

  @BindView(R.id.et_input_code)
  EditText mTxtInputCode;

  @BindView(R.id.tv_get_code)
  TextView mTxtGetCode;

  @BindView(R.id.v_line_three)
  View mLineThree;

  @BindView(R.id.iv_agree)
  ImageView mImgAgree;

  @BindView(R.id.tv_agreed)
  TextView mTxtAgreed;

  @BindView(R.id.tv_agreement)
  TextView mTxtAgreement;

  @BindView(R.id.btn_send_data)
  TextView mTxtSendData;

  private ProgressDialog progressDialog;

  @Override
  protected int initLayout() {
    return R.layout.activity_register;
  }

  @Override
  protected boolean needHeader() {
    return false;
  }

  @Override
  protected RegisterPresenterImp getBasePresenter() {
    return new RegisterPresenterImp();
  }

  @Override
  public void showMsg(String msg) {
    UIUtils.showToastSafe(msg);
  }

  @Override
  public void registerSuccessful() {
    EventBus.getDefault().post("", EventTag.RegisterSuccessful);
    gotoActivity(LoginActivity.class);
    finish();
  }

  @Override
  public void setVerificationCodeText(String txt) {
    mTxtGetCode.setText(txt);
  }

  @Override
  public void setVerificationCodeTextEnable(boolean enable) {
    mTxtGetCode.setEnabled(enable);
  }

  @Override
  public void showLoading() {
    progressDialog.show();
    progressDialog.setMessage("注册中...");
  }

  @Override
  public void hideLoading() {
    progressDialog.dismiss();
  }

  @Override
  protected void initView() {
    super.initView();
    progressDialog = ProgressDialogUtils.getInstance().getProgressDialog(this);
    presenter.switchEditTextBackground(
        mTxtAccount,
        mLineOne,
        mImgPhone,
        R.mipmap.icon_login_phone_focus,
        R.mipmap.icon_login_phone_unfocus);

    presenter.switchEditTextBackground(
        mTxtPassword,
        mLineTwo,
        mImgPassword,
        R.mipmap.icon_login_password_focus,
        R.mipmap.icon_login_password_unfocus);

    presenter.switchEditTextBackground(
        mTxtInputCode,
        mLineThree,
        mImgCode,
        R.mipmap.icon_registered_code_focus,
        R.mipmap.icon_registered_code_unfocus);

    presenter.switchEyeBackground(mImgEye, mTxtPassword);
  }

  @OnClick({R.id.tv_get_code, R.id.iv_agree, R.id.tv_agreed, R.id.btn_send_data,R.id.iv_back})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.tv_get_code:
        presenter.validateCode(Tools.getEditTextString(mTxtAccount));
        break;
      case R.id.iv_agree:
      case R.id.tv_agreed:
        presenter.switchAgreeBackground(mImgAgree);
        break;
      case R.id.iv_back:
        finish();
        break;
      case R.id.btn_send_data:
        if (Tools.isFastDoubleClick()) {
          presenter.verificationCredentials(
              Tools.getEditTextString(mTxtAccount),
              Tools.getEditTextString(mTxtPassword),
              Tools.getEditTextString(mTxtInputCode));
        }
        break;
    }
  }
}
