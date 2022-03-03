package com.example.mysnsaccount;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mysnsaccount.CustomWebView.WebViewActivity;
import com.example.mysnsaccount.Permissions.PermissionActivity;
import com.example.mysnsaccount.Recyclerview.RecyclerviewActivity;
import com.example.mysnsaccount.RetrofitExample.RetrofitActivity;
import com.example.mysnsaccount.SNSLogin.KakaoLogin;
import com.example.mysnsaccount.SNSLogin.SecondActivity;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    //카카오톡 로그인
    private ImageButton mKakaoLoginBtn;
    private LoginButton mKakaoLoginBtnBasic;
    private KakaoLogin.KakaoSessionCallback sessionCallback;
    //웹뷰버튼, 서버통신 버튼
    private Button wvbtn, apibtn, perbtn,recyclerviewbtn;


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
                Intent intentPer= new Intent(getApplicationContext(), PermissionActivity.class);
                startActivity(intentPer);
            }
        });

        //리사이클러뷰 버튼 클릭
        recyclerviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentRe= new Intent(getApplicationContext(), RecyclerviewActivity.class);
                startActivity(intentRe);
            }
        });

        //커스텀버튼 클릭시 카카오톡 로그인화면으로 넘어감 (세션이 있다면 재로그인 없이 즉시 콜백클래스 작동
        mKakaoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mKakaoLoginBtnBasic.performClick(); //로그인버튼클릭시 자동으로 카카오톡 로그인화면으로 넘어감
            }
        });

        if (!HasKakaoSession()) {
            sessionCallback = new KakaoLogin.KakaoSessionCallback(getApplicationContext(), MainActivity.this);
            Session.getCurrentSession().addCallback(sessionCallback);
        } else if (HasKakaoSession()) {
            sessionCallback = new KakaoLogin.KakaoSessionCallback(getApplicationContext(), MainActivity.this);
            Session.getCurrentSession().addCallback(sessionCallback);
            Session.getCurrentSession().checkAndImplicitOpen();
        }
        getHashKey(); //해시키 불러오기
    }

    //해시키 구하기
    private void getHashKey() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null");
        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //세션콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private boolean HasKakaoSession() {
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            return false;
        }
        return true;
    }

    public void directToSecondActivity(Boolean result) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (result) {
            Toast.makeText(getApplicationContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            ;
            finish();
        }
    }
}