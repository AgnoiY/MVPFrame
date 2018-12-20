package com.mvpframe.util;

import java.util.Calendar;

/**
 * @author yong
 */
public class Tools {

    public static int minClickDelayTime = 500;
    private static long lastClickTime = 0;

    /**
     * 防止重复点击
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
