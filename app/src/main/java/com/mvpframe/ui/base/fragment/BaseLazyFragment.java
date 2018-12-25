package com.mvpframe.ui.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.constant.Constants;

/**
 * Fragment 懒加载 防止fragment初始化时加载大量数据
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseLazyFragment<T, B extends ViewDataBinding> extends BaseFragment<T, IMvpView<T>, BasePresenter<IMvpView<T>>> {


    /**
     * 判断是否是初始化Fragment
     */
    private boolean hasStarted = false;

    protected B mLazyBinding;

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
            hasStarted = true;
            if (!Constants.isStartFragmen) {
                lazyLoad();
            }
        } else {
            if (hasStarted) {
                hasStarted = false;
                onInvisible();
            }
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
        if (Constants.isStartFragmen) {
            Constants.isStartFragmen = false;
            lazyLoad();
        }
    }
}