//package com.mvpframe.ui.base.activity;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.widget.Toast;
//
//import com.mvpframe.presenter.base.BasePresenter;
//import com.mvpframe.presenter.base.IMvpView;
//import com.mvpframe.view.dialog.DialogInterface;
//
///**
// * 带空页面，错误页面显示的BaseActivity 通过BaseActivity界面操作封装成View而来
// * Data：2018/12/18
// *
// * @author yong
// */
//public abstract class PermissionsActivity<T> extends BaseActivity<T, IMvpView<T>, BasePresenter<IMvpView<T>>> {
//
//    // 要申请的权限
//    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    private AlertDialog dialog;
//
//    @Override
//    void initPermissions() {
//        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // 检查该权限是否已经获取
//            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
//            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
//            if (i != PackageManager.PERMISSION_GRANTED) {
//                // 如果没有授予该权限，就去提示用户请求
//                showDialogTipUserRequestPermission();
//            }
//        }
//    }
//
//    // 提示用户该请求权限的弹出框
//    private void showDialogTipUserRequestPermission() {
//
//        new AlertDialog.Builder(this)
//                .setTitle("存储权限不可用")
//                .setMessage("由于支付宝需要获取存储空间，为你存储个人信息；\n否则，您将无法正常使用支付宝")
//                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startRequestPermission();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                }).setCancelable(false).show();
//    }
//
//    // 开始提交请求权限
//    private void startRequestPermission() {
//        ActivityCompat.requestPermissions(this, permissions, 321);
//    }
//
//    // 用户权限 申请 的回调方法
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == 321) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
//                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
//                    if (!b) {
//                        // 用户还是想用我的 APP 的
//                        // 提示用户去应用设置界面手动开启权限
//                        showDialogTipUserGoToAppSettting();
//                    } else
//                        finish();
//                } else {
//                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//
//    // 提示用户去应用设置界面手动开启权限
//
//    private void showDialogTipUserGoToAppSettting() {
//
//        dialog = new AlertDialog.Builder(this)
//                .setTitle("存储权限不可用")
//                .setMessage("请在-应用设置-权限-中，允许支付宝使用存储权限来保存用户数据")
//                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 跳转到应用设置界面
//                        goToAppSetting();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                }).setCancelable(false).show();
//    }
//
//    // 跳转到当前应用的设置界面
//    private void goToAppSetting() {
//        Intent intent = new Intent();
//
//        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);
//
//        startActivityForResult(intent, 123);
//    }
//
//    //
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 123) {
//
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                // 检查该权限是否已经获取
//                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
//                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
//                if (i != PackageManager.PERMISSION_GRANTED) {
//                    // 提示用户应该去应用设置界面手动开启权限
//                    showDialogTipUserGoToAppSettting();
//                } else {
//                    if (dialog != null && dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
//                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//}
