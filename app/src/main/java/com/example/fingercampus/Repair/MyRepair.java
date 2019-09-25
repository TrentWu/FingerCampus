package com.example.fingercampus.Repair;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.Apply.MyApplication;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRepair extends Activity {
    final List<Map<String,Object>> mList = new ArrayList<>();
    private Typeface typeface;
    ListView listView;
    private String TAG="MyRepair";
    private SimpleAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myrepair);
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
        adapter = new SimpleAdapter(this, mList, R.layout.repair_item,
                new String[]{"id", "type", "position", "usersnumber", "usphone", "description", "state"},
                new int[]{R.id.id, R.id.type, R.id.position, R.id.phone, R.id.date, R.id.description, R.id.state});
        listView.setAdapter(adapter);

        Button fresh = (Button) findViewById(R.id.fresh);
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setAdapter(adapter);
            }

        });
    }
    /**
     ***用户查询类
     */
    public void queryRequest() {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "MyRepair";    //注②

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
                            JSONArray jsonArr = (JSONArray)new JSONObject(response).get("list1");  //注③
                            LogUtil.d(TAG,jsonArr.toString());
                            Toast.makeText(MyRepair.this,"请刷新！",Toast.LENGTH_SHORT).show();
                            for(int i=0;i<jsonArr.length();i++){
                                Map<String ,Object> map=new HashMap<String, Object>();
                                map.put("id",i+1);
                                map.put("type",jsonArr.getJSONObject(i).get("odtype").toString());
                                map.put("position",jsonArr.getJSONObject(i).get("odplace").toString());
                                map.put("usphone",jsonArr.getJSONObject(i).get("odphone").toString());
                                map.put("description",jsonArr.getJSONObject(i).get("oddescription").toString());
                                map.put("state",jsonArr.getJSONObject(i).get("odstate").toString());
                                map.put("starttime",jsonArr.getJSONObject(i).get("odstarttime").toString());
                                map.put("endtime",jsonArr.getJSONObject(i).get("odendtime").toString());
                                map.put("imagePath",jsonArr.getJSONObject(i).get("imagePath").toString());
                                map.put("recordPath",jsonArr.getJSONObject(i).get("recordPath").toString());
                                mList.add(map);
                            }
                        } catch (JSONException e) {
                            //loginButton.setClickable(true);
                            Toast.makeText(MyRepair.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loginButton.setClickable(true);
                Toast.makeText(MyRepair.this, "请稍后重试", Toast.LENGTH_SHORT).show();
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


}
