
package com.mvpframe.bridge.sharepref;

import android.content.Context;

import com.mvpframe.bridge.BridgeFactory;
import com.mvpframe.bridge.BridgeLifeCycleListener;
import com.mvpframe.bridge.Bridges;

/**
 * <管理SharedPreference存储、读取>
 * Data：2018/12/18
 *
 * @author yong
 */
public class SharedPrefManager implements BridgeLifeCycleListener {

    /**
     * SharedPreference文件名列表
     */
    private static final String PREF_NAME_USERINFO = "userinfo";

    private static final String PREF_NAME_SETTING = "setting";

    private static final String PREF_NAME_OTHERS = "others";

    /**
     * 用户信息缓存
     */
    private SharedPrefUser mSharedPrefUser;

    /**
     * 设置信息缓存
     */
    private SharedPrefSetting mSharedPrefSetting;
    /**
     * 其他信息缓存
     */
    private SharedPrefOthers mSharedPrefOthers;

    private Context mApplicationContext;

    @Override
    public void initOnApplicationCreate(Context context) {
        mApplicationContext = context;
    }

    @Override
    public void clearOnApplicationQuit() {
        //退出App清理数据
    }


    /**
     * 用户信息缓存
     *
     * @return
     */
    public SharedPrefUser getKDPreferenceUserInfo() {
        if (mSharedPrefUser == null) {
            mSharedPrefUser = new SharedPrefUser(mApplicationContext, PREF_NAME_USERINFO);
        }
        return mSharedPrefUser;
    }

    /**
     * 设置信息缓存
     */
    public SharedPrefSetting getKDPreferenceTestSetting() {
        if (mSharedPrefSetting == null) {
            mSharedPrefSetting = new SharedPrefSetting(mApplicationContext, PREF_NAME_SETTING);
        }
        return mSharedPrefSetting;
    }

    /**
     * 设置信息缓存
     */
    public SharedPrefOthers getKDPreferenceOthers() {
        if (mSharedPrefOthers == null) {
            mSharedPrefOthers = new SharedPrefOthers(mApplicationContext, PREF_NAME_OTHERS);
        }
        return mSharedPrefOthers;
    }

    /**
     * 获取用户信息缓存基类
     */
    public static SharedPrefUser getUser() {

        SharedPrefManager sharedPref = BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);

        if (sharedPref == null) return null;

        return sharedPref.getKDPreferenceUserInfo();

    }

    /**
     * 获取设置信息缓存基类
     */
    public static SharedPrefSetting getSetting() {

        SharedPrefManager sharedPref = BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);

        if (sharedPref == null) return null;

        return sharedPref.getKDPreferenceTestSetting();


    }

    /**
     * 获取用户信息缓存基类
     */
    public static SharedPrefOthers getOthers() {

        SharedPrefManager sharedPref = BridgeFactory.getBridge(Bridges.SHARED_PREFERENCE);

        if (sharedPref == null) return null;

        return sharedPref.getKDPreferenceOthers();

    }
}
