package com.example.studyapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment {
    Button btnUser;
    TextView txtDate;
    FloatingActionButton btnAddSubject;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat;

    List<SubjectVO> arraySubject = new ArrayList<>(); // GroupVO형 데이터를 저장할 arrayUser
    SubjectAdapter subjectAdapter = new SubjectAdapter();
    ListView listSubject;

    String studyTime = "";
    String subjectName = "";

    public MainFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        mFormat = new SimpleDateFormat("yyyy.MM.dd");

        txtDate = view.findViewById(R.id.txtDate);
        txtDate.setText(mFormat.format(mDate));

        // 아이디 값
        listSubject = view.findViewById(R.id.listSubject);
        // 어댑터 set
        listSubject.setAdapter(subjectAdapter);

        Bundle extra = this.getArguments();
        if(extra != null) {
            extra = getArguments();
            studyTime = extra.getString("공부시간");
            subjectName = extra.getString("과목명");
        } else {
            studyTime = "";
            subjectName = "";
        }

        btnUser = view.findViewById(R.id.btnUser);
        // btnUser 클릭 리스너
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(), SettingLockActivity.class);
                //startActivity(intent);
                Intent mypage_act = new Intent(getActivity(), MyPageActivity.class);
                mypage_act.putExtra("userID", getActivity().getIntent().getStringExtra("userID"));
                startActivity(mypage_act);
            }
        });

        ListView list = view.findViewById(R.id.listSubject);
        list.setAdapter(subjectAdapter);


        btnAddSubject = view.findViewById(R.id.btnAddSubject);
        // btnUser 클릭 리스너
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(), SettingLockActivity.class);
                //startActivity(intent);

                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("과목 생성");
                ad.setMessage("과목명을 입력해주십시오.");

                final EditText subjectNameView = new EditText(getActivity());
                ad.setView(subjectNameView);

                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SubjectVO subject = new SubjectVO();
                        subject.setSubjectName(subjectNameView.getText().toString());
                        subject.setSubjectDate(mFormat.format(mDate));
                        subject.setSubjectTime("00:00:00");

                        arraySubject.add(subject);
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        subjectAdapter.notifyDataSetChanged();

        return view;
    }

    // 메세지 어댑터
    class SubjectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arraySubject.size();
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
            view = getLayoutInflater().inflate(R.layout.item_subject, viewGroup, false);
            long time = 0;

            TextView txtSubject=view.findViewById(R.id.txtSubjectName);
            TextView txtTime=view.findViewById(R.id.txtSubjectTime);

            txtSubject.setText(arraySubject.get(i).getSubjectName());

            if(subjectName.equals(arraySubject.get(i).getSubjectName())) {
                txtTime.setText(studyTime);
            } else {
                txtTime.setText(arraySubject.get(i).getSubjectTime());
            }
            txtSubject.setText(arraySubject.get(i).getSubjectName());

            Button btnStart = view.findViewById(R.id.btnStart);
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SettingLockActivity.class);
                    intent.putExtra("과목명", arraySubject.get(i).getSubjectName());
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
