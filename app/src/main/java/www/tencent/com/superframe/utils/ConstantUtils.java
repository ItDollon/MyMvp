package www.tencent.com.superframe.utils;

import www.tencent.com.superframe.BFragment;
import www.tencent.com.superframe.CFragment;
import www.tencent.com.superframe.R;
import www.tencent.com.superframe.fragamodule.view.fragment.AFragment;

/** 作者：王文彬 on 2017/4/21 16：15 邮箱：wwb199055@126.com */
public class ConstantUtils {

  public static final String IS_FIRST_OPEN = "is_first_open";

  public static final String GUIDE_ACTIVITY = "Guide_Activity";

  public static final String KEY_IMEI = "key_imei";

  public static final String APP_USER_DATA = "app_user_data";

  public static final String GO_TYPE = "go_type";

  public static final String tabName[] = {"客户", "业绩", "我"};

  public static final int imgId[] = {
    R.drawable.selector_afrag_btn, R.drawable.selector_bfrag_btn, R.drawable.selector_cfrag_btn
  };

  public static final Class fragArray[] = {AFragment.class, BFragment.class, CFragment.class};

  //引导界面背景
  public static final int guideBgIDs[] = {R.mipmap.guide_01, R.mipmap.guide_02, R.mipmap.guide_03};
}
