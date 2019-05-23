package com.example.fingercampus.Tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * 权限检测类
 * 用于检测权限是否授予
 */
public class PermissionsCheckerUtil {

    private final Context context;

    public PermissionsCheckerUtil(Context context){
        this.context = context;
    }

    public boolean lacksPermissions(String... permissions){
        for (String permission : permissions){
            if (lacksPermission(permission)){
                return true;
            }
        }
        return false;
    }

    private boolean lacksPermission(String permission){
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }

}
