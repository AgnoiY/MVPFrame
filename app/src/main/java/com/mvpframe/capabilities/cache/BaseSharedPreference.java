package com.mvpframe.capabilities.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.mvpframe.util.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <sharepref基础存储功能模块>
 * Data：2018/12/18
 *
 * @author yong
 */
public class BaseSharedPreference {
    private String fileName;

    private Context context;

    public BaseSharedPreference(Context context, String fileName) {
        this.fileName = fileName;
        this.context = context;
    }

    /**
     * Retrieve the package shared preferences object.
     *
     * @return
     */
    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public void put(String key, Object object) {

        if (context == null || object == null) {
            return;
        }

        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    public Integer getInt(String key, Integer defaultObject) {

        if (context == null) {
            return defaultObject;
        }

        SharedPreferences sp = getSharedPreferences();
        return sp.getInt(key, defaultObject);
    }

    public Long getLong(String key, Long defaultObject) {

        if (context == null) {
            return defaultObject;
        }

        SharedPreferences sp = getSharedPreferences();

        return sp.getLong(key, defaultObject);
    }


    public Boolean getBoolean(String key, Boolean defaultObject) {

        if (context == null) {
            return defaultObject;
        }

        SharedPreferences sp = getSharedPreferences();
        return sp.getBoolean(key, defaultObject);
    }

    public String getString(String key, String defaultObject) {

        if (context == null) {
            return defaultObject;
        }

        SharedPreferences sp = getSharedPreferences();
        return sp.getString(key, defaultObject);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object get(String key, Object defaultObject) {

        SharedPreferences sp = getSharedPreferences();

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String key) {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        SharedPreferences sp = getSharedPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        SharedPreferences sp = getSharedPreferences();
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public Map<String, ?> getAll() {
        SharedPreferences sp = getSharedPreferences();
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                LogUtil.w(e);
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                LogUtil.w(e);
            }
            editor.commit();
        }
    }
}
