package com.mvpframe.ui.base.interfaces;

import android.view.View;

/**
 * <页面基础公共功能抽象>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface PresentationLayerFunc {
    /**
     * 弹出消息
     *
     * @param msg
     */
    void showToast(String msg);

    /**
     * 显示软键盘
     *
     * @param focusView
     */
    void showSoftKeyboard(View focusView);

    /**
     * 隐藏软键盘
     */
    void hideSoftKeyboard();
}
