package com.mvpframe.ui.base.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.mvpframe.R;
import com.mvpframe.application.App;
import com.mvpframe.bean.event.BaseEventModel;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.PresentationLayerFuncHelper;
import com.mvpframe.ui.base.delegate.ActivityMvpDelegate;
import com.mvpframe.ui.base.delegate.ActivityMvpDelegateImpl;
import com.mvpframe.ui.base.interfaces.CreateInit;
import com.mvpframe.ui.base.interfaces.PresentationLayerFunc;
import com.mvpframe.ui.base.interfaces.PublishActivityCallBack;
import com.mvpframe.util.GeneralUtils;
import com.mvpframe.util.ToastUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.reactivex.disposables.CompositeDisposable;

import static com.mvpframe.constant.Constants.LOG_D;
import static com.mvpframe.constant.Constants.LOG_W;

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
        CreateInit.CreateInitActivity<V, P>, PublishActivityCallBack, PresentationLayerFunc<T>, IMvpView<T>, View.OnClickListener {

    private ActivityMvpDelegate mvpDelegate;

    private PresentationLayerFuncHelper helper;

    protected CompositeDisposable disposable;

    public final String TAG = this.getClass().getSimpleName();

    /**
     * Context对象
     */
    protected static Context mContext;

    /**
     * Activity对象
     */
    protected static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMvpDelegate().onCreate(savedInstanceState);

        disposable = new CompositeDisposable();
        helper = new PresentationLayerFuncHelper<T>(this, disposable);

        setContentView(savedInstanceState);
        mContext = this;
        mActivity = this;
        getWindow().setBackgroundDrawableResource(R.color.transparent);//移除布局根背景
        App.addActivity(this);
        EventBus.getDefault().register(this);

        initData();
        initStatusBarDarkTheme();
        initListeners();
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
     * 链接Presenter
     *
     * @return
     */
    @Override
    public P[] createPresenter() {
        return getPresenterArray();
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
        if (GeneralUtils.isDoubleClick()) return;
    }

    @Override
    protected void onResume() {
        App.setCurrentActivityName(this.getClass().getName());
        getMvpDelegate().onResume();
        super.onResume();
    }

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        helper.startActivity(openClass, bundle);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        helper.openActivityForResult(openClass, requestCode, bundle);
    }

    @Override
    public void setResultOk(Bundle bundle) {
        helper.setResultOk(bundle);
    }

    @Override
    public void showToast(String msg) {
        helper.showToast(msg);
    }

    @Override
    public void showSoftKeyboard(View focusView) {
        helper.showSoftKeyboard(focusView);
    }

    /**
     * 隐藏软键盘
     */
    @Override
    public void hideSoftKeyboard() {
        helper.hideSoftKeyboard();
    }

    /**
     * 发送EventBus事件线
     *
     * @param o
     */
    @Override
    public void getEventBusPost(Object... o) {
        helper.getEventBusPost(o);
    }

    /**
     * 处理事件线
     *
     * @param eventModel
     */
    @Subscribe
    @Override
    public void onEventMainThread(BaseEventModel<T> eventModel) {
        helper.onEventMainThread(eventModel);
    }

    /**
     * 延迟操作
     *
     * @param delay
     */
    @Override
    public void postDelayed(Object... delay) {
        helper.postDelayed(delay);
    }

    /**
     * 延迟后要进行的操作
     *
     * @param l
     */
    @Override
    public void nextStep(Long l, Object... delay) {
        log(l, LOG_D);
    }

    /**
     * 打印日志
     *
     * @param msg 0: 信息  1: 日志的样式 e/d/i
     */
    @Override
    public void log(Object... msg) {
        helper.log(msg);
    }

    @Override
    protected void onDestroy() {
        getMvpDelegate().onDestroy();
        App.deleteActivity(this);
        EventBus.getDefault().unregister(this);
        ToastUtil.destory();
        if (disposable != null) {
            disposable.dispose();
            disposable.clear();
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        try {
            helper.hideSoftKeyboard();
        } catch (Exception e) {
            log(e, LOG_W, "finish 输入法错误");
        }
        super.finish();
    }

    /**
     * 监听点击事件 实现点击页面上除EditView外的位置隐藏输入法
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    helper.hideKeyboard(v.getWindowToken());
                }
            }
        } catch (Exception e) {
            log(e, LOG_W, "dispatchTouchEvent 输入法错误");
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
