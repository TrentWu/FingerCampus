package com.example.fingercampus.Apply;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import  android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.BaseAdapter;

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
import com.example.fingercampus.Tools.TimeUtil;

import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyApplication extends Activity {

    ListView listView;
    ArrayList<Information> info;
    Typeface typeface;
    private boolean isopen =true;
    private String TAG="MyApplication";
    private String usphone;
    final List<Map<String,Object>> mList = new ArrayList<>();

    private SimpleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myapplication);

        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        usphone = sharedPreferences.getString(Constants.RECORD.rephone, null);
        listView = findViewById(R.id.list);
        typeface = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        Button back = findViewById(R.id.back);
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        queryRequest(usphone);
        adapter = new SimpleAdapter(this, mList, R.layout.application_item,
                new String[]{"id", "name", "classroom", "usersnumber", "date", "usphone", "section"},
                new int[]{R.id.id, R.id.name, R.id.classroom, R.id.users, R.id.date, R.id.phone, R.id.section});
    }
    /*
     ***用户查询类
     */
    public void queryRequest(final String usphone) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "MyApplication";    //注②

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
                            JSONArray jsonArr = (JSONArray)new JSONObject(response).get("list");  //注③
                            LogUtil.d(TAG,jsonArr.toString());
                            for(int i=0;i<jsonArr.length();i++){
                                Map<String ,Object> map=new HashMap<String, Object>();
                                map.put("id",i+1);
                                map.put("name",jsonArr.getJSONObject(i).get("name").toString());
                                map.put("classroom",jsonArr.getJSONObject(i).get("classroom").toString());
                                map.put("usphone",jsonArr.getJSONObject(i).get("usphone").toString());
                                map.put("date",jsonArr.getJSONObject(i).get("date").toString());
                                map.put("usersnumber",jsonArr.getJSONObject(i).get("usersnumber").toString());
                                map.put("section",jsonArr.getJSONObject(i).get("section").toString());
                                mList.add(map);
                                listView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            //loginButton.setClickable(true);
                            Toast.makeText(MyApplication.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loginButton.setClickable(true);
                Toast.makeText(MyApplication.this, "请稍后重试", Toast.LENGTH_SHORT).show();
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
