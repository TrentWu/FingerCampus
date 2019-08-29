package com.example.fingercampus.Apply;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.fingercampus.MainActivity;
import com.example.fingercampus.R;

public class ApplyActivity extends AppCompatActivity {
    EditText name,phone,classroom_number,date,users_number,section;
    Button submit,back;
    SQLiteDatabase db;
    Typeface typeface;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_myapplication, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applyforclass);
        findById();
        init();
    }

    private void init() {
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ApplyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        db=openOrCreateDatabase("apply.db",MODE_PRIVATE,null);
        db.execSQL("create table if not exists afc(id integer primary key autoincrement,name text not null,phone phone not null," +
                "classroom text not null,date text not null,users_number number not null,section text not null)");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toSql();
                Toast.makeText(ApplyActivity.this,"功能尚不完善，敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar_apply);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.toolbar_myapplication:
                        Toast.makeText(ApplyActivity.this, "我的申请", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ApplyActivity.this, MyApplication.class));
                        break;
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }
        //myapplication.setOnMenuItemClickListener(onMenuItemClick);


    private void findById() {
        typeface = Typeface.createFromAsset(getAssets(),"iconfont/iconfont.ttf");
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        //myapplication = findViewById(R.id.myapplication);
        name=findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        classroom_number = findViewById(R.id.classroom_number);
        date = findViewById(R.id.date);
        users_number = findViewById(R.id.users_number);
        section = findViewById(R.id.section);
    }

    void  toSql(){
        String nameText = name.getText().toString();
        String phoneText = phone.getText().toString();
        String classroom_numberText = classroom_number.getText().toString();
        String dateText = date.getText().toString();
        String users_numberText = users_number.getText().toString();
        String sectionText = section.getText().toString();
        if(nameText.equals("")){
            Toast.makeText(ApplyActivity.this,"请输入申请人姓名",Toast.LENGTH_SHORT).show();
        }
        else if(phoneText.equals("")){
            Toast.makeText(ApplyActivity.this,"请输入联系电话",Toast.LENGTH_SHORT).show();
        }
        else if(classroom_numberText.equals("")){
            Toast.makeText(ApplyActivity.this,"请输入教室编号",Toast.LENGTH_SHORT).show();
        }
        else if (dateText.equals("")) {
            Toast.makeText(ApplyActivity.this,"请输入教室使用日期",Toast.LENGTH_SHORT).show();
        }
        else if (users_numberText.equals("")) {
            Toast.makeText(ApplyActivity.this,"请输入使用人数",Toast.LENGTH_SHORT).show();
        }
        else if(sectionText.equals("")){
            Toast.makeText(ApplyActivity.this,"请输入教室使用节次",Toast.LENGTH_SHORT).show();
        }
        else {
            String sql = "insert into afc(id,name,phone,classroom,date,users_number,section) " +
                    "values(null,'" + nameText + "','" + phoneText + "','" + classroom_numberText + "','" + dateText + "'," +
                    "'" + users_numberText + "','" + sectionText + "')";
            db.execSQL(sql);
            Toast.makeText(ApplyActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
        }

    }
}
