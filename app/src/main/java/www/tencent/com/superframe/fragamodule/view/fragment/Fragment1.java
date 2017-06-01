package www.tencent.com.superframe.fragamodule.view.fragment;

import android.os.SystemClock;
import android.view.View;

import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.view.activity.BaseActivity;
import www.tencent.com.superframe.base.view.fragment.BasePagerFragment;
import www.tencent.com.superframe.utils.UIUtils;
import www.tencent.com.superframe.widget.LoadingStateWidget;

/** 作者：王文彬 on 2017/5/27 11：24 邮箱：wwb199055@126.com */
public class Fragment1 extends BasePagerFragment {

  @BindView(R.id.rl_fragment_container)
  AutoRelativeLayout mContainer;

  private LoadingStateWidget stateWidget;

  @Override
  protected boolean needHeader() {
    return false;
  }

  @Override
  protected int initLayout() {
    return R.layout.fragment_viewgroup_container;
  }

  @Override
  public String getTitle() {
    return "推荐";
  }

  @Override
  protected void init() {
    super.init();
    if (stateWidget == null) {
      stateWidget =
          new LoadingStateWidget(BaseActivity.getThisActivity()) {
            @Override
            protected View setSucceedView() {
              return Fragment1.this.createSucceedView();
            }

            @Override
            protected void loading() {
              Fragment1.this.loadData();
            }
          };
    } else {
      mContainer.removeView(stateWidget);
    }
    stateWidget.show();
    mContainer.addView(
        stateWidget,
        AutoLinearLayout.LayoutParams.MATCH_PARENT,
        AutoLinearLayout.LayoutParams.MATCH_PARENT);
  }

  private void loadData() {
    SystemClock.sleep(2000);
    stateWidget.showLoading(LoadingStateWidget.LoadResult.ERROR);
  }

  private View createSucceedView() {
    View view = UIUtils.inflate(R.layout.activity_home_succeed);
    return view;
  }
}
