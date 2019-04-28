package com.example.fingercampus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 主活动类
 */
public class MainActivity extends AppCompatActivity {

    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //查看SharedPreferences中是否存储了用户账号信息，来决定是否跳转到登录界面
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        if (sharedPreferences.getString(Constans.USER.uphone, null) == null) {
            startActivity(new Intent(MainActivity.this, StudentLoginActivity.class));
        }
        setContentView(R.layout.activity_main);

        init();
    }

    //初始化主程序
    private void init() {
        dao = new Dao(this);
    }
}
