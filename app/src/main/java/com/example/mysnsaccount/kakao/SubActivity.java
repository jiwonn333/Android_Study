package com.example.mysnsaccount.kakao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mysnsaccount.R;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        String strName, strProfileImg;
        strName = intent.getStringExtra("name");
        strProfileImg = intent.getStringExtra("profileImg");

        TextView tvName = findViewById(R.id.tv_name);
        ImageView ivProfile = findViewById(R.id.iv_profile);

        // 프로필 이름 set
        tvName.setText(strName);
        // 프로필 이미지 사진 set
        Glide.with(this).load(strProfileImg).into(ivProfile);

        //로그아웃
        findViewById(R.id.btn_logout).setOnClickListener(view -> UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 시 수행하는 부분
                finish(); // 현재 액티비티 종료
            }
        }));
    }
}