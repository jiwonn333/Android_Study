package com.example.mysnsaccount.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.example.mysnsaccount.util.UserPreference;
import com.kakao.sdk.user.UserApiClient;

public class LoginSuccessActivity extends AppCompatActivity {
    TextView kakaoUserName, loginUserName;
    ImageView kakaoProfile;
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

        kakaoLoginSuccess = findViewById(R.id.kakaoLogin);
        kakaoUserName = findViewById(R.id.tv_name);
        kakaoProfile = findViewById(R.id.iv_profile);
        btnKakaoLogout = findViewById(R.id.btn_logout_kakao);
        btnUnlink = findViewById(R.id.btn_unlink_kakao);

        loginSuccess = findViewById(R.id.loginLayout);
        loginUserName = findViewById(R.id.tv_id);
        btnLogout = findViewById(R.id.btn_logout);

        Intent intent = getIntent();
        String kakaoLoginUserName = intent.getStringExtra("kakaoLoginUserName");
        String kakaoProfileURL = intent.getStringExtra("profileURL");
        String loginName = intent.getStringExtra("loginName");
        boolean isKakaoLogin = intent.getBooleanExtra("isKakaoLogin", false);

        if (isKakaoLogin) {
            kakaoLoginSuccess.setVisibility(View.VISIBLE);
            kakaoUserName.setText(kakaoLoginUserName);
            Glide.with(LoginSuccessActivity.this).load(kakaoProfileURL).into(kakaoProfile);
        } else {
            loginSuccess.setVisibility(View.VISIBLE);
            loginUserName.setText(loginName);
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
            UserPreference.setClear(context);
            finish();
            Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show();
        });
    }
}