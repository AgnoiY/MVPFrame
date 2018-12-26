package com.mvpframe.view.recyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;

import com.mvpframe.R;
import com.mvpframe.databinding.EmptyViewBinding;

/**
 * 刷新方法回调
 * Data：2018/12/18
 *
 * @author yong
 */

public abstract class BaseRefreshCallBack<T> implements RefreshInterface<T> {

    private EmptyViewBinding emptyViewBinding;

    private Activity context;

    public BaseRefreshCallBack(Activity context) {
        this.context = context;
    }

    @Override
    public View getEmptyView() {
        if (context == null) {
            return null;
        }
        emptyViewBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.empty_view, null, false);
        return emptyViewBinding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void showEmptyState(String errorMsg, int errorImg) {
        if (emptyViewBinding == null) {
            return;
        }
        if (TextUtils.isEmpty(errorMsg) && errorImg == 0) {
            emptyViewBinding.getRoot().setVisibility(View.GONE);
            return;
        }
        emptyViewBinding.getRoot().setVisibility(View.VISIBLE);
        emptyViewBinding.tv.setText(errorMsg);
        if (errorImg <= 0) {
            emptyViewBinding.img.setVisibility(View.GONE);
        } else {
            emptyViewBinding.img.setImageResource(errorImg);
            emptyViewBinding.img.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRefresh(int pageindex, int limit) {

    }

    @Override
    public void onLoadMore(int pageindex, int limit) {

    }

    @Override
    public void onDestroy() {
        context = null;
    }
}
