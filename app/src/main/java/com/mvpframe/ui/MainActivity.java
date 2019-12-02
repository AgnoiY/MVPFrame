package com.mvpframe.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.mvpframe.R;
import com.mvpframe.bean.account.LoginModel;
import com.mvpframe.bridge.sharepref.SharedPrefManager;
import com.mvpframe.bridge.sharepref.SharedPrefUser;
import com.mvpframe.databinding.ActivityMainBinding;
import com.mvpframe.presenter.account.LoginPresenter;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BasePermissionsActivity;
import com.mvpframe.utils.LogUtils;
import com.mvpframe.utils.ToastUtils;
import com.mvpframe.view.dialog.CommonDialog;

import java.lang.reflect.Method;

/**
 * <功能详细描述>
 * 泛型传入
 * 1、网络请求实体类：如果有多个实体类可以传入Object
 * 2、自动生成ViewDataBinding
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class MainActivity extends BasePermissionsActivity<Object, ActivityMainBinding>
        implements ViewPager.OnPageChangeListener {

    private LoginPresenter presenter = new LoginPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onSuccess(String action, Object data) {
        super.onSuccess(action, data);
        SharedPrefManager.getUser().put(SharedPrefUser.USER_TOKEN, ((LoginModel) data).getToken());
        mLoadBinding.text.setText(SharedPrefManager.getUser().getString(SharedPrefUser.USER_TOKEN, ""));
        mLoadBinding.text1.setText(((LoginModel) data).getToken());
        mLoadBinding.bt.setText(action);
    }


    @Override
    public void initListeners() {
        mLoadBinding.bt.setOnClickListener(this);
        mLoadBinding.text.setOnClickListener(this);
    }


    @Override
    public void initData() {
//        presenter.login("15713802736", "a123456");
        mBaseBinding.titleView.setMidTitle("主页");
        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        setPermissions(getNoGrantedPermission(this));
    }

    @Override
    public BasePresenter<IMvpView<Object>>[] getPresenterArray() {
        return new BasePresenter[]{presenter};
    }

    @Override
    public void onEmptyTextClickListener() {
        super.onEmptyTextClickListener();
        presenter.login("15713802736", "a123456");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.bt) {
//            DBHelper dbHelper = DBHelper.get();
//            dbHelper.insertOrUpdate(new Download());
//            startActivity(LoginActivity.class, null);
            reboot();
        }
        if (v.getId() == R.id.text) {
            CommonDialog builder = new CommonDialog<>(mContext);
            builder.setTitle(R.string.app_name);
//            CommonDialog<ActivityMainBinding> builder = new CommonDialog<>(mContext, R.layout.activity_main, false);
//            CommonDialog<ActivityLoginBinding> builder = new CommonDialog<>(mContext, R.layout.activity_login);
            builder.setClickListenter(msg -> ToastUtils.makeCenterToast(mContext, "asd"));
            builder.shows();
        }
    }

    /**
     * 获取手机序列号
     *
     * @return 手机序列号
     */
    @SuppressLint("MissingPermission")
    public static String getSerialNumber() {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                serial = Build.getSerial();
            } else {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            LogUtils.w("读取设备序列号异常：", e);
        }

        LogUtils.d("读取设备序列号：", serial);
        return serial;
    }

    /**
     * 重启手机
     */
    private void reboot() {
        try {

            //获得ServiceManager类
            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");

            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);

            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null,Context.POWER_SERVICE);

            //获得IPowerManager.Stub类
            Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("reboot",boolean.class,String.class,boolean.class);
//            Method shutdown = oIPowerManager.getClass().getMethod("shutdown",boolean.class,String.class,boolean.class);
            //调用shutdown()方法
//            shutdown.invoke(oIPowerManager,false,true);
            shutdown.invoke(oIPowerManager,false,"",false);

        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
        }
    }

    /**
     * 滑动过程
     *
     * @param position             左侧view索引
     * @param positionOffset       滑动的半分比，左->右：0->1
     * @param positionOffsetPixels 滑动的距离，，左->右：0->屏幕的宽度
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //
    }

    /**
     * 停止滑动后view的索引
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        //
    }

    /**
     * 滑动的状态 0：空闲、1：开始滑动、2：结束滑动
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        //
    }
}
