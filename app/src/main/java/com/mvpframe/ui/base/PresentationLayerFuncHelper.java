package com.mvpframe.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mvpframe.bean.event.BaseEventModel;
import com.mvpframe.ui.base.activity.BaseActivity;
import com.mvpframe.ui.base.interfaces.PresentationLayerFunc;
import com.mvpframe.ui.base.interfaces.PublishActivityCallBack;
import com.mvpframe.util.LogUtil;
import com.mvpframe.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * <页面基础公共功能实现>
 * Data：2018/12/18
 *
 * @author yong
 */
public class PresentationLayerFuncHelper<T> implements PresentationLayerFunc<T>, PublishActivityCallBack {

    private Context context;
    private Activity activity;
    private String TAG;
    private CompositeDisposable disposable;


    public PresentationLayerFuncHelper(Object o, CompositeDisposable disposable) {
        SoftReference sofr = new SoftReference(o);
        this.context = (Context) sofr.get();
        this.activity = (Activity) sofr.get();
        this.TAG = sofr.get().getClass().getSimpleName();
        this.disposable = disposable;
    }

    /**
     * Toast提示
     *
     * @param msg
     */
    @Override
    public void showToast(String msg) {
        ToastUtil.makeCenterToast(context, msg);
    }

    /**
     * 显示软键盘
     *
     * @param focusView
     */
    @Override
    public void showSoftKeyboard(View focusView) {
        activity.getWindow().getDecorView().postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                focusView.requestFocus();
                imm.showSoftInput(focusView, 0);
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
        View view = activity.getWindow().peekDecorView();
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
            for (Object nameClass : eventModel.getList()) {
                if (TAG.equals(nameClass))
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
     * 延迟操作
     *
     * @param delay
     */
    @Override
    public void postDelayed(Object... delay) {
        if (delay.length > 0) {
            disposable.add(Observable.timer((long) delay[0], TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        ((BaseActivity) activity).nextStep(aLong, delay);
                    }, throwable -> {
                        LogUtil.w(TAG, throwable);
                    }));
        } else {
            log("延迟的时间为Null");
        }
    }

    /**
     * 打印日志
     *
     * @param msg 0: 信息  1: 日志的样式 e/d/i
     */
    @Override
    public void log(Object... msg) {
        if (msg.length > 1) {
            switch ((String) msg[1]) {
                case "e":
                    LogUtil.e(TAG, msg[0]);
                    break;
                case "i":
                    LogUtil.i(TAG, msg[0]);
                    break;
                case "d":
                    LogUtil.d(TAG, msg[0]);
                    break;
                case "w":
                    if (msg.length > 2)
                        LogUtil.w(TAG, msg[2], (Throwable) msg[0]);
                    else
                        LogUtil.w(TAG, (Throwable) msg[0]);
                    break;
                default:
                    LogUtil.d(TAG, msg[0]);
                    break;
            }
        } else if (msg.length == 1) {
            LogUtil.d(TAG, msg[0]);
        }

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

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        Intent intent = new Intent(activity, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(activity, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void setResultOk(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) ;
        intent.putExtras(bundle);
        activity.setResult(activity.RESULT_OK, intent);
        activity.finish();
    }
}
