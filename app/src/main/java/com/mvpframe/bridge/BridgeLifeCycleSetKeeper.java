
package com.mvpframe.bridge;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 如果Bridge模块需要有初始化和销毁操作，且生命周期和Application一致，则将其生命周期方法交由此类管理，
 * Application会统一调度此类中保管的所有Bridge生命周期
 * Data：2018/12/18
 *
 * @author yong
 */
public class BridgeLifeCycleSetKeeper implements BridgeLifeCycleListener {
    private static BridgeLifeCycleSetKeeper instance;

    private BridgeLifeCycleSetKeeper() {
        mBridgeLiftCycleListenerSet = Collections
                .synchronizedList(new ArrayList<BridgeLifeCycleListener>());
    }

    private List<BridgeLifeCycleListener> mBridgeLiftCycleListenerSet;

    public static BridgeLifeCycleSetKeeper getInstance() {
        if (instance == null) {
            instance = new BridgeLifeCycleSetKeeper();
        }
        return instance;
    }

    /**
     * 托管Bridge生命周期给Application管理
     *
     * @param listener
     */
    public void trustBridgeLifeCycle(BridgeLifeCycleListener listener) {
        if (mBridgeLiftCycleListenerSet != null && !mBridgeLiftCycleListenerSet.contains(listener)) {
            mBridgeLiftCycleListenerSet.add(listener);
        }
    }

    @Override
    public void initOnApplicationCreate(Context context) {
        for (Iterator<BridgeLifeCycleListener> ite = mBridgeLiftCycleListenerSet.iterator(); ite
                .hasNext(); ) {
            BridgeLifeCycleListener listener = ite.next();
            listener.initOnApplicationCreate(context);
        }
    }

    @Override
    public void clearOnApplicationQuit() {
        if (mBridgeLiftCycleListenerSet != null) {
            for (Iterator<BridgeLifeCycleListener> ite = mBridgeLiftCycleListenerSet.iterator(); ite
                    .hasNext(); ) {
                BridgeLifeCycleListener listener = ite.next();
                listener.clearOnApplicationQuit();
                ite.remove();
            }
        }
    }
}
