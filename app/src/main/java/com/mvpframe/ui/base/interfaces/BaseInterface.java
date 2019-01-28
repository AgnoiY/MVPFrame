package com.mvpframe.ui.base.interfaces;

/**
 * <公共方法抽象>
 * <p>
 * Data：2019/01/28
 *
 * @author yong
 */
public interface BaseInterface {

    /**
     * 添加布局layoutID
     *
     * @return
     */
    int getLayoutId();

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 增加按钮点击事件
     */
    void initListeners();

}
