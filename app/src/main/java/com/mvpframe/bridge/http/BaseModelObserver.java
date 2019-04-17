package com.mvpframe.bridge.http;

import android.content.Context;

import com.google.gson.Gson;
import com.mvpframe.R;
import com.mvpframe.app.App;
import com.mvpframe.bean.base.BaseResponseListModel;
import com.mvpframe.bean.base.BaseResponseModel;
import com.mvpframe.capabilities.http.observer.HttpObserver;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.util.LogUtil;
import com.mvpframe.view.dialog.UITipDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


/**
 * 根据业务进一步封装
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 * <p>
 * 重要提醒 : abstract 不能给删掉
 */
public abstract class BaseModelObserver<T> extends HttpObserver<T> {

    private BasePresenter mPresenter;
    private BaseResponseModel response;
    private BaseResponseListModel responseList;

    public BaseModelObserver(BasePresenter presenter) {
        super();
        this.mPresenter = presenter;
    }

    public BaseModelObserver(BasePresenter presenter, boolean isDialog, boolean isCabcelble) {
        super((Context) presenter.getMvpView(), isDialog, isCabcelble);
        this.mPresenter = presenter;
    }

    @Override
    public T onConvert(String data) {
        /**
         * 接口响应数据格式如@Response
         * 根据业务封装:
         * 1. response.isSuccess() (code==0) 业务逻辑成功回调convert()=>onSuccess()，否则失败回调onError()
         * 2.统一处理接口逻辑 例如:code==101 token过期等等
         */
        T tBase = new Gson().fromJson(data, getTypeClass());
        if (tBase instanceof BaseResponseModel) {
            response = (BaseResponseModel) tBase;
            return convertModel(response);
        } else if (tBase instanceof BaseResponseListModel) {
            responseList = (BaseResponseListModel) tBase;
            return convertListModel(responseList);
        }

        return null;
    }

    /**
     * 业务逻辑
     *
     * @param response
     * @return T
     */
    private T convertModel(BaseResponseModel response) {
        T t = null;

        int code = response.getCode();
        String msg = response.getMsg();

        switch (code) {
            case 0://成功
                if (response.isSuccess()) {//与服务器约定成功逻辑
                    t = (T) response.getData();
                    if (t == null || t instanceof String) {
                        t = (T) response;
                    }
                } else {
                    onError(getTag(), code, App.getAppString(R.string.server_agreement_error));
                }
                break;
            case 101://token过期，跳转登录页面重新登录
                isLoginToken();
                break;
            default://统一为错误处理
                onError(getTag(), code, msg);
                break;
        }
        return t;
    }

    /**
     * 业务逻辑
     *
     * @param responseList
     * @return List<T>
     */
    private T convertListModel(BaseResponseListModel responseList) {
        List<T> t = null;
        int code = responseList.getCode();
        String msg = responseList.getMsg();

        switch (code) {
            case 0:
                if (responseList.isSuccess()) {//与服务器约定成功逻辑
                    t = responseList.getData();
                } else {//统一为错误处理
                    onError(getTag(), code, App.getAppString(R.string.server_agreement_error));
                }
                break;
            case 101://token过期，跳转登录页面重新登录
                isLoginToken();
                break;
            case 102://系统公告
                break;
            default:
                onError(getTag(), code, msg);
                break;
        }
        return (T) t;
    }


    /**
     * 网络请求的错误信息
     * 如果有特殊处理需重写
     *
     * @param action 区分不同事件
     * @param code   错误码
     * @param desc   错误信息
     */
    public void onError(String action, int code, String desc) {
        UITipDialog.showFall((Context) mPresenter.getMvpView(), desc);
        mPresenter.getMvpView().onError(action, code, desc);
    }

    @Override
    public void onSuccess(String action, T value) {
        if (value instanceof List) {
            mPresenter.getMvpView().onSuccess(action, (List<T>) value);
        } else {
            mPresenter.getMvpView().onSuccess(action, value);
        }
    }

    /**
     * 取消回调
     * 如果有特殊处理需重写
     */
    public void onCancel() {
        LogUtil.d(App.getAppString(R.string.request_cancelled));
    }

    /**
     * 业务逻辑是否成功
     *
     * @return
     */
    @Override
    public boolean isBusinessOk() {
        if (response != null)
            return response.isSuccess();
        else if (responseList != null)
            return responseList.isSuccess();
        return false;
    }

    /**
     * 获取当前类泛型
     */
    public Type getTypeClass() {
        ParameterizedType ptClass = (ParameterizedType) getClass().getGenericSuperclass();
        Type type = null;
        if (ptClass != null) {
            type = ptClass.getActualTypeArguments()[0];
            LogUtil.d("当前类泛型:" + type);
        }
        return type;
    }

}
