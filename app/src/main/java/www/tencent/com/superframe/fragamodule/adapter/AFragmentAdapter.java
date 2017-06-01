package www.tencent.com.superframe.fragamodule.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import www.tencent.com.superframe.base.view.fragment.BasePagerFragment;
import www.tencent.com.superframe.utils.FragmentProducerUtils;

/** 作者：王文彬 on 2017/5/27 10：50 邮箱：wwb199055@126.com */
public class AFragmentAdapter extends FragmentStatePagerAdapter {

  public AFragmentAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    return FragmentProducerUtils.create(position);
  }

  @Override
  public int getCount() {
    return FragmentProducerUtils.getSize();
  }

  @Override
  public CharSequence getPageTitle(int position) {
    BasePagerFragment fragment = (BasePagerFragment) FragmentProducerUtils.create(position);
    return fragment.getTitle();
  }

  @Override
  public Parcelable saveState(){
    return null;
  }
}
