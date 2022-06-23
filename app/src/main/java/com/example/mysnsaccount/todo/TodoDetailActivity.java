package com.example.mysnsaccount.todo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysnsaccount.R;

public class TodoDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        tvTitle = findViewById(R.id.detail_tv_title);
        tvDate = findViewById(R.id.detail_tv_date);
        tvContent = findViewById(R.id.detail_tv_content);

        Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("title"));
        tvDate.setText(intent.getStringExtra("date"));
        tvContent.setText(intent.getStringExtra("content"));


    }
}
