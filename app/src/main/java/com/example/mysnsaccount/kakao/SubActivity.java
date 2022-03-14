package com.example.mysnsaccount.kakao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mysnsaccount.R;
import com.example.mysnsaccount.util.GLog;
import com.kakao.sdk.user.UserApiClient;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        String strName, strProfileImg;
        strName = intent.getStringExtra("name");
        strProfileImg = intent.getStringExtra("profileURL");

        TextView tvName = findViewById(R.id.tv_name);
        ImageView ivProfile = findViewById(R.id.iv_profile);
        Button btnLogout = findViewById(R.id.btn_logout);
        Button btnUnlink = findViewById(R.id.btn_unlink);

        //사용자 정보 기본 요청
        UserApiClient.getInstance().me((user, throwable) -> {
            if (throwable != null) {
                GLog.d("사용자 정보 요청 실패");
            } else {
                tvName.setText(strName);
                Glide.with(SubActivity.this).load(strProfileImg).into(ivProfile);
            }
            return null;
        });

        //로그아웃
        btnLogout.setOnClickListener(view -> UserApiClient.getInstance().logout(throwable -> {
            if (throwable != null) {
                GLog.d("로그아웃 실패");
            } else {
                GLog.d("로그아웃 성공");
                Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
            }
            Intent intentback = new Intent(getApplicationContext(), LoginMainActivity.class);
            startActivity(intentback);
            finish();
            return null;
        }));

        //연결끊기
        btnUnlink.setOnClickListener(view -> UserApiClient.getInstance().unlink(throwable -> {
            if (throwable != null) {
                GLog.d("연결 끊기 실패");
            } else {
                GLog.d("연결 끊기 성공");
                Toast.makeText(this, "연결 끊기 성공", Toast.LENGTH_SHORT).show();
            }
            Intent intentback = new Intent(getApplicationContext(), LoginMainActivity.class);
            startActivity(intentback);
            finish();
            return null;
        }));
    }
}