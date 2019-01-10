package com.mvpframe.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.mvpframe.R;
import com.mvpframe.adapters.Asd;
import com.mvpframe.adapters.Asddd;
import com.mvpframe.bean.home.LoginModel;
import com.mvpframe.bridge.sharePref.SharedPrefManager;
import com.mvpframe.bridge.sharePref.SharedPrefUser;
import com.mvpframe.databinding.ActivityMainBinding;
import com.mvpframe.presenter.account.LoginPresenter;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BaseLoadActivity;
import com.mvpframe.view.recyclerView.RecyclerInterface;
import com.mvpframe.view.recyclerView.RefreshHelper;

import java.util.ArrayList;
import java.util.List;

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
public class MainActivity extends BaseLoadActivity<LoginModel, ActivityMainBinding> implements RecyclerInterface<String> {

    private LoginPresenter presenter = new LoginPresenter(this);
    protected RefreshHelper mRefreshHelper;
    protected RefreshHelper mRefreshHelper1;

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
//        presenter.login("15713802736", "123456");
        mLoadBinding.in.rv.setTag("qw");
        mLoadBinding.in1.rv.setTag("12");
        mRefreshHelper = initRefreshHelper(mLoadBinding.in.refreshLayout, mLoadBinding.in.rv, 10).initEnableLoadmore();

        mRefreshHelper1 = initRefreshHelper(mLoadBinding.in1.refreshLayout, mLoadBinding.in1.rv, 10).initEnableRefresh();

        mRefreshHelper.onDefaluteMRefresh();
        mRefreshHelper1.onDefaluteMRefresh();

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
//        presenter.login("15713802736", "123456");
    }

    @Override
    public RecyclerView.Adapter getListAdapter(String tag, List<String> listData) {
        Log.e(TAG, "getListAdapter: " + mLoadBinding.in.rv.getId());
        Log.e(TAG, "getListAdapter: " + mLoadBinding.in1.rv.getId()+"=");
        Log.e(TAG, "getListAdapter: " + tag+"=1");
        if (tag.contains((String) mLoadBinding.in.rv.getTag())) {
            return new Asd(listData);
        }else if (tag.contains((String) mLoadBinding.in1.rv.getTag())) {
            return new Asddd(listData);
        }
        return null;
    }

    @Override
    public void getDataRequest(String tag, int pageindex, int limit) {
        if (tag.contains((String) mLoadBinding.in.rv.getTag())) {
            List<String> list = new ArrayList<>();
            list.add("asd");
            list.add("asd");
            list.add("asd");
            list.add("asd");
            mRefreshHelper.setData(list);


        }else if (tag.contains((String) mLoadBinding.in1.rv.getTag())) {
            List<String> list1 = new ArrayList<>();
            list1.add("123");
            list1.add("123");
            list1.add("13");
            list1.add("123");

            mRefreshHelper1.setData(list1);
        }


    }
}
