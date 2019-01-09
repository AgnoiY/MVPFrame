package com.mvpframe.presenter.base;

import android.support.annotation.UiThread;

import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.Bridges;
import com.mvpframe.bridge.http.RetrofitHttp;
import com.mvpframe.bridge.sharePref.SharedPrefManager;
import com.mvpframe.bridge.sharePref.SharedPrefUser;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.WeakReference;

/**
 * <基础业务类>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BasePresenter<V extends IMvpView> implements Presenter<V> {

    private WeakReference<V> viewRef;

    private SharedPrefManager sharedPref;

    private SecurityManager securityManager;

    private RetrofitHttp.Builder retrofitHttp;

    /**
     * 获取 View
     *
     * @return
     */
    @UiThread
    public V getMvpView() {
        return viewRef == null ? null : viewRef.get();
    }

    /**
     * 判断View是否已经添加
     *
     * @return
     */
    @UiThread
    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    /**
     * 绑定 View
     *
     * @param view
     */
    @UiThread
    @Override
    public void attachView(V view) {
        viewRef = new WeakReference<>(view);
    }

    /**
     * 移除 View
     */
    @Override
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    @Override
    public void destroy() {
    }

    /**
     * MD5加密
     */
    public SecurityManager getSecurityManager() {
        if (securityManager == null)
            securityManager = BridgeFactory.getBridge(Bridges.SECURITY);
        return securityManager;
    }

    /**
     * 网络请求
     */
    public RetrofitHttp.Builder getRetrofitHttp() {
        if (retrofitHttp == null)
            retrofitHttp = BridgeFactory.getBridge(Bridges.HTTP);
        retrofitHttp.clear();
        retrofitHttp.lifecycle((LifecycleProvider) getMvpView());
        retrofitHttp.addHeader(SharedPrefUser.USER_TOKEN, SharedPrefManager.getUser()
                .getString(SharedPrefUser.USER_TOKEN, ""));
        return retrofitHttp;
    }
}
