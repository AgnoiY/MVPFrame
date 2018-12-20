package com.mvpframe.capabilities.http.observer;

import android.content.Context;

import com.mvpframe.bridge.http.RetrofitHttp;
import com.mvpframe.capabilities.http.exception.ApiException;
import com.mvpframe.capabilities.http.exception.ExceptionEngine;
import com.mvpframe.capabilities.http.interfaces.CallBack;
import com.mvpframe.capabilities.http.utils.ThreadUtils;

import io.reactivex.annotations.NonNull;

/**
 * Http请求基础回调接口
 * 备注:处理基本逻辑
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BaseBackObserver<T> extends BaseObserver<T> implements CallBack<T> {

    public BaseBackObserver() {
    }

    public BaseBackObserver(Context context, boolean isDialog, boolean isCabcelble) {
        super(context, isDialog, isCabcelble);
    }

    @Override
    public void onNext(@NonNull T value) {
        super.onNext(value);
        inSuccess(getTag(), value);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            inError(getTag(), exception.getCode(), exception.getMsg());
        } else {
            inError(getTag(), ExceptionEngine.UN_KNOWN_ERROR, "未知错误");
        }
    }

    @Override
    public void onCanceled() {
        onCanceledLogic();
    }

    /**
     * Http被取消回调处理逻辑
     */
    private void onCanceledLogic() {
        if (!ThreadUtils.isMainThread()) {
            RetrofitHttp.Configure.get().getHandler().post(() -> {
                inCancel();
            });
        } else {
            inCancel();
        }
    }

}
