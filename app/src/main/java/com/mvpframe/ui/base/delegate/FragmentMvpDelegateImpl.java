package com.mvpframe.ui.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mvpframe.biz.IMvpView;
import com.mvpframe.biz.Presenter;
import com.mvpframe.ui.base.interfaces.CreateInit;

/**
 * Fragment 媒介
 * 备注:主要是连接 Fragment 的生命周期与 Presenter 实现特定生命周期绑定与解除 V
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class FragmentMvpDelegateImpl<V extends IMvpView, P extends Presenter<V>> implements FragmentMvpDelegate {

    /**
     * Fragment
     */
    protected Fragment fragment;

    /**
     * V & P
     */
    private CreateInit<V, P> createInit;

    public FragmentMvpDelegateImpl(Fragment fragment, CreateInit<V, P> createInit) {
        if (fragment == null) {
            throw new NullPointerException("Fragment is null!");
        }
        if (createInit == null) {
            throw new NullPointerException("createInit is null!");
        }
        this.fragment = fragment;
        this.createInit = createInit;
    }

    /**
     * 是否保留V&P实例
     *
     * @return
     */
    private static boolean retainVPInstance(Activity activity, Fragment fragment) {
        if (activity.isChangingConfigurations()) {
            return false;
        }
        if (activity.isFinishing()) {
            return false;
        }
        return !fragment.isRemoving();
    }


    /**
     * 获取Activity
     *
     * @return
     */
    private Activity getActivity() {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new NullPointerException("Activity returned by Fragment.getActivity() is null. Fragment is " + fragment);
        }
        return activity;
    }

    @Override
    public void onCreate(Bundle saved) {
    }

    @Override
    public void onDestroy() {
        Activity activity = getActivity();
        P[] pArray = createInit.createPresenter();
        if (pArray != null) {
            P presenter;
            for (int i = 0; i < pArray.length; i++) {
                presenter = pArray[i];
                if (presenter != null) {
                    if (!retainVPInstance(activity, fragment)) {
                        //销毁 V & P 实例
                        presenter.destroy();
                    }
                }
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        P[] pArray = createInit.createPresenter();
        if (pArray != null) {
            V[] vArray = createInit.createView();
            P p;
            V v;
            for (int i = 0; i < pArray.length; i++) {
                p = pArray[i];
                v = vArray[i];
                if (p != null && v != null) {
                    //关联view
                    p.attachView(v);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        P[] pArray = createInit.createPresenter();
        if (pArray != null) {
            P presenter;
            for (int i = 0; i < pArray.length; i++) {
                presenter = pArray[i];
                if (presenter != null) {
                    //解除View
                    presenter.detachView();
                }
            }
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public void onAttach(Activity activity) {
    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
