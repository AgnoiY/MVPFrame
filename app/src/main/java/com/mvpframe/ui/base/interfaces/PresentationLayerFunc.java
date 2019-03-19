package com.mvpframe.ui.base.interfaces;

import android.view.View;

import com.mvpframe.bean.event.BaseEventModel;

import org.greenrobot.eventbus.Subscribe;

/**
 * <页面基础公共功能抽象>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface PresentationLayerFunc<T> {
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

    /**
     * 处理事件线
     *
     * @param eventModel
     */
    @Subscribe
    void onEventMainThread(BaseEventModel<T> eventModel);

    /**
     * 发送EventBus事件线
     *
     * @param o
     */
    void getEventBusPost(Object... o);

    /**
     * 延迟操作
     *
     * @param delay
     */
    void postDelayed(long delay);

    /**
     * 打印日志
     *
     * @param msg 0: 信息  1: 日志的样式 e/d/i
     */
    void log(Object... msg);
}
