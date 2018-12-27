package com.mvpframe.ui.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;

/**
 * Fragment 懒加载 防止fragment初始化时加载大量数据
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseLazyFragment<T, B extends ViewDataBinding> extends BaseFragment<T, IMvpView<T>, BasePresenter<IMvpView<T>>> {

    protected B mLazyBinding;

    /**
     * Fragment第一次加载
     */
    private boolean isStartFragmen = false;
    /**
     * Fragment第一次加载数据
     */
    private boolean isStartFragmenData = false;

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
            isStartFragmen = true;
            if (isStartFragmen && isStartFragmenData) {
                isStartFragmenData = false;
                lazyLoad();
            }
        } else {
            isStartFragmen = false;
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
        isStartFragmenData = true;//多个Fragment复用时，加载数据
        if (isStartFragmen) {
            isStartFragmenData = false;
            lazyLoad();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isStartFragmen = false;
        isStartFragmenData = false;
    }
}