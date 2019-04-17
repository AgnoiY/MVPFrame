package com.mvpframe.capabilities.http.function;


import com.mvpframe.capabilities.http.exception.ExceptionEngine;
import com.mvpframe.util.LogUtil;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * http结果处理函数
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class HttpResultFunction<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        //打印具体错误
        LogUtil.w("HttpResultFunction:", throwable);
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
