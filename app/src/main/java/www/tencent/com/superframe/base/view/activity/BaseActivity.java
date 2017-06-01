package www.tencent.com.superframe.base.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.okhttplib.OkHttpUtil;
import com.okhttplib.OkHttpUtilInterface;
import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.AutoLinearLayout;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import www.tencent.com.superframe.R;

/** 作者：王文彬 on 2017/4/1 10：22 邮箱：wwb199055@126.com */
public abstract class BaseActivity extends AutoLayoutActivity implements View.OnClickListener {

  private static BaseActivity thisActivity = null;
  public OkHttpUtilInterface okHttpUtilInterface;
  private TextView mHeadTxt;
  private TextView mBackTxt;
  private TextView mRightTxt;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View view = View.inflate(this, initLayout(), null);
    setContentView(needHeader() ? getMergerView(view) : view);
    ButterKnife.bind(this);
    EventBus.getDefault().register(this);
    thisActivity = this;
    initHttp();
    initView();
    init();
  }

  protected abstract int initLayout();

  protected boolean needHeader() {
    return true;
  }

  private View getMergerView(View view) {
    AutoLinearLayout linearLayout = new AutoLinearLayout(this);
    linearLayout.setOrientation(LinearLayout.VERTICAL);
    View headView = View.inflate(this, R.layout.item_head_view_activity, linearLayout);
    mHeadTxt = (TextView) headView.findViewById(R.id.head_tv_center);
    mBackTxt = (TextView) headView.findViewById(R.id.head_tv_back);
    mBackTxt.setOnClickListener(this);
    mRightTxt = (TextView) headView.findViewById(R.id.head_right_txt);
    mRightTxt.setOnClickListener(this);
    linearLayout.addView(
        view,
        AutoLinearLayout.LayoutParams.MATCH_PARENT,
        AutoLinearLayout.LayoutParams.MATCH_PARENT);
    return linearLayout;
  }

  @Override
  public void setTitle(CharSequence title) {
    if (mHeadTxt != null) {
      mHeadTxt.setText(title);
    }
  }

  @Override
  public void setVisible(boolean visible) {
    if (visible == true) {
      mBackTxt.setVisibility(View.VISIBLE);
    } else {
      mBackTxt.setVisibility(View.INVISIBLE);
    }
  }

  public void setVisible(boolean visible, String txt) {
    if (visible == true) {
      mRightTxt.setVisibility(View.VISIBLE);
      mRightTxt.setText(txt);
    } else {
      mRightTxt.setVisibility(View.GONE);
    }
  }

  private void initHttp() {
    okHttpUtilInterface = OkHttpUtil.getDefault();
  }

  protected void initView() {}

  protected void init() {}

  public static BaseActivity getThisActivity() {
    return thisActivity;
  }

  public static String getThisActivityName() {
    return thisActivity.getThisActivity().getClass().getSimpleName();
  }

  @Override
  protected void onResume() {
    thisActivity = this;
    super.onResume();
  }

  @Override
  protected void onPause() {
    thisActivity = null;
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    EventBus.getDefault().unregister(this);
    super.onDestroy();
  }

  public static void gotoActivity(Class<?> clazz) {
    thisActivity.startActivity(new Intent(thisActivity, clazz));
  }

  private onRightTextDoListener mOnRightTextDoListener;

  public void setOnRightTextDoListener(onRightTextDoListener onRightTextDoListener) {
    this.mOnRightTextDoListener = onRightTextDoListener;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.head_tv_back:
        finish();
        break;
      case R.id.head_right_txt:
        mOnRightTextDoListener.onRightTextDo();
        break;
    }
  }

  public interface onRightTextDoListener {
    void onRightTextDo();
  }

  @Override
  public void onTrimMemory(int level) {
    super.onTrimMemory(level);
  }

  @Override
  public boolean isFinishing() {
    return super.isFinishing();
  }

  @SuppressLint("NewApi")
  @Override
  public boolean isDestroyed() {
    return super.isDestroyed();
  }
}
