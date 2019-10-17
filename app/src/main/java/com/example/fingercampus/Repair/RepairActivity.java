package com.example.fingercampus.Repair;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.graphics.Typeface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fingercampus.Constants;
import com.example.fingercampus.Net.FileUpload;
import com.example.fingercampus.Net.RegisterActivity;
import com.example.fingercampus.PermissionsActivity;
import com.example.fingercampus.R;
import com.example.fingercampus.Tools.AudioRecorderUtil;
import com.example.fingercampus.Tools.LogUtil;
import com.example.fingercampus.Tools.PermissionsCheckerUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.d;
import static com.example.fingercampus.R.layout.picpopupwindow;
import static com.example.fingercampus.R.layout.recordingpopuowindow;

public class RepairActivity extends AppCompatActivity {

    private Spinner select;
    private String selectText;
    private EditText position;
    private String positionText;
    private EditText description;
    private String descriptionText;
    private String imagePath;
    private LinearLayout plus_ll;
    private Button recording;
    private ImageView iv_CameraImg;
    private Context context;
    private String state;
    private String usphone;
    private String starttime;
    private String endtime;

    private PopupWindow popupWindow;

    private AudioRecorderUtil mrecord = new AudioRecorderUtil();

    private Button picCancel;

    private String imageName;

    private String path = Environment.getExternalStorageDirectory() +
            File.separator + Environment.DIRECTORY_DCIM + File.separator;
    public Uri photoUri;

    private String recordName = "rcd_" + getFileName() + ".amr";
    private String folderPath = Environment.getExternalStorageDirectory() + "/record/";
    private String recordPath = folderPath + recordName;

    private int GALLERY_CODE = 10;
    private int PICTURE_CODE = 01;
    private int RECORD_CODE = 100;

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO, Manifest.permission.CALL_PHONE};
    private PermissionsCheckerUtil permissionCheckerUtil;
    Typeface typeface;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_repair, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repairs_main);
        permissionCheckerUtil = new PermissionsCheckerUtil(this);
        init();

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar_repair);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.toolbar_myrepair:
                        startActivity(new Intent(RepairActivity.this, MyRepair.class));
                        break;
                    case R.id.toolbar_call:
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + "15553459906");
                        intent.setData(data);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        };
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        iv_CameraImg = findViewById(R.id.iv_CameraImg);
        picCancel = findViewById(R.id.picCancel);
        typeface = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        Button back = findViewById(R.id.repair_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        select = findViewById(R.id.repair_select);
        String[] types = {"请选择报修类型", "多媒体", "灯管", "风扇", "桌椅", "暖气片", "门窗", "其他"};
        SpinnerAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, types);
        select.setAdapter(adapter);
        position = findViewById(R.id.repair_position);
        description = findViewById(R.id.repair_des);
        Button plus = findViewById(R.id.repair_plus);
        Button audio = findViewById(R.id.repair_audio);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupWindow showPopupWindow = new ShowPopupWindow();
                showPopupWindow.recording(v);
                plus_ll.setVisibility(View.INVISIBLE);
                hideKeyboard(v);
            }
        });
        Button picture = findViewById(R.id.repair_picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupWindow showPopupWindow = new ShowPopupWindow();
                showPopupWindow.picture(v);
                plus_ll.setVisibility(View.INVISIBLE);
                hideKeyboard(v);
            }
        });
        plus_ll = findViewById(R.id.repair_plus_ll);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plus_ll.getVisibility() == View.INVISIBLE) {
                    plus_ll.setVisibility(View.VISIBLE);
                } else {
                    plus_ll.setVisibility(View.INVISIBLE);
                }
            }
        });
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                usphone = sharedPreferences.getString(Constants.RECORD.rephone, null);
                selectText = select.getSelectedItem().toString();
                positionText = position.getText().toString();
                descriptionText = description.getText().toString();
                state = "待处理";
                starttime = getFileName();

                endtime = "";
                if(recordPath == null){
                    recordPath = "";
                }
                if(imagePath == null){
                    imagePath = "";
                }

                RepairRequest(usphone,selectText,positionText,descriptionText,state,starttime,endtime,recordPath,imagePath);
            }
        });

        Button record = (Button) findViewById(R.id.repair_audio);
        record.setTypeface(typeface);
        Button album = (Button) findViewById(R.id.repair_picture);
        album.setTypeface(typeface);
        back.setTypeface(typeface);
    }

    private class ShowPopupWindow {
        void picture(View view) {
            @SuppressLint("InflateParams") View contentView = LayoutInflater.from(RepairActivity.this).inflate(picpopupwindow,
                    null);
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            popupWindow.setTouchable(true);
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.findViewById(R.id.openCamera).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //相机功能
                            imageName = "IMG_" + getFileName() + ".jpg";
                            imagePath = path + imageName;
                            File file = new File(imagePath);
                            try {
                                if (file.exists()) {
                                    file.delete();
                                }
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //判断版本号
                            if (Build.VERSION.SDK_INT >= 24) {
                                photoUri = FileProvider.getUriForFile(RepairActivity.this, "uri.provider", file);
                                //intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            } else {
                                photoUri = Uri.fromFile(file);
                            }
                            //启动相机
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, PICTURE_CODE);
                            popupWindow.dismiss();
                        }

                    });
                    //调用相册
                    v.findViewById(R.id.selectFromAlbum).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 调用相册接口
                            Intent intent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, GALLERY_CODE); // 打开相册
                            popupWindow.dismiss();
                        }
                    });

                    v.findViewById(R.id.picCancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                    return false;
                }
            });
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.de_equare));
            popupWindow.showAsDropDown(view);
        }

        void recording(View view) {
            @SuppressLint("InflateParams") View contentView = LayoutInflater.from(RepairActivity.this).inflate(recordingpopuowindow,
                    null);
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            popupWindow.setTouchable(true);
            recording = popupWindow.getContentView().findViewById(R.id.recording);
            recording.setTypeface(typeface);
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.findViewById(R.id.recordingSubmit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO 做提交录音操作
                            popupWindow.dismiss();
                            Toast.makeText(RepairActivity.this, "提交录音", Toast.LENGTH_SHORT).show();
                        }
                    });
                    v.findViewById(R.id.recordingCancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });
                    recording.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {

                                case MotionEvent.ACTION_DOWN:
                                    recording.setText("长按录音");
                                    String mpath = mrecord.startRecord();
                                    Toast.makeText(RepairActivity.this, mpath,Toast.LENGTH_SHORT).show();
                                    break;
                                //case MotionEvent.ACTION_MOVE: //移动// break;

                                case MotionEvent.ACTION_UP: //抬起
                                    recording.setText("录音结束");
                                    mrecord.stopRecord();
                                    break;
                            }
                            return true;
                        }
                    });
                    return false;
                }
            });
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.de_equare));
            popupWindow.showAsDropDown(view);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (permissionCheckerUtil.lacksPermissions(permissions)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, GALLERY_CODE, permissions);
        PermissionsActivity.startActivityForResult(this, PICTURE_CODE, permissions);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_CODE && resultCode == RESULT_OK) {
            iv_CameraImg.setImageURI(photoUri);
        } else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            //获取返回的数据
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //获取被选择的照片的数据视图
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            //从数据视图中获取已选择的图片路径
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
            //将图片显示到界面上
            iv_CameraImg.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        } else if (requestCode == RECORD_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }


    private String getFileName() {//按照时间对文件命名
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(date);
    }

    public void RepairRequest(final String usphone, final String selectText,final String positionText,final String descriptionText,final String state,final String starttime,final String endtime,final String recordPath,final String imagePath) {
        //请求地址
        String url = "http://119.3.232.205:8080/FingerCampus/*";
        final String tag = "repair";

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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {
                                if(imagePath == null || imagePath == ""){
                                    Toast.makeText(RepairActivity.this, "申请已经提交，等待管理员审核！", Toast.LENGTH_SHORT).show();
                                }else{
                                    final HashMap<String, String> params = new HashMap<>();
                                    params.put("RequestType", "UploadFile");
                                    params.put("recordPath", recordPath);
                                    params.put("odphone", usphone);
                                    params.put("odstarttime", starttime);
                                    final File uploadFile = new File(imagePath);
                                    final String newFileName = imageName;
                                    final String fileFormName = "image";
                                    final String urlStr = "http://119.3.232.205:8080/FingerCampus/UploadFileServlet";
                                    Boolean uploadResult = false;
                                    try {
                                        uploadResult = FileUpload.uploadForm(params, fileFormName, uploadFile, newFileName, urlStr);
                                    } catch (IOException e) {
                                        Toast.makeText(getApplicationContext(), "文件上传失败！", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    if(uploadResult){
                                        finish();
                                        Toast.makeText(RepairActivity.this, "申请已经提交，等待管理员审核！", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(RepairActivity.this, "文件上传失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "申请提交失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "请检查填写的内容！", Toast.LENGTH_SHORT).show();
                            LogUtil.e(TAG , e.getMessage()+" ");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "请检查网络连接，并重试！", Toast.LENGTH_SHORT).show();
                LogUtil.e(TAG, error.getMessage()+" ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("RequestType", tag);
                params.put("odphone", usphone);
                params.put("odtype",selectText );
                params.put("odplace",positionText);
                params.put("oddescription",descriptionText);
                params.put("odstate",state);
                params.put("odstarttime",starttime);
                params.put("odendtime",endtime);
                LogUtil.d(tag, "RequestType = "+tag+" odphone = "+usphone+" odtype = "+selectText
                        +" odplace = "+positionText+" oddescription = "+descriptionText+" odstate = "+state
                        +" odstarttime = "+starttime+" odendtime = "+endtime);
                return params;
            }
        };
        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    //隐藏软键盘
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

