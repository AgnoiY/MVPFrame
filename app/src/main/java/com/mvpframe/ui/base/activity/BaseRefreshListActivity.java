package com.mvpframe.ui.base.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mvpframe.R;
import com.mvpframe.databinding.LayoutCommonRecyclerRefreshBinding;
import com.mvpframe.view.recyclerView.BaseRefreshCallBack;
import com.mvpframe.view.recyclerView.RefreshHelper;

import java.util.List;

/**
 * 公用刷新
 * Data：2018/12/18
 *
 * @author yong
 */

public abstract class BaseRefreshListActivity<T> extends BaseLoadActivity<T, LayoutCommonRecyclerRefreshBinding> {

    protected RefreshHelper mRefreshHelper;

    @Override
    public int getLayout() {
        return R.layout.layout_common_recycler_refresh;
    }

    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper(int limit) {
        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack<T>(this) {
            @Override
            public View getRefreshLayout() {
                return mLoadBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mLoadBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List<T> listData) {
                return getListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit) {
                getListRequest(pageindex, limit);
            }
        });
        mRefreshHelper.init(limit);

    }


    abstract public RecyclerView.Adapter getListAdapter(List<T> listData);

    abstract public void getListRequest(int pageindex, int limit);

    @Override
    protected void initNotify(Context context) {
        super.initNotify(context);
        initRefreshHelper(setLimit());
        mRefreshHelper.onDefaluteMRefresh();
    }

    /**
     * 设置一页的加载数据量，默认加载15个
     *
     * @return
     */
    public abstract int setLimit();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRefreshHelper != null) {
            mRefreshHelper.onDestroy();
        }
    }
}
