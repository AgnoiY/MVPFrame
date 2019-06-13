package com.mvpframe.ui.base.delegate;

import android.app.Activity;
import android.os.Bundle;

import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.presenter.base.Presenter;
import com.mvpframe.ui.base.interfaces.CreateInit;

/**
 * Activity媒介
 * 备注:主要是连接 Activity 的生命周期与 Presenter 实现特定生命周期绑定与解除 V
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class ActivityMvpDelegateImpl<V extends IMvpView, P extends Presenter<V>> implements ActivityMvpDelegate {

    /**
     * Activity
     */
    protected Activity activity;

    /**
     * V & P
     */
    private CreateInit<V, P> createInit;

    public ActivityMvpDelegateImpl(Activity activity, CreateInit<V, P> createInit) {
        if (activity == null) {
            throw new NullPointerException("Activity is null!");
        }
        if (createInit == null) {
            throw new NullPointerException("createInit is null!");
        }
        this.activity = activity;
        this.createInit = createInit;
    }

    /**
     * 是否保留V&P实例
     *
     * @return
     */
    private static boolean retainVPInstance(Activity activity) {
        return activity.isChangingConfigurations() || !activity.isFinishing();
    }

    @Override
    public void onCreate(Bundle bundle) {

        P[] pArray = createInit.createPresenter();
        if (pArray != null) {
            V[] vArray = createInit.createView();
            P presenter;
            V view;
            for (int i = 0; i < pArray.length; i++) {
                presenter = pArray[i];
                view = vArray[i];
                if (presenter != null && view != null) {
                    //关联view
                    presenter.attachView(view);
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        P[] pArray = createInit.createPresenter();
        if (pArray != null) {
            P presenter;
            for (int i = 0; i < pArray.length; i++) {
                presenter = pArray[i];
                if (presenter != null) {
                    //解除View
                    presenter.detachView();
                    if (!retainVPInstance(activity)) {
                        //销毁 V & P 实例
                        presenter.destroy();
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {
        // 关联Activity的生命周期
    }

    @Override
    public void onResume() {
        // 关联Activity的生命周期
    }

    @Override
    public void onStart() {
        // 关联Activity的生命周期
    }

    @Override
    public void onStop() {
        // 关联Activity的生命周期
    }

    @Override
    public void onRestart() {
        // 关联Activity的生命周期
    }

    @Override
    public void onContentChanged() {
        // 关联Activity的生命周期
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // 关联Activity的生命周期
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        // 关联Activity的生命周期
    }
}
