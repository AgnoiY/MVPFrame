package com.mvpframe.ui;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mvpframe.R;
import com.mvpframe.bean.account.LoginModel;
import com.mvpframe.bridge.sharePref.SharedPrefManager;
import com.mvpframe.bridge.sharePref.SharedPrefUser;
import com.mvpframe.databinding.ActivityMainBinding;
import com.mvpframe.presenter.account.LoginPresenter;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.ui.base.activity.BaseLoadActivity;
import com.mvpframe.util.ToastUtil;
import com.mvpframe.view.dialog.BaseDialogClickListenter;
import com.mvpframe.view.dialog.CommonDialog;

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
        mLoadBinding.text.setText(SharedPrefManager.getUser().getString(SharedPrefUser.USER_NAME, ""));
        mLoadBinding.text1.setText(((LoginModel) data).getToken());
//        mLoadBinding.text1.setText(data.getToken());
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt:
//                List<PermissionsModel> list = new ArrayList<>();
//                list.add(new PermissionsModel().setPermissions(Manifest.permission.SEND_SMS).setTitle("权限").setContent("录音权限"));
//                list.add(new PermissionsModel().setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE).setContent("读写权限"));
//                setPermissions(list);
//                setPermissions(getNoGrantedPermission(mContext));
                new CommonDialog().setTitleMsg("安徽")
                        .setContentMsg("已禁用权限")
                        .setButtonOk("立即开启")
                        .shows(this)
                        .setClickListenter(new BaseDialogClickListenter() {
                            @Override
                            public void dialogTipsOk() {
                                ToastUtil.makeCenterToast(mContext, "ads");
                            }
                        });
                break;
        }
    }

    @Override
    public void onEmptyTextClickListener() {
        super.onEmptyTextClickListener();
        presenter.login("15713802736", "a123456");
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
