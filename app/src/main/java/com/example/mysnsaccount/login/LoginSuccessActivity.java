package com.example.mysnsaccount.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.mysnsaccount.R;
import com.example.mysnsaccount.util.GLog;
import com.kakao.sdk.user.UserApiClient;

public class LoginSuccessActivity extends AppCompatActivity {
    TextView tvName, tvId;
    ImageView ivProfile;
    Button btnKakaoLogout, btnLogout, btnUnlink;
    Context context;
    ConstraintLayout kakaoLoginSuccess;
    LinearLayout loginSuccess;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsuccess);

        context = this;

        tvName = findViewById(R.id.tv_name);
        ivProfile = findViewById(R.id.iv_profile);
        btnKakaoLogout = findViewById(R.id.btn_logout_kakao);
        btnUnlink = findViewById(R.id.btn_unlink_kakao);

        kakaoLoginSuccess = findViewById(R.id.kakaoLogin);
        loginSuccess = findViewById(R.id.loginLayout);

        tvId = findViewById(R.id.tv_id);
        btnLogout = findViewById(R.id.btn_logout);


        Intent intent = getIntent();

        String strName = intent.getStringExtra("nickName");
        String strProfileImg = intent.getStringExtra("profileURL");
        tvName.setText(strName);
        Glide.with(LoginSuccessActivity.this).load(strProfileImg).into(ivProfile);

        String strId = intent.getStringExtra("tvId");
        tvId.setText(strId);

        boolean isKakaoLogin = intent.getBooleanExtra("isKakaoLogin", false);
        GLog.d("isKakaoLogin" + isKakaoLogin);


        if (isKakaoLogin) {
            kakaoLoginSuccess.setVisibility(View.VISIBLE);

        } else {
            loginSuccess.setVisibility(View.VISIBLE);

        }


        //로그아웃
        btnKakaoLogout.setOnClickListener(view -> UserApiClient.getInstance().logout(throwable -> {
            if (throwable != null) {
                GLog.d("로그아웃 실패");
                GLog.d(throwable.getMessage());
            } else {
                GLog.d("로그아웃 성공");
                Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show();
            }
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
            finish();
            return null;
        }));


        // 로그아웃 하면 자동로그인 x
        btnLogout.setOnClickListener(view -> {
            SharedPreferences pref = context.getSharedPreferences("loginPref", Activity.MODE_PRIVATE);
            SharedPreferences.Editor prefEdit = pref.edit();
            prefEdit.clear();
            prefEdit.commit();
            finish();
            Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show();
        });
    }
}