package com.example.studyapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeActivity extends AppCompatActivity {
    private ViewPager viewPager;

    String txtSubject;
    String time;

    phpDown task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        if(intent != null) {
            txtSubject = intent.getStringExtra("과목명");
            time = intent.getStringExtra("공부시간");
        }


        // 뷰페이저
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        // viewPager.setCurrentItem(0); // 현재 ViewPager를 0번째 프래그먼트로 설정 (초기화면)

        task = new phpDown();

        // 탭 5개 생성
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.newTab();
        tabLayout.newTab();
        tabLayout.newTab();
        tabLayout.newTab();
        tabLayout.newTab();

        // 어댑터
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter); // ViewPager와 FragmentAdapter 연결

        // ViewPager에 fragmentAdapter를 통해 Fragment 추가
        StatisticsFragment statisticsFragment = new StatisticsFragment();
        PlannerFragment plannerFragment = new PlannerFragment();
        MainFragment mainFragment = new MainFragment();
        GroupFragment groupFragment = new GroupFragment();
        BoardFragment boardFragment = new BoardFragment();

        Bundle bundle = new Bundle();
        bundle.putString("공부시간", time);
        bundle.putString("과목명", txtSubject);
        mainFragment.setArguments(bundle);

        fragmentAdapter.addItem(statisticsFragment);
        fragmentAdapter.addItem(plannerFragment);
        fragmentAdapter.addItem(mainFragment);
        fragmentAdapter.addItem(groupFragment);
        fragmentAdapter.addItem(boardFragment);
        fragmentAdapter.notifyDataSetChanged();

        // mainFragment를 첫 화면(프래그먼트) 설정
        viewPager.setCurrentItem(2);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //php
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
}
