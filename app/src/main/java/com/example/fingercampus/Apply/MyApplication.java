package com.example.fingercampus.Apply;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import  android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.BaseAdapter;
import com.example.fingercampus.R;
import android.widget.TextView;
import java.util.ArrayList;


public class MyApplication extends Activity {

    ListView listView;
    ArrayList<Information> info;
    SQLiteDatabase db;
    Typeface typeface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myapplication);
        typeface = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        Button back = findViewById(R.id.back);
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        info = new ArrayList<>();
        db = openOrCreateDatabase("apply.db",MODE_PRIVATE,null);
        Cursor cursor = db.rawQuery("select * from afc",null);
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String classroom = cursor.getString(cursor.getColumnIndex("classroom"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String users = cursor.getString(cursor.getColumnIndex("users_number"));
            String section = cursor.getString(cursor.getColumnIndex("section"));
            Information in = new Information(id,name,phone,classroom,date,users,section);
            info.add(in);
        }
        cursor.close();
        listView = findViewById(R.id.list);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return info.size();
            }

            @Override
            public Object getItem(int i) {
                return info.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if(convertView == null ) {
                    LayoutInflater inflater = MyApplication.this.getLayoutInflater();
                    view = inflater.inflate(R.layout.application_item,null);
                }
                else {
                    view = convertView;
                }
                Information in = info.get(position);
                TextView id = view.findViewById(R.id.id);
                TextView name = view.findViewById(R.id.name);
                TextView phone  = view.findViewById(R.id.phone);
                TextView Class = view.findViewById(R.id.classroom);
                TextView date = view.findViewById(R.id.date);
                TextView user = view.findViewById(R.id.users);
                TextView section = view.findViewById(R.id.section);

                id.setText(in.getId());
                name.setText(in.getName());
                phone.setText(in.getPhone());
                Class.setText(in.getClassroom());
                date.setText(in.getDate());
                user.setText(in.getUsers());
                section.setText(in.getSection());
                return view;
            }
        });

    }
}
