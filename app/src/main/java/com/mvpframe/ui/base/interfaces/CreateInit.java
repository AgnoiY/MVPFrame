package com.mvpframe.ui.base.interfaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.presenter.base.Presenter;

/**
 * <公共方法抽象>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface CreateInit<V extends IMvpView, P extends Presenter> extends BaseInterface {

    interface CreateInitActivity<V extends IMvpView, P extends Presenter> extends CreateInit<V, P> {
        /**
         * 设置布局文件
         */
        void setContentView(Bundle savedInstanceState);

        /**
         * 设置状态栏样式
         */
        void initStatusBarDarkTheme();

        /**
         * 权限申请成功
         */
        void initPermissionSuccess();

    }

    interface CreateInitFragment<V extends IMvpView, P extends Presenter> extends CreateInit<V, P> {
        /**
         * 设置布局文件
         */
        View initView(LayoutInflater inflater);

    }

    /**
     * 获取 Presenter 数组
     */
    P[] getPresenterArray();

    /**
     * 连接P层
     *
     * @return
     */
    P[] createPresenter();

    /**
     * 注入view
     *
     * @return
     */
    V[] createView();

    /**
     * 延迟后要进行的操作
     *
     * @param l
     */
    void nextStep(Long l, Object... delay);

}

