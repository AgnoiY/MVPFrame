package com.mvpframe.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.BridgeLifeCycleSetKeeper;
import com.mvpframe.ui.LoadResActivity;
import com.mvpframe.util.LogUtil;
import com.mvpframe.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

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

    public static final String KEY_DEX2_SHA1 = "dex2-SHA1-Digest";
    public static final String WELCOME = "welcome";

    @Override
    public void onCreate() {
        super.onCreate();
        if (quickStart()) {
            return;
        }
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

    /**
     * dex文件太大，5.0以下不能运行问题
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LogUtil.e("App attachBaseContext ");
        if (!quickStart() && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//>=5.0的系统默认对dex进行oat优化
            if (needWait(base)) {
                waitForDexopt(base);
            }
            MultiDex.install(this);
        } else {
            return;
        }
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
     * @param o
     * @see [类、类#方法、类#成员]
     */
    public void deleteActivity(Object o) {
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
                } catch (InstantiationException e) {
                    LogUtil.e(e.getMessage());
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    LogUtil.e(e.getMessage());
                    e.printStackTrace();
                }
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

    public boolean quickStart() {
        if (getCurProcessName(this).contains(":mini")) {
            LogUtil.e(":mini start!");
            return true;
        }
        return false;
    }

    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    /**
     * 是否需要等待dexopt
     *
     * @param context
     * @return
     */
    public boolean needWait(Context context) {
        String flag = get2thDexSHA1(context);
        LogUtil.e("dex2-sha1=" + flag);
        SharedPreferences sp = context.getSharedPreferences(
                getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        String saveValue = sp.getString(KEY_DEX2_SHA1, "");
        return !flag.equals(saveValue);
    }

    public PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(e.getLocalizedMessage());
        }
        return new PackageInfo();
    }

    /**
     * 获取classes.dex文件签名
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void waitForDexopt(Context base) {
        Intent intent = new Intent();
        ComponentName componentName = new
                ComponentName("com.mvpframe", LoadResActivity.class.getName());
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        base.startActivity(intent);
        long startWait = System.currentTimeMillis();
        long waitTime = 10 * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            waitTime = 20 * 1000;//实测发现某些场景下有些2.3版本有可能10s都不能完成optdex
        }
        while (needWait(base)) {
            try {
                long nowWait = System.currentTimeMillis() - startWait;
                LogUtil.e("wait ms :" + nowWait);
                if (nowWait >= waitTime) {
                    LogUtil.e("加载完成");
                    return;
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * optDex finish
     *
     * @param context
     */
    public void installFinish(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context)).commit();
    }


    /**
     * get finish WelcomeActivity
     *
     * @param context
     * @return
     */
    public int needFinishWelCome(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        return sp.getInt(WELCOME, 0);

    }

    /**
     * put finish WelcomeActivity
     *
     * @param context
     * @param isFinish 0:等待状态、1:点击进入应用结束、2:返回键结束
     *
     */
    public void installFinishWelCome(Context context, int isFinish) {
        SharedPreferences sp = context.getSharedPreferences(
                getPackageInfo(context).versionName, MODE_MULTI_PROCESS);
        sp.edit().putInt(WELCOME, isFinish).commit();
    }

}
