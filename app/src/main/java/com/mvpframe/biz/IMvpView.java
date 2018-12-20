package com.mvpframe.biz;

import android.support.annotation.UiThread;

/**
 * <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 * <p>
 * action 方式: 考虑多个请求时 根据 action 区分处理
 */
public interface IMvpView<T> {

    /**
     * 网络请求的错误信息，已在请求中处理提示Toast
     *
     * @param action 区分不同事件
     * @param code   错误码
     * @param msg    错误信息
     */
    @UiThread
    void onError(String action, int code, String msg);

    /**
     * 成功返回结果
     *
     * @param action 区分不同事件
     * @param data   数据
     */
    @UiThread
    void onSuccess(String action, T data);

}
