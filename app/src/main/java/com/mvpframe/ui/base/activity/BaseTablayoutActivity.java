package com.mvpframe.ui.base.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.mvpframe.R;
import com.mvpframe.adapters.TablayoutAdapter;
import com.mvpframe.databinding.ActivityTabBinding;

import java.util.List;

/**
 * BaseTablayoutActivity
 * Data：2018/12/18
 *
 * @author yong
 */

public abstract class BaseTablayoutActivity<T> extends BaseLoadActivity<T, ActivityTabBinding> {

    /*Tablayout 适配器*/
    protected TablayoutAdapter tablayoutAdapter;

    private int tabMode = TabLayout.MODE_SCROLLABLE;//MODE_SCROLLABLE 可滑动 ，MODE_FIXED表示不可滑动

    @Override
    public int getLayoutId() {
        return R.layout.activity_tab;
    }

    protected void initViewPager() {

        tablayoutAdapter = new TablayoutAdapter(getSupportFragmentManager());

        List<Fragment> mFragments = getFragments();
        List<String> mTitles = getFragmentTitles();

        tablayoutAdapter.addFrag(mFragments, mTitles);

        mLoadBinding.viewpager.setAdapter(tablayoutAdapter);
        mLoadBinding.tablayout.setupWithViewPager(mLoadBinding.viewpager);//viewpager和tablayout关联
        mLoadBinding.viewpager.setOffscreenPageLimit(tablayoutAdapter.getCount());
        mLoadBinding.tablayout.setTabMode(tabMode);// 设置滑动模式
    }

    //获取要显示的fragment
    public abstract List<Fragment> getFragments();

    //获取要显示的title
    public abstract List<String> getFragmentTitles();

    public void setTabMode(int tabMode) {
        this.tabMode = tabMode;
        mLoadBinding.tablayout.setTabMode(tabMode);// 设置滑动模式
    }
}
