package com.mvpframe.ui.base.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mvpframe.R;
import com.mvpframe.databinding.LayoutCommonRecyclerRefreshBinding;
import com.mvpframe.view.recyclerView.BaseRefreshCallBack;
import com.mvpframe.view.recyclerView.RecyclerInterface;
import com.mvpframe.view.recyclerView.RefreshHelper;

import java.util.List;

/**
 * 公用刷新
 * Data：2018/12/18
 *
 * @author yong
 */

public abstract class BaseRefreshListFragment<T> extends BaseLazyFragment<T, LayoutCommonRecyclerRefreshBinding>
        implements RecyclerInterface<T> {


    protected RefreshHelper mRefreshHelper;

    @Override
    protected int getLayout() {
        return R.layout.layout_common_recycler_refresh;
    }

    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper(int limit) {
        mRefreshHelper = new RefreshHelper<>(this, mLazyBinding.refreshLayout, mLazyBinding.rv).init(limit);
        mRefreshHelper.onDefaluteMRefresh();

    }

    @Override
    protected void lazyLoad() {
        initRefreshHelper(setLimit());
    }

    /**
     * 设置一页的加载数据量，默认加载15个
     *
     * @return
     */
    public abstract int setLimit();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRefreshHelper != null) {
            mRefreshHelper.onDestroy();
        }
    }
}
