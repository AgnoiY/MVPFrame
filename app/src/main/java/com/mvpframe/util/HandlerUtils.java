package com.mvpframe.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler
 * Data：2019/04/23
 *
 * @author yong
 */
public class HandlerUtils {

    private HandleMsgInterface mHandleInterface;
    private List<BaseHandler> mHandlerList;

    public HandlerUtils(Activity wAcitivity) {
        add(new BaseHandler(wAcitivity));
    }

    public HandlerUtils(Fragment wFragment) {
        add(new BaseHandler(wFragment));
    }

    private BaseHandler add(BaseHandler mHandler) {
        if (mHandlerList == null) {
            mHandlerList = new ArrayList<>();
        } else if (mHandler != null) {
            mHandlerList.add(mHandler);
        }
        return mHandler;
    }

    private class BaseHandler extends Handler {
        private WeakReference<Activity> wAcitivity;
        private WeakReference<Fragment> wFragment;

        public BaseHandler(Activity wAcitivity) {
            this.wAcitivity = new WeakReference<>(wAcitivity);
        }

        public BaseHandler(Fragment wFragment) {
            this.wFragment = new WeakReference<>(wFragment);
        }

        public Activity getRefAcitivity() {
            return wAcitivity != null ? wAcitivity.get() : null;
        }

        public Fragment getRefFragment() {
            return wFragment != null ? wFragment.get() : null;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Activity mActivity = getRefAcitivity();
            Fragment mFragment = getRefFragment();
            if (mActivity != null && !mActivity.isFinishing() && mHandleInterface != null) {
                mHandleInterface.handleMsg(msg, mActivity);
            }
            if (mFragment != null && !mFragment.isRemoving() && mHandleInterface != null) {
                mHandleInterface.handleMsg(msg, mFragment);
            }
        }
    }

    public interface HandleMsgInterface {
        void handleMsg(Message msg, Object tag);
    }

    public HandlerUtils setHandleInterface(HandleMsgInterface mHandleInterface) {
        this.mHandleInterface = mHandleInterface;
        return this;
    }

    /**
     * 移除Handler防止内存泄露
     */
    public void clearHandler() {
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
