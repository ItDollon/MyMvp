package www.tencent.com.superframe.rootview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.tencent.com.superframe.R;


/**
 * 作者：王文彬 on 2017/4/21 16：22
 * 邮箱：wwb199055@126.com
 */

public class GuideActivity extends AutoLayoutActivity {

    @BindView(R.id.sp_btn)
    TextView mTxt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        intView();
    }

    private void intView() {
        mTxt.setText("引导界面结束！");
    }
}
