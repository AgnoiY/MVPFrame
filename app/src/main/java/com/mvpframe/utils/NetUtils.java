package com.mvpframe.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.mvpframe.view.dialog.LoadingDialog;

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
            if (null != info && info.isConnected() && info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
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
     * 打开移动数据设置界面
     *
     * @param context
     */
    public static void openSetting(Context context) {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT > 10) {
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
     * 打开手机的WIFI
     *
     * @param context
     * @return
     */
    public static boolean openWifi(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.showDialog();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        while (true)
            if (isWifi(context)) {
                loadingDialog.closeDialog();
                return wifiManager.isWifiEnabled();
            }
    }

    /**
     * 打开手机的移动数据
     *
     * @param context
     * @return
     */

    public static boolean openMobileData(Context context) {

        if (Build.VERSION.SDK_INT >= 21)
            openSetting(context);
        else {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            Method setMobileDataEnable;

            boolean isDataEnabled = false;

            try {
                setMobileDataEnable = mConnectivityManager.getClass().getMethod("setMobileDataEnabled", boolean.class);
                isDataEnabled = (boolean) setMobileDataEnable.invoke(mConnectivityManager, true);
                checkConnectState(context);
                return isDataEnabled;
            } catch (Exception e) {
                LogUtils.w("移动数据设置错误: ", e);
            }
            return isDataEnabled;
        }
        return false;
    }

    /**
     * 判断当前网络是否是移动数据网络.
     *
     * @param context the context
     * @return boolean
     */

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
            LogUtils.d("移动数据开启状态：" + isDataEnabled);
            return isDataEnabled;
        } catch (Exception e) {
            LogUtils.w("移动数据设置错误: ", e);
        }
        return isDataEnabled;
    }

}
