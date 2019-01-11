package com.mvpframe.util.statusbar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

import java.lang.reflect.Method;

/**
 * 使用KitKat时管理状态和导航栏色调效果的类
 * 半透明系统UI模式
 * Data：2019/01/11
 *
 * @author yong
 */

public class SystemBarTintManager {

    static {
        //Android允许系统属性覆盖导航栏的存在。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                sNavBarOverride = null;
            }
        }
    }


    /**
     * 默认系统栏色调颜色值。
     */
    public static final int DEFAULT_TINT_COLOR = 0x99000000;

    private static String sNavBarOverride;

    private final SystemBarConfig mConfig;
    private boolean mStatusBarAvailable;
    private boolean mNavBarAvailable;
    private boolean mStatusBarTintEnabled;
    private boolean mNavBarTintEnabled;
    private View mStatusBarTintView;
    private View mNavBarTintView;

    /**
     * 构造函数。在主机活动onCreate方法之后调用它
     * 内容视图已设置。您应该始终创建新实例
     * 重新创建主机活动。
     *
     * @param activity
     */
    @SuppressLint("ResourceType")
    @TargetApi(19)
    public SystemBarTintManager(Activity activity) {

        Window win = activity.getWindow();
        ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 检查主题attrs
            int[] attrs = {android.R.attr.windowTranslucentStatus,
                    android.R.attr.windowTranslucentNavigation};
            TypedArray a = activity.obtainStyledAttributes(attrs);
            try {
                mStatusBarAvailable = a.getBoolean(0, false);
                mNavBarAvailable = a.getBoolean(1, false);
            } finally {
                a.recycle();
            }

            // 检查窗口标志
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((winParams.flags & bits) != 0) {
                mStatusBarAvailable = true;
            }
            bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            if ((winParams.flags & bits) != 0) {
                mNavBarAvailable = true;
            }
        }

        mConfig = new SystemBarConfig(activity, mStatusBarAvailable, mNavBarAvailable);
        // 设备可能没有虚拟导航键
        if (!mConfig.hasNavigtionBar()) {
            mNavBarAvailable = false;
        }

        if (mStatusBarAvailable) {
            setupStatusBarView(activity, decorViewGroup);
        }
        if (mNavBarAvailable) {
            setupNavBarView(activity, decorViewGroup);
        }

    }

    /**
     * 启用系统状态栏的着色。
     * <p>
     * 如果平台正在运行Jelly Bean或更早版本，或半透明系统
     * 主题或通过窗口标志尚未启用UI模式，
     * 那么这种方法什么都不做。
     *
     * @param enabled True表示启用tinting，false表示禁用它（默认）。
     */
    public void setStatusBarTintEnabled(boolean enabled) {
        mStatusBarTintEnabled = enabled;
        if (mStatusBarAvailable) {
            mStatusBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 启用系统导航栏的着色。
     * <p>
     * 如果平台没有软导航键，则运行Jelly Bean
     * 或更早，或者半透明的系统UI模式都没有启用
     * 主题或通过窗口标志，然后这个方法什么都不做。
     *
     * @param enabled True表示启用tinting，false表示禁用它（默认）。
     */
    public void setNavigationBarTintEnabled(boolean enabled) {
        mNavBarTintEnabled = enabled;
        if (mNavBarAvailable) {
            mNavBarTintView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 将指定的颜色色调应用于所有系统UI栏。
     *
     * @param color 背景色调的颜色。
     */
    public void setTintColor(int color) {
        setStatusBarTintColor(color);
        setNavigationBarTintColor(color);
    }

    /**
     * 将指定的drawable或color资源应用于所有系统UI栏。
     *
     * @param res 资源的标识符。
     */
    public void setTintResource(int res) {
        setStatusBarTintResource(res);
        setNavigationBarTintResource(res);
    }

    /**
     * 将指定的drawable应用于所有系统UI栏。
     *
     * @param drawable 用作背景的drawable，或null以删除它.
     */
    public void setTintDrawable(Drawable drawable) {
        setStatusBarTintDrawable(drawable);
        setNavigationBarTintDrawable(drawable);
    }

    /**
     * 将指定的alpha应用于所有系统UI栏。
     *
     * @param alpha 要使用的alpha
     */
    public void setTintAlpha(float alpha) {
        setStatusBarAlpha(alpha);
        setNavigationBarAlpha(alpha);
    }

    /**
     * 将指定的颜色色调应用于系统状态栏。
     *
     * @param color 背景色调的颜色。
     */
    public void setStatusBarTintColor(int color) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundColor(color);
        }
    }

    /**
     * 将指定的drawable或color resource应用于系统状态栏。
     *
     * @param res 资源的标识符。
     */
    public void setStatusBarTintResource(int res) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundResource(res);
        }
    }

    /**
     * 将指定的drawable应用于系统状态栏。
     *
     * @param drawable 用作背景的drawable，或null以删除它。
     */
    @SuppressWarnings("deprecation")
    public void setStatusBarTintDrawable(Drawable drawable) {
        if (mStatusBarAvailable) {
            mStatusBarTintView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 将指定的alpha应用于系统状态栏。
     *
     * @param alpha 要使用的alpha
     */
    @TargetApi(11)
    public void setStatusBarAlpha(float alpha) {
        if (mStatusBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mStatusBarTintView.setAlpha(alpha);
        }
    }

    /**
     * 将指定的颜色色调应用于系统导航栏。
     *
     * @param color 背景色调的颜色。
     */
    public void setNavigationBarTintColor(int color) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundColor(color);
        }
    }

    /**
     * 将指定的可绘制或颜色资源应用于系统导航栏。
     *
     * @param res 资源的标识符。
     */
    public void setNavigationBarTintResource(int res) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundResource(res);
        }
    }

    /**
     * 将指定的drawable应用于系统导航栏。
     *
     * @param drawable 用作背景的drawable，或null以删除它。
     */
    @SuppressWarnings("deprecation")
    public void setNavigationBarTintDrawable(Drawable drawable) {
        if (mNavBarAvailable) {
            mNavBarTintView.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 将指定的alpha应用于系统导航栏。
     *
     * @param alpha 要使用的alpha
     */
    @TargetApi(11)
    public void setNavigationBarAlpha(float alpha) {
        if (mNavBarAvailable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mNavBarTintView.setAlpha(alpha);
        }
    }

    /**
     * 获取系统栏配置。
     *
     * @return 当前设备配置的系统栏配置。
     */
    public SystemBarConfig getConfig() {
        return mConfig;
    }

    /**
     * 是否为系统状态栏启用了着色？
     *
     * @return 如果启用则为True，否则为False。
     */
    public boolean isStatusBarTintEnabled() {
        return mStatusBarTintEnabled;
    }

    /**
     * 是否为系统导航栏启用了着色？
     *
     * @return 如果启用则为True，否则为False。
     */
    public boolean isNavBarTintEnabled() {
        return mNavBarTintEnabled;
    }

    private void setupStatusBarView(Context context, ViewGroup decorViewGroup) {
        mStatusBarTintView = new View(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mConfig.getStatusBarHeight());
        params.gravity = Gravity.TOP;
        if (mNavBarAvailable && !mConfig.isNavigationAtBottom()) {
            params.rightMargin = mConfig.getNavigationBarWidth();
        }
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mStatusBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mStatusBarTintView);
    }

    private void setupNavBarView(Context context, ViewGroup decorViewGroup) {
        mNavBarTintView = new View(context);
        LayoutParams params;
        if (mConfig.isNavigationAtBottom()) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, mConfig.getNavigationBarHeight());
            params.gravity = Gravity.BOTTOM;
        } else {
            params = new LayoutParams(mConfig.getNavigationBarWidth(), LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.RIGHT;
        }
        mNavBarTintView.setLayoutParams(params);
        mNavBarTintView.setBackgroundColor(DEFAULT_TINT_COLOR);
        mNavBarTintView.setVisibility(View.GONE);
        decorViewGroup.addView(mNavBarTintView);
    }

    /**
     * 描述系统条尺寸和当前其他特性的类
     * 设备配置。
     */
    public static class SystemBarConfig {

        private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";
        private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
        private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
        private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
        private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";

        private final boolean mTranslucentStatusBar;
        private final boolean mTranslucentNavBar;
        private final int mStatusBarHeight;
        private final int mActionBarHeight;
        private final boolean mHasNavigationBar;
        private final int mNavigationBarHeight;
        private final int mNavigationBarWidth;
        private final boolean mInPortrait;
        private final float mSmallestWidthDp;

        private SystemBarConfig(Activity activity, boolean translucentStatusBar, boolean traslucentNavBar) {
            Resources res = activity.getResources();
            mInPortrait = (res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
            mSmallestWidthDp = getSmallestWidthDp(activity);
            mStatusBarHeight = getInternalDimensionSize(res, STATUS_BAR_HEIGHT_RES_NAME);
            mActionBarHeight = getActionBarHeight(activity);
            mNavigationBarHeight = getNavigationBarHeight(activity);
            mNavigationBarWidth = getNavigationBarWidth(activity);
            mHasNavigationBar = (mNavigationBarHeight > 0);
            mTranslucentStatusBar = translucentStatusBar;
            mTranslucentNavBar = traslucentNavBar;
        }

        @TargetApi(14)
        private int getActionBarHeight(Context context) {
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                TypedValue tv = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
                result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            }
            return result;
        }

        @TargetApi(14)
        private int getNavigationBarHeight(Context context) {
            Resources res = context.getResources();
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (hasNavBar(context)) {
                    String key;
                    if (mInPortrait) {
                        key = NAV_BAR_HEIGHT_RES_NAME;
                    } else {
                        key = NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
                    }
                    return getInternalDimensionSize(res, key);
                }
            }
            return result;
        }

        @TargetApi(14)
        private int getNavigationBarWidth(Context context) {
            Resources res = context.getResources();
            int result = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (hasNavBar(context)) {
                    return getInternalDimensionSize(res, NAV_BAR_WIDTH_RES_NAME);
                }
            }
            return result;
        }

        @TargetApi(14)
        private boolean hasNavBar(Context context) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
            if (resourceId != 0) {
                boolean hasNav = res.getBoolean(resourceId);
                // check override flag (see static block)
                if ("1".equals(sNavBarOverride)) {
                    hasNav = false;
                } else if ("0".equals(sNavBarOverride)) {
                    hasNav = true;
                }
                return hasNav;
            } else { // fallback
                return !ViewConfiguration.get(context).hasPermanentMenuKey();
            }
        }

        private int getInternalDimensionSize(Resources res, String key) {
            int result = 0;
            int resourceId = res.getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
            return result;
        }

        @SuppressLint("NewApi")
        private float getSmallestWidthDp(Activity activity) {
            DisplayMetrics metrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            } else {
                // TODO this is not correct, but we don't really care pre-kitkat
                activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            }
            float widthDp = metrics.widthPixels / metrics.density;
            float heightDp = metrics.heightPixels / metrics.density;
            return Math.min(widthDp, heightDp);
        }

        /**
         * 导航栏是否应显示在当前屏幕的底部
         * 设备配置？导航栏可能会出现在右侧
         * 某些配置的屏幕。
         *
         * @return 如果导航应出现在屏幕底部，则为True，否则为False。
         */
        public boolean isNavigationAtBottom() {
            return (mSmallestWidthDp >= 600 || mInPortrait);
        }

        /**
         * 获取系统状态栏的高度。
         *
         * @return 状态栏的高度（以像素为单位）。
         */
        public int getStatusBarHeight() {
            return mStatusBarHeight;
        }

        /**
         * 获取操作栏的高度。
         *
         * @return 操作栏的高度（以像素为单位）。
         */
        public int getActionBarHeight() {
            return mActionBarHeight;
        }

        /**
         * 此设备是否有系统导航栏？
         *
         * @return 如果此设备使用软键导航，则为True，否则为False。
         */
        public boolean hasNavigtionBar() {
            return mHasNavigationBar;
        }

        /**
         * 获取系统导航栏的高度。
         *
         * @return 导航栏的高度（以像素为单位）。如果设备没有
         * 软导航键，这将始终返回0。
         */
        public int getNavigationBarHeight() {
            return mNavigationBarHeight;
        }

        /**
         * 当系统导航栏垂直放置在屏幕上时，获取系统导航栏的宽度。
         *
         * @return 导航栏的宽度（以像素为单位）。如果设备没有
         * 软导航键，这将始终返回0。
         */
        public int getNavigationBarWidth()

        {
            return mNavigationBarWidth;
        }

        /**
         * 获取显示在屏幕顶部的任何系统UI的布局插图。
         *
         * @param withActionBar True表示操作栏的高度，否则为False。
         * @return 布局插图（以像素为单位）。
         */
        public int getPixelInsetTop(
                boolean withActionBar)

        {
            return (mTranslucentStatusBar ? mStatusBarHeight : 0) + (withActionBar ? mActionBarHeight :
                    0)
                    ;
        }

        /**
         * 获取显示在屏幕底部的任何系统UI的布局插图。
         *
         * @return 布局插图（以像素为单位）。
         */
        public int getPixelInsetBottom()

        {
            if (mTranslucentNavBar && isNavigationAtBottom()) {
                return mNavigationBarHeight;
            } else {
                return
                        0;
            }
        }

        /**
         * 获取屏幕右侧显示的任何系统UI的布局插图。
         *
         * @return 布局插图（以像素为单位）。
         */
        public int getPixelInsetRight()

        {
            if (mTranslucentNavBar && !isNavigationAtBottom()) {
                return mNavigationBarWidth;
            } else {
                return 0;
            }
        }

    }

}
