package com.mvpframe.ui.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.mvpframe.app.MyApplication;
import com.mvpframe.biz.base.BasePresenter;
import com.mvpframe.biz.base.IMvpView;
import com.mvpframe.constant.Constants;
import com.mvpframe.ui.base.PresentationLayerFuncHelper;
import com.mvpframe.ui.base.delegate.ActivityMvpDelegate;
import com.mvpframe.ui.base.delegate.ActivityMvpDelegateImpl;
import com.mvpframe.ui.base.interfaces.CreateInit;
import com.mvpframe.ui.base.interfaces.PresentationLayerFunc;
import com.mvpframe.ui.base.interfaces.PublishActivityCallBack;
import com.mvpframe.util.LogUtil;
import com.mvpframe.util.Tools;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * * 备注:
 * 1.XXActivity 继承 BaseActivity,当页面存在 Presenter 时，具体 Activity 需要调用 setPresenter(P... presenter)
 * 2.支持一个 Activity 存在多个 Presenter
 *
 * @param <T>
 * @param <V>
 * @param <P> Data：2018/12/18
 * @author yong
 */
public abstract class BaseActivity<T, V extends IMvpView, P extends BasePresenter<V>> extends RxAppCompatActivity implements
        CreateInit.CreateInitActivity<V, P>, PublishActivityCallBack, PresentationLayerFunc, IMvpView<T>, View.OnClickListener {

    protected ActivityMvpDelegate mvpDelegate;

    private PresentationLayerFuncHelper presentationLayerFuncHelper;

    public final String TAG = this.getClass().getSimpleName();

    /**
     * Context对象
     */
    protected static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMvpDelegate().onCreate(savedInstanceState);

        presentationLayerFuncHelper = new PresentationLayerFuncHelper(this);
        setContentView(savedInstanceState);
        mContext = this;
        Constants.isStartFragmen = false;

        initData();
        initListeners();
        MyApplication.mApplication.addActivity(this);
        EventBus.getDefault().register(this);
    }

    /**
     * 注入View
     *
     * @return
     */
    @Override
    public V[] createView() {
        V[] views = null;
        P[] pArray = createPresenter();
        if (pArray != null) {
            views = (V[]) new IMvpView[pArray.length];
            for (int i = 0; i < pArray.length; i++) {
                views[i] = (V) this;
            }
        }
        return views;
    }

    /**
     * 关联Activity的生命周期
     *
     * @return
     */
    @NonNull
    protected ActivityMvpDelegate<V, P> getMvpDelegate() {
        if (mvpDelegate == null) {
            mvpDelegate = new ActivityMvpDelegateImpl(this, this);
        }
        return mvpDelegate;
    }

    /**
     * 事件线
     *
     * @param eventModel
     */
    @Subscribe
    public void onEventMainThread(T eventModel) {

    }

    /**
     * 链接Presenter
     *
     * @return
     */
    @Override
    public P[] createPresenter() {
        return getPresenterArray();
    }

    /**
     * 获取 Presenter 数组
     */
    public abstract P[] getPresenterArray();

    /**
     * 网络请求的错误信息，已在请求中处理提示Toast
     * 如果有特殊处理需重写
     *
     * @param action 区分不同事件
     * @param code   错误码
     * @param msg    错误信息
     */
    @Override
    public void onError(String action, int code, String msg) {

    }

    /**
     * 配合DataBinding点击事件监听
     * 添加防止重复点击
     * 有点击事件只需重写
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (Tools.isDoubleClick()) return;
    }

    @Override
    protected void onResume() {
        MyApplication.mApplication.currentActivityName = this.getClass().getName();
        getMvpDelegate().onResume();
        super.onResume();
    }

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void setResultOk(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) ;
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showToast(String msg) {
        presentationLayerFuncHelper.showToast(msg);
    }

    @Override
    public void showSoftKeyboard(View focusView) {
        presentationLayerFuncHelper.showSoftKeyboard(focusView);
    }

    @Override
    public void hideSoftKeyboard() {
        presentationLayerFuncHelper.hideSoftKeyboard();
    }

    @Override
    protected void onDestroy() {
        getMvpDelegate().onDestroy();
        MyApplication.mApplication.deleteActivity(this);
        EventBus.getDefault().unregister(this);
        Constants.isStartFragmen = true;

        super.onDestroy();
    }

    @Override
    public void finish() {
        try {
            presentationLayerFuncHelper.hideSoftKeyboard();
        } catch (Exception e) {
            LogUtil.E("finish 输入法错误");
        }
        super.finish();
    }

    //监听点击事件 实现点击页面上除EditView外的位置隐藏输入法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    presentationLayerFuncHelper.hideKeyboard(v.getWindowToken());
                }
            }
        } catch (Exception e) {
            LogUtil.E("dispatchTouchEvent 输入法错误");
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getMvpDelegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getMvpDelegate().onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMvpDelegate().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getMvpDelegate().onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMvpDelegate().onRestart();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        getMvpDelegate().onContentChanged();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getMvpDelegate().onPostCreate(savedInstanceState);
    }
}
