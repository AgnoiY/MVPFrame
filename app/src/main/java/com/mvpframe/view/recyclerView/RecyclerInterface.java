package com.mvpframe.view.recyclerView;

import android.support.v7.widget.RecyclerView;

import java.util.List;
/**
 * 公用刷新
 * Data：2018/12/18
 *
 * @author yong
 */
public interface RecyclerInterface<T> {

    RecyclerView.Adapter getListAdapter(List<T> listData);

    void getDataRequest(int pageindex, int limit);

}
