package com.example.fingercampus.Net;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.Constants;
import com.example.fingercampus.Database.Dao;
import com.example.fingercampus.MainActivity;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.Time;
import com.example.fingercampus.Tools.Verification;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 学生登录类
 * 用于学生账号的登录
 */
public class LoginActivity extends Activity {

    private EditText account_edit;
    private EditText password_edit;
    private Button loginButton;
    private String TAG = "LoginActivity";
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //查看SharedPreferences中是否存储了用户账号信息，来决定是否跳转到登录界面
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        if (sharedPreferences.getString(Constants.RECORD.rephone, null) != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        //使用阿里图标库
        setContentView(R.layout.activity_start);
//        Typeface typeface=Typeface.createFromAsset(getAssets(),"iconfont");//添加字体包
//        TextView textView=findViewById(R.id.test);
//        textView.setTypeface(typeface);
        init();
    }

    //初始化登录界面
    public void init() {
        dao = new Dao(this);
        MobSDK.init(this);
        account_edit = findViewById(R.id.account);
        account_edit.setText(dao.uQuery());
        password_edit = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = account_edit.getText().toString().trim();
                String password = password_edit.getText().toString().trim();
                if (account.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    if (Verification.phoneNumber(account)) {
                        loginButton.setClickable(false);
                        LoginRequest(account, password);
                    } else {
                        Toast.makeText(LoginActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * 用户登录请求类
     *
     * @param usphone    用户手机号
     * @param uspassword 用户密码
     */
    public void LoginRequest(final String usphone, final String uspassword) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "Login";    //注②

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
                                //利用SharedPreferences存储用户信息
                                SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.RECORD.rephone, usphone);
                                editor.apply();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String netDate = Time.getNetTime();
                                        dao.uRecordInsert(usphone, netDate);
                                        Log.d(TAG, "rephone=" + usphone + " redate=" + netDate);
                                    }
                                }).start();
                                loginButton.setClickable(true);
                                Toast.makeText(LoginActivity.this, "欢迎你，" + usphone + "！", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                password_edit.setText(null);
                            } else {
                                loginButton.setClickable(true);
                                Toast.makeText(LoginActivity.this, "手机号或密码错误！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            loginButton.setClickable(true);
                            Toast.makeText(LoginActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginButton.setClickable(true);
                Toast.makeText(LoginActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                params.put("usphone", usphone);  //注⑥
                params.put("uspassword", uspassword);
                Log.d(TAG, "usphone=" + usphone);
                Log.d(TAG, "uspassword=" + uspassword);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}
