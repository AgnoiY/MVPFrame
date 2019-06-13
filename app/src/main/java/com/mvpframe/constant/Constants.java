package com.mvpframe.constant;

import com.mvpframe.BuildConfig;

/**
 * <全局常量>
 * Data：2018/12/18
 *
 * @author yong
 */
public class Constants {

    Constants() {
        throw new IllegalStateException("Constants class");
    }

    /**
     * 打开日志打开日志
     */
    public static final boolean IS_DEBUG = BuildConfig.DEBUG;
    public static final String LOG_V = "v";
    public static final String LOG_E = "e";
    public static final String LOG_I = "i";
    public static final String LOG_D = "d";
    public static final String LOG_W = "w";

    /**
     * 超时时长: 默认60秒
     */
    public static final long TIME_OUT = 60;

    /**
     * 数据缓存时间
     */
    public static final String URL_TIME = "3600*24";
}
