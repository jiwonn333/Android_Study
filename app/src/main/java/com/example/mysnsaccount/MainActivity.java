package com.example.mysnsaccount;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private ImageButton mKakaoLoginBtn;
    private LoginButton mKakaoLoginBtnBasic;
    private KakaoLogin.KakaoSessionCallback sessionCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKakaoLoginBtn = findViewById(R.id.btn_kakao_login);
        mKakaoLoginBtnBasic = findViewById(R.id.btn_kakao_login_basic);

        //커스텀버튼 ㅡㄹ릭시 카카오톡 로그인화면으로 넘어감 (세션이 있다면 재로그인 없이 즉시 콜백클래스 작동
        mKakaoLoginBtn.setOnClickListener(new View.OnClickListener(){
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
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null");
        for (Signature signature : packageInfo.signatures){
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature="+ signature, e);
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
        if (!Session.getCurrentSession().checkAndImplicitOpen()){
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