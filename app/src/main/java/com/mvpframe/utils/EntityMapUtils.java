package com.mvpframe.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <将对象转换成Map>
 * <p>
 * Data：2019/06/24
 *
 * @author yong
 */
public class EntityMapUtils {

    private static final String TAG = EntityMapUtils.class.getSimpleName();

    EntityMapUtils() {
        throw new IllegalStateException("EntityMapUtils class");
    }

    /**
     * 将对象转换成Map
     *
     * @param obj 带参数的对象
     * @return
     */
    public static Map<String, Object> getEntityMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Field[] fields = obj.getClass().getDeclaredFields();//获取类的各个属性值
        for (Field field : fields) {
            String fieldName = field.getName();//获取类的属性名称
            if (getValueByFieldName(fieldName, obj) != null)//获取类的属性名称对应的值
                map.put(fieldName, getValueByFieldName(fieldName, obj));
        }
        return map;
    }

    /**
     * 获取类的属性名称对应的值
     *
     * @param fieldName
     * @param object
     * @return
     */
    public static Object getValueByFieldName(String fieldName, Object object) {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        try {
            Class[] classes = new Class[]{};
            Object[] objects = new Object[]{};
            Method method = object.getClass().getMethod(getter, classes);
            return method.invoke(object, objects);
        } catch (Exception e) {
            LogUtils.w(TAG, e);
            return null;
        }
    }
}
