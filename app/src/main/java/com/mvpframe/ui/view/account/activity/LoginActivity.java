package com.mvpframe.ui.view.account.activity;

import com.mvpframe.R;
import com.mvpframe.bean.account.LoginModel;
import com.mvpframe.databinding.ActivityLoginBinding;
import com.mvpframe.presenter.account.LoginPresenter;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BaseLoadActivity;
import com.mvpframe.util.ToastUtil;

/**
 * 账号登录
 * <p>
 * Data：2019/01/11
 *
 * @author yong
 */
public class LoginActivity extends BaseLoadActivity<LoginModel, ActivityLoginBinding> {

    private LoginPresenter presenter = new LoginPresenter();

    @Override
    public BasePresenter<IMvpView<LoginModel>>[] getPresenterArray() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void onSuccess(String action, LoginModel data) {
        super.onSuccess(action, data);
        ToastUtil.makeCenterToast(this,data.getUserId()+"");
    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }


    @Override
    public void initData() {
        presenter.login("15713802736", "123456");
    }

    @Override
    public void initListeners() {

    }
}
