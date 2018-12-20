package com.mvpframe.capabilities.http.interfaces;


/**
 * <页面网络加载提示基础公共功能抽象>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface ProgressDialogObserver {
    /**
     * 网络请求加载框
     */
    void showProgressDialog();

    /**
     * 隐藏网络请求加载框
     */
    void hideProgressDialog();

    /**
     * 取消订阅
     */
    void onCancleProgress();
}
