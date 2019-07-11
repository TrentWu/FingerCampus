package com.example.fingercampus.Apply;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fingercampus.R;

public class ApplyActivity extends Activity {

    Typeface typeface;
    private EditText name,number,classroom_number,date,users_number,section;
    private String nameText,numberText,classroom_numberText,dateText,users_numberText,sectionText;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applyforclass);
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

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = findViewById(R.id.name);
                nameText = name.getText().toString();
                number = findViewById(R.id.number);
                numberText = number.getText().toString();
                classroom_number = findViewById(R.id.classroom_number);
                classroom_numberText = classroom_number.getText().toString();
                date = findViewById(R.id.date);
                dateText = date.getText().toString();
                users_number = findViewById(R.id.users_number);
                users_numberText = users_number.getText().toString();
                section = findViewById(R.id.section);
                sectionText = section.getText().toString();
            }
        });
    }
}
