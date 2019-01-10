package com.mvpframe.view.recyclerView;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * 根据需求自己定义
 * Data：2018/12/18
 *
 * @author yong
 */
public interface RefreshInterface<T> {

    View getRefreshLayout();

    RecyclerView getRecyclerView();

    RecyclerView.Adapter getAdapter(String tag, List<T> listData);

    View getEmptyView();

    void showEmptyState(String errorMsg, @DrawableRes int errorImg);

    void onRefresh(int pageindex, int limit);

    void onLoadMore(int pageindex, int limit);

    void getListDataRequest(String tag, int pageindex, int limit);

    void onDestroy();

}
