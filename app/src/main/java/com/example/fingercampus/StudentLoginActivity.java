package com.example.fingercampus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * 学生登录类
 * 用于学生账号的登录
 */
public class StudentLoginActivity extends Activity {

    private EditText account_edit;
    private EditText password_edit;
    private ImageButton login_ibtn;
    private Dao dao = new Dao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
    }

    //初始化登录界面
    public void init() {
        account_edit = findViewById(R.id.account);
        password_edit = findViewById(R.id.password);
        login_ibtn = findViewById(R.id.login);
        login_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = account_edit.getText().toString().trim();
                String password = password_edit.getText().toString().trim();
                if (account.equals("")) {
                    Toast.makeText(StudentLoginActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(StudentLoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    String uname = dao.uloginConfirm(account, password);
                    if (uname != null) {
                        //利用SharedPreferences存储用户信息
                        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constans.USER.uphone, account);
                        editor.apply();
                        Toast.makeText(StudentLoginActivity.this, "欢迎你，" + uname + "！", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(StudentLoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(StudentLoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
