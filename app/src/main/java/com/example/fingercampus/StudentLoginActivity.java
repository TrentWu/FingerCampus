package com.example.fingercampus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 学生登录类
 * 用于学生账号的登录
 */
public class StudentLoginActivity extends Activity {

    private EditText account_edit;
    private EditText password_edit;
    private Button login_ibtn;
    private Dao dao = new Dao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        init();
    }

    //初始化登录界面
    public void init() {
        account_edit = findViewById(R.id.account);
        password_edit = findViewById(R.id.password);
        login_ibtn = findViewById(R.id.login);
        login_ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stu_account = account_edit.getText().toString().trim();
                String stu_password = password_edit.getText().toString().trim();
                if (stu_account.equals("")) {
                    Toast.makeText(StudentLoginActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
                } else if (stu_password.equals("")) {
                    Toast.makeText(StudentLoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    StuLoginRequest(stu_account, stu_password);
                }
            }
        });
    }

    public void StuLoginRequest(final String accountNumber, final String password) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/LoginServlet";    //注①
        String tag = "Login";    //注②

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
                            String stdname = jsonObject.getString("stdname");
                            if (result.equals("success")) {  //注⑤
                                //利用SharedPreferences存储用户信息
                                SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constans.USER.uphone, accountNumber);
                                editor.apply();
                                Toast.makeText(StudentLoginActivity.this, "欢迎你，" + stdname + "！", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(StudentLoginActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(StudentLoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(StudentLoginActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(StudentLoginActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", accountNumber);  //注⑥
                params.put("Password", password);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}
