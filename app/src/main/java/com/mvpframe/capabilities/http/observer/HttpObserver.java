package com.mvpframe.capabilities.http.observer;

import android.content.Context;

import com.mvpframe.capabilities.http.exception.ExceptionEngine;
import com.mvpframe.capabilities.http.interfaces.ParseHelper;
import com.mvpframe.util.LogUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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
        } catch (Exception e) {
            callSuccess = false;
            e.printStackTrace();
            onError(getTag(), ExceptionEngine.ANALYTIC_CLIENT_DATA_ERROR, "解析数据出错");
        }
        return t;
    }


    @Override
    public void inSuccess(String action, T value) {
        T result = parse((String) value);
        if (callSuccess && isBusinessOk()) {
            if (result instanceof List) {
                onSuccess(action, (List<T>) result);
            } else {
                onSuccess(action, result);
            }
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

    @Deprecated
    protected Class<T> getTypeClass() {
        /**
         * 获取当前类泛型(暂时保留)
         */
        ParameterizedType ptClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> mClass = null;
        if (ptClass != null) {
            Type type = ptClass.getActualTypeArguments()[0];
            mClass = (Class<T>) type;
            LogUtil.e("当前类泛型:" + mClass);
        }
        return mClass;
    }

}
