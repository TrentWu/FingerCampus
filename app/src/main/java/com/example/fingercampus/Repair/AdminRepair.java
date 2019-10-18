package com.example.fingercampus.Repair;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.example.fingercampus.Constants;
import com.example.fingercampus.MainActivity;
import com.example.fingercampus.Net.LoginActivity;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.LogUtil;
import com.example.fingercampus.Tools.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminRepair extends Activity {
    final List<Map<String,Object>> mList = new ArrayList<>();
    private Typeface typeface;
    ListView listView;
    private String state = "未完成";
    private String TAG="AdminRepair";
    private SimpleAdapter adapter;
    private String odid;
    private String odendtime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_repair);
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
                new String[]{"id", "type", "position", "usphone", "description", "state"},
                new int[]{R.id.id, R.id.type, R.id.position, R.id.phone, R.id.description, R.id.state}){
                //在这个重写的函数里设置 每个 item 中按钮的响应事件
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                final int p=position;
                final View view=super.getView(p, convertView, parent);
                final String odstate = mList.get(p).get("state").toString();
                if (odstate.equals("已完成")){
                    LinearLayout state_ll = view.findViewById(R.id.state_ll);
                    state_ll.setVisibility(View.GONE);
                }else if(odstate.equals("待处理")){
                    LinearLayout state_ll = view.findViewById(R.id.state_ll);
                    state_ll.setVisibility(View.VISIBLE);
                    Button button=view.findViewById(R.id.state_button);
                    button.setText("完成");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //警告框的写法
                            new AlertDialog.Builder(AdminRepair.this)
                                    .setTitle("提示")
                                    .setMessage("确定完成？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            odid = mList.get(p).get("odid").toString();
                                            odendtime = getCurrentTime();
                                            UpdateStateRequest("已完成",odendtime,odid);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            setTitle("点击了对话框上的取消按钮");
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    });
                }
                return view;
            }
        };
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
                            for(int i=0;i<jsonArr.length();i++){
                                Map<String ,Object> map=new HashMap<String, Object>();
                                map.put("id",i+1);
                                map.put("odid",jsonArr.getJSONObject(i).get("odid"));
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
                                listView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            //loginButton.setClickable(true);
                            Toast.makeText(AdminRepair.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //loginButton.setClickable(true);
                Toast.makeText(AdminRepair.this, "请稍后重试", Toast.LENGTH_SHORT).show();
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

    /**
     * 用户登录请求类
     *
     * @param state 订单状态
     * @param odendtime 订单完成时间
     * @param odid 订单编号
     */
    public void UpdateStateRequest(final String state, final String odendtime,final String odid) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "RepairState";    //注②

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
                                Toast.makeText(AdminRepair.this,"状态修改成功",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AdminRepair.this, "失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(AdminRepair.this, "无网络连接", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminRepair.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                params.put("state", state);  //注⑥
                params.put("odendtime", odendtime);
                params.put("odid",odid);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    private String getCurrentTime() {//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        return dateFormat.format(date);
    }
}
