package com.mvpframe.view.recyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mvpframe.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 刷新方法
 * Data：2018/12/18
 *
 * @author yong
 */
public class RefreshHelper<T> {

    public static int LIMITE = 15;

    private RefreshInterface mRefreshInterface;//刷新接口

    private BaseQuickAdapter mAdapter;//数据适配器

    private SmartRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager linearLayoutManager;//设置布局样式

    private int mPageIndex; //分页下标

    private int mLimit;//数据大小

    private List<T> mDataList;//数据

    private Context mContext;

    private View mEmptyView;

    public int getmPageIndex() {
        return mPageIndex;
    }

    public int getmLimit() {
        return mLimit;
    }

    public List<T> getmDataList() {
        return mDataList;
    }

    public BaseQuickAdapter getmAdapter() {
        return mAdapter;
    }

    public SmartRefreshLayout getmRefreshLayout() {
        return mRefreshLayout;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }


    public RefreshHelper(Context context, RefreshInterface mRefreshInterface) {
        this.mRefreshInterface = mRefreshInterface;
        this.mContext = context;
    }

    /**
     * 初始化
     *
     * @param limit 分页个数
     */
    public void init(int limit) {

        mPageIndex = 1;//分页从1开始

        if (limit <= 0) limit = LIMITE;

        mLimit = limit;//分页数量

        mDataList = new ArrayList<>();

        if (mRefreshInterface != null) {
            mRefreshLayout = (SmartRefreshLayout) mRefreshInterface.getRefreshLayout();
            mRecyclerView = mRefreshInterface.getRecyclerView();

            mAdapter = (BaseQuickAdapter) mRefreshInterface.getAdapter(mDataList);

            mEmptyView = mRefreshInterface.getEmptyView();
            if (linearLayoutManager == null) {
                linearLayoutManager = new LinearLayoutManager(mContext);
            }
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }

        if (mAdapter != null) {
            View tv = new View(mContext); //先设置 不显示任何东西的 emptyView
            mAdapter.setEmptyView(tv);
            mRecyclerView.setAdapter(mAdapter);
        }
        initRefreshLayout();
    }


    /**
     * 初始化刷新加载
     */
    private void initRefreshLayout() {

        if (mRefreshLayout == null) return;

        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);//不满一行启动上啦加载

        mRefreshLayout.setEnableAutoLoadmore(false);//禁用惯性

        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) { //刷新
                onMRefresh(1, mLimit);
                if (mRefreshInterface != null) {
                    mRefreshInterface.onRefresh(1, mLimit);
                }

            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {//加载
                if (mDataList.size() > 0) {
                    mPageIndex++;
                }
                onMLoadMore(mPageIndex, mLimit);
                if (mRefreshInterface != null) {
                    mRefreshInterface.onLoadMore(mPageIndex, mLimit);
                }
            }
        });
    }

    //执行默认刷新 mPageIndex变为一
    public void onDefaluteMRefresh() {
        mPageIndex = 1;
        if (mRefreshInterface != null) {
            mRefreshInterface.getListDataRequest(mPageIndex, mLimit);
        }
    }

    //执行默认刷新 mPageIndex++
    public void onDefaluteMLoadMore() {
        if (mDataList.size() > 0) {
            mPageIndex++;
        }
        if (mRefreshInterface != null) {
            mRefreshInterface.getListDataRequest(mPageIndex, mLimit);
        }
    }

    //刷新
    public void onMRefresh(int pageindex, int limit) {
        mPageIndex = pageindex;
        mLimit = limit;
        if (mRefreshInterface != null) {
            mRefreshInterface.getListDataRequest(pageindex, limit);
        }

    }

    //加载
    public void onMLoadMore(int pageIndex, int limit) {
        mPageIndex = pageIndex;
        mLimit = limit;
        if (mRefreshInterface != null) {
            mRefreshInterface.getListDataRequest(pageIndex, limit);
        }
    }

    /**
     * 设置加载数据 实现分页逻辑
     *
     * @param datas
     */
    public void setData(List<T> datas, String noData, int noDataImg) {
        if (mRefreshLayout != null) {
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.finishRefresh();
            }
            if (mRefreshLayout.isLoading()) {
                mRefreshLayout.finishLoadmore();
            }
        }

        if (mPageIndex == 1) {         //如果当前加载的是第一页数据
            if (datas != null && datas.size() > 0) {
                mDataList.clear();
                mDataList.addAll(datas);
            } else {
                mDataList.clear();
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        } else if (mPageIndex > 1) {
            if (datas == null || datas.size() <= 0) {
                mPageIndex--;
            } else {
                mDataList.addAll(datas);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        if (mEmptyView != null && mDataList.isEmpty()) {
            if (mRefreshInterface != null) {
                mRefreshInterface.showEmptyState(noData, noDataImg);
            }
            if (mAdapter != null) mAdapter.setEmptyView(mEmptyView);
        }
    }

    public void setData(List<T> datas, int noDataImg) {
        setData(datas, mContext.getString(R.string.noData), noDataImg);
    }

    /**
     * 防止内存泄漏
     */
    public void onDestroy() {
        if (mRefreshInterface != null) {
            mRefreshInterface.onDestroy();
        }
        mContext = null;
    }

    public RefreshHelper setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
        return this;
    }
}
