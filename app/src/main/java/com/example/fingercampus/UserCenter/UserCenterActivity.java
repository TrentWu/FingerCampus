package com.example.fingercampus.UserCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.Net.LoginActivity;
import com.example.fingercampus.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserCenterActivity extends Activity {

    Intent intent;
    String usphone;
    String TAG = "UserCenterActivity";

    TextView nametv;
    String name;
    TextView telephonetv;
    String telephone;
    TextView sextv;
    String sex;
    TextView classroomtv;
    String classroom;
    TextView studentidtv;
    String studentid;
    Button backbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person);
        intent =getIntent();
        usphone = intent.getStringExtra("usphone");
        init();
    }

    private void init(){
        nametv = findViewById(R.id.name);
        telephonetv = findViewById(R.id.telephone);
        sextv = findViewById(R.id.sex);
        classroomtv = findViewById(R.id.classroom);
        studentidtv = findViewById(R.id.studentid);
        backbtn = findViewById(R.id.back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     *
     * @param usphone    用户手机号
     */
    public void UserRequest(final String usphone, final String uspassword) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "User";    //注②

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
                                name = jsonObject.getString("name");
                                telephone = jsonObject.getString("telephone");
                                sex = jsonObject.getString("sex");
                                classroom = jsonObject.getString("classroom");
                                studentid = jsonObject.getString("studentid");
                                nametv.setText(name);
                                telephonetv.setText(telephone);
                                sextv.setText(sex);
                                classroomtv.setText(classroom);
                                studentidtv.setText(studentid);
                            } else {
                                Toast.makeText(UserCenterActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
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
                params.put("usphone", usphone);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}
