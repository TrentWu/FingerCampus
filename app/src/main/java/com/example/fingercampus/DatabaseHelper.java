package com.example.fingercampus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库管理类
 * 用于数据库的创建及更新
 * 此数据库创建使用单例模式
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static Context context;
    private static final String TAG = "DatabaseHelper";

    /**
     * @ context   上下文
     * @ name      数据库名称
     * @ factory   游标工厂
     * @ version   版本号
     */
    //将构造方法私有化，防止被外部访问
    private DatabaseHelper(Context context) {
        super(context, Constans.DATABASE_NAME, null, Constans.VERSION_CODE);
    }

    //在调用该类的getInstance()方法时才会去初始化mInstance
    public static DatabaseHelper getInstance(Context context) {
        DatabaseHelper.context = context;
        return DatabaseHelperHolder.mInstance;
    }

    //静态内部类
    //因为一个ClassLoader下同一个类只会加载一次，保证了并发时不会得到不同的对象
    public static class DatabaseHelperHolder {
        public static DatabaseHelper mInstance = new DatabaseHelper(DatabaseHelper.context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建时的回调
        Log.d(TAG, "创建数据库……");
        //创建字段
        String sqlstr =
                "CREATE TABLE " + Constans.TABLE_NAME.USER +
                        "(uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "uphone CHAR(11) NOT NULL)";
        db.execSQL(sqlstr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //升级数据库时的回调
        Log.d(TAG, "升级数据库……");

    }
}
