package com.mvpframe.capabilities.http.cancel;

/**
 * 请求取消接口
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface RequestCancel {

    /**
     * 取消请求
     */
    void cancel();

    /**
     * 请求被取消
     */
    void onCanceled();
}
