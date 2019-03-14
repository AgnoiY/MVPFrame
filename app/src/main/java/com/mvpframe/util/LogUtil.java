package com.mvpframe.util;

import android.util.Log;

import static com.mvpframe.constant.Constants.isDebug;


/**
 * log工具类
 * Data：2018/12/18
 *
 * @author yong
 */
public class LogUtil {

    private static final String TAG = "log_yong";

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, Object msg) {
        d(tag + ":  " + msg);
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, Object msg) {
        i(tag + ":  " + msg);
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, Object msg) {
        e(tag + ":  " + msg);
    }

    public static void biglog(String responseInfo) {
        if (!isDebug) {
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