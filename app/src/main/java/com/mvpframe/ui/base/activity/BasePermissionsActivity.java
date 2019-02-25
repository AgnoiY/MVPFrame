package com.mvpframe.ui.base.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.mvpframe.R;
import com.mvpframe.bean.permissions.PermissionsModel;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.util.Tools;
import com.mvpframe.view.dialog.BaseDialogClickListenter;
import com.mvpframe.view.dialog.CommonDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 带空页面，错误页面显示的BaseActivity 通过BaseActivity界面操作封装成View而来
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BasePermissionsActivity<T> extends BaseActivity<T, IMvpView<T>, BasePresenter<IMvpView<T>>> {

    // 要申请的权限
    private List<PermissionsModel> modelList = new ArrayList<>();

    private void initPermissions() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            for (int i = 0; i < modelList.size(); i++) {
                int pId = ContextCompat.checkSelfPermission(this, modelList.get(i).getPermissions());
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (pId != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求
                    showDialogTipUserRequestPermission(i);
                }
            }
        }
    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission(int position) {

        PermissionsModel model = modelList.get(position);

        if (Tools.isNull(model.getContent()))
            return;

//        new CommonDialog().setTitleMsg(model.getTitle())
//                .setContentMsg(model.getContent())
//                .setButtonOk(Tools.isNotNull(model.getOpen()) ? model.getOpen() : getString(R.string.permissions_open))
//                .shows(this)
//                .setClickListenter(() -> {
                    startRequestPermission();
//                });
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        String[] permissions = new String[modelList.size()];

        for (int i = 0; i < modelList.size(); i++) {
            permissions[i] = modelList.get(i).getPermissions();
        }
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        boolean b = shouldShowRequestPermissionRationale(permissions[i]);
                        if (!b) {
                            // 提示用户去应用设置界面手动开启权限
                            showDialogTipUserGoToAppSettting(i);
                        }

                    } else {
                        initPermissionSuccess();
                    }
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting(int position) {

        PermissionsModel model = modelList.get(position);

        new CommonDialog().setTitleMsg(model.getTitle())
                .setContentMsg("请在应用设置权限中，已禁用权限")
                .setButtonOk(Tools.isNotNull(model.getOpen()) ? model.getOpen() : getString(R.string.permissions_open))
                .shows(this)
                .setClickListenter(new BaseDialogClickListenter() {
                    @Override
                    public void dialogTipsOk() {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                });
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < modelList.size(); i++) {
                    // 检查该权限是否已经获取
                    int pId = ContextCompat.checkSelfPermission(this, modelList.get(i).getPermissions());
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (pId != PackageManager.PERMISSION_GRANTED) {
                        // 提示用户应该去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting(i);
                    } else {
                        initPermissionSuccess();
                    }
                }
            }
        }
    }

    /**
     * 开启权限申请
     *
     * @param modelList
     */
    public void setPermissions(@NonNull List<PermissionsModel> modelList) {
        this.modelList = modelList;
        initPermissions();
    }

}
