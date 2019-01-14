package com.mvpframe.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.BridgeLifeCycleSetKeeper;
import com.mvpframe.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.mvpframe.constant.Constants.isDebug;

/**
 * <应用初始化> <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class MyApplication extends Application {
    /**
     * app实例
     */
    private static MyApplication mApplication;
    /**
     * app实例
     */
    public static Context context;


    /**
     * 本地activity栈
     */
    public static List<Activity> activitys = new ArrayList<Activity>();

    /**
     * 当前avtivity名称
     */
    public static String currentActivityName = "";

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mApplication = this;
        context = getApplicationContext();
        BridgeFactory.init(this);
        BridgeLifeCycleSetKeeper.getInstance().initOnApplicationCreate(getApplicationContext());
        EventBus.builder().throwSubscriberException(isDebug).installDefaultEventBus();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        onDestory();
    }

    /**
     * 退出应用，清理内存
     */
    private void onDestory() {
        BridgeLifeCycleSetKeeper.getInstance().clearOnApplicationQuit();
        BridgeFactory.destroy();
        ToastUtil.destory();
    }


    /**
     * <添加> <功能详细描述>
     *
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void addActivity(Activity activity) {
        activitys.add(activity);
    }

    /**
     * <删除>
     * <功能详细描述>
     *
     * @param activity
     * @see [类、类#方法、类#成员]
     */
    public void deleteActivity(Activity activity) {
        if (activity != null) {
            activitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * finish全部Activity
     */
    public void clearAllAcitity() {
        for (Activity activity : activitys) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activitys.clear();
    }

    public static Context getContext() {
        return context;
    }

    public static MyApplication getApplication() {
        return mApplication;
    }
}
