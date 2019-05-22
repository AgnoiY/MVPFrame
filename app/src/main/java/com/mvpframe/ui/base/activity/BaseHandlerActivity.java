package com.mvpframe.ui.base.activity;

import android.databinding.ViewDataBinding;
import android.os.Handler;
import android.os.Message;

import com.mvpframe.ui.base.fragment.BaseLazyFragment;
import com.mvpframe.util.Tools;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.mvpframe.constant.Constants.logd;

/**
 * Handler
 * Data：2019/04/23
 *
 * @author yong
 */
public abstract class BaseHandlerActivity<T, B extends ViewDataBinding> extends BaseLoadActivity<T, B> {

    private static List<BaseHandler> mHandlerList;

    public static BaseHandler createHandler(BaseHandlerActivity wAcitivity) {
        return add(new BaseHandler(wAcitivity));
    }

    public static BaseHandler createHandler(BaseLazyFragment wFragment) {
        return add(new BaseHandler(wFragment));
    }

    private static BaseHandler add(BaseHandler mHandler) {
        if (mHandlerList == null) {
            mHandlerList = new ArrayList<>();
        } else if (mHandler != null) {
            mHandlerList.add(mHandler);
        }
        return mHandler;
    }

    public static class BaseHandler extends Handler {
        private WeakReference<BaseHandlerActivity> wAcitivity;
        private WeakReference<BaseLazyFragment> wFragment;

        public BaseHandler(BaseHandlerActivity wAcitivity) {
            this.wAcitivity = new WeakReference<>(wAcitivity);
        }

        public BaseHandler(BaseLazyFragment wFragment) {
            this.wFragment = new WeakReference<>(wFragment);
        }

        public BaseHandlerActivity getRefAcitivity() {
            return wAcitivity != null ? wAcitivity.get() : null;
        }

        public BaseLazyFragment getRefFragment() {
            return wFragment != null ? wFragment.get() : null;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseHandlerActivity mActivity = getRefAcitivity();
            BaseLazyFragment mFragment = getRefFragment();
            if (mActivity != null && !mActivity.isFinishing()) {
                mActivity.handleMessage(msg, mActivity);
            }
            if (mFragment != null && !mFragment.isRemoving()) {
                mFragment.handleMessage(msg, mFragment);
            }
        }
    }

    @Override
    public void handleMessage(Message msg, Object tag) {
        log("BaseHandler:" + tag, logd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearHandler();
    }

    /**
     * 移除Handler防止内存泄露
     */
    private void clearHandler() {
        if (Tools.isNotNullOrZeroSize(mHandlerList)) {
            for (int i = 0; i < mHandlerList.size(); i++) {
                if (mHandlerList.get(i) != null) {
                    mHandlerList.get(i).removeCallbacksAndMessages(null);
                }
                if (i == mHandlerList.size() - 1) {
                    mHandlerList.clear();
                }
            }
        }
    }
}
