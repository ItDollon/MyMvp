package www.tencent.com.superframe.widget;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhy.autolayout.AutoFrameLayout;

import www.tencent.com.superframe.R;
import www.tencent.com.superframe.utils.ThreadManagerUtils;
import www.tencent.com.superframe.utils.UIUtils;

/** 作者：王文彬 on 2017/5/9 14：22 邮箱：wwb199055@126.com */
public abstract class LoadingStateWidget extends AutoFrameLayout {

  private static final int STATE_UNLOAD = 0; //默认加载状态

  private static final int STATE_LOADING = 1; //加载状态

  private static final int STATE_ERROR = 2; //错误状态

  private static final int STATE_EMPTY = 3; //空状态

  private static final int STATE_SUCCEED = 4; //成功状态

  private int mState; //当前加载状态

  private View mLoadingView;
  private View mErrorView;
  private View mEmptyView;
  private View mSucceedView;

  public LoadingStateWidget(@NonNull Context context) {
    super(context);
    initView();
  }

  private void initView() {

    mState = STATE_UNLOAD;
    //初始化三个状态的view，三个view叠加在一起
    mLoadingView = setLoadingView();
    if (mLoadingView != null) {
      addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    mErrorView = setErrorView();
    if (mErrorView != null) {
      addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
      ImageView imageView = (ImageView) mErrorView.findViewById(R.id.iv_error);
      if (UIUtils.getContext() != null) {
        Picasso.with(UIUtils.getContext()).load(R.mipmap.icon_refresh_bg).into(imageView);
      }
      mErrorView
          .findViewById(R.id.btn_refresh)
          .setOnClickListener(
              new OnClickListener() {
                @Override
                public void onClick(View v) {
                  show();
                }
              });
    }

    mEmptyView = setEmptyView();
    if (mEmptyView != null) {
      addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    showSafeLoadingView();
  }

  public void show() {
    //第一次进来，即使是error或者empty也要让状态是unload
    if (mState == STATE_ERROR || mState == STATE_EMPTY) {
      mState = STATE_UNLOAD;
    }

    //如果是unload就把状态变为loading，此时从服务器去拿数据
    if (mState == STATE_UNLOAD) {
      mState = STATE_LOADING;
      TaskRunnable task = new TaskRunnable();
      ThreadManagerUtils.getLongPool().execute(task);
    }

    showSafeLoadingView();
  }

  private void showSafeLoadingView() {
    UIUtils.runInMainThread(
        new Runnable() {
          @Override
          public void run() {
            showSafeView();
          }
        });
  }

  private void showSafeView() {

    if (null != mLoadingView) {
      mLoadingView.setVisibility(
          mState == STATE_UNLOAD || mState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
    }
    if (null != mErrorView) {
      mErrorView.setVisibility(mState == STATE_ERROR ? View.VISIBLE : View.INVISIBLE);
    }
    if (null != mEmptyView) {
      mEmptyView.setVisibility(mState == STATE_EMPTY ? View.VISIBLE : View.INVISIBLE);
    }

    if (mState == STATE_SUCCEED && mSucceedView == null) {
      mSucceedView = setSucceedView();
      addView(mSucceedView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    if (null != mSucceedView) {
      mSucceedView.setVisibility(mState == STATE_SUCCEED ? View.VISIBLE : View.INVISIBLE);
    }
  }

  protected abstract View setSucceedView();

  protected abstract void loading();

  protected View setEmptyView() {
    if (getEmptyView() == null) {
      return UIUtils.inflate(R.layout.widget_loding_state_empty);
    } else {
      return getEmptyView();
    }
  }

  public View getEmptyView() {
    return null;
  }

  protected View setErrorView() {
    return UIUtils.inflate(R.layout.widget_loding_state_error);
  }

  protected View setLoadingView() {
    return UIUtils.inflate(R.layout.widget_loding_state_loading);
  }

  public enum LoadResult {
    ERROR(2),

    EMPTY(3),

    SUCCEED(4);

    private int value;

    private LoadResult(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  class TaskRunnable implements Runnable {
    @Override
    public void run() {
      loading();
    }
  }

  public void showLoading(final LoadResult loadResult) {
    SystemClock.sleep(500);
    UIUtils.runInMainThread(
        new Runnable() {
          @Override
          public void run() {
            mState = loadResult.getValue();
            showSafeLoadingView();
          }
        });
  }
}
