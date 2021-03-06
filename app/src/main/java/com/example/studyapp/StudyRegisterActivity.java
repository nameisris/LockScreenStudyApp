package com.example.studyapp;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StudyRegisterActivity extends AppCompatActivity{
    private ImageButton back;
    private Button contract;
    private EditText et_GropName;
    private EditText et_Introduce;
    private EditText et_Personnel;
    private EditText et_GropPassword;

    private long backKeyPressedTime = 0;
    private Toast toast;

    phpDown task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_register);
        back = findViewById(R.id.back);
        contract = findViewById(R.id.contract);

        et_GropName = findViewById(R.id.et_GropName);
        et_Introduce = findViewById(R.id.et_Introduce);
        et_Personnel = findViewById(R.id.et_Personnel);
        et_GropPassword = findViewById(R.id.et_GropPassword);

        task = new phpDown();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyRegisterActivity.this, HomeActivity.class);
                intent.putExtra("userID", getIntent().getStringExtra("userID"));
                intent.putExtra("", 1);
                startActivity(intent);
            }
        });

        contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = getIntent().getStringExtra("userID");
                String GropName = et_GropName.getText().toString();
                String Introduce = et_Introduce.getText().toString();
                String Personnel = et_Personnel.getText().toString();
                String GropPassword = et_GropPassword.getText().toString();
                if (userID.equals("") || GropName.equals("") || Introduce.equals("") || Personnel.equals("")) {
                    Toast.makeText(getApplicationContext(), "????????? ??? ???????????????!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        try {
                            task.execute("http://studyman.ivyro.net/study_reg.php?userID=" + userID + "&GropName=" + GropName + "&Introduce=" + Introduce + "&Personnel=" + Personnel + "&GropPassword=" + GropPassword);
                            Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StudyRegisterActivity.this, HomeActivity.class);
                            intent.putExtra("userID", getIntent().getStringExtra("userID"));
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            task.cancel(true);
                            task = new phpDown();
                            task.execute("http://studyman.ivyro.net/study_reg.php?userID=" + userID + "&GropName=" + GropName + "&Introduce=" + Introduce + "&Personnel=" + Personnel + "&GropPassword=" + GropPassword);
                            Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StudyRegisterActivity.this, HomeActivity.class);
                            intent.putExtra("userID", getIntent().getStringExtra("userID"));
                            startActivity(intent);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "?????? ????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                        task.cancel(true);
                        task = new phpDown();
                    }
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
            toast = Toast.makeText(this, "?????? ?????? ????????? ??? ??? ??? ???????????? ???????????????.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ?????? ?????? ????????? ?????? ???
        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ????????? ???????????? ??????
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }
}
