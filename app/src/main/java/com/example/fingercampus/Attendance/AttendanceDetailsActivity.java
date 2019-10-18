package com.example.fingercampus.Attendance;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.R;
import com.example.fingercampus.Repair.AdminRepair;
import com.example.fingercampus.Tools.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceDetailsActivity extends AppCompatActivity {

    private String TAG = "AttendanceDetailsActivity";
    final List<Map<String,Object>> mList = new ArrayList<>();
    private Typeface typeface;
    ListView listView;
    private SimpleAdapter adapter;
    Intent intent;
    String usphone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendancedetails);
        intent = getIntent();
        usphone = intent.getStringExtra("usphone");

        init();
    }

    private void init(){
        listView = findViewById(R.id.listView);
        typeface = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        Button back = findViewById(R.id.back);
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        queryRequest();
        adapter = new SimpleAdapter(this, mList, R.layout.attendancedetails_item,
                new String[]{"id", "csname", "csweek", "csday", "stdname", "stdcollege", "lsattendance"},
                new int[]{R.id.id, R.id.csname, R.id.csweek, R.id.csday, R.id.stdname, R.id.stdcollege, R.id.lsattendance}){
        };
    }

    /**
     * **用户查询类
     */
    public void queryRequest() {
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
                            JSONArray jsonArr = (JSONArray) new JSONObject(response).get("list1");  //注③
                            for (int i = 0; i < jsonArr.length(); i++) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("id", i + 1);
                                map.put("csname", jsonArr.getJSONObject(i).getString("csname"));
                                map.put("csweek", jsonArr.getJSONObject(i).getString("csweek"));
                                map.put("csday", jsonArr.getJSONObject(i).getString("csday"));
                                map.put("stdname", jsonArr.getJSONObject(i).getString("stdname"));
                                map.put("stdcollege", jsonArr.getJSONObject(i).getString("stdcollege"));
                                map.put("lsattendance", jsonArr.getJSONObject(i).getString("lsattendance"));
                                mList.add(map);
                                listView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            //loginButton.setClickable(true);
                            Toast.makeText(AttendanceDetailsActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loginButton.setClickable(true);
                Toast.makeText(AttendanceDetailsActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
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
