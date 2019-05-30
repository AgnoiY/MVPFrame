package com.mvpframe.ui.base.activity;

import android.content.Context;

import com.mvpframe.R;
import com.mvpframe.databinding.LayoutRecyclerRefreshBinding;
import com.mvpframe.view.recyclerview.RecyclerInterface;
import com.mvpframe.view.recyclerview.RefreshHelper;

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
    public int getLayoutId() {
        return R.layout.layout_recycler_refresh;
    }

    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper(int limit) {
        mRefreshHelper = initRefreshHelper(mLoadBinding.refreshLayout, mLoadBinding.rv, limit);
        mRefreshHelper.onDefaluteMRefresh();
    }


    @Override
    public void initNotify(Context context) {
        super.initNotify(context);
        initRefreshHelper(setLimit());
    }

    /**
     * 设置一页的加载数据量，为0时默认加载10个
     *
     * @return
     */
    public abstract int setLimit();
}
