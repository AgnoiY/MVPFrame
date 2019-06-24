package com.mvpframe.utils;

import java.util.Calendar;

/**
 * <公共类>
 * Data：2018/12/18
 *
 * @author yong
 */
public class GeneralUtils {

    private static int minClickDelayTime = 500;
    private static long lastClickTime = 0;

    GeneralUtils() {
        throw new IllegalStateException("GeneralUtils class");
    }

    /**
     * 防止重复点击
     *
     * @return
     */
    public static boolean isDoubleClick() {

        long currentTime = Calendar.getInstance().getTimeInMillis();

        boolean isDoubleClick;
        if (currentTime - lastClickTime > minClickDelayTime) {
            isDoubleClick = false;
        } else {
            isDoubleClick = true;
        }
        lastClickTime = currentTime;
        return isDoubleClick;
    }
}
