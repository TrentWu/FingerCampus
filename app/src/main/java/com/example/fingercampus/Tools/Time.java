package com.example.fingercampus.Tools;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time {
    /**
     * 获取网络时间
     * 需要开子线程
     *
     * @return 返回中国科学院国家授时中心网址时间
     */
    public static String getNetTime() {
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
