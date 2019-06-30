package com.mvpframe.bridge.http;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mvpframe.bridge.BridgeLifeCycleListener;
import com.mvpframe.capabilities.http.RetrofitUtils;
import com.mvpframe.capabilities.http.api.Api;
import com.mvpframe.capabilities.http.cancel.RequestManagerImpl;
import com.mvpframe.capabilities.http.observable.HttpObservable;
import com.mvpframe.capabilities.http.observer.HttpObserver;
import com.mvpframe.capabilities.http.upload.UploadRequestBody;
import com.mvpframe.capabilities.http.utils.Method;
import com.mvpframe.capabilities.http.utils.RequestUtils;
import com.mvpframe.constant.Constants;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Http请求类
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class RetrofitHttp {

    /*请求方式*/
    private Method method;
    /*请求参数*/
    private Map<String, Object> parameter;
    /*header*/
    private Map<String, Object> header;
    /*LifecycleProvider*/
    private LifecycleProvider lifecycle;
    /*ActivityEvent*/
    private ActivityEvent activityEvent;
    /*FragmentEvent*/
    private FragmentEvent fragmentEvent;
    /*HttpObserver*/
    private HttpObserver httpObserver;
    /*标识请求的TAG*/
    private String tag;
    /*文件map*/
    private Map<String, File> fileMap;
    /*上传文件回调*/
    private UploadObserver uploadCallback;
    /*基础URL*/
    private String baseUrl;
    /*apiUrl*/
    private String apiUrl;
    /*String参数*/
    String bodyString;
    /*是否强制JSON格式*/
    boolean isJson;

    /*构造函数*/
    private RetrofitHttp(Builder builder) {
        this.parameter = builder.parameter;
        this.header = builder.header;
        this.lifecycle = builder.lifecycle;
        this.activityEvent = builder.activityEvent;
        this.fragmentEvent = builder.fragmentEvent;
        this.tag = builder.tag;
        this.fileMap = builder.fileMap;
        this.baseUrl = builder.baseUrl;
        this.apiUrl = builder.apiUrl;
        this.isJson = builder.isJson;
        this.bodyString = builder.bodyString;
        this.method = builder.method;
    }

    /*普通Http请求*/
    public void request(HttpObserver httpObserver) {
        this.httpObserver = httpObserver;
        if (httpObserver == null) {
            throw new NullPointerException("HttpObserver must not null!");
        } else {
            doRequest();
        }
    }

    /*上传文件请求*/
    public void upload(UploadObserver uploadCallback) {
        this.uploadCallback = uploadCallback;
        if (uploadCallback == null) {
            throw new NullPointerException("UploadObserver must not null!");
        } else {
            doUpload();
        }
    }

    /*执行请求*/
    private void doRequest() {

        /*设置请求唯一标识*/
        httpObserver.setTag(TextUtils.isEmpty(tag) ? disposeApiUrl() : tag);

        /*header处理*/
        disposeHeader();

        /*Parameter处理*/
        disposeParameter();

        /*请求方式处理*/
        Observable apiObservable = disposeApiObservable();

        /* 被观察者 httpObservable */
        HttpObservable httpObservable = new HttpObservable.Builder(apiObservable)
                .baseObserver(httpObserver)
                .lifecycleProvider(lifecycle)
                .activityEvent(activityEvent)
                .fragmentEvent(fragmentEvent)
                .build();
        /* 观察者  httpObserver */
        /*设置监听*/
        httpObservable.observe().subscribe(httpObserver);

    }

    /*执行文件上传*/
    private void doUpload() {

        /*设置请求唯一标识*/
        uploadCallback.setTag(TextUtils.isEmpty(tag) ? disposeApiUrl() : tag);

        /*header处理*/
        disposeHeader();

        /*Parameter处理*/
        disposeParameter();

        /*处理文件集合*/
        List<MultipartBody.Part> fileList = new ArrayList<>();
        if (fileMap != null && fileMap.size() > 0) {
            int size = fileMap.size();
            int index = 1;
            File file;
            RequestBody requestBody;
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                file = entry.getValue();
                requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData(entry.getKey(), file.getName(), new UploadRequestBody(requestBody, file, index, size, uploadCallback));
                fileList.add(part);
                index++;
            }
        }

        /*请求处理*/
        Observable apiObservable = RetrofitUtils.get().getRetrofit(getBaseUrl(), header).create(Api.class).upload(disposeApiUrl(), parameter, header, fileList);

        /* 被观察者 httpObservable */
        HttpObservable httpObservable = new HttpObservable.Builder(apiObservable)
                .baseObserver(uploadCallback)
                .lifecycleProvider(lifecycle)
                .activityEvent(activityEvent)
                .fragmentEvent(fragmentEvent)
                .build();
        /* 观察者  uploadCallback */
        /*设置监听*/
        httpObservable.observe().subscribe(uploadCallback);

    }

    /*获取基础URL*/
    private String getBaseUrl() {
        //如果没有重新指定URL则是用默认配置
        return TextUtils.isEmpty(baseUrl) ? Configure.get().getBaseUrl() : baseUrl;
    }

    /*ApiUrl处理*/
    private String disposeApiUrl() {
        return TextUtils.isEmpty(apiUrl) ? "" : apiUrl;
    }

    /*处理Header*/
    private void disposeHeader() {

        /*header空处理*/
        if (header == null) {
            header = new TreeMap<>();
        }

        //添加基础 Header
        Map<String, Object> baseHeader = Configure.get().getBaseHeader();
        if (baseHeader != null && baseHeader.size() > 0) {
            header.putAll(baseHeader);
        }

        if (!header.isEmpty()) {
            //处理header中文或者换行符出错问题
            for (Map.Entry<String, Object> entry : header.entrySet()) {
                header.put(entry.getKey(), RequestUtils.getHeaderValueEncoded(entry.getValue()));
            }
        }

    }

    /*处理 Parameter*/
    private void disposeParameter() {

        /*空处理*/
        if (parameter == null) {
            parameter = new TreeMap<>();
        }

        //添加基础 Parameter
        Map<String, Object> baseParameter = Configure.get().getBaseParameter();
        if (baseParameter != null && baseParameter.size() > 0) {
            parameter.putAll(baseParameter);
        }
    }

    /*处理ApiObservable*/
    private Observable disposeApiObservable() {

        Observable apiObservable = null;

        /*是否JSON格式提交参数*/
        boolean hasBodyString = !TextUtils.isEmpty(bodyString);
        RequestBody requestBody = null;
        if (hasBodyString) {
            String mediaType = isJson ? "application/json; charset=utf-8" : "text/plain;charset=utf-8";
            requestBody = RequestBody.create(okhttp3.MediaType.parse(mediaType), bodyString);
        }

        /*Api接口*/
        Api apiService = RetrofitUtils.get().getRetrofit(getBaseUrl(), header).create(Api.class);
        /*未指定默认POST*/
        if (method == null) method = Method.POST;

        switch (method) {
            case GET:
                apiObservable = apiService.get(disposeApiUrl(), parameter, header);
                break;
            case POST:
                if (hasBodyString)
                    apiObservable = apiService.post(disposeApiUrl(), requestBody, header);
                else
                    apiObservable = apiService.post(disposeApiUrl(), parameter, header);
                break;
            case DELETE:
                apiObservable = apiService.delete(disposeApiUrl(), parameter, header);
                break;
            case PUT:
                apiObservable = apiService.put(disposeApiUrl(), parameter, header);
                break;
            default:
                break;
        }
        return apiObservable;
    }

    /**
     * Configure配置
     */
    public static final class Configure {

        /*请求基础路径*/
        String baseUrl;
        /*超时时长*/
        long timeout;
        /*时间单位*/
        TimeUnit timeUnit;
        /*全局上下文*/
        Context context;
        /*全局Handler*/
        Handler handler;
        /*请求参数*/
        Map<String, Object> parameter;
        /*header*/
        Map<String, Object> header;
        /*是否显示Log*/
        boolean showLog;


        public static Configure get() {
            return Configure.Holder.holders;
        }

        private static class Holder {
            private static Configure holders = new Configure();
        }

        private Configure() {
            timeout = Constants.TIME_OUT;//默认60秒
            timeUnit = TimeUnit.SECONDS;//默认秒
            showLog = true;//默认打印LOG
        }

        /*请求基础路径*/
        public RetrofitHttp.Configure baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        /*基础参数*/
        public RetrofitHttp.Configure baseParameter(Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }

        public Map<String, Object> getBaseParameter() {
            return parameter;
        }

        /*基础Header*/
        public RetrofitHttp.Configure baseHeader(Map<String, Object> header) {
            this.header = header;
            return this;
        }

        /* 增加 Header 不断叠加 Header 包括基础 Header */
        public RetrofitHttp.Configure addHeader(String key, Object header) {
            this.header = new TreeMap<>();
            this.header.put(key, header);
            return this;
        }

        public Map<String, Object> getBaseHeader() {
            return header;
        }

        /*超时时长*/
        public RetrofitHttp.Configure timeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public long getTimeout() {
            return timeout;
        }

        /*是否显示LOG*/
        public RetrofitHttp.Configure showLog(boolean showLog) {
            this.showLog = showLog;
            return this;
        }

        public boolean isShowLog() {
            return showLog;
        }

        /*时间单位*/
        public RetrofitHttp.Configure timeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        /*Handler*/
        public Handler getHandler() {
            return handler;
        }

        /*Context*/
        public Context getContext() {
            return context;
        }

        /*初始化全局上下文*/
        public RetrofitHttp.Configure init(Application app) {
            this.context = app.getApplicationContext();
            this.handler = new Handler(Looper.getMainLooper());
            return this;
        }

    }

    /**
     * Builder
     * 构造Request所需参数，按需设置
     */
    public static class Builder implements BridgeLifeCycleListener {
        /*请求方式*/
        Method method;
        /*请求参数*/
        Map<String, Object> parameter;
        /*header*/
        Map<String, Object> header;
        /*LifecycleProvider*/
        LifecycleProvider lifecycle;
        /*ActivityEvent*/
        ActivityEvent activityEvent;
        /*FragmentEvent*/
        FragmentEvent fragmentEvent;
        /*标识请求的TAG*/
        String tag;
        /*文件map*/
        Map<String, File> fileMap;
        /*基础URL*/
        String baseUrl;
        /*apiUrl*/
        String apiUrl;
        /*String参数*/
        String bodyString;
        /*是否强制JSON格式*/
        boolean isJson;

        private volatile Builder instance;

        public Builder getInstance() {
            Builder builder = instance;
            if (builder == null) {
                synchronized (RetrofitHttp.class) {
                    builder = instance;
                    if (builder == null) {
                        instance = builder = new Builder();
                    }
                }
            }
            return builder;
        }

        /*GET*/
        public RetrofitHttp.Builder get() {
            this.method = Method.GET;
            return this;
        }

        /*POST*/
        public RetrofitHttp.Builder post() {
            this.method = Method.POST;
            return this;
        }

        /*DELETE*/
        public RetrofitHttp.Builder delete() {
            this.method = Method.DELETE;
            return this;
        }

        /*PUT*/
        public RetrofitHttp.Builder put() {
            this.method = Method.PUT;
            return this;
        }

        /*基础URL*/
        public RetrofitHttp.Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /*API URL*/
        public RetrofitHttp.Builder apiUrl(@NonNull String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        /* 增加 Parameter 不断叠加参数 包括基础参数 */
        public RetrofitHttp.Builder addParameter(Map<String, Object> parameter) {
            if (this.parameter == null)
                this.parameter = parameter;
            else
                this.parameter.putAll(parameter);
            return this;
        }

        /* 增加 Parameter 不断叠加参数 包括基础参数 */
        public RetrofitHttp.Builder addParameter(String key, Object parameter) {
            if (this.parameter == null) {
                this.parameter = new TreeMap<>();
            }
            if (parameter != null) {
                this.parameter.put(key, parameter);
            }
            return this;
        }

        /*设置 Parameter 会覆盖 Parameter 包括基础参数*/
        public RetrofitHttp.Builder setParameter(Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }

        /* 设置String 类型参数  覆盖之前设置  isJson:是否强制JSON格式    bodyString设置后Parameter则无效 */
        public RetrofitHttp.Builder setBodyString(String bodyString, boolean isJson) {
            this.isJson = isJson;
            this.bodyString = bodyString;
            return this;
        }

        /* 增加 Header 不断叠加 Header 包括基础 Header */
        public RetrofitHttp.Builder addHeader(String key, Object header) {
            if (this.header == null) {
                this.header = new TreeMap<>();
            }
            this.header.put(key, header);
            return this;
        }

        /* 增加 Header 不断叠加 Header 包括基础 Header */
        public RetrofitHttp.Builder addHeader(Map<String, Object> header) {
            if (this.header == null)
                this.header = header;
            else
                this.header.putAll(header);
            return this;
        }

        /*设置 Header 会覆盖 Header 包括基础参数*/
        public RetrofitHttp.Builder setHeader(Map<String, Object> header) {
            this.header = header;
            return this;
        }

        /*LifecycleProvider*/
        public RetrofitHttp.Builder lifecycle(LifecycleProvider lifecycle) {
            this.lifecycle = lifecycle;
            return this;
        }

        /*ActivityEvent*/
        public RetrofitHttp.Builder activityEvent(ActivityEvent activityEvent) {
            this.activityEvent = activityEvent;
            return this;
        }

        /*FragmentEvent*/
        public RetrofitHttp.Builder fragmentEvent(FragmentEvent fragmentEvent) {
            this.fragmentEvent = fragmentEvent;
            return this;
        }

        /*tag*/
        public RetrofitHttp.Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        /*文件*/
        public RetrofitHttp.Builder file(String name, File file) {
            if (fileMap == null) {
                fileMap = new IdentityHashMap();
            }
            this.fileMap.put(name, file);
            return this;
        }

        /*文件集合*/
        public RetrofitHttp.Builder file(Map<String, File> file) {
            this.fileMap = file;
            return this;
        }

        /*一个Key对应多个文件*/
        public RetrofitHttp.Builder file(String key, List<File> fileList) {
            if (fileMap == null) {
                fileMap = new IdentityHashMap();
            }
            if (fileList != null && !fileList.isEmpty()) {
                for (File file : fileList) {
                    fileMap.put(key, file);
                }
            }
            return this;
        }

        public RetrofitHttp.Builder clear() {
            this.method = Method.POST;
            this.parameter = null;
            this.header = null;
            this.lifecycle = null;
            this.activityEvent = null;
            this.fragmentEvent = null;
            this.tag = "";
            this.fileMap = null;
            this.apiUrl = "";
            this.bodyString = "";
            this.isJson = false;
            this.instance = null;
            return this;
        }

        public RetrofitHttp build() {

            return new RetrofitHttp(this);
        }


        @Override
        public void initOnApplicationCreate(Context context) {
            //开始进入Applicattion
        }

        @Override
        public void clearOnApplicationQuit() {
            clear();
            RequestManagerImpl.getInstance().cancelAll();
        }
    }
}
