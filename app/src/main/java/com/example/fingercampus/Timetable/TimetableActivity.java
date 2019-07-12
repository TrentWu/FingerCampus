package com.example.fingercampus.Timetable;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fingercampus.Attendance.AttendanceActivity;
import com.example.fingercampus.Database.DatabaseHelper;
import com.example.fingercampus.MainActivity;
import com.example.fingercampus.R;
import com.example.fingercampus.Repair.RepairActivity;

import java.util.ArrayList;

public class TimetableActivity extends AppCompatActivity {

    Typeface typeface;

    //星期几
    private RelativeLayout day;

    //SQLite Helper类
    //private DatabaseHelper databaseHelper = new DatabaseHelper(this, "database.db", null, 1);

    int currentCoursesNumber = 0;
    int maxCoursesNumber = 0;

    /**
     * 绑定res/menu中的菜单到活动
     * @param menu  需要绑定的菜单
     * @return      处理结果
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_timetable, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

//        //工具条
//        Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        //从数据库读取数据
//        loadData();
//
        init();
        initToolbar();
    }

//    //从数据库加载数据
//    private void loadData(){
//        ArrayList<Course> coursesList = new ArrayList<>();//课程列表
//        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
//        Cursor cursor=sqLiteDatabase.rawQuery("select * from courses",null);
//        if(cursor.moveToFirst()){
//            do {
//                coursesList.add(new Course(
//                        cursor.getString(cursor.getColumnIndex("course_name")),
//                        cursor.getString(cursor.getColumnIndex("teacher")),
//                        cursor.getString(cursor.getColumnIndex("class_room")),
//                        cursor.getInt(cursor.getColumnIndex("day")),
//                        cursor.getInt(cursor.getColumnIndex("class_start")),
//                        cursor.getInt(cursor.getColumnIndex("class_end"))));
//            }while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        //使用从数据库读取出来的课程信息来加载课程表视图
//        for (Course course : coursesList) {
//            createLeftView(course);
//            createItemCourseView(course);
//        }
//    }
//
//    //保存数据到数据库
//    private void saveData(Course course) {
//        SQLiteDatabase sqLiteDatabase =  databaseHelper.getWritableDatabase();
//        sqLiteDatabase.execSQL
//                ("insert into courses(course_name, teacher, class_room, day, class_start, class_end) " + "values(?, ?, ?, ?, ?, ?)",
//                        new String[] {course.getCourseName(),
//                                course.getTeacher(),
//                                course.getClassRoom(),
//                                course.getDay()+"",
//                                course.getStart()+"",
//                                course.getEnd()+""}
//                );
//    }
//
//    //创建"第几节数"视图
//    private void createLeftView(Course course) {
//        int endNumber = course.getEnd();
//        if (endNumber > maxCoursesNumber) {
//            for (int i = 0; i < endNumber-maxCoursesNumber; i++) {
//                View view = LayoutInflater.from(this).inflate(R.layout.left_view, null);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(110,180);
//                view.setLayoutParams(params);
//
//                TextView text = view.findViewById(R.id.class_number_text);
//                text.setText(String.valueOf(++currentCoursesNumber));
//
//                LinearLayout leftViewLayout = findViewById(R.id.left_view_layout);
//                leftViewLayout.addView(view);
//            }
//            maxCoursesNumber = endNumber;
//        }
//    }
    private void init() {
        typeface = Typeface.createFromAsset(getAssets(),"iconfont.ttf");
        Button back = findViewById(R.id.back);
        back.setTypeface(typeface);
        //调用图标
        TextView ba=findViewById(R.id.back);
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
                        startActivity(new Intent(TimetableActivity.this,AddcourseActivity.class));
                        break;
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

}
