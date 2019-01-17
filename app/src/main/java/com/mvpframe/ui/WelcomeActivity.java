package com.mvpframe.ui;

import android.os.Build;
import android.os.Handler;

import com.mvpframe.R;
import com.mvpframe.app.MyApplication;
import com.mvpframe.databinding.ActivityWelcomeBinding;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BaseLoadActivity;

/**
 * <欢迎页>
 * <p>
 * Data：2019/01/17
 *
 * @author yong
 */
public class WelcomeActivity extends BaseLoadActivity<Object, ActivityWelcomeBinding> {

    private long delayMillis = 2000;

    @Override
    public BasePresenter<IMvpView<Object>>[] getPresenterArray() {
        return new BasePresenter[0];
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (((MyApplication) getApplication()).needFinishWelCome(getApplication())) {
            case 1:
                if (MyApplication.getApplication().needFinishWelCome(this) == 1)
                    waitLoadMain();
                break;
            case 2:
                ((MyApplication) getApplication()).installFinishWelCome(getApplication(), 1);
                finish();
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    waitLoadMain();
                break;
        }
    }

    @Override
    public void initData() {
        setShowStatusBar(false);
    }

    @Override
    public void initListeners() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onSucceed(String action, Object data) {

    }

    /**
     * 等待加载主界面
     */
    private void waitLoadMain() {
        new Handler().postDelayed(() -> {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && MyApplication.getApplication().needWait(this)) {
                startActivity(LoadResActivity.class, null);
            } else
                startActivity(MainActivity.class, null);

            finish();

        }, delayMillis);
    }
}
