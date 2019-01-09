package com.mvpframe.bridge.sharePref;

import android.content.Context;
import android.text.TextUtils;

import com.mvpframe.capabilities.cache.BaseSharedPreference;
import com.mvpframe.util.GeneralUtils;


/**
 * <用户信息缓存>
 * Data：2018/12/18
 *
 * @author yong
 */
public class SharedPrefUser extends BaseSharedPreference {
    /**
     * 用户ID
     */
    public static final String USER_ID = "user_id";
    /**
     * 用户token
     */
    public static final String USER_TOKEN = "token";
    /**
     * 用户名
     */
    public static final String USER_NAME = "user_name";

    public SharedPrefUser(Context context, String fileName) {
        super(context, fileName);
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public boolean isLoginNoStart() {
        return GeneralUtils.isNullOrZeroLenght(this.getString(USER_TOKEN, ""));
    }

}
