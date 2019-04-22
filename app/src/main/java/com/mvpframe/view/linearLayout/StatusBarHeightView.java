package com.mvpframe.view.linearLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mvpframe.util.DensityUtil;

/**
 * 功能:状态栏高度View,用于沉浸占位
 * Data：2019/01/11
 *
 * @author yong
 */

public class StatusBarHeightView extends LinearLayout {

    private Activity activity;
    private int statusBarHeight;

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        activity = (Activity) context;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        } else {
            //低版本 直接设置0
            statusBarHeight = 0;
        }

        setPadding(getPaddingLeft(), statusBarHeight, getPaddingRight(), getPaddingBottom());

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (getHeight() != DensityUtil.getYScreenpx(activity) && getHeight() == DensityUtil.getScreenSize(activity)[1]) {
            setPadding(getPaddingLeft(), 0, getPaddingRight(), getPaddingBottom());
        }
    }

    /**
     * 状态栏的高度为0
     *
     * @return
     */
    public StatusBarHeightView setStatusBarHeight() {
        setPadding(getPaddingLeft(), 0, getPaddingRight(), getPaddingBottom());
        return this;
    }
}
