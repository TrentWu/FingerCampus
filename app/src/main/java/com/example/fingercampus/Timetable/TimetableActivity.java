package com.example.fingercampus.Timetable;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fingercampus.R;

public class TimetableActivity extends AppCompatActivity {

    Typeface typeface;
    private Button back;
    //星期几
    private RelativeLayout day;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_timetable, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        init();
        initToolbar();
    }

    private void init() {
        typeface = Typeface.createFromAsset(getAssets(),"iconfont.ttf");
        //调用图标
        back=findViewById(R.id.back);
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button bt = findViewById(R.id.bt1_1);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TimetableActivity.this, ShowcourseActivity.class));
            }
        });
    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.addCourse:
                        Toast.makeText(TimetableActivity.this, "添加课程", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(TimetableActivity.this, AddCourseActivity.class));
                        break;
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }
//    private void show(int phone,String csbtime,String csetime){
//
//        int csid=
//
//
//
//    }

}
