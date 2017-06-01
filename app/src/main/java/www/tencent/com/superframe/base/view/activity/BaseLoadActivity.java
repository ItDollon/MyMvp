package www.tencent.com.superframe.base.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;
import www.tencent.com.superframe.utils.UIUtils;
import www.tencent.com.superframe.widget.LoadingStateWidget;


/**
 * 作者：王文彬 on 2017/5/11 16：50
 * 邮箱：wwb199055@126.com
 */

public abstract class BaseLoadActivity extends BaseActivity {

  private LoadingStateWidget stateWidget;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    stateWidget = new LoadingStateWidget(UIUtils.getContext()) {
      @Override
      protected View setSucceedView() {
        return BaseLoadActivity.this.createSucceedView();
      }

      @Override
      public void loading() {
        BaseLoadActivity.this.load();
      }
    };

    stateWidget.show();
    setContentView(stateWidget);
    ButterKnife.bind(this);

  }
  //请求服务器，获取当前状态
  protected abstract void load();
  //刷新页面
  protected abstract View createSucceedView();
}
