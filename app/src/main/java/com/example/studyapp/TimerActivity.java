package com.example.studyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class TimerActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private String hh;
    private String mm;
    private String ss;
    private String txtSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Intent intent = getIntent();
        txtSubject = intent.getStringExtra("과목명");

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        Button btn = (Button)findViewById(R.id.stopbutton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                chronometer.stop(); // 스톱워치 중지

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class); //LockActivity에 이전화면으로 돌아갈 액티비티 명
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //MainFragment mainFragment = new MainFragment();

                /*
                Bundle bundle = new Bundle();
                bundle.putString("공부시간", hh+":"+mm+":"+ss);
                bundle.putString("과목명", txtSubject);
                mainFragment.setArguments(bundle);
                */

                startActivity(intent);
            }
        });
    }
}