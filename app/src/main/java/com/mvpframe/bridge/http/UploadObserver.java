package com.mvpframe.bridge.http;

import android.content.Context;

import com.mvpframe.R;
import com.mvpframe.application.App;
import com.mvpframe.capabilities.http.interfaces.UploadProgressCallback;
import com.mvpframe.capabilities.http.observer.HttpObserver;
import com.mvpframe.utils.LogUtils;
import com.mvpframe.view.dialog.UITipDialog;

import java.io.File;

/**
 * 上传回调接口
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class UploadObserver<T> extends HttpObserver<T> implements UploadProgressCallback {

    public UploadObserver() {
    }

    public UploadObserver(Context context, boolean isDialog, boolean isCabcelble) {
        super(context, isDialog, isCabcelble);
    }

    @Override
    public void progress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile) {
        onProgress(file, currentSize, totalSize, progress, currentIndex, totalFile);
    }

    /**
     * 上传回调
     *
     * @param file
     * @param currentSize
     * @param totalSize
     * @param progress
     * @param currentIndex
     * @param totalFile
     */
    public abstract void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile);

    /**
     * 数据转换/解析数据
     *
     * @param data
     * @return
     */
    public abstract T onConvert(String data);

    /**
     * 失败回调
     *
     * @param action
     * @param code
     * @param desc
     */
    public void onError(String action, int code, String desc) {
        UITipDialog.showFall(App.getApplication(), desc);
    }

    /**
     * 取消回调
     */
    public void onCancel() {
        LogUtils.d(App.getAppString(R.string.request_cancelled));
    }

}
