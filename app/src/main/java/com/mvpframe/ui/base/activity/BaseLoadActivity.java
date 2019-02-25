package com.mvpframe.ui.base.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mvpframe.R;
import com.mvpframe.capabilities.http.exception.ExceptionEngine;
import com.mvpframe.databinding.ActivityBaseLoadBinding;
import com.mvpframe.ui.base.interfaces.LoadCreateClickListener;
import com.mvpframe.util.LogUtil;
import com.mvpframe.util.NetUtils;
import com.mvpframe.util.ToastUtil;
import com.mvpframe.util.Tools;
import com.mvpframe.util.statusbar.StatusBarUtil;
import com.mvpframe.view.recyclerView.RefreshHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 带空页面，错误页面显示的BaseActivity 通过BaseActivity界面操作封装成View而来
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseLoadActivity<T, B extends ViewDataBinding>
        extends BasePermissionsActivity<T>
//        extends BaseActivity<T, IMvpView<T>, BasePresenter<IMvpView<T>>>
        implements LoadCreateClickListener {

    protected ActivityBaseLoadBinding mBaseBinding;

    protected B mLoadBinding;

    protected List<RefreshHelper> listRefreshHelper;

    /**
     * 布局文件xml的resId,无需添加标题栏、加载、错误及空页面
     */
    @Override
    public void setContentView(Bundle savedInstanceState) {
        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_load);

        mBaseBinding.contentView.addContentView(addMainView());

        initTitleView();

        initNotify(this);
    }

    public void initNotify(Context context) {

    }

    /**
     * 设置状态栏风格
     */
    @Override
    public void initStatusBarDarkTheme() {

        //当FitsSystemWindows设置true时，会在屏幕最上方预留出状态栏高度的padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);

        setStatusBarDarkTheme(true);
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

        mBaseBinding.contentView.setEmptyTextClickListener(this);
    }

    /**
     * 是否加载状态栏
     *
     * @param isShow true: 显示、flase: 隐藏
     */
    protected void setShowStatusBar(boolean isShow) {
        mBaseBinding.statusBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
        setShowTitle(isShow);
        if (!isShow)
            mBaseBinding.statusBarHeightView.setStatusBarHeight();
    }

    /**
     * 设置状态栏字体颜色
     *
     * @param dark
     */
    @SuppressLint("Range")
    protected void setStatusBarDarkTheme(boolean dark) {

        ViewGroup.LayoutParams params = mBaseBinding.statusBar.getLayoutParams();
        params.height = StatusBarUtil.getStatusBarHeight(this);
        mBaseBinding.statusBar.setBackgroundColor(mBaseBinding.titleView.getBackgroundColor());
        mBaseBinding.statusBar.setLayoutParams(params);

        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //设置状态使用深色文字图标风格
        if (!StatusBarUtil.setStatusBarDarkTheme(this, dark) &&
                mBaseBinding.titleView.getBackgroundColor() == ContextCompat.getColor(mContext, R.color.white) &&
                mBaseBinding.titleView.getVisibility() == View.VISIBLE) {
            //设置一个状态栏颜色为半透明,
            StatusBarUtil.setStatusBarColor(this, 0x50000000);
        }
    }

    /**
     * 加载标题
     *
     * @return
     */
    private boolean canLoadTopTitleView() {
        return true;
    }

    /**
     * 是否显示title
     *
     * @param isShow true: 显示、flase: 隐藏
     */
    protected void setShowTitle(boolean isShow) {
        mBaseBinding.titleView.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mBaseBinding.viewV.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题栏标题
     */
    protected void setTopTitle(String title) {
        mBaseBinding.titleView.setMidTitle(title);
    }

    /**
     * 添加要显示的View
     */
    private View addMainView() {
        mLoadBinding = DataBindingUtil.inflate(getLayoutInflater(), getLayoutId(), null, false);
        return mLoadBinding.getRoot();
    }

    /**
     * 标题返回点击事件监听
     */
    public void onTopTitleLeftClickListener() {
        finish();
    }

    /**
     * 标题右侧点击事件监听
     */
    public void onTopTitleRightClickListener() {

    }

    /**
     * 设置title背景信息
     */
    protected void setTitleBg() {
        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.title_bg));
        mBaseBinding.titleView.setLeftTitleColor(R.color.text_three);
        mBaseBinding.titleView.setRightTitleColor(R.color.text_three);
        mBaseBinding.titleView.setMidTitleColor(R.color.text_three);
        mBaseBinding.titleView.setLeftImg(R.mipmap.back_black);
//        mBaseBinding.titleView.setLeftTitle(getString(R.string.back));
    }

    /**
     * 网络请求的错误信息，已在请求中处理提示Toast
     * 如果有特殊处理需重写
     *
     * @param action 区分不同事件
     * @param code   错误码
     * @param msg    错误信息
     */
    @Override
    public void onError(String action, int code, String msg) {
        LogUtil.e(TAG, "url=" + action + ";  code=" + code + ";  msg=" + msg);
        if (code == ExceptionEngine.CONNECT_ERROR) {//网络连接失败
            mBaseBinding.contentView.setShowText(msg);
            mBaseBinding.contentView.setShowImage(R.mipmap.ic_launcher);
        }
    }

    /**
     * 成功返回结果
     *
     * @param action 区分不同事件
     * @param data   数据
     */
    @Override
    public void onSuccess(String action, T data) {
        LogUtil.e(TAG, "url=" + action + ";  data=" + data);
        mBaseBinding.contentView.hindEmptyAll();
    }

    /**
     * 成功返回结果
     *
     * @param action 区分不同事件
     * @param data   数据
     */
    @Override
    public void onSuccess(String action, List<T> data) {
        LogUtil.e(TAG, "url=" + action + ";  data=" + data);
        mBaseBinding.contentView.hindEmptyAll();
    }

    /**
     * 错误布局，显示信息点击监听
     */
    private void onEmptyTextClickListeners() {
        LogUtil.e(TAG, "错误布局，显示信息点击监听");
        if (!NetUtils.isConnected(mContext)) {//网络连接失败
            ToastUtil.makeCenterToast(mContext, getString(R.string.no_netwoek));
        } else
            onEmptyTextClickListener();
    }

    /**
     * 错误布局，显示信息点击监听
     */
    @Override
    public void onEmptyTextClickListener() {

    }

    @Override
    public void initPermissionSuccess() {
        ToastUtil.makeCenterToast(mContext,"权限申请成功");
        LogUtil.e(TAG, "权限申请成功");
    }

    /**
     * 初始化刷新相关
     *
     * @param refreshLayout
     * @param recyclerView
     * @param limit         为0时默认是10条
     * @return
     */
    protected RefreshHelper initRefreshHelper(View refreshLayout, RecyclerView recyclerView, int limit) {
        RefreshHelper helper = new RefreshHelper<>(this, refreshLayout, recyclerView).init(limit);
        if (Tools.isNullOrZeroSize(listRefreshHelper)) listRefreshHelper = new ArrayList<>();
        listRefreshHelper.add(helper);
        return helper;
    }

    /**
     * 初始化刷新相关
     *
     * @param refreshLayout
     * @param recyclerView
     * @return
     */
    protected RefreshHelper initRefreshHelper(View refreshLayout, RecyclerView recyclerView) {
        return initRefreshHelper(refreshLayout, recyclerView, 0);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fram_img_back:
                onTopTitleLeftClickListener();
                break;
            case R.id.fllayout_right:
                onTopTitleRightClickListener();
                break;
            case R.id.tv_empty:
                onEmptyTextClickListeners();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Tools.isNotNullOrZeroSize(listRefreshHelper)) {
            for (RefreshHelper helper : listRefreshHelper) {
                if (helper != null)
                    helper.onDestroy();
            }
        }
    }
}
