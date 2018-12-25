package com.mvpframe.ui.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mvpframe.constant.Constants;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragment 懒加载 防止fragment初始化时加载大量数据
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseLazyFragment<T, B extends ViewDataBinding> extends BaseFragment<T, IMvpView<T>, BasePresenter<IMvpView<T>>> {

    protected B mLazyBinding;

    /**
     * 全局控制Fragment第一次加载数据
     */
    private Map<Object, Boolean> mapStarted = new HashMap<>();

    private String key;

    /**
     * fragment的标识
     */
    protected String bundleKey = "bundleKey";

    /**
     * 传递的数据
     */
    protected Bundle bundle;

    @Override
    public View initView(LayoutInflater inflater) {
        mLazyBinding = DataBindingUtil.inflate(inflater, getLayout(), null, false);
        return mLazyBinding.getRoot();
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!Constants.isStartFragmen) {
                if (mapStarted.size() == 0 || !mapStarted.get(key)) {
                    mapStarted.put(key, true);
                    lazyLoad();
                }
            }
        } else {
            onInvisible();
        }
    }

    /**
     * fragment显示时调用
     */
    protected abstract void lazyLoad();

    /**
     * fragment隐藏时调用
     */
    protected abstract void onInvisible();

    protected abstract int getLayout();

    @Override
    public void initData() {
        if (getArguments() != null)
            bundle = getArguments();
        if (bundle != null)
            key = TAG + bundle.get(bundleKey);
        else
            key = TAG;
        if (Constants.isStartFragmen) {
            Constants.isStartFragmen = false;
            mapStarted.put(key, true);
            lazyLoad();
        }
    }
}