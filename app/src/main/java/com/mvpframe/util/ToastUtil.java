package com.mvpframe.util;

import android.content.Context;
import android.widget.Toast;

import com.mvpframe.app.MyApplication;

/**
 * <提示公共类>
 * Data：2018/12/18
 *
 * @author yong
 */
public final class ToastUtil {
    private static Toast toast;

    /**
     * <显示toast提示>
     *
     * @param context
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextShort(Context context, String msg) {
        if (MyApplication.currentActivityName.equals(context.getClass().getName())) {
            if (toast == null) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            toast.show();
        }
    }

    /**
     * <显示toast提示>
     *
     * @param context
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextLong(Context context, String msg) {
        if (MyApplication.currentActivityName.equals(context.getClass().getName())) {
            if (toast == null) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            } else {
                toast.setText(msg);
            }
            toast.show();
        }
    }

    /**
     * <显示失败信息 >
     *
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextError(Context context, String msg) {
        makeTextShort(context, msg);
    }

    /**
     * <显示失败信息 >
     *
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextErrorLong(Context context, String msg) {
        makeTextLong(context, msg);
    }

    public static void destory() {
        toast = null;
    }
}
