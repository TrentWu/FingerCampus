package com.example.fingercampus.Timetable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.Constants;
import com.example.fingercampus.Database.Dao;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCourseActivity extends Activity {

    private Spinner select_csday;
    private Spinner select_csbweek;
    private Spinner select_cseweek;

    private Button finishButton;
    private EditText csname_edit;
    private EditText csbweek_edit;
    private EditText cseweek_edit;
    private Spinner csday_edit;
    private Spinner csbtime_edit;
    private Spinner csetime_edit;
    private EditText classroom_edit;
    private EditText teacher_edit;
    private String TAG = "AddCourseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcourse);
//        setFinishOnTouchOutside(false);

        select_csday = (Spinner)findViewById(R.id.csday);
        select_csbweek = (Spinner)findViewById(R.id.csbtime);
        select_cseweek = (Spinner)findViewById(R.id.csetime);

        csname_edit = (EditText) findViewById(R.id.csname);
        csbweek_edit = (EditText)findViewById(R.id.csbweek);
        cseweek_edit = (EditText)findViewById(R.id.cseweek);
        csday_edit = (Spinner)findViewById(R.id.csday);
        csbtime_edit = (Spinner)findViewById(R.id.csbtime);
        csetime_edit = (Spinner)findViewById(R.id.csetime);
        classroom_edit = (EditText)findViewById(R.id.clrid);
        teacher_edit= (EditText)findViewById(R.id.tcid);

        finishButton = (Button) findViewById(R.id.finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String csname = csname_edit.getText().toString().trim();
                String csbweek = csbweek_edit.getText().toString().trim();
                String cseweek = cseweek_edit.getText().toString().trim();
                String csday = select_csday.getSelectedItem().toString();
                String csbtime = select_csbweek.getSelectedItem().toString();
                if(csbtime=="一")
                {
                    csbtime="08:00:00";
                }else if(csbtime=="二"){
                    csbtime="10:00:00";
                }else if(csbtime=="三"){
                    csbtime="14:00:00";
                }else if(csbtime=="四"){
                    csbtime="16:00:00";
                }else if(csbtime=="五"){
                    csbtime="19:00:00";
                }else if(csbtime=="六"){
                    csbtime="21:00:00";
                }
                String csetime = select_cseweek.getSelectedItem().toString();
                if(csetime=="一")
                {
                    csetime="08:00:00";
                }else if(csbtime=="二"){
                    csetime="10:00:00";
                }else if(csbtime=="三"){
                    csetime="14:00:00";
                }else if(csbtime=="四"){
                    csetime="16:00:00";
                }else if(csbtime=="五"){
                    csetime="19:00:00";
                }else if(csbtime=="六"){
                    csetime="21:00:00";
                }
                String classroom = classroom_edit.getText().toString().trim();
                String teacher = teacher_edit.getText().toString().trim();
                if (csname==null||csname.isEmpty() || csbweek==null||csbweek.isEmpty() || cseweek==null||cseweek.isEmpty() || csday==null||csday.isEmpty() || csbtime==null||csbtime.isEmpty() || csetime==null||csetime.isEmpty()){
                    Toast.makeText(AddCourseActivity.this, "请输入完整信息！", Toast.LENGTH_SHORT).show();
                }else{
                    TimetableRequest(csname,csbweek,cseweek,csday,csbtime,csetime,classroom,teacher);
                }
            }
        });

    }

    /*
     * 用户登录请求类
     *
     * @param csname    课程名
     * @param cwbtime   开始周次
     * @param cwetime   结束周次
     * @param csday       星期几
     * @param csbtime   开始节次
     * @param csetime   结束节次
     * @param clrid     教室
     * @param tcid      教师
     */
    public void TimetableRequest(final String csname, final String csbweek, final String cseweek, final String csday, final String csbtime, final String csetime, final String clrid, final String tcid) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "Timetable";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                    String result = jsonObject.getString("Result");  //注④
                    if (result.equals("success")) {  //注⑤
                        Toast.makeText(AddCourseActivity.this, "导入成功！" , Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddCourseActivity.this, TimetableActivity.class));
                        finish();
                    }
                    else{
                        Toast.makeText(AddCourseActivity.this, "导入失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    finishButton.setClickable(true);
                    Toast.makeText(AddCourseActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finishButton.setClickable(true);
                Toast.makeText(AddCourseActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                params.put("csname", csname);  //注⑥
                params.put("csbweek", csbweek);
                params.put("cseweek", cseweek);
                params.put("csday", csday);
                params.put("csbtime", csbtime);
                params.put("csetime", csetime);
                params.put("clrid", clrid);
                params.put("tcid", tcid);
                LogUtil.d(TAG, "csname=" + csname);
                LogUtil.d(TAG, "csbweek=" + csbweek);
                LogUtil.d(TAG, "cseweek=" + cseweek);
                LogUtil.d(TAG, "csday=" + csday);
                LogUtil.d(TAG, "csbtime=" + csbtime);
                LogUtil.d(TAG, "csetime=" + csetime);
                LogUtil.d(TAG, "clrid=" + clrid);
                LogUtil.d(TAG, "tcid=" + tcid);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}
