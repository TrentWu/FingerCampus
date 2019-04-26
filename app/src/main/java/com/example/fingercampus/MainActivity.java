package com.example.fingercampus;

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
        setContentView(R.layout.activity_main);

        init();
    }
    //初始化主程序
    private void init(){
        dao = new Dao(this);
    }
}
