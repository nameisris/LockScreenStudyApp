package com.example.studyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {
    private FloatingActionButton btnAddGroup;


    /*
    List<GroupVO> arrayGroup = new ArrayList<>(); // GroupVO형 데이터를 저장할 arrayUser
    GroupAdapter groupAdapter = new GroupAdapter();

    */

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_group, container, false);

        btnAddGroup = view.findViewById(R.id.btnAddGroup);
        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent study_reg_act = new Intent(getActivity(), StudyRegisterActivity.class);
                study_reg_act.putExtra("userID", getActivity().getIntent().getStringExtra("userID"));
                startActivity(study_reg_act);
            }
        });

        /*
        ListView list = view.findViewById(R.id.listGroup);
        list.setAdapter(groupAdapter);

         */

        return view;
    }

    /*
    // 메세지 어댑터
    class GroupAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arrayGroup.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item_group, viewGroup, false);

            return view;
        }
    }
    */

}
