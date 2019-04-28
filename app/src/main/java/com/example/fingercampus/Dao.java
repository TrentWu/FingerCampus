package com.example.fingercampus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Filter;

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
     * @param upassword 学生密码
     * @return 学生id
     */
    public long uinsert(String uphone, String upassword) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constans.USER.uphone, uphone);
        contentValues.put(Constans.USER.upassword, upassword);
        return databaseHelper.insert(Constans.TABLE_NAME.USER, null, contentValues);
    }

    //学生信息删除
    public void udelete() {

    }

    //学生信息更新
    public void uupdate() {

    }

    /**
     * 查询单个学生信息，返回Map
     *
     * @param uphone
     * @return
     */
    public Map uquery(String uphone) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constans.USER.uphone, uphone);
        Cursor c = databaseHelper.query(Constans.TABLE_NAME.USER,
                null,
                Constans.USER.uphone,
                new String[]{uphone},
                null,
                null,
                null);
        c.moveToNext();
        Map map = new HashMap();
        map.put(Constans.USER.uname, c.getString(1));
        map.put(Constans.USER.usex, c.getString(2));
        map.put(Constans.USER.ubirthday, c.getString(3));
        map.put(Constans.USER.uage, c.getInt(4));
        map.put(Constans.USER.uuniversity, c.getString(5));
        map.put(Constans.USER.ucollege, c.getString(6));
        map.put(Constans.USER.umajor, c.getString(7));
        map.put(Constans.USER.uclass, c.getString(8));
        map.put(Constans.USER.uno, c.getString(9));
        map.put(Constans.USER.uemail, c.getString(10));
        map.put(Constans.USER.uphone, c.getString(11));
        map.put(Constans.USER.upassword, c.getString(12));
        c.close();
        return map;
    }


    /**
     * 登录确认，成功返回学生姓名，失败返回null
     *
     * @param uphone    学生手机号
     * @param upassword 学生密码
     * @return 学生姓名
     */
    public String uloginConfirm(String uphone, String upassword) {
        Cursor cursor = databaseHelper.query(Constans.TABLE_NAME.USER,
                null,
                Constans.USER.uphone + "=? and " + Constans.USER.upassword + "=?",
                new String[]{uphone, upassword},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            String uname = cursor.getString(1);
            Log.d(TAG, "uname = " + uname);
            cursor.close();
            return uname;
        }
        return null;
    }

}
