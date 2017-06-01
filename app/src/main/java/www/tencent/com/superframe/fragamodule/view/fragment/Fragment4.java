package www.tencent.com.superframe.fragamodule.view.fragment;

import www.tencent.com.superframe.R;
import www.tencent.com.superframe.base.view.fragment.BasePagerFragment;

/** 作者：王文彬 on 2017/5/27 11：24 邮箱：wwb199055@126.com */
public class Fragment4 extends BasePagerFragment {

  @Override
  protected boolean needHeader() {
    return false;
  }

  @Override
  protected int initLayout() {
    return R.layout.fragment_content;
  }

  @Override
  public String getTitle() {
    return "图片";
  }
}
