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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.LogUtil;

import java.util.HashMap;
import java.util.Map;

public class TimetableActivity extends AppCompatActivity {

    Typeface typeface;
    private Button back;

//    private String TAG = "TimetableActivity";
//
//    private Spinner select_csday;
//    private Spinner select_csbweek;
//    private Spinner select_cseweek;
//    private EditText csname_edit;
//    private EditText csbweek_edit;
//    private EditText cseweek_edit;
//    private Spinner csday_edit;
//    private Spinner csbtime_edit;
//    private Spinner csetime_edit;
//    private EditText classroom_edit;
//    private EditText teacher_edit;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_timetable, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

//        select_csday = (Spinner)findViewById(R.id.csday);
//        select_csbweek = (Spinner)findViewById(R.id.csbtime);
//        select_cseweek = (Spinner)findViewById(R.id.csetime);
//        csname_edit = (EditText) findViewById(R.id.csname);
//        csbweek_edit = (EditText)findViewById(R.id.csbweek);
//        cseweek_edit = (EditText)findViewById(R.id.cseweek);
//        csday_edit = (Spinner)findViewById(R.id.csday);
//        csbtime_edit = (Spinner)findViewById(R.id.csbtime);
//        csetime_edit = (Spinner)findViewById(R.id.csetime);
//        classroom_edit = (EditText)findViewById(R.id.clrid);
//        teacher_edit= (EditText)findViewById(R.id.tcid);

        init();
        initToolbar();

//        String csname = csname_edit.getText().toString().trim();
//        String csbweek = csbweek_edit.getText().toString().trim();
//        String cseweek = cseweek_edit.getText().toString().trim();
//        String csday = select_csday.getSelectedItem().toString();
//        String csbtime = select_csbweek.getSelectedItem().toString();
//        if(csbtime=="一")
//        {
//            csbtime="08:00:00";
//        }else if(csbtime=="二"){
//            csbtime="10:00:00";
//        }else if(csbtime=="三"){
//            csbtime="14:00:00";
//        }else if(csbtime=="四"){
//            csbtime="16:00:00";
//        }else if(csbtime=="五"){
//            csbtime="19:00:00";
//        }else if(csbtime=="六"){
//            csbtime="21:00:00";
//        }
//        String csetime = select_cseweek.getSelectedItem().toString();
//        if(csetime=="一")
//        {
//            csetime="08:00:00";
//        }else if(csbtime=="二"){
//            csetime="10:00:00";
//        }else if(csbtime=="三"){
//            csetime="14:00:00";
//        }else if(csbtime=="四"){
//            csetime="16:00:00";
//        }else if(csbtime=="五"){
//            csetime="19:00:00";
//        }else if(csbtime=="六"){
//            csetime="21:00:00";
//        }
//        String classroom = classroom_edit.getText().toString().trim();
//        String teacher = teacher_edit.getText().toString().trim();
//

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

                        startActivity(new Intent(TimetableActivity.this, AddcourseActivity.class));
                        break;
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

//    public void TimetableShow(final String csname, final String csbweek, final String cseweek, final String csday, final String csbtime, final String csetime, final String clrid, final String tcid) {
//        //请求地址
//        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
//        final String tag = "Timetable";    //注②
//
//        //取得请求队列
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//        //防止重复请求，所以先取消tag标识的请求队列
//        requestQueue.cancelAll(tag);
//
//        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()) {
//
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<>();
//                params.get("RequestType");
//                params.get("csname");
//                params.get("csbweek");
//                params.get("cseweek");
//                params.get("csday");
//                params.get("csbtime");
//                params.get("csetime");
//                params.get("clrid");
//                params.get("tcid");
//                LogUtil.d(TAG, "csname=" + csname);
//                LogUtil.d(TAG, "csbweek=" + csbweek);
//                LogUtil.d(TAG, "cseweek=" + cseweek);
//                LogUtil.d(TAG, "csday=" + csday);
//                LogUtil.d(TAG, "csbtime=" + csbtime);
//                LogUtil.d(TAG, "csetime=" + csetime);
//                LogUtil.d(TAG, "clrid=" + clrid);
//                LogUtil.d(TAG, "tcid=" + tcid);
//                return params;
//            }
//        };
//        //设置Tag标签
//        request.setTag(tag);
//
//        //将请求添加到队列中
//        requestQueue.add(request);
//    }
}
