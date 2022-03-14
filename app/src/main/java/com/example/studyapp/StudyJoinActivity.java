package com.example.studyapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StudyJoinActivity extends AppCompatActivity {

    private ImageButton back;
    private TextView tv_1;
    private EditText et_GropName;
    private TextView tv_2;
    private EditText et_GropPassword;
    private Button btn_1;

    private long backKeyPressedTime = 0;
    private Toast toast;

    phpDown task;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_register);
        back = findViewById(R.id.back);
        btn_1 = findViewById(R.id.btn_1);
        tv_1 = findViewById(R.id.tv_1);
        et_GropName = findViewById(R.id.et_GropName);
        tv_2 = findViewById(R.id.tv_3);
        et_GropPassword = findViewById(R.id.et_GropPassword);

        task = new phpDown();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyJoinActivity.this, HomeActivity.class);
                intent.putExtra("userID", getIntent().getStringExtra("userID"));
                intent.putExtra("", 1);
                startActivity(intent);
            }
        });

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = getIntent().getStringExtra("userID");
                String GropName = et_GropName.getText().toString();
                String GropPassword = et_GropPassword.getText().toString();
                try {
                    try {
                        task.execute("http://studyman.ivyro.net/join_grop.php?userID=" + userID + "&GropName=" + GropName + "&GropPassword=" + GropPassword);
                        Toast.makeText(getApplicationContext(), "그룹 참가에 성공했습니다!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(StudyJoinActivity.this, HomeActivity.class);
                        intent.putExtra("userID", getIntent().getStringExtra("userID"));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        task.cancel(true);
                        task = new phpDown();
                        task.execute("http://studyman.ivyro.net/join_grop.php?userID=" + userID + "&GropName=" + GropName + "&GropPassword=" + GropPassword);
                        Toast.makeText(getApplicationContext(), "그룹 참가에 성공했습니다!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(StudyJoinActivity.this, HomeActivity.class);
                        intent.putExtra("userID", getIntent().getStringExtra("userID"));
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "그룹 참가에 실패했습니다!", Toast.LENGTH_SHORT).show();
                    task.cancel(true);
                    task = new phpDown();
                }
            }
        });
    }
    private class phpDown extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for (; ; ) {
                            String line = br.readLine();
                            if (line == null) break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonHtml.toString();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}
