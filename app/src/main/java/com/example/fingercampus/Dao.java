package com.example.fingercampus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 数据库操作类
 * 执行数据库的增删改查
 */
class Dao {

    private final SQLiteDatabase databaseHelper;
    private final String TAG = "Dao";

    Dao(Context context) {
        //获取单例模式数据库
        databaseHelper = DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    /**
     * 插入用户登录记录
     *
     * @param rephone 登录手机号
     * @param redate  登录时间
     */
    void uRecordInsert(String rephone, String redate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.RECORD.rephone, rephone);
        contentValues.put(Constants.RECORD.redate, redate);
        long reid = databaseHelper.insert(Constants.TABLE_NAME.RECORD, null, contentValues);
        Log.d(TAG, "reid=" + reid + "rephone=" + rephone + " redate=" + redate);
    }

    /**
     * 查询最近一条登录信息
     *
     * @return 登录手机号
     */
    String uQuery() {
        Cursor cursor = databaseHelper.query(Constants.TABLE_NAME.RECORD, null, null, null, null, null, Constants.RECORD.reid);
        if (cursor.moveToLast()) {
            String rephone = cursor.getString(1);
            cursor.close();
            Log.d(TAG, "rephone=" + rephone);
            return rephone;
        }
        cursor.close();
        return null;
    }
}
