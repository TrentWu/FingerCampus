package com.example.fingercampus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库操作类
 * 执行数据库的增删改查
 */
public class Dao {

    private final SQLiteDatabase databaseHelper;

    public Dao(Context context){
        //获取单例模式数据库
        databaseHelper = DatabaseHelper.getInstance(context).getWritableDatabase();
    }

    public long insert(String uphone, String upassword){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constans.USER.uphone, uphone);
        contentValues.put(Constans.USER.upassword, upassword);
        return databaseHelper.insert(Constans.TABLE_NAME.USER, null, contentValues);
    }

    public void delete(){

    }

    public void update(){

    }

    public void query(){

    }
}
