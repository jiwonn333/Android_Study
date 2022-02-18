package com.example.mysnsaccount.SNSLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysnsaccount.MainActivity;
import com.example.mysnsaccount.R;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    //로그아웃, 회원탈퇴
    private TextView tvSecondUserID, tvSecondNickname;
    private Button btnSecondLogout, btnSecondResign;
    private List<String> userInfo = new ArrayList<>();

    Button.OnClickListener mLogoutListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            GlobalAuthHelper.accountLogout(getApplicationContext(), SecondActivity.this);
        }
    };

    Button.OnClickListener mResignListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GlobalAuthHelper.accountResign(getApplicationContext(), SecondActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        tvSecondUserID = findViewById(R.id.tv_second_userid);
        tvSecondNickname = findViewById(R.id.tv_second_nickname);
        btnSecondLogout = findViewById(R.id.btn_second_logout);
        btnSecondResign = findViewById(R.id.btn_second_resign);

        initView();

        btnSecondLogout.setOnClickListener(mLogoutListener);
        btnSecondResign.setOnClickListener(mResignListener);

    }

    //계정정보 출력
    private void initView() {
        GlobalHelper mGlobalHelper = new GlobalHelper();
        userInfo = mGlobalHelper.getGlobalUserLoginInfo();
        tvSecondUserID.setText("아이디 :" + userInfo.get(0));
        tvSecondNickname.setText("닉네임 :" + userInfo.get(1));
    }

    public void directToMainActivity(Boolean result) {
        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
