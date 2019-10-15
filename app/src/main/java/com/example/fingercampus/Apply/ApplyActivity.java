package com.example.fingercampus.Apply;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.MainActivity;
import com.example.fingercampus.Net.RegisterActivity;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.SMSSDK;

public class ApplyActivity extends AppCompatActivity {
    private EditText name,phone,classroom_number,date,users_number,section;
    private Button submit,back;
    private Typeface typeface;

    private String TAG = "ApplyActivity";

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
                LogUtil.d(TAG, "我要退出啦！");
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText = name.getText().toString().trim();
                String phoneText = phone.getText().toString().trim();
                String classroom_numberText = classroom_number.getText().toString().trim();
                String dateText = date.getText().toString().trim();
                String users_numberText = users_number.getText().toString().trim();
                String sectionText = section.getText().toString().trim();
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
                    ApplyRequest(nameText, phoneText, classroom_numberText, dateText, users_numberText, sectionText);
                    //Toast.makeText(ApplyActivity.this,"功能尚不完善，敬请期待！",Toast.LENGTH_SHORT).show();
                }
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
                        //Toast.makeText(ApplyActivity.this, "我的申请", Toast.LENGTH_SHORT).show();
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
        name=findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        classroom_number = findViewById(R.id.classroom_number);
        date = findViewById(R.id.date);
        users_number = findViewById(R.id.users_number);
        section = findViewById(R.id.section);
    }

    public void ApplyRequest(final String name, final String usphone,final String classroom,final String date,final String usersnumber,final String section) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";
        final String tag = "Apply";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {
                                Toast.makeText(ApplyActivity.this, "申请提交成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "申请提交失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "请正确填写申请信息！", Toast.LENGTH_SHORT).show();
                            LogUtil.e(TAG , e.getMessage()+" ");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "请检查网络连接，并重试！", Toast.LENGTH_SHORT).show();
                LogUtil.e(TAG, error.getMessage()+" ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                params.put("name",name);
                params.put("usphone", usphone);
                params.put("date",date);
                params.put("classroom",classroom);
                params.put("usersnumber",usersnumber);
                params.put("section",section);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}
