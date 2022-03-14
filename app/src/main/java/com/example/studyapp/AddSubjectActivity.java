package com.example.studyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddSubjectActivity extends AppCompatActivity {
    SubjectVO subjectVO;
    EditText subjectNameEditText;
    String subjectName;

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        mFormat = new SimpleDateFormat("yyyy.MM.dd");

        subjectNameEditText = findViewById(R.id.subjectName);
        subjectName = subjectNameEditText.getText().toString();

        subjectVO.setSubjectName(subjectName);
        subjectVO.setSubjectDate(mFormat.format(mDate));
        subjectVO.setSubjectTime("00:00:00");

        Intent intent = new Intent(AddSubjectActivity.this, MainFragment.class);
        intent.putExtra("과목", (Parcelable) subjectVO);
    }
}