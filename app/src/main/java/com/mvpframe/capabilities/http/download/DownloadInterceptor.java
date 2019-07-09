package com.mvpframe.capabilities.http.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 通过Interceptor回调监听Response进度
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
public class DownloadInterceptor implements Interceptor {

    private DownloadProgressCallback callback;

    public DownloadInterceptor(DownloadProgressCallback callback) {
        this.callback = callback;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder()
                .body(new DownloadResponseBody(response.body(), callback))
                .build();
    }
}
