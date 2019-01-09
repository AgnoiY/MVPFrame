package com.mvpframe.util;

import android.util.Log;


/**
 * log工具类
 * Data：2018/12/18
 *
 * @author yong
 */
public class LogUtil {

    public static Boolean isDeBug = false;

    private static final String TAG = "LOG_YONG";

    public static void i(String msg) {
        if (isDeBug) {
            Log.i(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isDeBug) {
            Log.e(TAG, msg);
        }
    }

    public static void biglog(String responseInfo) {
        if (!isDeBug) {
            return;
        }
        if (responseInfo.length() > 3000) {
            int chunkCount = responseInfo.length() / 3000;
            for (int i = 0; i <= chunkCount; i++) {
                int max = 3000 * (i + 1);
                if (max >= responseInfo.length()) {
                    Log.e(TAG, responseInfo.substring(3000 * i));
                } else {
                    Log.e(TAG, responseInfo.substring(3000 * i, max));
                }
            }
        } else {
            Log.e(TAG, responseInfo);
        }
    }
}