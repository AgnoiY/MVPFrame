package com.mvpframe.constant;

import com.mvpframe.BuildConfig;

/**
 * <全局常量>
 * Data：2018/12/18
 *
 * @author yong
 */
public class Constants {

    /**
     * 打开日志打开日志
     */
    public static final boolean isDebug = BuildConfig.DEBUG;

    /**
     * 超时时长: 默认60秒
     */
    public static final long timeOut = 60;

    /**
     * 数据缓存时间
     */
    public static final String URL_TIME = "3600*24";
}
