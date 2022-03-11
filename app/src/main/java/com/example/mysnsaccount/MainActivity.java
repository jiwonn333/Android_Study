package com.example.mysnsaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysnsaccount.customwebview.WebViewActivity;
import com.example.mysnsaccount.kakao.LoginMainActivity;
import com.example.mysnsaccount.permission.PermissionActivity;
import com.example.mysnsaccount.recyclerview.RecyclerViewActivity;
import com.example.mysnsaccount.retrofit.RetrofitActivity;
import com.kakao.usermgmt.LoginButton;

public class MainActivity extends AppCompatActivity {

    //카카오톡 로그인
    private Button mKakaoLoginBtn;
    private LoginButton mKakaoLoginBtnBasic;
    //    private KakaoLogin.KakaoSessionCallback sessionCallback;
    //웹뷰버튼, 서버통신 버튼
    private Button wvbtn, apibtn, perbtn, recyclerviewbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKakaoLoginBtn = findViewById(R.id.btn_kakao_login);
        mKakaoLoginBtnBasic = findViewById(R.id.btn_kakao_login_basic);
        wvbtn = findViewById(R.id.wvbtn);
        apibtn = findViewById(R.id.apibtn);
        perbtn = findViewById(R.id.perbtn);
        recyclerviewbtn = findViewById(R.id.recyclerviewbtn);

        //웹뷰 버튼 클릭
        wvbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                startActivity(intent);
            }
        });

        //서버통신 버튼 클릭
        apibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentApi = new Intent(getApplicationContext(), RetrofitActivity.class);
                startActivity(intentApi);
            }
        });

        //퍼미션 버튼 클릭
        perbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPer = new Intent(getApplicationContext(), PermissionActivity.class);
                startActivity(intentPer);
            }
        });

        //리사이클러뷰 버튼 클릭
        recyclerviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRe = new Intent(getApplicationContext(), RecyclerViewActivity.class);
                startActivity(intentRe);
            }
        });

        //커스텀버튼 클릭시 카카오톡 로그인화면으로 넘어감 (세션이 있다면 재로그인 없이 즉시 콜백클래스 작동
        mKakaoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLogin = new Intent(getApplicationContext(), LoginMainActivity.class);
                startActivity(intentLogin);
            }
        });
    }
}
