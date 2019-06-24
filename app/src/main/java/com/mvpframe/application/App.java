package com.mvpframe.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;

import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.BridgeLifeCycleSetKeeper;
import com.mvpframe.utils.LogUtils;
import com.mvpframe.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.mvpframe.constant.Constants.IS_DEBUG;

/**
 * <应用初始化> <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class App extends Application {
    /**
     * app实例
     */
    private static App mApplication;

    /**
     * 本地Activity栈
     */
    private static List<Activity> activitys;

    /**
     * 当前Avtivity名称
     */
    private static String currentActivityName;

    /**
     * dex文件太大不能运行问题
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        setApp(this);
        BridgeFactory.init(this);
        BridgeLifeCycleSetKeeper.getInstance().initOnApplicationCreate(getApplicationContext());
        EventBus.builder().throwSubscriberException(IS_DEBUG).installDefaultEventBus();
    }

    private static void setApp(App mApp) {
        mApplication = mApp;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        onDestory();
    }

    /**
     * 退出应用，清理内存
     */
    private static void onDestory() {
        BridgeLifeCycleSetKeeper.getInstance().clearOnApplicationQuit();
        BridgeFactory.destroy();
        ToastUtils.destory();
        if (activitys != null) {
            activitys.clear();
            activitys = null;
        }
    }


    /**
     * <添加> <功能详细描述>
     *
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public static void addActivity(Activity activity) {
        if (activitys == null) {
            activitys = new ArrayList<>();
        }
        activitys.add(activity);
    }

    /**
     * <删除>
     * <功能详细描述>
     *
     * @param o
     * @see [类、类#方法、类#成员]
     */
    public static void deleteActivity(Object o) {
        if (o != null)
            if (o instanceof Activity) {
                Activity activity = ((Activity) o);
                activitys.remove(activity);
                activity.finish();
            } else if (o instanceof Class) {
                try {
                    Activity activity = (Activity) ((Class) o).newInstance();
                    activitys.remove(activity);
                    activity.finish();
                } catch (Exception e) {
                    LogUtils.w(e);
                }
            }
    }

    /**
     * Finish全部Activity
     */
    public static void clearAllAcitity() {

        if (activitys == null) return;

        for (Activity activity : activitys) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activitys.clear();
    }

    public static String getAppString(@StringRes int resId) {
        return mApplication.getString(resId);
    }

    public static App getApplication() {
        return mApplication;
    }

    public static List<Activity> getActivitys() {
        return activitys;
    }

    public static String getCurrentActivityName() {
        return currentActivityName;
    }

    public static void setCurrentActivityName(String currentActivityName) {
        App.currentActivityName = currentActivityName;
    }
}
