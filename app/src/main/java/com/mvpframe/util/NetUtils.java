package com.mvpframe.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <跟网络相关的工具类>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class NetUtils {
    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取网络状态
     *
     * @param context
     * @return
     */
    public static int getNetworkState(Context context) {
        if (!isConnected(context)) {
            return -1;
        } else {
            if (!isWifi(context)) {
                return ConnectivityManager.TYPE_MOBILE;
            } else {
                return ConnectivityManager.TYPE_WIFI;
            }
        }
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return false;
        }
        return activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 判断当前网络是否是移动数据网络.
     *
     * @param context the context
     * @return boolean
     */
    public static boolean isMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Context context) {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT > 10) {
//            intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

    /**
     * 设置手机的移动数据
     */

    public static void setDataEnable(Context context) {

        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Object object = Build.VERSION.SDK_INT >= 21 ? mTelephonyManager : mConnectivityManager;
        String methodName = Build.VERSION.SDK_INT >= 21 ? "setDataEnabled" : "setMobileDataEnabled";
        Method setMobileDataEnable;

        try {
            setMobileDataEnable = object.getClass().getMethod(methodName, boolean.class);
            setMobileDataEnable.invoke(object, true);
            checkConnectState(context);
        } catch (InvocationTargetException e) {
            LogUtil.e("此处接收被调用方法内部未被捕获的异常");
            Throwable t = e.getTargetException();// 获取目标异常
            LogUtil.e("移动数据设置错误1: " + e.toString());
            t.printStackTrace();
            LogUtil.e("移动数据设置错误2: " + t.toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("移动数据设置错误: " + e.toString());
        }

    }

    public static Boolean checkConnectState(Context context) {

        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Object object = Build.VERSION.SDK_INT >= 21 ? mTelephonyManager : mConnectivityManager;
        String methodName = Build.VERSION.SDK_INT >= 21 ? "getDataEnabled" : "getMobileDataEnabled";
        Method getMobileDataEnable;
        boolean isDataEnabled = false;
        try {
            getMobileDataEnable = object.getClass().getMethod(methodName);
            isDataEnabled = (Boolean) getMobileDataEnable.invoke(object);
            LogUtil.e("移动数据开启状态：" + isDataEnabled);
            return isDataEnabled;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("移动数据设置错误: " + e.toString());
        }
        return isDataEnabled;
    }

}
