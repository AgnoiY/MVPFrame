package com.mvpframe.view;

import android.view.View;

import com.mvpframe.util.LogUtil;

import static com.mvpframe.util.GeneralUtils.isDoubleClick;

/**
 * 防止重复点击
 * Data：2018/12/18
 *
 * @author yong
 */

public class OnNoDoubleClickListener implements View.OnClickListener {

    private OnNoDoubleClick onNoDoubleClick;

    @Override
    public void onClick(View v) {
        if (isDoubleClick()) {
            LogUtil.e("重复点击");
        } else {
            onNoDoubleClick.onNoDoubleClick(v);
        }
    }

    public OnNoDoubleClickListener(OnNoDoubleClick click) {
        this.onNoDoubleClick = click;
    }
}
