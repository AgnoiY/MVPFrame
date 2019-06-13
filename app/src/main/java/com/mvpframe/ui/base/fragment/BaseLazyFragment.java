package com.mvpframe.ui.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.interfaces.LazyCreateInit;
import com.mvpframe.util.HandlerUtils;
import com.mvpframe.util.Tools;
import com.mvpframe.view.recyclerview.RefreshHelper;

import java.util.ArrayList;
import java.util.List;

import static com.mvpframe.constant.Constants.LOG_E;
import static com.mvpframe.constant.Constants.LOG_I;

/**
 * Fragment 懒加载 防止fragment初始化时加载大量数据
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseLazyFragment<T, B extends ViewDataBinding>
        extends BaseFragment<T, IMvpView<T>, BasePresenter<IMvpView<T>>>
        implements LazyCreateInit {

    protected B mLazyBinding;

    private List<RefreshHelper> listRefreshHelper;

    protected Bundle mBundle;

    protected HandlerUtils mHandlers;

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
        mLazyBinding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
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
            if (isStartFragmenData) {
                isStartFragmenData = false;
                getBundle();
            }
        } else {
            isStartFragmen = false;
            onInvisible();
        }
    }

    @Override
    public void initData() {
        isStartFragmenData = true;//多个Fragment复用时，加载数据
        if (isStartFragmen) {
            isStartFragmenData = false;
            getBundle();
        }
    }

    /**
     * 获取传递的Bundle
     */
    private void getBundle() {
        if (getArguments() != null) {
            mBundle = getArguments();
        }

        lazyLoad();
    }

    /**
     * 网络请求的错误信息，已在请求中处理提示Toast
     * 如果有特殊处理需重写
     *
     * @param action 区分不同事件
     * @param code   错误码
     * @param msg    错误信息
     */
    @Override
    public void onError(String action, int code, String msg) {
        log("url=" + action + ";  code=" + code + ";  msg=" + msg, LOG_E);
    }

    /**
     * 成功返回结果
     *
     * @param action 区分不同事件
     * @param data   数据
     */
    @Override
    public void onSuccess(String action, T data) {
        log("url=" + action + ";  data=" + data, LOG_I);
    }

    /**
     * 成功返回结果
     *
     * @param action 区分不同事件
     * @param data   数据
     */
    @Override
    public void onSuccess(String action, List<T> data) {
        log("url=" + action + ";  data=" + data, LOG_I);
    }

    /**
     * 初始化刷新相关
     *
     * @param refreshLayout
     * @param recyclerView
     * @param limit         为0时默认是10条
     * @return
     */
    protected RefreshHelper initRefreshHelper(View refreshLayout, RecyclerView recyclerView, int limit) {
        RefreshHelper helper = new RefreshHelper<>(this, refreshLayout, recyclerView).init(limit);
        if (Tools.isNullOrZeroSize(listRefreshHelper)) listRefreshHelper = new ArrayList<>();
        listRefreshHelper.add(helper);
        return helper;
    }

    /**
     * 初始化刷新相关
     *
     * @param refreshLayout
     * @param recyclerView
     * @return
     */
    protected RefreshHelper initRefreshHelper(View refreshLayout, RecyclerView recyclerView) {
        return initRefreshHelper(refreshLayout, recyclerView, 0);
    }

    /**
     * 创建Handler
     *
     * @return
     */
    protected HandlerUtils initHandler() {
        mHandlers = new HandlerUtils(this);
        return mHandlers;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isStartFragmen = false;
        isStartFragmenData = false;
        if (Tools.isNotNullOrZeroSize(listRefreshHelper)) {
            for (int i = 0; i < listRefreshHelper.size(); i++) {
                if (listRefreshHelper.get(i) != null) {
                    listRefreshHelper.get(i).onDestroy();
                }
                if (i == listRefreshHelper.size() - 1) {
                    listRefreshHelper.clear();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandlers != null) {
            mHandlers.clearHandler();
        }
    }
}