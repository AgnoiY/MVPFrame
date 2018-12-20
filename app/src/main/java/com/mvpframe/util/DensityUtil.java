package com.mvpframe.util;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * <像素转换>
 * Data：2018/12/18
 *
 * @author yong
 */
public class DensityUtil {
    /**
     * <将px值转换为dip或dp值，保证尺寸大小不变>
     *
     * @param pxValue DisplayMetrics类中属性density
     * @return
     */
    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * <将dip或dp值转换为px值，保证尺寸大小不变>
     *
     * @param dipValue DisplayMetrics类中属性density
     * @return
     */
    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * <将px值转换为sp值，保证文字大小不变>
     * <功能详细描述>
     *
     * @param pxValue DisplayMetrics类中属性scaledDensity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int px2sp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * <将sp值转换为px值，保证文字大小不变>
     * <功能详细描述>
     *
     * @param spValue DisplayMetrics类中属性scaledDensity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int sp2px(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * <获取屏幕像素x>
     *
     * @param activity
     * @return
     */
    public static int getXScreenpx(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * <获取屏幕像素y>
     *
     * @param activity
     * @return
     */
    public static int getYScreenpx(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
}
