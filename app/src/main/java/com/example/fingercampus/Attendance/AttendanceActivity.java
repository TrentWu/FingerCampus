package com.example.fingercampus.Attendance;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.fingercampus.PermissionsActivity;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.PermissionsCheckerUtil;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

/**
 * 考勤活动类
 */
public class AttendanceActivity extends Activity {

    private static final int REQUEST_CODE_SCAN = 1;
    private static final int REQUEST_CODE = 2;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private PermissionsCheckerUtil permissionsCheckerUtil = new PermissionsCheckerUtil(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_result);
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
    }
}
