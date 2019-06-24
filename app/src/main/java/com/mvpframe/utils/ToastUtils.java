package com.mvpframe.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mvpframe.R;
import com.mvpframe.application.App;

/**
 * <提示公共类>
 * Data：2018/12/18
 *
 * @author yong
 */
public final class ToastUtils {

    private static Toast toast;

    ToastUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * <显示toast提示>
     *
     * @param context
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public static void makeTextShort(Context context, String msg) {
        if (App.getCurrentActivityName().equals(context.getClass().getName())) {
            if (toast == null) {
                toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            }
            toast.setText(msg);
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
        if (App.getCurrentActivityName().equals(context.getClass().getName())) {
            if (toast == null) {
                toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
            }
            toast.setText(msg);
            toast.show();
        }
    }

    /**
     * <中间显示toast提示 >
     *
     * @param context
     * @see [类、类#方法、类#成员]
     */
    public static void makeCenterToast(Context context, String msg) {
        if (App.getCurrentActivityName().equals(context.getClass().getName())) {
            if (toast == null) {
                toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout layout = (LinearLayout) toast.getView();
                layout.setBackgroundResource(R.drawable.toast_bg);
            }
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(msg);
            toast.show();
        }
    }

    /**
     * 清空Toast
     */
    public static void destory() {
        if (toast != null)
            toast.cancel();
        toast = null;
    }
}
