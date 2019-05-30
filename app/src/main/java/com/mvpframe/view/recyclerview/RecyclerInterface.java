package com.mvpframe.view.recyclerview;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * 公用刷新
 * Data：2018/12/18
 *
 * @author yong
 */
public interface RecyclerInterface<T> {

    /**
     * 设置adapter
     *
     * @param tag      RecyclerView的ID+设置的Tag
     * @param listData 数据
     * @return
     */
    RecyclerView.Adapter getListAdapter(String tag, List<T> listData);

    /**
     * 添加数据源
     *
     * @param isRefresh 刷新时不加载弹窗
     * @param tag       RecyclerView的ID+设置的Tag
     * @param pageindex 页数
     * @param limit     每页数量
     */
    void getDataRequest(boolean isRefresh, String tag, int pageindex, int limit);

}
