package com.mvpframe.ui.view.account.activity;

import com.mvpframe.R;
import com.mvpframe.bean.home.LoginModel;
import com.mvpframe.databinding.ActivityLoginBinding;
import com.mvpframe.presenter.account.LoginPresenter;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BaseLoadActivity;

/**
 * 账号登录
 * <p>
 * Data：2019/01/11
 *
 * @author yong
 */
public class LoginActivity extends BaseLoadActivity<LoginModel, ActivityLoginBinding> {

    private LoginPresenter presenter = new LoginPresenter(this);

    @Override
    public BasePresenter<IMvpView<LoginModel>>[] getPresenterArray() {
        return new BasePresenter[]{presenter};
    }


    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onSucceed(String action, LoginModel data) {

    }


    @Override
    public void initData() {
        presenter.login("15713802736", "123456");
    }

    @Override
    public void initListeners() {

    }
}
