package com.mvpframe.capabilities.http.interfaces;

import java.util.List;

/**
 * <请求返回状态>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface CallBack<T> {
    /**
     * 请求成功
     *
     * @param action
     * @param t
     */
    void inSuccess(String action, T t);

    /**
     * 请求出错
     *
     * @param action
     * @param code
     * @param desc
     */
    void inError(String action, int code, String desc);

    /**
     * 请求取消
     */
    void inCancel();

    /**
     * 成功回调
     *
     * @param action
     * @param value
     */
    void onSuccess(String action, T value);

    /**
     * 成功回调
     *
     * @param action
     * @param value
     */
    void onSuccess(String action, List<T> value);

    /**
     * 失败回调
     *
     * @param action
     * @param code
     * @param msg
     */
    void onError(String action, int code, String msg);

    /**
     * 取消回调
     */
    void onCancel();

    /**
     * 业务逻辑是否成功
     *
     * @return
     */
    boolean isBusinessOk();

    /**
     * 数据转换/解析数据
     *
     * @param data
     * @return
     */
    T onConvert(String data) throws Exception;

    /**
     * token过期，跳转登录页面重新登录
     */
    void isLoginToken();

}
