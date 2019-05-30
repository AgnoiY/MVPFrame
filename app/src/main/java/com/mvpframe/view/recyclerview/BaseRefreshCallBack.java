package com.mvpframe.view.recyclerview;

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

public abstract class BaseRefreshCallBack<T> implements RefreshInterface<T>, View.OnClickListener {

    private EmptyViewBinding emptyViewBinding;

    private Activity mActivity;
    private RefreshHelper mHelper;


    public BaseRefreshCallBack(RefreshHelper mHelper, Activity mActivity) {
        this.mHelper = mHelper;
        this.mActivity = mActivity;
    }

    @Override
    public View getEmptyView() {
        if (mActivity == null) {
            return null;
        }
        emptyViewBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.empty_view, null, false);
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
        if (TextUtils.isEmpty(errorMsg)) {
            emptyViewBinding.emptyTv.setVisibility(View.GONE);
        } else {
            emptyViewBinding.emptyTv.setText(errorMsg);
            emptyViewBinding.emptyTv.setVisibility(View.VISIBLE);
            emptyViewBinding.emptyTv.setOnClickListener(this);
        }
        if (errorImg <= 0) {
            emptyViewBinding.emptyImg.setVisibility(View.GONE);
        } else {
            emptyViewBinding.emptyImg.setImageResource(errorImg);
            emptyViewBinding.emptyImg.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.empty_tv) {
            getListDataRequest(false, mHelper.getTag(), 1, mHelper.getLimit());
        }
    }

    @Override
    public void onDestroy() {
        mActivity = null;
    }
}
