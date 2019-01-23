package com.mvpframe.ui.base.interfaces;

import android.content.Context;

/**
 * <功能详细描述>
 * <p>
 * Data：2019/01/15
 *
 * @author yong
 */
public interface LoadCreateClickListener<T> {

    /**
     * 初始化操作
     *
     * @param context
     */
    void initNotify(Context context);

    /**
     * 标题返回点击事件监听
     */
    void onTopTitleLeftClickListener();

    /**
     * 标题右侧点击事件监听
     */
    void onTopTitleRightClickListener();

    /**
     * 添加布局layoutID
     *
     * @return
     */
    int getLayout();

    /**
     * 加载显示错误布局，全布局点击事件监听
     */
    void onEmptyClickListener();

    /**
     * 加载显示错误布局，显示信息点击事件监听
     */
    void onEmptyTextClickListener();

    /**
     * 获取当前类泛型
     */
    Class<T> getTypeClass();

}
