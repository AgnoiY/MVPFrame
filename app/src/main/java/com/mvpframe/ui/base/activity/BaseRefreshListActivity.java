package com.mvpframe.ui.base.activity;

import android.content.Context;

import com.mvpframe.R;
import com.mvpframe.databinding.LayoutRecyclerRefreshBinding;
import com.mvpframe.view.recyclerView.RecyclerInterface;
import com.mvpframe.view.recyclerView.RefreshHelper;

/**
 * 公用刷新
 * Data：2018/12/18
 *
 * @author yong
 */

public abstract class BaseRefreshListActivity<T> extends BaseLoadActivity<T, LayoutRecyclerRefreshBinding>
        implements RecyclerInterface<T> {

    protected RefreshHelper mRefreshHelper;

    @Override
    public int getLayout() {
        return R.layout.layout_recycler_refresh;
    }

    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper(int limit) {
        mRefreshHelper = new RefreshHelper<>(this, mLoadBinding.refreshLayout, mLoadBinding.rv).init(limit);
        mRefreshHelper.onDefaluteMRefresh();
    }


    @Override
    protected void initNotify(Context context) {
        super.initNotify(context);
        initRefreshHelper(setLimit());
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
