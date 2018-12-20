package com.mvpframe.ui.base.interfaces;

import android.os.Bundle;

/**
 * <页面跳转封装>
 * Data：2018/12/18
 *
 * @author yong
 */
public interface PublishActivityCallBack {
    /**
     * 打开新界面
     *
     * @param openClass 新开页面
     * @param bundle    参数
     */
    void startActivity(Class<?> openClass, Bundle bundle);

    /**
     * 打开新界面，期待返回
     *
     * @param openClass   新界面
     * @param requestCode 请求码
     * @param bundle      参数
     */
    void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle);

    /**
     * 返回到上个页面
     *
     * @param bundle 参数
     */
    void setResultOk(Bundle bundle);
}
