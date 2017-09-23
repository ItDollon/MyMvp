package www.tencent.com.superframe.rootview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.loginmodule.view.activity.LoginActivity;
import www.tencent.com.superframe.utils.ConstantUtils;
import www.tencent.com.superframe.utils.SpUtil;
import www.tencent.com.superframe.utils.UIUtils;

import static www.tencent.com.superframe.utils.ConstantUtils.guideBgIDs;

/** 作者：王文彬 on 2017/4/21 16：22 邮箱：wwb199055@126.com */
public class GuideActivity extends AutoLayoutActivity {

  List<ImageView> mGuideBgs = new ArrayList<>();

  @BindView(R.id.sp_vp_bg)
  ViewPager mViewPager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams
    // .FLAG_FORCE_NOT_FULLSCREEN);
    setContentView(R.layout.activity_guide);
    ButterKnife.bind(this);
    init();
  }

  @SuppressLint("NewApi")
  @SuppressWarnings("desprecation")
  private void init() {
    for (int i = 0; i < guideBgIDs.length; i++) {
      ImageView imageView = new ImageView(this);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      int currentVersion = Build.VERSION.SDK_INT;
      if (currentVersion >= 16) {
        imageView.setBackground(UIUtils.getDrawable(guideBgIDs[i]));
      } else {
        imageView.setBackgroundDrawable(UIUtils.getDrawable(guideBgIDs[i]));
      }
      mGuideBgs.add(imageView);
    }

    mViewPager.setAdapter(new GuidePagerAdapter());
    mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
  }

  private class GuidePagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
      return mGuideBgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      ImageView iv = mGuideBgs.get(position);
      container.addView(iv);
      return iv;
    }
  }

  private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
      if (position == mGuideBgs.size() - 1) {
        mGuideBgs
            .get(mGuideBgs.size() - 1)
            .setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    SpUtil.setBoolean(ConstantUtils.GUIDE_ACTIVITY, true);
                    startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                    finish();
                  }
                });
      }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
  }
}
