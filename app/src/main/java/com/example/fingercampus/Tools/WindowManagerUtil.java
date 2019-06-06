package com.example.fingercampus.Tools;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class WindowManagerUtil {

    private Context context;

    public WindowManagerUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取屏幕宽度（像素）
     * @return          屏幕宽度
     */
    public int getWindowWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度（像素）
     * @return          屏幕高度
     */
    public int getWindowHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕密度
     * @return          屏幕密度
     */
    public float getWindowDensity(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    /**
     * 获取屏幕密度Dpi
     * @return          屏幕密度Dpi
     */
    public int getWindowDensityDpi(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.densityDpi;
    }
}
