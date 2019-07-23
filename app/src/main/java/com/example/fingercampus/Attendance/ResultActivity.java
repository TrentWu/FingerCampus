package com.example.fingercampus.Attendance;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.fingercampus.R;

/**
 * 签到结果活动
 */
public class ResultActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_result);
    }

}
