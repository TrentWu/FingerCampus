package com.example.fingercampus.Timetable;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.fingercampus.R;

public class TimetableActivity extends Activity {

    Typeface typeface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_main);
        init();
    }
    private void init() {
        typeface = Typeface.createFromAsset(getAssets(),"iconfont/iconfont.ttf");
        Button back = findViewById(R.id.back);
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
