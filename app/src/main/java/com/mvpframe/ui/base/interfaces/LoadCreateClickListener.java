package com.mvpframe.ui.base.interfaces;

/**
 * <功能详细描述>
 * <p>
 * Data：2019/01/15
 *
 * @author yong
 */
public interface LoadCreateClickListener {

    /**
     * 初始化操作
     */
    void initNotify();

    /**
     * 标题返回点击事件监听
     */
    void onTopTitleLeftClickListener();

    /**
     * 标题右侧点击事件监听
     */
    void onTopTitleRightClickListener();

    /**
     * 加载显示错误布局，显示信息点击事件监听
     */
    void onEmptyTextClickListener();

}
