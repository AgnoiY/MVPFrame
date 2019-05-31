package com.mvpframe.ui;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mvpframe.R;
import com.mvpframe.bean.account.LoginModel;
import com.mvpframe.bridge.sharepref.SharedPrefManager;
import com.mvpframe.bridge.sharepref.SharedPrefUser;
import com.mvpframe.databinding.ActivityMainBinding;
import com.mvpframe.presenter.account.LoginPresenter;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BaseLoadActivity;
import com.mvpframe.ui.view.account.activity.LoginActivity;

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
public class MainActivity extends BaseLoadActivity<Object, ActivityMainBinding>
        implements ViewPager.OnPageChangeListener {

    private LoginPresenter presenter = new LoginPresenter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onSuccess(String action, Object data) {
        super.onSuccess(action, data);
        SharedPrefManager.getUser().put(SharedPrefUser.USER_TOKEN,((LoginModel) data).getToken());
        mLoadBinding.text.setText(SharedPrefManager.getUser().getString(SharedPrefUser.USER_TOKEN, ""));
        mLoadBinding.text1.setText(((LoginModel) data).getToken());
        mLoadBinding.bt.setText(action);
    }


    @Override
    public void initListeners() {
        mLoadBinding.bt.setOnClickListener(this);
    }


    @Override
    public void initData() {
        presenter.login("15713802736", "a123456");
        mBaseBinding.titleView.setMidTitle("主页");
        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
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
        if (v.getId() == R.id.bt){
            startActivity(LoginActivity.class,null);
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

    }

    /**
     * 停止滑动后view的索引
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {

    }

    /**
     * 滑动的状态 0：空闲、1：开始滑动、2：结束滑动
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
