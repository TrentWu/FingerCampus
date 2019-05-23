package com.example.fingercampus.Repair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.graphics.Typeface;

import com.example.fingercampus.R;

import java.lang.reflect.Type;

import static com.example.fingercampus.R.layout.picpopupwindow;
import static com.example.fingercampus.R.layout.recordingpopuowindow;

public class RepairActivity extends Activity {

    private Spinner select;
    private String selectText;
    private EditText position;
    private String positionText;
    private EditText description;
    private String descriptionText;
    private LinearLayout plus_ll;
    private Button audio;
    private Button picture;

    private PopupWindow popupWindow;
    private Button openCamera;
    private Button selectFromAlbum;
    private Button picCancel;

    private int REQUEST_CODE_TAKE_PHOTO = 1;

    Typeface typeface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repairs_main);
        init();
    }

    private void init() {
        typeface = Typeface.createFromAsset(getAssets(),"iconfont/iconfont.ttf");
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
        audio = findViewById(R.id.repair_audio);
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 显示录音PopupWindow
                ShowPopupWindow showPopupWindow = new ShowPopupWindow();
                showPopupWindow.recording(v);
            }
        });
        picture = findViewById(R.id.repair_picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPopupWindow showPopupWindow = new ShowPopupWindow();
                showPopupWindow.picture(v);
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
                selectText = select.getSelectedItem().toString();
                positionText = position.getText().toString();
                descriptionText = description.getText().toString();
            }
        });

        Button record = (Button)findViewById(R.id.repair_audio);
        record.setTypeface(typeface);
        Button album =(Button)findViewById(R.id.repair_picture);
        album.setTypeface(typeface);
        back.setTypeface(typeface);
    }

    private class ShowPopupWindow{
        void picture(View view){
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
                            //TODO 调用相机接口
                            Toast.makeText(RepairActivity.this, "打开相机", Toast.LENGTH_SHORT).show();
                        }
                    });
                    v.findViewById(R.id.selectFromAlbum).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO 调用相册接口
                            Toast.makeText(RepairActivity.this, "从相册选取", Toast.LENGTH_SHORT).show();
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
        void recording(View view){
            @SuppressLint("InflateParams") View contentView = LayoutInflater.from(RepairActivity.this).inflate(recordingpopuowindow,
                    null);
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            popupWindow.setTouchable(true);
            Button recording = popupWindow.getContentView().findViewById(R.id.recording);
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
                    v.findViewById(R.id.recording).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //TODO 做录音操作
                            Toast.makeText(RepairActivity.this, "录音", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return false;
                }
            });
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.de_equare));
            popupWindow.showAsDropDown(view);

        }
    }
}
