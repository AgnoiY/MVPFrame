package com.mvpframe.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * DrawHook
 * Created by Zane on 2015/3/4.
 */
public class TipsInfoView extends View {

    private Paint mPaint;
    private float value;
    //估值
    private float fraction;
    private ValueAnimator mAnimator;
    //动画速度
    private final float SPEED = 100;
    private float max;

    private boolean isSuccess;

    public TipsInfoView(Context context) {
        super(context);
        initPain();
    }

    public TipsInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPain();
    }

    public TipsInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPain();
    }

    /**
     * 初始化画笔
     */
    private void initPain() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        //消除锯齿
        mPaint.setAntiAlias(true);
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float radius = getWidth() / 2.0f;

        mPaint.setColor(isSuccess ? Color.GREEN : Color.RED);

        canvas.drawCircle(radius, radius, radius, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(20);

        if (isSuccess) {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            int tra = 4;
            if (fraction <= tra / max) {
                float[] pts = {radius * 0.6f, radius, radius * 0.6f + radius * 0.3f * value / tra, radius + radius / 3 * value / tra};
                canvas.drawLines(pts, mPaint);
            } else {
                float[] pts = {radius * 0.6f, radius, radius * 0.9f, radius * 4 / 3,
                        radius * 0.9f, radius * 4 / 3, radius * 0.9f + radius * 0.58f * (value - tra) / (max - tra),
                        radius * 4 / 3 - radius * 0.57f * (value - tra) / (max - tra)};
                canvas.drawLines(pts, mPaint);
            }
        } else {
            float i = radius * 0.6f;
            float x = radius * 1.4f;
            float[] pts = {i, i, x, x, i, x, x, i};
            canvas.drawLines(pts, mPaint);
        }
    }

    /**
     * 开启动画
     *
     * @param duration
     */
    private void startAnimator(long duration) {
        max = duration / SPEED;
        mAnimator = ValueAnimator.ofFloat(0, max);
        mAnimator.setDuration(duration);
        mAnimator.addUpdateListener(animation -> {
            value = (float) animation.getAnimatedValue();
            fraction = animation.getAnimatedFraction();
            postInvalidate();
        });
        mAnimator.start();
    }

    /**
     * 停止动画
     */
    private void stopAnimator() {
        if (mAnimator != null) {
            mAnimator.removeAllUpdateListeners();
            mAnimator.cancel();
            mAnimator = null;
        }
    }


    public TipsInfoView setSuccess(boolean success, long duration) {
        isSuccess = success;
        if (isSuccess) {
            startAnimator(duration);
        }
        postInvalidate();
        return this;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimator();
    }
}
