package com.mvpframe.bridge.sharepref;

import android.content.Context;

import com.mvpframe.capabilities.cache.BaseSharedPreference;


/**
 * <设置信息缓存>
 * Data：2018/12/18
 *
 * @author yong
 */
public class SharedPrefSetting extends BaseSharedPreference {
    /**
     * 声音提醒 默认已开启
     */
    public static final String SOUND_REMINDER = "sound_reminder";

    /**
     * 震动提醒 默认已开启
     */
    public static final String VIBRATION_REMINDER = "vibration_reminder";

    public SharedPrefSetting(Context context, String fileName) {
        super(context, fileName);
    }
}
