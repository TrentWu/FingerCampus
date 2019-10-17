package com.example.fingercampus.UserCenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.R;
import com.example.fingercampus.Repair.MyRepair;
import com.example.fingercampus.Repair.RepairActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserCenterActivity extends AppCompatActivity {

    Typeface typeface;

    Intent intent;
    String usphone;
    String TAG = "UserCenterActivity";

    int stdid;

    LinearLayout stdname_ll;
    TextView stdname_tv;
    String stdname;
    LinearLayout stduniversity_ll;
    TextView stduniversity_tv;
    String stduniversity;
    LinearLayout stdcollege_ll;
    TextView stdcollege_tv;
    String stdcollege;
    LinearLayout stdmajor_ll;
    TextView stdmajor_tv;
    String stdmajor;
    LinearLayout edustartdate_ll;
    TextView edustartdate_tv;
    String edustartdate;
    LinearLayout stdphone_ll;
    TextView stdphone_tv;
    String stdphone;
    LinearLayout stdsex_ll;
    TextView stdsex_tv;
    String stdsex;
    LinearLayout stdclass_ll;
    TextView stdclass_tv;
    String stdclass;
    LinearLayout stdno_ll;
    TextView stdno_tv;
    String stdno;


    Button backbtn;
    Button btn_addstudentinfo;
    Button btn_edit;
    Button btn_save;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_usercenter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person);
        intent =getIntent();
        usphone = intent.getStringExtra("usphone");
        init();
    }

    private void init(){
        typeface = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        stdname_tv = findViewById(R.id.stdname_tv);
        stdname_ll = findViewById(R.id.stdname_ll);
        stdphone_tv = findViewById(R.id.stdphone_tv);
        stdphone_ll = findViewById(R.id.stdphone_ll);
        stdsex_tv = findViewById(R.id.stdsex_tv);
        stdsex_ll = findViewById(R.id.stdsex_ll);
        stdclass_tv = findViewById(R.id.stdclass_tv);
        stdclass_ll = findViewById(R.id.stdclass_ll);
        stdno_tv = findViewById(R.id.stdno_tv);
        stdno_ll = findViewById(R.id.stdno_ll);
        stdmajor_tv = findViewById(R.id.stdmajor_tv);
        stdmajor_ll = findViewById(R.id.stdmajor_ll);
        stdcollege_tv = findViewById(R.id.stdcollege_tv);
        stdcollege_ll = findViewById(R.id.stdcollege_ll);
        stduniversity_tv = findViewById(R.id.stduniversity_tv);
        stduniversity_ll = findViewById(R.id.stduniversity_ll);
        edustartdate_tv = findViewById(R.id.edustartdate_tv);
        edustartdate_ll = findViewById(R.id.edustartdate_ll);
        btn_addstudentinfo = findViewById(R.id.btn_addstudentinfo);
        btn_edit = findViewById(R.id.btn_edit);
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStudentRequest(stdid, stdname, stdsex, stduniversity, stdcollege, stdmajor, stdclass, stdno, edustartdate);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInfo();
                btn_edit.setVisibility(View.GONE);
                btn_save.setVisibility(View.VISIBLE);
            }
        });
        backbtn = findViewById(R.id.back);
        //调用图标
        backbtn.setTypeface(typeface);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar_usercenter);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){

                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        StudentRequest(usphone);
    }

    private void editInfo(){
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) stdname_tv.getLayoutParams();
        final EditText stdname_et = new EditText(this);
        stdname_et.setText(stdname);
        stdname_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stdname = stdname_et.getText().toString();
            }
        });
        final EditText stdsex_et = new EditText(this);
        stdsex_et.setText(stdsex);
        stdsex_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stdsex = stdsex_et.getText().toString();
            }
        });
        final EditText stdno_et = new EditText(this);
        stdno_et.setText(stdno);
        stdno_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stdno = stdno_et.getText().toString();
            }
        });
        final EditText stduniversity_et = new EditText(this);
        stduniversity_et.setText(stduniversity);
        stduniversity_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stduniversity = stduniversity_et.getText().toString();
            }
        });
        final EditText stdcollege_et = new EditText(this);
        stdcollege_et.setText(stdcollege);
        stdcollege_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stdcollege = stdcollege_et.getText().toString();
            }
        });
        final EditText stdmajor_et = new EditText(this);
        stdmajor_et.setText(stdmajor);
        stdmajor_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stdmajor = stdmajor_et.getText().toString();
            }
        });
        final EditText stdclass_et = new EditText(this);
        stdclass_et.setText(stdclass);
        stdclass_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                stdclass = stdclass_et.getText().toString();
            }
        });
        final EditText edustartdate_et = new EditText(this);
        edustartdate_et.setText(edustartdate);
        edustartdate_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edustartdate = edustartdate_et.getText().toString();
            }
        });
        stdname_ll.removeView(stdname_tv);
        stdname_ll.addView(stdname_et, layoutParams);
        stdsex_ll.removeView(stdsex_tv);
        stdsex_ll.addView(stdsex_et, layoutParams);
        stdno_ll.removeView(stdno_tv);
        stdno_ll.addView(stdno_et, layoutParams);
        stduniversity_ll.removeView(stduniversity_tv);
        stduniversity_ll.addView(stduniversity_et, layoutParams);
        stdcollege_ll.removeView(stdcollege_tv);
        stdcollege_ll.addView(stdcollege_et, layoutParams);
        stdmajor_ll.removeView(stdmajor_tv);
        stdmajor_ll.addView(stdmajor_et, layoutParams);
        stdclass_ll.removeView(stdclass_tv);
        stdclass_ll.addView(stdclass_et, layoutParams);
        edustartdate_ll.removeView(edustartdate_tv);
        edustartdate_ll.addView(edustartdate_et, layoutParams);
    }


    /**
     *
     * @param usphone    用户手机号
     */
    public void StudentRequest(final String usphone) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "Student";    //注②

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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                stdid = jsonObject.getInt("stdid");
                                stdname = jsonObject.getString("stdname");
                                stdname_tv.setText(stdname);
                                stdphone = jsonObject.getString("usphone");
                                stdphone_tv.setText(stdphone);
                                stdsex = jsonObject.getString("stdsex");
                                stdsex_tv.setText(stdsex);
                                stdclass = jsonObject.getString("stdclass");
                                stdclass_tv.setText(stdclass);
                                stdno = jsonObject.getString("stdno");
                                stdno_tv.setText(stdno);
                                stdmajor = jsonObject.getString("stdmajor");
                                stdmajor_tv.setText(stdmajor);
                                stdcollege = jsonObject.getString("stdcollege");
                                stdcollege_tv.setText(stdcollege);
                                stduniversity = jsonObject.getString("stduniversity");
                                stduniversity_tv.setText(stduniversity);
                                edustartdate = jsonObject.getString("edustartdate");
                                edustartdate_tv.setText(edustartdate);
                                btn_addstudentinfo.setVisibility(View.GONE);
                            } else if (result.equals("user")){
                                stdphone = jsonObject.getString("usphone");
                                stdphone_tv.setText(stdphone);
                                stdname_ll.setVisibility(View.GONE);
                                stdsex_ll.setVisibility(View.GONE);
                                stdclass_ll.setVisibility(View.GONE);
                                stdcollege_ll.setVisibility(View.GONE);
                                stdmajor_ll.setVisibility(View.GONE);
                                stdno_ll.setVisibility(View.GONE);
                                stduniversity_ll.setVisibility(View.GONE);
                                edustartdate_ll.setVisibility(View.GONE);
                                btn_edit.setVisibility(View.GONE);
                                Toast.makeText(UserCenterActivity.this, "该账户未填写学生信息！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(UserCenterActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserCenterActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                params.put("action", "query");
                params.put("usphone", usphone);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    /**
     * @param stdid
     * @param stdname
     * @param stdsex
     * @param stduniversity
     * @param stdcollege
     * @param stdmajor
     * @param stdclass
     * @param stdno
     * @param edustartdate
     */
    public void UpdateStudentRequest(final int stdid, final String stdname, final String stdsex, final String stduniversity, final String stdcollege, final String stdmajor, final String stdclass, final String stdno, final String edustartdate) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "Student";    //注②

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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                Toast.makeText(UserCenterActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(UserCenterActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserCenterActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                params.put("action", "update");
                params.put("stdid", String.valueOf(stdid));
                params.put("stdname", stdname);  //注⑥
                params.put("stdsex", stdsex);  //注⑥
                params.put("stduniversity", stduniversity);  //注⑥
                params.put("stdcollege", stdcollege);  //注⑥
                params.put("stdmajor", stdmajor);  //注⑥
                params.put("stdclass", stdclass);  //注⑥
                params.put("stdno", stdno);  //注⑥
                params.put("edustartdate", edustartdate);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
