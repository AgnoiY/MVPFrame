package com.mvpframe.ui.base.activity;


import android.annotation.SuppressLint;
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
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.interfaces.LoadCreateClickListener;
import com.mvpframe.utils.GeneralUtils;
import com.mvpframe.utils.HandlerUtils;
import com.mvpframe.utils.NetUtils;
import com.mvpframe.utils.ToastUtils;
import com.mvpframe.utils.ToolsUtils;
import com.mvpframe.statusbar.StatusBarUtils;
import com.mvpframe.view.recyclerview.RefreshHelper;

import java.util.ArrayList;
import java.util.List;

import static com.mvpframe.constant.Constants.LOG_D;
import static com.mvpframe.constant.Constants.LOG_E;
import static com.mvpframe.constant.Constants.LOG_I;

/**
 * 带空页面，错误页面显示的BaseActivity 通过BaseActivity界面操作封装成View而来
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseLoadActivity<T, B extends ViewDataBinding>
        extends BaseActivity<T, IMvpView<T>, BasePresenter<IMvpView<T>>>
        implements LoadCreateClickListener {

    protected ActivityBaseLoadBinding mBaseBinding;

    protected B mLoadBinding;

    private List<RefreshHelper> listRefreshHelper;

    protected HandlerUtils mHandlers;

    /**
     * 布局文件xml的resId,无需添加标题栏、加载、错误及空页面
     */
    @Override
    public void setContentView(Bundle savedInstanceState) {
        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_load);

        mBaseBinding.contentView.addContentView(addMainView());

        initTitleView();

        initNotify();
    }

    public void initNotify() {

    }

    /**
     * 设置状态栏风格
     */
    @Override
    public void initStatusBarDarkTheme() {

        //当FitsSystemWindows设置true时，会在屏幕最上方预留出状态栏高度的padding
        StatusBarUtils.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtils.setTranslucentStatus(this);

        setStatusBarDarkTheme(true);
    }

    /**
     * 初始化标题View
     */
    private void initTitleView() {
        mBaseBinding.titleView.setVisibility(View.VISIBLE);
        mBaseBinding.viewV.setVisibility(View.VISIBLE);

        mBaseBinding.titleView.setLeftFraClickListener(this);
        mBaseBinding.titleView.setRightFraClickListener(this);

        setTitleBg();

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
        params.height = StatusBarUtils.getStatusBarHeight(this);
        mBaseBinding.statusBar.setBackgroundColor(mBaseBinding.titleView.getBackgroundColor());
        mBaseBinding.statusBar.setLayoutParams(params);

        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //设置状态使用深色文字图标风格
        if (!StatusBarUtils.setStatusBarDarkTheme(this, dark) &&
                mBaseBinding.titleView.getBackgroundColor() == ContextCompat.getColor(mContext, R.color.white) &&
                mBaseBinding.titleView.getVisibility() == View.VISIBLE) {
            //设置一个状态栏颜色为半透明,
            StatusBarUtils.setStatusBarColor(this, 0x50000000);
        }
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
        log("url=" + action + ";  code=" + code + ";  msg=" + msg, LOG_E);
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
        log("url=" + action + ";  data=" + data, LOG_I);
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
        log("url=" + action + ";  data=" + data, LOG_I);
        mBaseBinding.contentView.hindEmptyAll();
    }

    /**
     * 错误布局，显示信息点击监听
     */
    private void onEmptyTextClickListeners() {
        log("错误布局，显示信息点击监听", LOG_D);
        if (!NetUtils.isConnected(mContext)) {//网络连接失败
            ToastUtils.makeCenterToast(mContext, getString(R.string.no_netwoek));
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
        ToastUtils.makeCenterToast(mContext, "权限申请成功");
        log("权限申请成功", LOG_D);
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
        if (ToolsUtils.isNullOrZeroSize(listRefreshHelper)) listRefreshHelper = new ArrayList<>();
        listRefreshHelper.add(helper);
        return helper;
    }

    /**
     * 创建Handler
     *
     * @return
     */
    protected HandlerUtils initHandler() {
        mHandlers = new HandlerUtils(mActivity);
        return mHandlers;
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

    /**
     * 配合DataBinding点击事件监听
     * 添加防止重复点击
     * 有点击事件只需重写
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (GeneralUtils.isDoubleClick()) return;

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
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandlers != null) {
            mHandlers.clearHandler();
        }
        if (ToolsUtils.isNotNullOrZeroSize(listRefreshHelper)) {
            for (int i = 0; i < listRefreshHelper.size(); i++) {
                if (listRefreshHelper.get(i) != null) {
                    listRefreshHelper.get(i).onDestroy();
                }
                if (i == listRefreshHelper.size() - 1) {
                    listRefreshHelper.clear();
                }
            }
        }
    }
}
