package com.mvpframe.view.linearLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mvpframe.R;

/**
 * 功能:状态栏高度View,用于沉浸占位
 * Data：2019/01/11
 *
 * @author yong
 */

public class StatusBarHeightView extends LinearLayout {
    private int statusBarHeight;

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {

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

    /**
     * 状态栏的高度为0
     * @return
     */
    public StatusBarHeightView setStatusBarHeight() {
        setPadding(getPaddingLeft(), 0, getPaddingRight(), getPaddingBottom());
        return this;
    }
}
