package com.mvpframe.presenter.base;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

/**
 * <基础业务类>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface Presenter<V> {

    /**
     * 将 View 添加到当前 Presenter
     *
     * @param view
     */
    @UiThread
    void attachView(@NonNull V view);

    /**
     * 将 View 从 Presenter 中移除
     */
    @UiThread
    void detachView();

    /**
     * 销毁 V 实例
     */
    @UiThread
    void destroy();
}
