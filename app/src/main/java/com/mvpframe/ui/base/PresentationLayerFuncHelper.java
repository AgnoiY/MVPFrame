package com.mvpframe.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mvpframe.bean.event.BaseEventModel;
import com.mvpframe.ui.base.interfaces.PresentationLayerFunc;
import com.mvpframe.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * <页面基础公共功能实现>
 * Data：2018/12/18
 *
 * @author yong
 */
public class PresentationLayerFuncHelper<T> implements PresentationLayerFunc<T> {

    private Context context;
    private Activity activity;
    private String TAG;

    public PresentationLayerFuncHelper(Object o) {
        SoftReference sofr = new SoftReference(o);
        this.context = (Context) sofr.get();
        this.activity = (Activity) sofr.get();
        this.TAG = sofr.get().getClass().getSimpleName();
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
     * 处理事件线
     *
     * @param eventModel
     */
    @Subscribe
    @Override
    public void onEventMainThread(BaseEventModel eventModel) {
        if (eventModel.getList() != null)
            for (int i = 0; i < eventModel.getList().size(); i++) {
                if (TAG.equals(eventModel.getList().get(i)))
                    activity.finish();
            }
    }
    /**
     * 发送EventBus事件线
     *
     * @param o
     */
    @Override
    public void getEventBusPost(Object... o) {
        EventBus eventBus = EventBus.getDefault();
        BaseEventModel model = new BaseEventModel<T>();
        boolean isBaseEventModel = false;
        for (int i = 0; i < o.length; i++) {
            if (o[i] instanceof BaseEventModel) {
                model.setList(((BaseEventModel) o[i]).getList());
                model.setModel(((BaseEventModel) o[i]).getModel());
                isBaseEventModel = true;
            } else if (o[i] instanceof Class) {
                model.add((Class) o[i]);
                isBaseEventModel = true;
            } else if (o[i] instanceof List) {
                model.setList((List<String>) o[i]);
                isBaseEventModel = true;
            } else
                eventBus.post(o);
        }
        if (isBaseEventModel)
            eventBus.post(model);
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
