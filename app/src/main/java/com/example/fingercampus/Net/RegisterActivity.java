package com.example.fingercampus.Net;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.LogUtil;
import com.example.fingercampus.Tools.VerificationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 学生注册类
 * 用于学生账户的注册
 */
public class RegisterActivity extends Activity {

    private EditText phone_edit;
    private EditText password_edit;
    private EditText code_edit;
    private Button code_btn;

    private String phone;
    private String password;
    private String code;

    private EventHandler eventHandler;

    private String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    //初始化注册界面
    private void init() {
        //SMSSDK相关事件处理程序
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = new Message();
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                handler.sendMessage(message);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        TextView back_text = findViewById(R.id.back);
        back_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        phone_edit = findViewById(R.id.phone);
        password_edit = findViewById(R.id.password);
        code_edit = findViewById(R.id.code);
        code_btn = findViewById(R.id.btncode);
        code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phone_edit.getText().toString().trim();
                password = password_edit.getText().toString().trim();
                if (phone.equals("")){
                    Toast.makeText(RegisterActivity.this, "请输入手机号！", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")){
                    Toast.makeText(RegisterActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    if (!VerificationUtil.phoneNumber(phone)) {
                        Toast.makeText(RegisterActivity.this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
                    } else if (!VerificationUtil.password(password)) {
                        Toast.makeText(RegisterActivity.this, "请输入6-16位密码！", Toast.LENGTH_SHORT).show();
                    } else {
                        SMSSDK.getVerificationCode("86", phone);
                    }
                }
            }
        });
        Button register_f = findViewById(R.id.register_f);
        register_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code = code_edit.getText().toString().trim();
                if (code.equals("")){
                    Toast.makeText(RegisterActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")){
                    Toast.makeText(RegisterActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else if (!VerificationUtil.password(password)){
                    Toast.makeText(RegisterActivity.this, "请输入6-16位密码！", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.submitVerificationCode("86", phone, code);
                }
            }
        });
    }
    public void RegisterRequest(final String usphone, final String uspassword) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";
        final String tag = "Register";

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
                                Toast.makeText(RegisterActivity.this, "注册成功，快去登录看看吧！", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (result.equals("existed")){
                                Toast.makeText(RegisterActivity.this, "该手机号已注册！", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "注册失败,请检查注册信息是否填写正确！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "请正确填写注册信息！", Toast.LENGTH_SHORT).show();
                            LogUtil.e(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "请检查网络连接，并重试！", Toast.LENGTH_SHORT).show();
                LogUtil.e(TAG, error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                params.put("usphone", usphone);
                params.put("uspassword", uspassword);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    //SMSSDK处理程序
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    MCountDownTimer mCountDownTimer = new MCountDownTimer(60000, 1000);
                    mCountDownTimer.start();
                    code_edit.requestFocus();
                    Toast.makeText(RegisterActivity.this, "验证码发送成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "验证码发送失败，请检查网络连接是否正常！", Toast.LENGTH_SHORT).show();
                }
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    RegisterRequest(phone, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入正确的验证码！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private class MCountDownTimer extends CountDownTimer {

        MCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            code_btn.setClickable(false);
            code_btn.setText(millisUntilFinished / 1000 + "秒");
        }

        @Override
        public void onFinish() {
            code_btn.setClickable(true);
            code_btn.setText("重新获取");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
