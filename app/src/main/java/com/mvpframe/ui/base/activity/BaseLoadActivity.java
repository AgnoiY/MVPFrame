package com.mvpframe.ui.base.activity;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.mvpframe.R;
import com.mvpframe.biz.base.BasePresenter;
import com.mvpframe.biz.base.IMvpView;
import com.mvpframe.databinding.ActivityBaseLoadBinding;

/**
 * 带空页面，错误页面显示的BaseActivity 通过BaseActivity界面操作封装成View而来
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseLoadActivity<T, B extends ViewDataBinding> extends BaseActivity<T, IMvpView<T>, BasePresenter<IMvpView<T>>> {

    protected ActivityBaseLoadBinding mBaseBinding;

    protected B mLoadBinding;

    /**
     * 布局文件xml的resId,无需添加标题栏、加载、错误及空页面
     */
    @Override
    public void setContentView(Bundle savedInstanceState) {
        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_load);

        initNotify(this);

        mBaseBinding.contentView.addContentView(addMainView());

        initTitleView();
    }

    protected void initNotify(Context context) {

    }

    /**
     * 初始化标题View
     */
    private void initTitleView() {
        mBaseBinding.titleView.setVisibility(canLoadTopTitleView() ? View.VISIBLE : View.GONE);
        mBaseBinding.viewV.setVisibility(canLoadTopTitleView() ? View.VISIBLE : View.GONE);

        if (canLoadTopTitleView()) {
            mBaseBinding.titleView.setLeftFraClickListener(this);

            mBaseBinding.titleView.setRightFraClickListener(this);

            setTitleBg();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fram_img_back:
                topTitleViewleftClick();
                break;
            case R.id.fllayout_right:
                topTitleViewRightClick();
                break;
        }
    }

    /**
     * 设置标题栏标题
     */
    protected void setTopTitle(String title) {
        mBaseBinding.titleView.setMidTitle(title);
    }

    /**
     * 能否加载标题
     *
     * @return
     */
    protected boolean canLoadTopTitleView() {
        return true;
    }

    /**
     * 添加要显示的View
     */
    private View addMainView() {
        mLoadBinding = DataBindingUtil.inflate(getLayoutInflater(), getLayout(), null, false);
        return mLoadBinding.getRoot();
    }

    /**
     * 添加布局layoutID
     *
     * @return
     */
    public abstract int getLayout();

    public void topTitleViewleftClick() {
        finish();
    }

    public void topTitleViewRightClick() {

    }

    /**
     * 是否显示title
     *
     * @param isShow
     */
    protected void setShowTitle(boolean isShow) {
        mBaseBinding.titleView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mBaseBinding.viewV.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置title背景信息
     */
    protected void setTitleBg() {
        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.title_bg));
        mBaseBinding.titleView.setLeftTitleColor(R.color.text_black_cd);
        mBaseBinding.titleView.setRightTitleColor(R.color.text_black_cd);
        mBaseBinding.titleView.setMidTitleColor(R.color.text_black_cd);
        mBaseBinding.titleView.setLeftImg(R.mipmap.back_black);
//        mBaseBinding.titleView.setLeftTitle(getString(R.string.back));
    }

}
