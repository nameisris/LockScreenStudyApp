package com.example.studyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class SettingLockActivity extends AppCompatActivity {

    private static final String TAG = "";
    private Button startButton;

    private EditText hourText;
    private EditText minText;
    private EditText secondText;

    private CountDownTimer countDownTimer;
    private String countdownTime;

    private boolean timerRunning; // 타이머 상태

    private long time = 0;

    private CheckBox checkLock;

    private String txtSubject;

    FrameLayout timeSetting; // 셋팅화면

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_lock);

        Intent intent = getIntent();
        txtSubject = intent.getStringExtra("과목명");

        // 시, 분, 초 EditText 최소,최대값 설정
        hourText = findViewById(R.id.hour);
        hourText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        minText = findViewById(R.id.min);
        minText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "60")});
        secondText = findViewById(R.id.second);
        secondText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "60")});

        timeSetting = findViewById(R.id.timeSetting);
        timeSetting.setVisibility(timeSetting.GONE);

        checkLock = findViewById(R.id.checkLock);
        checkLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLock.isChecked()) { // 잠금 활성화
                    timeSetting.setVisibility(timeSetting.VISIBLE);
                } else { // 잠금 비활성화
                    timeSetting.setVisibility(timeSetting.GONE);
                    hourText.setText("");
                    minText.setText("");
                    secondText.setText("");
                }

            }
        });

        startButton = findViewById(R.id.countdown_button);
        // 타이머 시작 (설정 값 전달)
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SettingLockActivity.this, LockActivity.class);
                Intent intent1 = new Intent(SettingLockActivity.this, LockedActivity.class);
                Intent intent2 = new Intent(SettingLockActivity.this, LockActivity.class);
                Intent intent3 = new Intent(SettingLockActivity.this, TimerActivity.class);

                if (checkLock.isChecked()) { // 잠금 활성화
                    if(hourText.getText().toString().equals("0") && minText.getText().toString().equals("0") && secondText.getText().toString().equals("0")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "0시간 0분 0초로는 시작 불가합니다.",Toast.LENGTH_SHORT);
                        toast.show();
                    } else if(hourText.getText().toString().equals("") || minText.getText().toString().equals("") || secondText.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "시간을 설정해주세요",Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        updateTimer();
                        intent2.putExtra("시간", time);
                        intent2.putExtra("과목명", txtSubject);
                        startActivity(intent1);
                        startService(intent2);
                    }
                } else {
                    intent2.putExtra("시간", time);
                    intent2.putExtra("과목명", txtSubject);
                    startActivity(intent3);
                }
                // startStop();
            }
        });

    }

    // 시간 업데이트
    // countdownTime 문자열 값에 시간 세팅
    private void updateTimer() {
        String sHour = hourText.getText().toString();
        String sMin = minText.getText().toString();
        String sSecond = secondText.getText().toString();

        time = (Long.parseLong(sHour) * 3600000) + (Long.parseLong(sMin) * 60000) + (Long.parseLong(sSecond) * 1000);
        // countdownText.setText(timeLeftText);

    }

}