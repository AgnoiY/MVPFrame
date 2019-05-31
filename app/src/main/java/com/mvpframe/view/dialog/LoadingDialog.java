package com.mvpframe.view.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;

import com.mvpframe.R;
import com.mvpframe.capabilities.http.interfaces.ProgressDialogObserver;
import com.mvpframe.constant.Constants;

/**
 * <请求加载弹窗>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public final class LoadingDialog extends ProgressDialog {

    private long delayMillis = Constants.TIME_OUT * 1000;
    private int mWidth;
    private int mHeight;
    private LoadingView mLoadingView;
    private boolean isCancelable = false;
    private boolean isCanceledOnTouchOutside = false;
    private ProgressDialogObserver progressDialogObserver;


    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogLight);
        this.mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        getWindow().setLayout(mWidth, mHeight);  //设置宽高
        getWindow().setGravity(Gravity.CENTER);  //设置居中
        initData();
        initView();
    }

    private void initData() {
        setCanceledOnTouchOutside(isCanceledOnTouchOutside);  // 设置当点击对话框以外区域是否关闭对话框
        setCancelable(isCancelable);  // 设置当返回键按下是否关闭对话框
        if (isCancelable) {
            setOnCancelListener(dialogInterface -> {
                progressDialogObserver.onCancleProgress();
            });
        }
    }

    private void initView() {
        mLoadingView = (LoadingView) findViewById(R.id.loadingview);
    }

    public void showDialog() {

        if (!isShowing()) {
            show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isShowing()) {
                        dismiss();
                    }
                }
            }, delayMillis);
        }
    }

    public void closeDialog() {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override
    public void show() {
        super.show();
        if (mLoadingView != null) {
            mLoadingView.start();
        }
    }

    @Override
    public void dismiss() {
        if (mLoadingView != null) {
            mLoadingView.stop();
        }
        super.dismiss();
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    public LoadingDialog setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    public LoadingDialog setIsCancelable(boolean cancelable) {
        isCancelable = cancelable;
        return this;
    }

    public LoadingDialog setIsCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        isCanceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public LoadingDialog setProgressDialogObserver(ProgressDialogObserver progressDialogObserver) {
        this.progressDialogObserver = progressDialogObserver;
        return this;
    }
}
