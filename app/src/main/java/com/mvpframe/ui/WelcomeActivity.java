package com.mvpframe.ui;

import com.mvpframe.R;
import com.mvpframe.app.MyApplication;
import com.mvpframe.databinding.ActivityWelcomeBinding;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BaseLoadActivity;
import com.mvpframe.util.LogUtil;

public class WelcomeActivity extends BaseLoadActivity<Object, ActivityWelcomeBinding> {

    @Override
    public BasePresenter<IMvpView<Object>>[] getPresenterArray() {
        return new BasePresenter[0];
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (((MyApplication) getApplication()).needFinishWelCome(getApplication())) {
            ((MyApplication) getApplication()).installFinishWelCome(getApplication(), false);
            finish();
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
}
