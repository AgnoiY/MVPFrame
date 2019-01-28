package com.mvpframe.ui.base.fragment;

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

public abstract class BaseTablayoutFragment<T> extends BaseLazyFragment<T, ActivityTabBinding> {

    /*Tablayout 适配器*/
    protected TablayoutAdapter tablayoutAdapter;

    private int tabMode = TabLayout.MODE_SCROLLABLE;//TabLayout.MODE_SCROLLABLE 可滑动 ，TabLayout.MODE_FIXED表示不可滑动

    @Override
    public int getLayoutId() {
        return R.layout.activity_tab;
    }

    protected void initViewPager() {

        tablayoutAdapter = new TablayoutAdapter(getFragmentManager());

        List<Fragment> mFragments = getFragments();
        List<String> mTitles = getFragmentTitles();

        tablayoutAdapter.addFrag(mFragments, mTitles);

        mLazyBinding.viewpager.setOffscreenPageLimit(mFragments.size()-1);
        mLazyBinding.viewpager.setAdapter(tablayoutAdapter);
        mLazyBinding.tablayout.setupWithViewPager(mLazyBinding.viewpager);//viewpager和tablayout关联
        mLazyBinding.viewpager.setOffscreenPageLimit(tablayoutAdapter.getCount());
        mLazyBinding.tablayout.setTabMode(tabMode);// 设置滑动模式
    }

    //获取要显示的fragment
    public abstract List<Fragment> getFragments();

    //获取要显示的title
    public abstract List<String> getFragmentTitles();

    public void setTabMode(int tabMode) {
        this.tabMode = tabMode;
        mLazyBinding.tablayout.setTabMode(tabMode);// 设置滑动模式
    }
}
