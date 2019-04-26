package com.example.fingercampus;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * 学生登录类
 * 用于学生账号的登录
 */
public class StudentLoginActivity extends Activity {

    private EditText account_edit;
    private EditText password_edit;
    private ImageButton loginorup_ibtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
    }

    //初始化登录界面
    public void init(){
        account_edit = findViewById(R.id.account);
        password_edit = findViewById(R.id.password);
        loginorup_ibtn = findViewById(R.id.loginorup);
    }


}
