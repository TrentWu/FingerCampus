package com.example.fingercampus.Timetable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.LogUtil;
import com.example.fingercampus.Tools.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimetableActivity extends AppCompatActivity {

    Typeface typeface;

    String usphone;
    int curcsweek = 0;
    TextView curweek_tv;

    private String TAG = "TimetableActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_timetable, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_main);

        init();
        initToolbar();
    }

    private void init() {
        typeface = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        //调用图标
        Button back = findViewById(R.id.back);
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        usphone = intent.getStringExtra("usphone");
        TimetableRequest(usphone);

        curweek_tv = findViewById(R.id.curweek_tv);

        Calendar calendar = Calendar.getInstance();
        String dateString = "2019-08-22";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar initcalendar = Calendar.getInstance();
        initcalendar.setTime(date);

        int curweek = calendar.get(Calendar.WEEK_OF_YEAR);
        int initweek = initcalendar.get(Calendar.WEEK_OF_YEAR);
        curcsweek = curweek - initweek;
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.addCourse:
                        Toast.makeText(TimetableActivity.this, "添加课程", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TimetableActivity.this, AddCourseActivity.class));
                        break;
                    default:
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    /**
     * 用户课程表请求类
     *
     * @param usphone 用户手机号
     */
    public void TimetableRequest(final String usphone) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";    //注①
        final String tag = "Timetable";    //注②

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
                            JSONArray timetables = (JSONArray) new JSONObject(response).get("timetablelist");
                            for (int i = 0; i < timetables.length(); i++) {
                                String csname = timetables.getJSONObject(i).getString("csname");
                                int csbweek = timetables.getJSONObject(i).getInt("csbweek");
                                int cseweek = timetables.getJSONObject(i).getInt("cseweek");
                                int csweek = timetables.getJSONObject(i).getInt("csweek");
                                if (cseweek >= curcsweek && csbweek <= curcsweek) {
                                    String csday = timetables.getJSONObject(i).getString("csday");
                                    String csbtime = timetables.getJSONObject(i).get("csbtime").toString();
                                    String csetime = timetables.getJSONObject(i).get("csetime").toString();
                                    int clrid = timetables.getJSONObject(i).getInt("clrid");
                                    int tcid = timetables.getJSONObject(i).getInt("tcid");
                                    String classroom = timetables.getJSONObject(i).getString("classroom");
                                    String teacher = timetables.getJSONObject(i).getString("teacher");

                                    curweek_tv.setText("第" + curcsweek + "周");

                                    StringBuilder buttonId = new StringBuilder();
                                    String buttonid;
                                    Button button = null;
                                    String[] times = csbtime.split(":");
                                    int csbtime_i = Integer.valueOf(times[0]);
                                    switch (csday) {
                                        case "一":
                                            buttonId.append("bt");
                                            if (csbtime_i >= 8 && csbtime_i < 10) {
                                                buttonId.append("1_1");
                                            } else if (csbtime_i >= 10 && csbtime_i < 13) {
                                                buttonId.append("1_2");
                                            } else if (csbtime_i >= 13 && csbtime_i < 16) {
                                                buttonId.append("1_3");
                                            } else if (csbtime_i >= 16 && csbtime_i < 18) {
                                                buttonId.append("1_4");
                                            } else if (csbtime_i >= 18 && csbtime_i < 20) {
                                                buttonId.append("1_5");
                                            } else if (csbtime_i >= 20 && csbtime_i < 22) {
                                                buttonId.append("1_6");
                                            }
                                            buttonid = buttonId.toString();
                                            switch (buttonid) {
                                                case "bt1_1":
                                                    button = findViewById(R.id.bt1_1);
                                                    break;
                                                case "bt1_2":
                                                    button = findViewById(R.id.bt1_2);
                                                    break;
                                                case "bt1_3":
                                                    button = findViewById(R.id.bt1_3);
                                                    break;
                                                case "bt1_4":
                                                    button = findViewById(R.id.bt1_4);
                                                    break;
                                                case "bt1_5":
                                                    button = findViewById(R.id.bt1_5);
                                                    break;
                                                case "bt1_6":
                                                    button = findViewById(R.id.bt1_6);
                                                    break;
                                            }
                                            break;
                                        case "二":
                                            buttonId.append("bt");
                                            if (csbtime_i >= 8 && csbtime_i < 10) {
                                                buttonId.append("2_1");
                                            } else if (csbtime_i >= 10 && csbtime_i < 13) {
                                                buttonId.append("2_2");
                                            } else if (csbtime_i >= 13 && csbtime_i < 16) {
                                                buttonId.append("2_3");
                                            } else if (csbtime_i >= 16 && csbtime_i < 18) {
                                                buttonId.append("2_4");
                                            } else if (csbtime_i >= 18 && csbtime_i < 20) {
                                                buttonId.append("2_5");
                                            } else if (csbtime_i >= 20 && csbtime_i < 22) {
                                                buttonId.append("2_6");
                                            }
                                            buttonid = buttonId.toString();
                                            switch (buttonid) {
                                                case "bt2_1":
                                                    button = findViewById(R.id.bt2_1);
                                                    break;
                                                case "bt2_2":
                                                    button = findViewById(R.id.bt2_2);
                                                    break;
                                                case "bt2_3":
                                                    button = findViewById(R.id.bt2_3);
                                                    break;
                                                case "bt2_4":
                                                    button = findViewById(R.id.bt2_4);
                                                    break;
                                                case "bt2_5":
                                                    button = findViewById(R.id.bt2_5);
                                                    break;
                                                case "bt2_6":
                                                    button = findViewById(R.id.bt2_6);
                                                    break;
                                            }
                                            break;
                                        case "三":
                                            buttonId.append("bt");
                                            if (csbtime_i >= 8 && csbtime_i < 10) {
                                                buttonId.append("3_1");
                                            } else if (csbtime_i >= 10 && csbtime_i < 13) {
                                                buttonId.append("3_2");
                                            } else if (csbtime_i >= 13 && csbtime_i < 16) {
                                                buttonId.append("3_3");
                                            } else if (csbtime_i >= 16 && csbtime_i < 18) {
                                                buttonId.append("3_4");
                                            } else if (csbtime_i >= 18 && csbtime_i < 20) {
                                                buttonId.append("3_5");
                                            } else if (csbtime_i >= 20 && csbtime_i < 22) {
                                                buttonId.append("3_6");
                                            }
                                            buttonid = buttonId.toString();
                                            switch (buttonid) {
                                                case "bt3_1":
                                                    button = findViewById(R.id.bt3_1);
                                                    break;
                                                case "bt3_2":
                                                    button = findViewById(R.id.bt3_2);
                                                    break;
                                                case "bt3_3":
                                                    button = findViewById(R.id.bt3_3);
                                                    break;
                                                case "bt3_4":
                                                    button = findViewById(R.id.bt3_4);
                                                    break;
                                                case "bt3_5":
                                                    button = findViewById(R.id.bt3_5);
                                                    break;
                                                case "bt3_6":
                                                    button = findViewById(R.id.bt3_6);
                                                    break;
                                            }
                                            break;
                                        case "四":
                                            buttonId.append("bt");
                                            if (csbtime_i >= 8 && csbtime_i < 10) {
                                                buttonId.append("4_1");
                                            } else if (csbtime_i >= 10 && csbtime_i < 13) {
                                                buttonId.append("4_2");
                                            } else if (csbtime_i >= 13 && csbtime_i < 16) {
                                                buttonId.append("4_3");
                                            } else if (csbtime_i >= 16 && csbtime_i < 18) {
                                                buttonId.append("4_4");
                                            } else if (csbtime_i >= 18 && csbtime_i < 20) {
                                                buttonId.append("4_5");
                                            } else if (csbtime_i >= 20 && csbtime_i < 22) {
                                                buttonId.append("4_6");
                                            }
                                            buttonid = buttonId.toString();
                                            switch (buttonid) {
                                                case "bt4_1":
                                                    button = findViewById(R.id.bt4_1);
                                                    break;
                                                case "bt4_2":
                                                    button = findViewById(R.id.bt4_2);
                                                    break;
                                                case "bt4_3":
                                                    button = findViewById(R.id.bt4_3);
                                                    break;
                                                case "bt4_4":
                                                    button = findViewById(R.id.bt4_4);
                                                    break;
                                                case "bt4_5":
                                                    button = findViewById(R.id.bt4_5);
                                                    break;
                                                case "bt4_6":
                                                    button = findViewById(R.id.bt4_6);
                                                    break;
                                            }
                                            break;
                                        case "五":
                                            buttonId.append("bt");
                                            if (csbtime_i >= 8 && csbtime_i < 10) {
                                                buttonId.append("5_1");
                                            } else if (csbtime_i >= 10 && csbtime_i < 13) {
                                                buttonId.append("5_2");
                                            } else if (csbtime_i >= 13 && csbtime_i < 16) {
                                                buttonId.append("5_3");
                                            } else if (csbtime_i >= 16 && csbtime_i < 18) {
                                                buttonId.append("5_4");
                                            } else if (csbtime_i >= 18 && csbtime_i < 20) {
                                                buttonId.append("5_5");
                                            } else if (csbtime_i >= 20 && csbtime_i < 22) {
                                                buttonId.append("5_6");
                                            }
                                            buttonid = buttonId.toString();
                                            switch (buttonid) {
                                                case "bt5_1":
                                                    button = findViewById(R.id.bt5_1);
                                                    break;
                                                case "bt5_2":
                                                    button = findViewById(R.id.bt5_2);
                                                    break;
                                                case "bt5_3":
                                                    button = findViewById(R.id.bt5_3);
                                                    break;
                                                case "bt5_4":
                                                    button = findViewById(R.id.bt5_4);
                                                    break;
                                                case "bt5_5":
                                                    button = findViewById(R.id.bt5_5);
                                                    break;
                                                case "bt5_6":
                                                    button = findViewById(R.id.bt5_6);
                                                    break;
                                            }
                                            break;
                                        case "六":
                                            buttonId.append("bt");
                                            if (csbtime_i >= 8 && csbtime_i < 10) {
                                                buttonId.append("6_1");
                                            } else if (csbtime_i >= 10 && csbtime_i < 13) {
                                                buttonId.append("6_2");
                                            } else if (csbtime_i >= 13 && csbtime_i < 16) {
                                                buttonId.append("6_3");
                                            } else if (csbtime_i >= 16 && csbtime_i < 18) {
                                                buttonId.append("6_4");
                                            } else if (csbtime_i >= 18 && csbtime_i < 20) {
                                                buttonId.append("6_5");
                                            } else if (csbtime_i >= 20 && csbtime_i < 22) {
                                                buttonId.append("6_6");
                                            }
                                            buttonid = buttonId.toString();
                                            switch (buttonid) {
                                                case "bt6_1":
                                                    button = findViewById(R.id.bt6_1);
                                                    break;
                                                case "bt6_2":
                                                    button = findViewById(R.id.bt6_2);
                                                    break;
                                                case "bt6_3":
                                                    button = findViewById(R.id.bt6_3);
                                                    break;
                                                case "bt6_4":
                                                    button = findViewById(R.id.bt6_4);
                                                    break;
                                                case "bt6_5":
                                                    button = findViewById(R.id.bt6_5);
                                                    break;
                                                case "bt6_6":
                                                    button = findViewById(R.id.bt6_6);
                                                    break;
                                            }
                                            break;
                                        case "日":
                                            buttonId.append("bt");
                                            if (csbtime_i >= 8 && csbtime_i < 10) {
                                                buttonId.append("7_1");
                                            } else if (csbtime_i >= 10 && csbtime_i < 13) {
                                                buttonId.append("7_2");
                                            } else if (csbtime_i >= 13 && csbtime_i < 16) {
                                                buttonId.append("7_3");
                                            } else if (csbtime_i >= 16 && csbtime_i < 18) {
                                                buttonId.append("7_4");
                                            } else if (csbtime_i >= 18 && csbtime_i < 20) {
                                                buttonId.append("7_5");
                                            } else if (csbtime_i >= 20 && csbtime_i < 22) {
                                                buttonId.append("7_6");
                                            }
                                            buttonid = buttonId.toString();
                                            switch (buttonid) {
                                                case "bt7_1":
                                                    button = findViewById(R.id.bt7_1);
                                                    break;
                                                case "bt7_2":
                                                    button = findViewById(R.id.bt7_2);
                                                    break;
                                                case "bt7_3":
                                                    button = findViewById(R.id.bt7_3);
                                                    break;
                                                case "bt7_4":
                                                    button = findViewById(R.id.bt7_4);
                                                    break;
                                                case "bt7_5":
                                                    button = findViewById(R.id.bt7_5);
                                                    break;
                                                case "bt7_6":
                                                    button = findViewById(R.id.bt7_6);
                                                    break;
                                            }
                                            break;
                                    }
                                    button.setText(csname + "\n" + teacher + "\n" + classroom + "\n第" + csbweek + " 到 " + cseweek + "周");
                                }
                            }
                        } catch (JSONException e) {
                            Toast.makeText(TimetableActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TimetableActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                Log.e(TAG, error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                params.put("action", "getAll");
                params.put("usphone", usphone);  //注⑥
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TimetableRequest(usphone);
    }

    class Timetable {
        String csname;
        String csbweek;
        String cseweek;
        String csday;
        Time csbtime;
        Time csetime;
        int clrid;
        int tcid;

        public String getCsname() {
            return csname;
        }

        public void setCsname(String csname) {
            this.csname = csname;
        }

        public String getCsbweek() {
            return csbweek;
        }

        public void setCsbweek(String csbweek) {
            this.csbweek = csbweek;
        }

        public String getCseweek() {
            return cseweek;
        }

        public void setCseweek(String cseweek) {
            this.cseweek = cseweek;
        }

        public String getCsday() {
            return csday;
        }

        public void setCsday(String csday) {
            this.csday = csday;
        }

        public Time getCsbtime() {
            return csbtime;
        }

        public void setCsbtime(Time csbtime) {
            this.csbtime = csbtime;
        }

        public Time getCsetime() {
            return csetime;
        }

        public void setCsetime(Time csetime) {
            this.csetime = csetime;
        }

        public int getClrid() {
            return clrid;
        }

        public void setClrid(int clrid) {
            this.clrid = clrid;
        }

        public int getTcid() {
            return tcid;
        }

        public void setTcid(int tcid) {
            this.tcid = tcid;
        }
    }
}