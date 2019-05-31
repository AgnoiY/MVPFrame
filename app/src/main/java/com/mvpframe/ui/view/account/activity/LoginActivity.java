package com.mvpframe.ui.view.account.activity;

import android.support.v7.widget.RecyclerView;

import com.mvpframe.bean.account.LoginModel;
import com.mvpframe.presenter.account.LoginPresenter;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BaseRefreshListActivity;

import java.util.List;

/**
 * 账号登录
 * <p>
 * Data：2019/01/11
 *
 * @author yong
 */
public class LoginActivity extends BaseRefreshListActivity<LoginModel> {

    private LoginPresenter mPresenter = new LoginPresenter();

    @Override
    public BasePresenter<IMvpView<LoginModel>>[] getPresenterArray() {
        return new BasePresenter[]{mPresenter};
    }

    @Override
    public void onSuccess(String action, LoginModel data) {
        super.onSuccess(action, data);
        mRefreshHelper.setData(null);
    }

    @Override
    public int setLimit() {
        return 0;
    }


    @Override
    public void initData() {
//
    }

    @Override
    public void initListeners() {
//
    }

    @Override
    public RecyclerView.Adapter getListAdapter(String tag, List<LoginModel> listData) {
        return null;
    }

    @Override
    public void getDataRequest(boolean isRefresh, String tag, int pageindex, int limit) {
        mPresenter.getSmsRecord(isRefresh, pageindex, limit);
    }
}
