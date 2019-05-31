
package com.mvpframe.bridge;

/**
 * <与Bridge模块一一对应，用以在BridgeFactory获取某个Bridge对应的Key>
 * Data：2018/12/18
 *
 * @author yong
 */
public class Bridges {

    private Bridges() {
        throw new IllegalStateException("Bridges class");
    }

    /**
     * 本地缓存(sd卡存储和手机内部存储)
     */
    public static final String LOCAL_FILE_STORAGE = "com.mvpframe.LOCAL_FILE_STORAGE";

    /**
     * SharedPreference缓存
     */
    public static final String SHARED_PREFERENCE = "com.mvpframe.SHARED_PREFERENCE";

    /**
     * 安全
     */
    public static final String SECURITY = "com.mvpframe.SECURITY";

    /**
     * 用户Session
     */
    public static final String USER_SESSION = "com.mvpframe.USER_SESSION";

    /**
     * CoreService，主要维护功能模块
     */
    public static final String CORE_SERVICE = "com.mvpframe.CORE_SERVICE";


    /**
     * 数据库模块
     */
    public static final String DATABASE = "com.mvpframe.DATABASE";

    /**
     * http请求
     */
    public static final String HTTP = "com.mvpframe.HTTP";

}
