package com.example.mysnsaccount.kakao;

import android.app.Application;

import com.example.mysnsaccount.R;
import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, getString(R.string.kakao_app_key));
    }
}