package com.mvpframe.ui;

import android.view.View;

import com.mvpframe.R;
import com.mvpframe.bean.home.LoginModel;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.presenter.account.LoginPresenter;
import com.mvpframe.bridge.sharePref.SharedPrefManager;
import com.mvpframe.bridge.sharePref.SharedPrefUser;
import com.mvpframe.databinding.ActivityMainBinding;
import com.mvpframe.ui.base.activity.BaseLoadActivity;

/**
 * <功能详细描述>
 * 泛型传入
 * 1、网络请求实体类：如果有多个实体类可以传入Object或是通过BaseListMode中set、get方法设置
 * 2、自动生成ViewDataBinding
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class MainActivity extends BaseLoadActivity<LoginModel, ActivityMainBinding> {

    private LoginPresenter presenter = new LoginPresenter(this);

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onSucceed(String action, LoginModel data) {
        mLoadBinding.text.setText(SharedPrefManager.getUser().getString(SharedPrefUser.USER_NAME, ""));
        mLoadBinding.text1.setText(data.getNickName());
        mLoadBinding.bt.setText(action);
    }

    @Override
    public void initListeners() {
        mLoadBinding.bt.setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter.login("15713802736", "123456");
    }

    @Override
    public BasePresenter<IMvpView<LoginModel>>[] getPresenterArray() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void onEmptyClickListener() {
        super.onEmptyClickListener();
        presenter.login("15713802736", "123456");
    }
}
