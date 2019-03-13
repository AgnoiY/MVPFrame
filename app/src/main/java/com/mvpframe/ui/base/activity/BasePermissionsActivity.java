package com.mvpframe.ui.base.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.mvpframe.R;
import com.mvpframe.presenter.base.BasePresenter;
import com.mvpframe.presenter.base.IMvpView;
import com.mvpframe.view.dialog.BaseDialogClickListenter;
import com.mvpframe.view.dialog.CommonDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 带空页面，错误页面显示的BaseActivity 通过BaseActivity界面操作封装成View而来
 * Data：2018/12/18
 *
 * @author yong
 */
public abstract class BasePermissionsActivity<T> extends BaseActivity<T, IMvpView<T>, BasePresenter<IMvpView<T>>> {

    private int settingCode = 321;
    private int permissionsCode = 123;
    // 要申请的权限
    private List<String> permissionslList;

    private void initPermissions() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            for (int i = 0; i < permissionslList.size(); i++) {
                int pId = ContextCompat.checkSelfPermission(this, permissionslList.get(i));
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (pId != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求
                    startRequestPermission();
                }
            }
        }
    }

    /**
     * 开始提交请求权限
     */
    private void startRequestPermission() {
        if (!permissionslList.isEmpty())
            ActivityCompat.requestPermissions(this, permissionslList.toArray(new String[0]), permissionsCode);
        else
            initPermissionSuccess();
    }

    /**
     * 用户权限申请的回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == this.permissionsCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        boolean b = shouldShowRequestPermissionRationale(permissions[i]);
                        if (!b) {
                            // 提示用户去应用设置界面手动开启权限
                            showDialogTipUserGoToAppSettting();
                        }

                    } else {
                        initPermissionSuccess();
                    }
                }
            }
        }
    }

    /**
     * 提示用户去应用设置界面手动开启权限
     */
    private void showDialogTipUserGoToAppSettting() {
        new CommonDialog().setTitleMsg("权限已禁用")
                .setContentMsg("请在应用设置中开启权限")
                .setButtonOk(getString(R.string.permissions_open))
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

        startActivityForResult(intent, settingCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == settingCode) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (int i = 0; i < permissionslList.size(); i++) {
                    // 检查该权限是否已经获取
                    int pId = ContextCompat.checkSelfPermission(this, permissionslList.get(i));
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (pId != PackageManager.PERMISSION_GRANTED) {
                        // 提示用户应该去应用设置界面手动开启权限
                        showToast("权限未开启");
                    } else {
                        initPermissionSuccess();
                    }
                }
            }
        }
    }

    /**
     * 获取未授权的[PermissionInfo.PROTECTION_DANGEROUS]权限
     *
     * @param context
     * @return
     */
    public List<String> getNoGrantedPermission(Context context) {
        PackageInfo pi = null;
        PackageManager packageManager = context.getPackageManager();
        try {
            pi = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        List<String> permissions = new ArrayList<>();
        for (int i = 0; i < pi.requestedPermissions.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) == 0) {
                    try {
                        PermissionInfo permissionInfo = packageManager.getPermissionInfo(pi.requestedPermissions[i], PackageManager.GET_META_DATA);
                        if (permissionInfo.protectionLevel != PermissionInfo.PROTECTION_DANGEROUS) {
                            continue;
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        continue;
                    }

                    permissions.add(pi.requestedPermissions[i]);
                }
            }
        }
        return permissions;
    }


    /**
     * 开启权限申请
     *
     * @param permissionslList
     */
    public void setPermissions(@NonNull List<String> permissionslList) {
        this.permissionslList = permissionslList;
        initPermissions();
    }

    /**
     * 开启权限申请
     *
     * @param permissionsl
     */
    public void setPermissions(@NonNull String permissionsl) {
        if (permissionslList == null)
            permissionslList = new ArrayList<>();
        permissionslList.add(permissionsl);
        initPermissions();
    }

}
