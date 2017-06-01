package www.tencent.com.superframe.base.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;

import org.simple.eventbus.EventBus;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.view.activity.BaseActivity;

/** 作者：王文彬 on 2017/5/24 16：38 邮箱：wwb199055@126.com */
public abstract class BaseFragment extends Fragment {

  public BaseActivity mActivity;
  protected View view;
  private TextView mHeadTxt;
  private TextView mBackTxt;
  private TextView mRightTxt;
  protected WeakReference<View> v;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    if (v == null || v.get() == null) {
      view = inflater.inflate(initLayout(), container, false);
      v = new WeakReference<>(view);
      ButterKnife.bind(this, v.get());
    } else {
      ViewGroup parent = (ViewGroup) v.get().getParent();
      if (parent != null) {
        parent.removeView(v.get());
      }
    }
    return needHeader() ? getMergerView(v.get()) : v.get();
  }

  private View getMergerView(View v) {
    AutoLinearLayout linearLayout = new AutoLinearLayout(getActivity());
    linearLayout.setOrientation(AutoLinearLayout.VERTICAL);
    View headView = View.inflate(getActivity(), R.layout.item_head_view_frag, linearLayout);
    mHeadTxt = (TextView) headView.findViewById(R.id.head_tv_center);
    mHeadTxt.setText(setTitle());
    mRightTxt = (TextView) headView.findViewById(R.id.head_right_txt);
    linearLayout.addView(
        v, AutoLinearLayout.LayoutParams.MATCH_PARENT, AutoLinearLayout.LayoutParams.MATCH_PARENT);
    return linearLayout;
  }

  protected String setTitle() {
    return "";
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    EventBus.getDefault().register(this);
    mActivity = (BaseActivity) getActivity();
    init();
  }

  protected void init() {}

  protected abstract boolean needHeader();

  protected abstract int initLayout();

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
