package www.tencent.com.superframe.loginmodule.view.activity;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.view.activity.BaseMvpActivity;
import www.tencent.com.superframe.forgetpwdmodule.view.activity.ForgetPwdActivity;
import www.tencent.com.superframe.loginmodule.presenter.imp.LoginPresenterImp;
import www.tencent.com.superframe.loginmodule.view.view.LoginView;
import www.tencent.com.superframe.registermodule.view.activity.RegisterActivity;
import www.tencent.com.superframe.rootview.HomeActivity;
import www.tencent.com.superframe.utils.ConstantUtils;
import www.tencent.com.superframe.utils.ProgressDialogUtils;
import www.tencent.com.superframe.utils.SpUtil;
import www.tencent.com.superframe.utils.Tools;
import www.tencent.com.superframe.utils.UIUtils;

/** 作者：王文彬 on 2017/4/21 17：13 邮箱：wwb199055@126.com */
public class LoginActivity extends BaseMvpActivity<LoginPresenterImp, LoginView>
    implements LoginView {

  @BindView(R.id.login_name)
  EditText mName;

  @BindView(R.id.login_password)
  EditText mPassword;

  @BindView(R.id.login_login)
  Button mLogin;

  @BindView(R.id.login_name_line)
  View mNameLine;

  @BindView(R.id.login_password_line)
  View mPasswordLine;

  ProgressDialog progressDialog;

  @BindView(R.id.tv_login_register)
  TextView mRegister;

  @BindView(R.id.tv_login_forget)
  TextView mForgetPwd;

  @Override
  protected boolean needHeader() {
    return false;
  }

  @Override
  protected int initLayout() {
    return R.layout.activity_login;
  }

  @Override
  protected void initView() {
    super.initView();
    progressDialog = ProgressDialogUtils.getInstance().getProgressDialog(this);
    presenter.switchLineBackground(mName, mNameLine);
    presenter.switchLineBackground(mPassword, mPasswordLine);
  }

  @Override
  protected LoginPresenterImp getBasePresenter() {
    return new LoginPresenterImp();
  }

  @OnClick({R.id.login_login, R.id.tv_login_register,R.id.tv_login_forget})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.login_login:
        if (Tools.isFastDoubleClick()) {
          presenter.validateCredentials(
              Tools.getEditTextString(mName), Tools.getEditTextString(mPassword));
        }
        break;
      case R.id.tv_login_register:
        gotoActivity(RegisterActivity.class);
        break;
      case R.id.tv_login_forget:
        gotoActivity(ForgetPwdActivity.class);
        break;
    }
  }

  @Override
  public void showMsg(String msg) {
    UIUtils.showToastSafe(msg);
  }

  @Override
  public void loginSuccessful() {
    gotoActivity(HomeActivity.class);
    SpUtil.setInt(ConstantUtils.GO_TYPE, 0);
    finish();
  }

  @Override
  public void showLoading() {
    progressDialog.show();
    progressDialog.setMessage("正在登陆中...");
  }

  @Override
  public void hideLoading() {
    progressDialog.dismiss();
  }
}
