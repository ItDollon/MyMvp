package www.tencent.com.superframe.fragamodule.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.view.fragment.BaseFragment;
import www.tencent.com.superframe.fragamodule.adapter.AFragmentAdapter;

/** 作者：王文彬 on 2017/5/24 17：57 邮箱：wwb199055@126.com */
public class AFragment extends BaseFragment {

  /*  private TabLayout mTab;;
  private ViewPager mPager;*/

  @BindView(R.id.tl_tab)
  TabLayout mTab;

  @BindView(R.id.vp_pager)
  ViewPager mPager;

  @Override
  protected boolean needHeader() {
    return true;
  }

  @Override
  protected String setTitle() {
    return "Dollon";
  }

  @Override
  protected int initLayout() {
    return R.layout.fragment_a_pager;
  }

  @Override
  protected void init() {
    super.init();
    AFragmentAdapter adapter = new AFragmentAdapter(getChildFragmentManager());
    //mPager.setOffscreenPageLimit(3);//设置缓存页数
    mPager.setAdapter(adapter); //给viewPager设置适配器
    mTab.setupWithViewPager(mPager); //将tabLayout和viewPager关联起来
    //mTab.setTabsFromPagerAdapter(adapter);//给tab设置适配器
  }
}
