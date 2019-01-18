package com.mvpframe.util;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * <倒计时>
 * <p>
 * Data：2019/01/18
 *
 * @author yong
 */
public class DownTime extends CountDownTimer {

    /**
     * 开始倒计时控件
     */
    private TextView view;

    /**
     * 倒计时过程中字体变化
     */
    private String processText;
    /**
     * 计时结束字体
     */
    private String endText;

    /**
     * 倒计时过程是否能点击：默认不能点击
     */
    private boolean isEnabled;

    public DownTime(TextView view, long millisInFuture) {
        this(view, millisInFuture, "s", "0s", false);
    }

    public DownTime(TextView view, long millisInFuture, String endText) {
        this(view, millisInFuture, "s", endText, false);
    }

    public DownTime(TextView view, long millisInFuture, boolean isEnabled) {
        this(view, millisInFuture, "s", "0s", isEnabled);
    }

    public DownTime(TextView view, long millisInFuture, String processText, String endText, boolean isEnabled) {
        super(millisInFuture * 1000, 999);
        this.view = view;
        this.processText = processText;
        this.endText = endText;
        this.isEnabled = isEnabled;
        start();
    }

    @Override
    public void onTick(long l) {
        LogUtil.e("onTick: " + l);
        view.setEnabled(isEnabled);
        view.setText(l / 1000 + processText);
    }

    @Override
    public void onFinish() {
        view.setEnabled(true);
        view.setText(endText);
        cancel();
    }
}
