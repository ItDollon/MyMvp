package www.tencent.com.superframe.utils;

import java.util.HashMap;

import www.tencent.com.superframe.base.view.fragment.BaseFragment;
import www.tencent.com.superframe.fragamodule.view.fragment.Fragment1;
import www.tencent.com.superframe.fragamodule.view.fragment.Fragment2;
import www.tencent.com.superframe.fragamodule.view.fragment.Fragment3;
import www.tencent.com.superframe.fragamodule.view.fragment.Fragment4;

/** 作者：王文彬 on 2017/5/24 17：03 邮箱：wwb199055@126.com */
public class FragmentProducerUtils {
  public static final int A = 0;
  public static final int B = 1;
  public static final int C = 2;
  public static final int D = 3;
  private static final int MAX_SIZE = 4;
  private static HashMap<Integer, BaseFragment> fragMap = new HashMap<>();

  public static BaseFragment create(int i) {
    BaseFragment fragment = fragMap.get(i);
    if (fragment != null) {
      return fragment;
    } else {
      switch (i) {
        case A:
          fragment = new Fragment1();
          break;
        case B:
          fragment = new Fragment2();
          break;
        case C:
          fragment = new Fragment3();
          break;
        case D:
          fragment = new Fragment4();
          break;
      }
      fragMap.put(i, fragment);
    }
    return fragment;
  }

  public static int getSize() {
    return MAX_SIZE;
  }
}
