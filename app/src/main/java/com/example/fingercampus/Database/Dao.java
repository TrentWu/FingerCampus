package com.example.fingercampus.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fingercampus.Constants;
import com.example.fingercampus.Tools.LogUtil;

/**
 * 数据库操作类
 * 执行数据库的增删改查
 */
public class Dao {

    private final SQLiteDatabase databaseHelper;
    private final String TAG = "Dao";

    public Dao(Context context) {
        //获取单例模式数据库
        databaseHelper = DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    /**
     * 插入用户登录记录
     *
     * @param rephone 登录手机号
     * @param redate  登录时间
     */
    public void uRecordInsert(String rephone, String redate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.RECORD.rephone, rephone);
        contentValues.put(Constants.RECORD.redate, redate);
        long reid = databaseHelper.insert(Constants.TABLE_NAME.RECORD, null, contentValues);
        LogUtil.d(TAG, "reid=" + reid + "rephone=" + rephone + " redate=" + redate);
    }

    /**
     * 查询最近一条登录信息
     *
     * @return 登录手机号
     */
    public String uQuery() {
        Cursor cursor = databaseHelper.query(Constants.TABLE_NAME.RECORD, null, null, null, null, null, Constants.RECORD.reid);
        if (cursor.moveToLast()) {
            String rephone = cursor.getString(1);
            cursor.close();
            LogUtil.d(TAG, "rephone=" + rephone);
            return rephone;
        }
        cursor.close();
        return null;
    }
}
