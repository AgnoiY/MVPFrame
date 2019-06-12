package com.mvpframe.capabilities.http.utils;

import android.os.Looper;

/**
 * 线程工具类
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class ThreadUtils {

    ThreadUtils() {
        throw new IllegalStateException("ThreadUtils class");
    }

    /**
     * 是否主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }
}
