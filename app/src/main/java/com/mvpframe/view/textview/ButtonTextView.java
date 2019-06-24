package com.mvpframe.view.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mvpframe.R;
import com.mvpframe.utils.DensityUtils;

/**
 * <按钮式textview，带边框样式>
 * <p>
 * Data：2019/01/17
 *
 * @author yong
 */
@SuppressLint("AppCompatCustomView")
public class ButtonTextView extends TextView {

    // 默认边框宽度, 1dp
    public static final float DEFAULT_STROKE_WIDTH = 1.0f;
    // 默认圆角半径, 2dp
    public static final float DEFAULT_CORNER_RADIUS = 3.0f;
    // 默认左右内边距
    public static final float DEFAULT_LR_PADDING = 20f;
    // 默认上下内边距
    public static final float DEFAULT_TB_PADDING = 6f;

    // 边框线宽
    private int strokeWidth;
    // 边框颜色
    private int strokeColor;
    // 圆角半径
    private int cornerRadius;
    // 边框颜色是否跟随文字颜色
    private boolean mFollowTextColor;
    // 实心或空心，默认空心
    private boolean isSolid;
    // 字数量改变边距，默认：true 改变
    private boolean isTextNumPadding;

    // 画边框所使用画笔对象
    private Paint mPaint = new Paint();
    // 画边框要使用的矩形
    private RectF mRectF;

    public ButtonTextView(Context context) {
        super(context);
        setCustomAttributes(context, null);
    }

    public ButtonTextView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        setCustomAttributes(context, attrs);
    }

    public ButtonTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomAttributes(context, attrs);
    }

    private void setCustomAttributes(Context context, AttributeSet attrs) {
        // 将DIP单位默认值转为PX
        strokeWidth = DensityUtils.dip2px(DEFAULT_STROKE_WIDTH);
        cornerRadius = DensityUtils.dip2px(DEFAULT_CORNER_RADIUS);
        // 读取属性值
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BorderLabelTextView);
            strokeWidth = ta.getDimensionPixelSize(R.styleable.BorderLabelTextView_strokeWidth, strokeWidth);
            cornerRadius = ta.getDimensionPixelSize(R.styleable.BorderLabelTextView_cornerRadius, cornerRadius);
            strokeColor = ta.getColor(R.styleable.BorderLabelTextView_strokeColor, Color.TRANSPARENT);
            mFollowTextColor = ta.getBoolean(R.styleable.BorderLabelTextView_followTextColor, false);
            isSolid = ta.getBoolean(R.styleable.BorderLabelTextView_isSolid, false);
            isTextNumPadding = ta.getBoolean(R.styleable.BorderLabelTextView_isTextNumPadding, true);
            ta.recycle();
        }

        mRectF = new RectF();

        // 如果使用时没有设置内边距, 设置默认边距
        int paddingLeft = getPaddingLeft() == 0 ? DensityUtils.dip2px(DEFAULT_LR_PADDING) : getPaddingLeft();
        int paddingRight = getPaddingRight() == 0 ? DensityUtils.dip2px(DEFAULT_LR_PADDING) : getPaddingRight();
        int paddingTop = getPaddingTop() == 0 ? DensityUtils.dip2px(DEFAULT_TB_PADDING) : getPaddingTop();
        int paddingBottom = getPaddingBottom() == 0 ? DensityUtils.dip2px(DEFAULT_TB_PADDING) : getPaddingBottom();
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isSolid)
            mPaint.setStyle(Paint.Style.FILL);// 实心效果
        else
            mPaint.setStyle(Paint.Style.STROKE);// 空心效果

        mPaint.setAntiAlias(true);// 设置画笔为无锯齿
        mPaint.setStrokeWidth(strokeWidth);// 线宽

        // 设置边框线的颜色, 如果声明为边框跟随文字颜色且当前边框颜色与文字颜色不同时重新设置边框颜色
        if (mFollowTextColor && strokeColor != getCurrentTextColor())
            strokeColor = getCurrentTextColor();
        mPaint.setColor(strokeColor);

        // 画空心圆矩形
        mRectF.left = mRectF.top = 0.5f * strokeWidth;
        mRectF.right = getMeasuredWidth() - strokeWidth;
        mRectF.bottom = getMeasuredHeight() - strokeWidth;
        canvas.drawRoundRect(mRectF, cornerRadius, cornerRadius, mPaint);

        super.onDraw(canvas);
        // drawRoundRect放在 super.onDraw(canvas)前面
        // 如果drawRoundRect放在 super.onDraw(canvas)后面，父类先画TextView，然后再画子类矩形，
        // 如果画的是实心矩形，则会遮盖父类的text字体

        // 根据文本长度改变边距
        if (getText().length() > 2 && isTextNumPadding) {
            int paddingLeft = DensityUtils.dip2px(DEFAULT_LR_PADDING * 1.5f);
            int paddingRight = DensityUtils.dip2px((DEFAULT_LR_PADDING * 1.5f));
            int paddingTop = DensityUtils.dip2px(DEFAULT_TB_PADDING * 2);
            int paddingBottom = DensityUtils.dip2px(DEFAULT_TB_PADDING * 2);
            setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    /**
     * 设置边框线宽度
     *
     * @param size
     */
    public void setStrokeWidth(int size) {
        strokeWidth = DensityUtils.dip2px(size);
        invalidate();
    }

    /**
     * 设置边框颜色
     *
     * @param color
     */
    public void setStrokeColor(int color) {
        strokeColor = color;
        invalidate();
    }

    /**
     * 设置圆角半径
     *
     * @param radius
     */
    public void setCornerRadius(int radius) {
        cornerRadius = DensityUtils.dip2px(radius);
        invalidate();
    }

    /**
     * 设置边框颜色是否跟随文字颜色
     *
     * @param isFollow
     */
    public void setFollowTextColor(boolean isFollow) {
        mFollowTextColor = isFollow;
        invalidate();
    }

    /**
     * 设置实心或空心，默认空心
     *
     * @param solid
     */
    public void setSolid(boolean solid) {
        isSolid = solid;
        invalidate();
    }

    /**
     * 字数量改变边距，默认：true 改变
     *
     * @param textNumPadding
     */
    public void setTextNumPadding(boolean textNumPadding) {
        isTextNumPadding = textNumPadding;
        invalidate();
    }
}
