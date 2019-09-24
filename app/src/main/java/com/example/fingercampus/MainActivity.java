package com.example.fingercampus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fingercampus.Apply.ApplyActivity;
import com.example.fingercampus.Attendance.AttendanceActivity;
import com.example.fingercampus.Database.Dao;
import com.example.fingercampus.Net.LoginActivity;
import com.example.fingercampus.Repair.RepairActivity;
import com.example.fingercampus.Timetable.TimetableActivity;
import com.example.fingercampus.Tools.LogUtil;

/**
 * 主活动类
 */
public class MainActivity extends AppCompatActivity {

    private long exitTime = 0;
    private String usphone;

    /**
     * 绑定res/menu中的菜单到活动
     * @param menu  需要绑定的菜单
     * @return      处理结果
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.LEVEL = LogUtil.VERBOSE;//控制日志信息的打印 NOTHING=不打印任何日志 VERBOSE=打印所有日志信息

        new Dao(this);//创建SQLite数据库实例
        initToolbar();
        initViewEvent();
    }

    /**
     * 初始化视图组件交互事件
     */
    private void initViewEvent() {
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(MainActivity.this, "注销成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        Button repair = findViewById(R.id.repair);
        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                usphone = intent.getStringExtra("usphone");
                intent = new Intent(MainActivity.this,RepairActivity.class);
                intent.putExtra("usphone",usphone);
                startActivity(intent);
            }
        });
        Button apply = findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"申请教室",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ApplyActivity.class));
            }
        });
    }

    /**
     * 初始化工具栏视图及交互事件
     */
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.toolbar_attendance:
                        startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
                        Toast.makeText(MainActivity.this, "签到", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.toolbar_timetable:
                        startActivity(new Intent(MainActivity.this, TimetableActivity.class));
                        Toast.makeText(MainActivity.this, "课程表", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "啦啦啦", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        Button user = findViewById(R.id.toolbar_user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 跳转到个人中心活动
                Toast.makeText(MainActivity.this, "个人中心", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 监听返回键，实现按两次返回键退出程序
     * @param keyCode   键码
     * @param event     事件
     * @return          处理结果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if ((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
