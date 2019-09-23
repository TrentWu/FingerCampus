package com.example.fingercampus.Timetable;

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
import com.example.fingercampus.MainActivity;
import com.example.fingercampus.Net.LoginActivity;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCourseActivity extends Activity {

    private Spinner select_csday;
    private Spinner select_csbtime;
    private Spinner select_csetime;
    private Spinner select_csbweek;
    private Spinner select_cseweek;

    private Button finishButton;
    private EditText csname_edit;
    private EditText classroom_edit;
    private EditText teacher_edit;
    private String TAG = "AddCourseActivity";

    private String usphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_addcourse);

        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        usphone = sharedPreferences.getString(Constants.RECORD.rephone, null);

        select_csday = findViewById(R.id.csday);
        select_csbtime = findViewById(R.id.csbtime);
        select_csetime = findViewById(R.id.csetime);
        select_csbweek = findViewById(R.id.csbweek);
        select_cseweek = findViewById(R.id.cseweek);

        csname_edit = findViewById(R.id.csname);
        classroom_edit = findViewById(R.id.clrid);
        teacher_edit = findViewById(R.id.tcid);

        finishButton = findViewById(R.id.finish);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String csname = csname_edit.getText().toString().trim();
                String csbweek = select_csbweek.getSelectedItem().toString();
                String cseweek = select_cseweek.getSelectedItem().toString();
                String csday = select_csday.getSelectedItem().toString();
                String csbtime = select_csbtime.getSelectedItem().toString();
                switch (csbtime) {
                    case "一":
                        csbtime = "08:00:00";
                        break;
                    case "二":
                        csbtime = "10:10:00";
                        break;
                    case "三":
                        csbtime = "14:00:00";
                        break;
                    case "四":
                        csbtime = "16:00:00";
                        break;
                    case "五":
                        csbtime = "19:00:00";
                        break;
                    case "六":
                        csbtime = "21:00:00";
                        break;
                }
                String csetime = select_csetime.getSelectedItem().toString();
                switch (csetime) {
                    case "一":
                        csetime = "09:50:00";
                        break;
                    case "二":
                        csetime = "12:00:00";
                        break;
                    case "三":
                        csetime = "15:50:00";
                        break;
                    case "四":
                        csetime = "17:50:00";
                        break;
                    case "五":
                        csetime = "20:50:00";
                        break;
                    case "六":
                        csetime = "21:50:00";
                        break;
                }
                String classroom = classroom_edit.getText().toString().trim();
                String teacher = teacher_edit.getText().toString().trim();
                if (csname.isEmpty() || csbweek.isEmpty() || cseweek.isEmpty() || csday.isEmpty() || csbtime.isEmpty() || csetime.isEmpty()) {
                    Toast.makeText(AddCourseActivity.this, "请输入完整信息！", Toast.LENGTH_SHORT).show();
                } else {
                    TimetableRequest(csname, csbweek, cseweek, csday, csbtime, csetime, classroom, teacher);
                }
            }
        });

    }

    /*
     * 用户登录请求类
     *
     * @param csname    课程名
     * @param csbweek   开始周次
     * @param cseweek   结束周次
     * @param csday     星期几
     * @param csbtime   开始节次
     * @param csetime   结束节次
     * @param clrname   教室名称
     * @param tcname    教师名称
     */
    public void TimetableRequest(final String csname, final String csbweek, final String cseweek, final String csday, final String csbtime, final String csetime, final String clrname, final String tcname) {
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
                        Toast.makeText(AddCourseActivity.this, "导入成功！", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddCourseActivity.this, TimetableActivity.class));
                        finish();
                    } else {
                        Toast.makeText(AddCourseActivity.this, "导入失败！", Toast.LENGTH_LONG).show();
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
                params.put("clrname", clrname);
                params.put("tcname", tcname);
                params.put("usphone", usphone);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}
