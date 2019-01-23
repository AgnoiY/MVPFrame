package com.mvpframe.view.frameLayout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.mvpframe.R;
import com.mvpframe.databinding.CommonTitleInfoBinding;

/**
 * 顶部TitleView
 * Data：2018/12/18
 *
 * @author yong
 */

public class TopTitleLayout extends FrameLayout {

    private CommonTitleInfoBinding mBinding;

    private Context mContext;

    public TopTitleLayout(@NonNull Context context) {
        this(context, null);
    }

    public TopTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.common_title_info, this, true);
    }

    public void setLeftTitle(String text) {
        mBinding.tvBack.setText(text);
        mBinding.tvBack.setVisibility(text != null ? VISIBLE : GONE);
        mBinding.framImgBack.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setLeftTitleColor(@ColorRes int color) {
        mBinding.tvBack.setTextColor(ContextCompat.getColor(mContext, color));
    }

    public void setRightTitleColor(@ColorRes int color) {
        mBinding.tvTopRight.setTextColor(ContextCompat.getColor(mContext, color));
    }

    public void setMidTitleColor(@ColorRes int color) {
        mBinding.tvTopTitleAbs.setTextColor(ContextCompat.getColor(mContext, color));
    }

    public void setRightTitle(String text) {
        mBinding.tvTopRight.setText(text);
        mBinding.tvTopRight.setVisibility(text != null ? VISIBLE : GONE);
        mBinding.fllayoutRight.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setMidTitle(String text) {
        mBinding.tvTopTitleAbs.setText(text);
        mBinding.tvTopTitleAbs.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setLeftImg(@DrawableRes int img) {
        mBinding.imgBack.setVisibility(img != 0 ? VISIBLE : GONE);
        mBinding.framImgBack.setVisibility(img != 0 ? VISIBLE : GONE);
        if (img != 0) {
            mBinding.imgBack.setImageResource(img);
        }
    }

    public void setRightImg(@DrawableRes int img) {
        mBinding.imgRight.setVisibility(img != 0 ? VISIBLE : GONE);
        mBinding.fllayoutRight.setVisibility(img != 0 ? VISIBLE : GONE);
        if (img != 0) {
            mBinding.imgRight.setImageResource(img);
        }
    }

    public void setLeftFraClickListener(OnClickListener listener) {
        mBinding.framImgBack.setOnClickListener(listener);
    }

    public void setRightFraClickListener(OnClickListener listener) {
        mBinding.fllayoutRight.setOnClickListener(listener);
    }

    public void setBackgroundColor(int color) {
        findViewById(R.id.fram_title).setBackgroundColor(color);
    }

    public int getBackgroundColor() {
        ColorDrawable colorDrawable = (ColorDrawable) findViewById(R.id.fram_title).getBackground();
        return colorDrawable.getColor();
    }

}
