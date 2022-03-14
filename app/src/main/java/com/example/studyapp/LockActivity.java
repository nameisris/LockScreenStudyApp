package com.example.studyapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class LockActivity extends Service {

    private static final String	TAG = "Service";
    
    // 서비스 관련 변수
    private WindowManager.LayoutParams params1, params2;
    private WindowManager winMgr;
    
    // 뷰
    private LayoutInflater inflater;
    private View lockView;
    private View emergencyUseView;
    private View emergencyCallView;
    
    // 스톱워치
    private Chronometer chronometer;
    private String hh;
    private String mm;
    private String ss;

    // 타이머
    private CountDownTimer countDownTimer; // 카운트다운 함수
    private TextView countdownView;  // 타이머 현황
    private boolean timerRunning; // 타이머 상태
    private boolean firstState = true;  // 처음상태인지

    // 타이머 시간 변수
    private long time = 0;
    private long tempTime = 0;
    private long countdownTime = 0; // 전달받을 변수
    private long id = 0;

    // 잠금 여부 체크 변수
    private boolean checkLock; // 잠금 여부 확인

    // 긴급 사용 했는지 여부
    private boolean aleadyUsed = false;

    // 긴급 사용 및 긴급 사용 정지 버튼
    Button btnEmergencyUse;
    Button btnStopEmergencyUse;
    Button btnEmergencyCall;
    Button btnStopEmergencyCall;

    Handler hand = new Handler();

    // 기타
    private String txtSubject;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

    }

    public void onProcessCommand() {
        // 뷰
        inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        lockView = (View) inflater.inflate(R.layout.activity_lock, null);
        emergencyUseView = (View) inflater.inflate(R.layout.emergency_use, null);
        emergencyCallView = (View) inflater.inflate(R.layout.emergency_call, null);

        // 타이머
        countdownView = lockView.findViewById(R.id.countdownView);
        startTimer(); // 타이머 시작

        // 스톱워치
        chronometer = lockView.findViewById(R.id.chronometer);
        chronometer.setFormat("%s");

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s = (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
            }
        });

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start(); // 스톱워치 시작

        // 잠금 뷰 제거, 홈 화면 이동, 긴급사용 정지 버튼 뷰 표시
        btnEmergencyUse = lockView.findViewById(R.id.btnEmergencyUse);
        // btnEmergencyUse 클릭 리스너
        btnEmergencyUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!aleadyUsed) {
                    emergencyUse();

                    hand.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!checkLock){
                                //chronometer.start();
                                stopEmergencyUse();
                            }
                        }
                    }, 20000);
                    aleadyUsed = true;
                } else {
                    //Toast toast = Toast.makeText(getApplicationContext(), "긴급 사용은 1회 제한입니다.",Toast.LENGTH_SHORT);
                    //toast.show();
                }
                //chronometer.stop();
            }
        });

        // 긴급사용 정지 버튼 뷰 제거, 잠금 뷰 표시
        btnStopEmergencyUse = emergencyUseView.findViewById(R.id.btnStopEmergencyUse);
        // btnUse 클릭 리스너
        btnStopEmergencyUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopEmergencyUse();
                //chronometer.start();
            }
        });

        // 긴급통화 정지 버튼 뷰 제거, 잠금 뷰 표시
        btnStopEmergencyCall = emergencyCallView.findViewById(R.id.btnStopEmergencyCall);
        // btnUse 클릭 리스너
        btnStopEmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopEmergencyCall();
                //chronometer.start();
            }
        });

        btnEmergencyCall = lockView.findViewById(R.id.btnEmergencyCall);
        btnEmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergencyCall();
            }
        });

        params1 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        // 화면은 landscape 로 하는데, 뒤집어도 같은 모습으로 보기 위해서 sensor landscape로 설정
        // params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

        params2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT
        );

        try {
            winMgr = (WindowManager) getSystemService(WINDOW_SERVICE);
            winMgr.addView(lockView, params1);
            checkLock = true;
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception message : " + e.getLocalizedMessage());
        }
    }

    // SettingLockActivity에서 값 받아오기
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand() called");

        if (intent == null) {
            Log.d(TAG, "전달받은 데이터 없음");
            return Service.START_STICKY; //서비스가 종료되어도 자동으로 다시 실행시켜줘!
        } else {
            // intent가 null이 아니다.
            // 액티비티에서 intent를 통해 전달한 내용을 뽑아낸다.(if exists)
            countdownTime = intent.getLongExtra("시간", id);
            txtSubject = intent.getStringExtra("과목명");
            Log.d(TAG, "전달받은 데이터: " + countdownTime);
            // etc..
            onProcessCommand();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    // 서비스 종료
    @Override
    public void onDestroy() {
        super.onDestroy();
        winMgr.removeView(lockView);
    }

    public void emergencyUse() {
        if(winMgr != null) {
            if(lockView != null) {
                winMgr.removeView(lockView);
            }
        }

        Intent intent = new Intent(Intent.ACTION_MAIN); // 태스크의 첫 액티비티로 시작
        intent.addCategory(Intent.CATEGORY_HOME);   // 홈화면 표시
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 새로운 태스크를 생성하여 그 태스크안에서 액티비티 추가
        startActivity(intent);

        winMgr.addView(emergencyUseView, params2);

        checkLock = false;
    }

    public void stopEmergencyUse() {
        if(winMgr != null) {
            if(emergencyUseView != null) {
                winMgr.removeView(emergencyUseView);
            }
        }

        Intent intent = new Intent(getApplicationContext(), LockedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        winMgr.addView(lockView, params1);

        checkLock = true;
    }

    public void emergencyCall() {
        if(winMgr != null) {
            winMgr.removeView(lockView);
        }

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        winMgr.addView(emergencyCallView, params2);
    }

    public void stopEmergencyCall() {
        if(winMgr != null) {
            if(emergencyCallView != null) {
                winMgr.removeView(emergencyCallView);
            }
        }

        Intent intent = new Intent(getApplicationContext(), LockedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        winMgr.addView(lockView, params1);

        checkLock = true;
    }

    // 타이머 구현
    private void startTimer() {
        Log.d(TAG, "startTimer 1");
        if(firstState) { // 첫 시작인 경우, 전달받은 값으로 시작
            time = countdownTime;
        } else { // 이후에는 전달받은 값이 아닌
            time = tempTime;
        }
        countDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tempTime = millisUntilFinished;
                updateTimer();
                Log.d(TAG, "startTimer 2");
            }

            @Override
            public void onFinish() { }
        }.start();

        timerRunning = true;
        firstState = false;
    }

    // 시간 업데이트
    private void updateTimer() {
        int hour = (int) tempTime / 3600000;
        int minutes = (int) tempTime % 3600000 / 60000;
        int seconds = (int) tempTime % 3600000 % 60000 / 1000;

        String timeLeftText = "";

        timeLeftText = "" + hour + ":";

        if(minutes<10) timeLeftText += "0";
        timeLeftText += minutes +":";

        if(seconds <10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownView.setText(timeLeftText);

        Log.d(TAG, "시간 : " + timeLeftText);

        if(timeLeftText.equals("0:00:00")) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("공부시간", hh+":"+mm+":"+ss);
            intent.putExtra("과목명", txtSubject);

            //MainFragment mainFragment = new MainFragment();

            /*
            Bundle bundle = new Bundle();
            bundle.putString("공부시간", hh+":"+mm+":"+ss);
            bundle.putString("과목명", txtSubject);
            mainFragment.setArguments(bundle);
            */

            startActivity(intent);
            onDestroy();
            Log.d(TAG, "종료");
        }
    }

}