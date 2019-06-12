package com.mvpframe.bridge.security;

import android.content.Context;

import com.mvpframe.bridge.BridgeLifeCycleListener;

/**
 * <加解密管理类>
 * Data：2018/12/18
 *
 * @author yong
 */
public class SecurityManager implements BridgeLifeCycleListener {

    @Override
    public void initOnApplicationCreate(Context context) {
        //开始进入Applicattion
    }

    @Override
    public void clearOnApplicationQuit() {
        //退出App清理数据
    }

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public String get32MD5Str(String str) {
        return str;
    }

}
