package com.mvpframe.capabilities.http.observer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.mvpframe.R;
import com.mvpframe.application.App;
import com.mvpframe.bridge.sharepref.SharedPrefManager;
import com.mvpframe.capabilities.http.exception.ExceptionEngine;
import com.mvpframe.capabilities.http.interfaces.ParseHelper;
import com.mvpframe.ui.view.account.activity.LoginActivity;
import com.mvpframe.utils.ToastUtils;

import org.json.JSONException;

/**
 * Http请求回调
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class HttpObserver<T> extends BaseBackObserver<T> implements ParseHelper<T> {

    public HttpObserver() {
    }

    public HttpObserver(Context context, boolean isDialog, boolean isCabcelble) {
        super(context, isDialog, isCabcelble);
    }

    /**
     * 是否回调成功函数
     */
    private boolean callSuccess = true;

    @Override
    public T parse(String data) {
        T t = null;
        try {
            t = onConvert(data);
            callSuccess = true;
        } catch (JSONException e) {
            callSuccess = false;
            onError(getTag(), ExceptionEngine.ANALYTIC_CLIENT_DATA_ERROR, App.getAppString(R.string.data_parsing_error));
        }
        return t;
    }


    @Override
    public void inSuccess(String action, T value) {
        T result = parse((String) value);
        if (callSuccess && result != null) {
            onSuccess(action, result);
        }
    }

    @Override
    public void inError(String action, int code, String desc) {
        onError(action, code, desc);
    }

    @Override
    public void inCancel() {
        onCancel();
    }

    @Override
    public void isLoginToken() {
        SharedPrefManager.getUser().clear();
        Activity activity = App.getActivitys().get(App.getActivitys().size() - 1);
        App.clearAllAcitity();
        activity.startActivity(new Intent(activity, LoginActivity.class));
        ToastUtils.makeCenterToast(activity, App.getAppString(R.string.login_elsewhere));
    }
}
