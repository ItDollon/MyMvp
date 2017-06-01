package www.tencent.com.superframe.base.view.activity;

import www.tencent.com.superframe.base.presenter.BasePresenter;
import www.tencent.com.superframe.base.view.view.BaseView;

/** 作者：王文彬 on 2017/4/25 10：11 邮箱：wwb199055@126.com */
public abstract class BaseMvpActivity<P extends BasePresenter, V extends BaseView>
    extends BaseActivity {

  protected P presenter;

  @Override
  protected void initView() {
    super.initView();
    presenter = getBasePresenter();
    presenter.bind((V) this);
  }

  protected abstract P getBasePresenter();

  @Override
  protected void onResume() {
    super.onResume();
    presenter.bind((V) this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    presenter.unbind();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    presenter.unbind();
  }
}
