package com.mvpframe.bean.permissions;

public class PermissionsModel {

    // 要申请的权限
    private String permissions;
    // 弹窗标题
    private String title;
    // 弹窗内容
    private String content;
    // 设置打开的按钮文字 (默认:立即开启)
    private String open;

    public String getPermissions() {
        return permissions;
    }

    public PermissionsModel setPermissions(String permissions) {
        this.permissions = permissions;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PermissionsModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PermissionsModel setContent(String content) {
        this.content = content;
        return this;
    }

    public String getOpen() {
        return open;
    }

    public PermissionsModel setOpen(String open) {
        this.open = open;
        return this;
    }
}
