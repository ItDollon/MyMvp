package www.tencent.com.superframe;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * 作者：王文彬 on 2017/5/24 17：57
 * 邮箱：wwb199055@126.com
 */

public class BFragment extends Fragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View inflate = inflater.inflate(R.layout.activity_home_succeed, container,false);
    return inflate;
  }

}
