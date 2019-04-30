package com.example.fingercampus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
/**
 * 数据库操作类
 * 执行数据库的增删改查
 */
public class Dao {

    private final SQLiteDatabase databaseHelper;
    private final String TAG =  "Dao";

    public Dao(Context context) {
        //获取单例模式数据库
        databaseHelper = DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    /**
     * 插入学生信息
     *
     * @param uphone    学生手机号
     * @return 学生id
     */
    public long uinsert(String uphone) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constans.USER.uphone, uphone);
        return databaseHelper.insert(Constans.TABLE_NAME.USER, null, contentValues);
    }
}
