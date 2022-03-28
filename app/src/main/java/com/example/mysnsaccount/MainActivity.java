package com.example.mysnsaccount;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysnsaccount.customwebview.WebViewActivity;
import com.example.mysnsaccount.dialogtest.AlertDialogActivity;
import com.example.mysnsaccount.login.LoginActivity;
import com.example.mysnsaccount.permission.PermissionActivity;
import com.example.mysnsaccount.recyclerview.RecyclerViewActivity;
import com.example.mysnsaccount.retrofit.RetrofitActivity;

public class MainActivity extends AppCompatActivity {

    //카카오톡 로그인
    Button mKakaoLoginBtn;
    //웹뷰버튼, 서버통신 버튼
    Button wvbtn, apibtn, perbtn, recyclerviewbtn, alertdialogbtn;
    ImageView startLogin, startWebView, startRetrofit, startPer, startRecycler, startDialog;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKakaoLoginBtn = findViewById(R.id.btn_kakao_login);
        wvbtn = findViewById(R.id.wvbtn);
        apibtn = findViewById(R.id.apibtn);
        perbtn = findViewById(R.id.perbtn);
        recyclerviewbtn = findViewById(R.id.recyclerviewbtn);
        alertdialogbtn = findViewById(R.id.alertdialogbtn);


        startLogin = findViewById(R.id.icon_login);
        startWebView = findViewById(R.id.icon_webview);
        startRetrofit = findViewById(R.id.icon_api);
        startPer = findViewById(R.id.icon_per);
        startRecycler = findViewById(R.id.icon_recycler);
        startDialog = findViewById(R.id.icon_dialog);

        //웹뷰 버튼 클릭
        startWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                startActivity(intent);
            }
        });

        //서버통신 버튼 클릭
        startRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentApi = new Intent(getApplicationContext(), RetrofitActivity.class);
                startActivity(intentApi);
            }
        });

        //퍼미션 버튼 클릭
        startPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPer = new Intent(getApplicationContext(), PermissionActivity.class);
                startActivity(intentPer);
            }
        });

        //리사이클러뷰 버튼 클릭
        startRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRe = new Intent(getApplicationContext(), RecyclerViewActivity.class);
                startActivity(intentRe);
            }
        });

        //alertDialog 버튼 클릭
        startDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAlert = new Intent(getApplicationContext(), AlertDialogActivity.class);
                startActivity(intentAlert);
            }
        });


        //로그인페이지 이동
        startLogin.setOnClickListener(view -> {
            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intentLogin);
        });
    }
}
