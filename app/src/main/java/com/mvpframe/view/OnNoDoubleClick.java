package com.mvpframe.view;

import android.view.View;

/**
 * <防止重复点击>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
@FunctionalInterface
public interface OnNoDoubleClick {
    void onNoDoubleClick(View view);
}
