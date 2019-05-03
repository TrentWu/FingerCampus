package com.example.fingercampus;

import android.annotation.SuppressLint;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 学生登录类
 * 用于学生账号的登录
 */
public class StudentLoginActivity extends Activity {

    private EditText account_edit;
    private EditText password_edit;
    private String TAG = "StudentLoginActivity";
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //查看SharedPreferences中是否存储了用户账号信息，来决定是否跳转到登录界面
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        if (sharedPreferences.getString(Constants.RECORD.rephone, null) != null) {
            startActivity(new Intent(StudentLoginActivity.this, MainActivity.class));
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
        account_edit = findViewById(R.id.account);
        password_edit = findViewById(R.id.password);
        account_edit.setText(dao.uQuery());
        final Button loginImageBtn = findViewById(R.id.login);
        loginImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stu_account = account_edit.getText().toString().trim();
                String stu_password = password_edit.getText().toString().trim();
                if (stu_account.equals("")) {
                    Toast.makeText(StudentLoginActivity.this, "请输入手机号码！", Toast.LENGTH_SHORT).show();
                } else if (stu_password.equals("")) {
                    Toast.makeText(StudentLoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    LoginRequest(stu_account, stu_password);
                }
            }
        });
    }

    /**
     * 用户登录请求类
     *
     * @param usphone       用户手机号
     * @param uspassword    用户密码
     */
    public void LoginRequest(final String usphone, final String uspassword) {
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
                            if (result.equals("success")) {  //注⑤
                                //利用SharedPreferences存储用户信息
                                SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Constants.RECORD.rephone, usphone);
                                editor.apply();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String netDate = getNetTime();
                                        dao.uRecordInsert(usphone, netDate);
                                        Log.d(TAG, "rephone=" + usphone + " redate=" + netDate);
                                    }
                                }).start();
                                Toast.makeText(StudentLoginActivity.this, "欢迎你，" + usphone + "！", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(StudentLoginActivity.this, MainActivity.class));
                                password_edit.setText(null);
                            } else {
                                Toast.makeText(StudentLoginActivity.this, "手机号或密码错误！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(StudentLoginActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(StudentLoginActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
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

    /**
     * 获取网络时间
     * 需要开子线程
     *
     * @return 返回中国科学院国家授时中心网址时间
     */
    public String getNetTime() {
        String netDate = "1970-01-01 00:00:00";
        URL url;
        try {
            url = new URL("http://www.ntsc.ac.cn/");
            URLConnection urlConnection = url.openConnection();//生成链接对象
            urlConnection.connect();//发出链接
            long longDate = urlConnection.getDate();//取得网站日期时间
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(longDate);
            netDate = dateFormat.format(calendar.getTime());
            return netDate;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return netDate;
    }

}
