package com.mvpframe.view.linearlayout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mvpframe.utils.DensityUtils;

/**
 * 功能:状态栏高度View,用于沉浸占位
 * Data：2019/01/11
 *
 * @author yong
 */

public class StatusBarHeightView extends LinearLayout {

    private Context mContext;
    private int mStatusBarHeight;

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public StatusBarHeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mContext = context;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (resourceId > 0) {
                mStatusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        } else {
            //低版本 直接设置0
            mStatusBarHeight = 0;
        }

        setPadding(getPaddingLeft(), mStatusBarHeight, getPaddingRight(), getPaddingBottom());

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (getHeight() != DensityUtils.getYScreenpx(mContext) && getHeight() == DensityUtils.getScreenSize(mContext)[1]) {
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
