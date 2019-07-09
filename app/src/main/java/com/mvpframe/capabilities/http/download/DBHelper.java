package com.mvpframe.capabilities.http.download;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.mvpframe.bridge.http.RetrofitHttp;
import com.mvpframe.utils.EntityMapUtils;
import com.mvpframe.utils.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库辅助类
 * <p>
 * Data：2019/07/08
 *
 * @author yong
 */
public class DBHelper extends SQLiteOpenHelper {

    /*数据库名称*/
    private static final String DB_NAME = "retrofit.download.db";
    private static final int DB_VERSION = 1;
    private static DBHelper mInstance;
    private static Context mContext;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("create table if not exists ");
//        String sql = "create table if not exists " + DB_NAME + " (" +
//                "_id" + " integer primary key autoincrement," +
//                "localUrl" + " varchar not null," +
//                "serverUrl" + " varchar not null," +
//                "totalSize" + " long not null," +
//                "currentSize" + " long not null," +
//                "state" + " enum('NONE','WAITING','LOADING','PAUSE','ERROR','FINISH') not null default 'NONE')";

        List<String> list = getEntityMap(new Download());
        for (int i = 0; i < list.size(); i += 2) {
            String key = list.get(i);
            String value = list.get(i + 1);
            if (key.equals("table")) {
                builder.append(value + " (");
                continue;
            }

            builder.append(key + value);
        }

        LogUtils.d(builder.replace(builder.length() - 1, builder.length(), ")").toString());

        db.execSQL(builder.replace(builder.length() - 1, builder.length(), ")").toString());
    }

    /**
     * 将对象中注解转换成Map
     *
     * @param load 带参数的对象
     * @return
     */
    private static List<String> getEntityMap(Download load) {
        List<String> list = new ArrayList<>();
        if (load == null) {
            return list;
        }
        Class cs = load.getClass();
        Table table = (Table) cs.getAnnotation(Table.class);
        list.add("table");
        list.add(table.value());
        Field[] fields = cs.getDeclaredFields();//获取类的各个属性值
        for (Field field : fields) {
            String fieldType = " " + field.getType().getSimpleName();//获取类的属性类型
            if (fieldType.contains("String") || fieldType.contains("State")) {
                fieldType = " varchar";
            } else if (fieldType.contains("int")) {
                fieldType = " Integer";
            }
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
                fieldType = fieldType + primaryKey.value();
            }
            if (field.isAnnotationPresent(NotNull.class)) {
                NotNull notNull = field.getAnnotation(NotNull.class);
                fieldType += notNull.value();
            }
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if (column.value().contains("id")) {
                    list.add(2, column.value());
                    list.add(3, fieldType + ",");
                    continue;
                }
                list.add(column.value());
                list.add(fieldType + ",");
            }
        }
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static DBHelper get() {
        DBHelper dbHelper = mInstance;
        if (dbHelper == null) {
            synchronized (DBHelper.class) {
                if (dbHelper == null) {
                    if (RetrofitHttp.Configure.get().getContext() == null) {
                        throw new NullPointerException("RetrofitHttp not init!");
                    }
                    mContext = RetrofitHttp.Configure.get().getContext();
                    mInstance = dbHelper = new DBHelper(mContext);
                }
            }
        }
        return dbHelper;
    }

    /**
     * 插入或者更新对象
     * 备注:有则更新，无则插入
     *
     * @param download
     * @return
     */
    public long insertOrUpdate(Download download) {
        long count = 0;
        String table = "";
        if (mInstance != null) {
            SQLiteDatabase database = mInstance.getWritableDatabase();
            ContentValues values = new ContentValues();
            List<String> list = getEntityMap(new Download());
            for (int i = 0; i < list.size(); i += 2) {
                String key = list.get(i);
                String value = list.get(i + 1);
                if (key.equals("table")) {
                    table = value;
                    continue;
                }
                Object object = EntityMapUtils.getValueByFieldName(key, download);
                if (object instanceof String) {
                    values.put(key, (String) object);
                } else if (object instanceof Integer) {
                    values.put(key, (Integer) object);
                } else if (object instanceof Long) {
                    values.put(key, (Long) object);
                }
            }
            if (TextUtils.isEmpty(table)) return 0;
            database.query(table, null, "localUrl=?", new String[]{download.getLocalUrl()}, null, null, null);
            database.insert(table, null, values);
            values.clear();
        }
        return count;
    }

    /**
     * 删除对象
     *
     * @param var1
     * @return
     */
    public int delete(Object var1) {
        int count = 0;
//        if (db != null) {
//            count = db.delete(var1);
//            LogUtils.e("count======" + count);
//        }
        return count;
    }

    /**
     * 查询数据总数
     *
     * @param var1
     * @param <T>
     * @return
     */
    public <T> long queryCount(Class<T> var1) {
        long count = 0;
//        if (db != null) {
//            count = db.queryCount(var1);
//        }
        return count;
    }

    /**
     * 查询列表
     *
     * @param var1
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> query(Class<T> var1) {
        ArrayList<T> list = new ArrayList<>();
//        if (db != null) {
//            list = db.query(var1);
//        }
        return list;
    }

    /**
     * 根据ID查询数据
     *
     * @param var1
     * @param var2
     * @param <T>
     * @return
     */
    public <T> T queryById(long var1, Class<T> var2) {
        T t = null;
//        if (db != null) {
//            t = db.queryById(var1, var2);
//        }
        return t;
    }
}
