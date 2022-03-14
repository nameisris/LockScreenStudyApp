package com.example.studyapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {
    private ViewPager viewPager;
    public CalendarView calendarView;

    public StatisticsFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        // final??

        calendarView =view.findViewById(R.id.calendarView);

        // 뷰페이저
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        //viewPager.setCurrentItem(0); // 현재 ViewPager를 0번째 프래그먼트로 설정 (초기화면)

        // 탭 3개 생성
        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.newTab();
        tabLayout.newTab();
        tabLayout.newTab();

        // 어댑터
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter); // ViewPager와 PagerAdapter 연결

        // ViewPager에 pagerAdapter를 통해 Fragment 추가
        StatisticsDayFragment statisticsDayFragment = new StatisticsDayFragment();
        StatisticsWeekFragment statisticsWeekFragment = new StatisticsWeekFragment();
        StatisticsMonthFragment statisticsMonthFragment = new StatisticsMonthFragment();

        pagerAdapter.addItem(statisticsDayFragment);
        pagerAdapter.addItem(statisticsWeekFragment);
        pagerAdapter.addItem(statisticsMonthFragment);
        pagerAdapter.notifyDataSetChanged();

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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });

        return view;
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        // ViewPager에 들어갈 Fragment들을 담을 리스트
        private ArrayList<Fragment> fragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        public Fragment getItem(int position){
            switch (position){
                case 0:
                    StatisticsDayFragment statisticsDayFragment = new StatisticsDayFragment();
                    return statisticsDayFragment;
                case 1:
                    StatisticsWeekFragment statisticsWeekFragment = new StatisticsWeekFragment();
                    return statisticsWeekFragment;
                case 2:
                    StatisticsMonthFragment statisticsMonthFragment = new StatisticsMonthFragment();
                    return statisticsMonthFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        // List에 Fragment를 담을 함수
        void addItem(Fragment fragment) {
            fragments.add(fragment);
        }
    }

}
