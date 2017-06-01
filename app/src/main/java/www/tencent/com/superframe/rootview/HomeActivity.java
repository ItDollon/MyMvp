package www.tencent.com.superframe.rootview;

import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import butterknife.BindView;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.view.activity.BaseActivity;

import static www.tencent.com.superframe.utils.ConstantUtils.fragArray;
import static www.tencent.com.superframe.utils.ConstantUtils.imgId;
import static www.tencent.com.superframe.utils.ConstantUtils.tabName;

/** 作者：王文彬 on 2017/4/25 16：36 邮箱：wwb199055@126.com */
public class HomeActivity extends BaseActivity {

  @BindView(android.R.id.tabhost)
  FragmentTabHost mTabHost;

  @Override
  protected int initLayout() {
    return R.layout.activity_home;
  }

  @Override
  protected boolean needHeader() {
    return false;
  }

  @Override
  protected void initView() {
    mTabHost.setup(this, getSupportFragmentManager(), R.id.fl_content);
    for (int i = 0; i < fragArray.length; i++) {
      TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabName[i]).setIndicator(getView(i));
      mTabHost.addTab(tabSpec, fragArray[i], null);
    }
    mTabHost.getTabWidget().setDividerDrawable(null);
  }

  private View getView(int i) {
    View view = View.inflate(this, R.layout.item_tab_content, null);
    TextView mTv = (TextView) view.findViewById(R.id.tv_txt);
    ImageView mIv = (ImageView) view.findViewById(R.id.iv_img);
    mTv.setText(tabName[i]);
    mIv.setImageResource(imgId[i]);
    return view;
  }
}
