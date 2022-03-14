package com.example.studyapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;

class FragmentAdapter extends FragmentPagerAdapter {

    // ViewPager에 들어갈 Fragment들을 담을 리스트
    private ArrayList<Fragment> fragments = new ArrayList<>();

    // 필수 생성자
    FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                return statisticsFragment;
            case 1:
                PlannerFragment plannerFragment = new PlannerFragment();
                return plannerFragment;
            case 2:
                MainFragment mainFragment = new MainFragment();
                return mainFragment;
            case 3:
                GroupFragment groupFragment = new GroupFragment();
                return groupFragment;
            case 4:
                BoardFragment boardFragment = new BoardFragment();
                return boardFragment;

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