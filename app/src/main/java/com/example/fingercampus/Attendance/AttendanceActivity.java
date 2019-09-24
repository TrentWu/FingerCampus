package com.example.fingercampus.Attendance;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
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
import com.example.fingercampus.PermissionsActivity;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.LogUtil;
import com.example.fingercampus.Tools.PermissionsCheckerUtil;
import com.example.fingercampus.Tools.TimeUtil;
import com.google.zxing.qrcode.encoder.QRCode;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 考勤活动类
 */
public class AttendanceActivity extends Activity {

    String usphone;

    private static final int REQUEST_CODE_SCAN = 1;
    private static final int REQUEST_CODE = 2;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final String TAG = AttendanceActivity.class.getSimpleName();
    private PermissionsCheckerUtil permissionsCheckerUtil = new PermissionsCheckerUtil(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //查看当前用户登录手机号，来实现签到
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        usphone = sharedPreferences.getString(Constants.RECORD.rephone, null);
        init();
    }

    private void init(){
        //打开扫描二维码界面
        Intent intent = new Intent(AttendanceActivity.this, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(true);//是否扫描条形码 默认为true
        config.setReactColor(R.color.firstColor);//设置扫描框四个角的颜色 默认为白色
        config.setFrameLineColor(R.color.colorless);//设置扫描框边框颜色 默认无色
        config.setScanLineColor(R.color.scanLineColor);//设置扫描线的颜色 默认白色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //检查权限申请情况
        if (permissionsCheckerUtil.lacksPermissions(PERMISSIONS)){
            startPermissionsActivity();
        }
    }

    /**
     * 开启权限申请活动
     */
    private void startPermissionsActivity(){
        //使用静态方法开启活动
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED){
            finish();
        }
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK){
            if (data != null){
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                if (content!=null){
                    QRCodeVerifyRequest(usphone, content);
                }else{
                    Toast.makeText(this, "扫码结果出错！", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_CANCELED){
            Toast.makeText(this, "取消签到", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void QRCodeVerifyRequest(final String usphone, final String qvvalue) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "getQRCode";    //注②

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
                            String result = (String) new JSONObject(response).get("qvvalue");  //注③
                            if (result.equals(qvvalue)) {  //注⑤
                                // 签到成功操作
                                AttendanceRequest(usphone);
                                finish();
                            } else {
                                Toast.makeText(AttendanceActivity.this, "请扫描正确的签到码！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(AttendanceActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AttendanceActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public void AttendanceRequest(final String usphone) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "Attendance";    //注②

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
                            String result = (String) new JSONObject(response).get("Result");  //注③
                            switch (result) {
                                case "success": {  //注⑤
                                    String coursename = (String) new JSONObject(response).get("CourseName");
                                    Toast.makeText(AttendanceActivity.this, "课程：" + coursename + "，签到成功！", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                }
                                case "noresult":
                                    Toast.makeText(AttendanceActivity.this, "未来十分钟内没有需要签到的课程", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                case "alreadyattendance": {
                                    String coursename = (String) new JSONObject(response).get("CourseName");
                                    Toast.makeText(AttendanceActivity.this, "课程：" + coursename + "，已签到", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                }
                                default:
                                    Toast.makeText(AttendanceActivity.this, "请扫描正确的签到码！", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(AttendanceActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AttendanceActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
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
