package com.mvpframe.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mvpframe.ui.base.interfaces.PresentationLayerFunc;
import com.mvpframe.util.ToastUtil;

/**
 * <页面基础公共功能实现>
 * Data：2018/12/18
 *
 * @author yong
 */
public class PresentationLayerFuncHelper implements PresentationLayerFunc {

    private Context context;

    public PresentationLayerFuncHelper(Context context) {
        this.context = context;
    }

    /**
     * Toast提示
     *
     * @param msg
     */
    @Override
    public void showToast(String msg) {
        ToastUtil.makeTextShort(context, msg);
    }

    /**
     * 显示软键盘
     *
     * @param focusView
     */
    @Override
    public void showSoftKeyboard(View focusView) {
        ((Activity) context).getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    focusView.requestFocus();
                    imm.showSoftInput(focusView, 0);
                }
            }
        }, 100);
    }

    /**
     * 隐藏软键盘
     */
    @Override
    public void hideSoftKeyboard() {
        if ((context == null || ((Activity) context).getWindow() == null)) {
            return;
        }
        View view = ((Activity) context).getWindow().peekDecorView();
        if (view == null || view.getWindowToken() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    public void hideKeyboard(IBinder token) {
        if (token != null) {

            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (im != null) {

                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);

            }
        }
    }
}
