package com.example.mysnsaccount.customwebview;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysnsaccount.R;

public class WebViewActivity extends AppCompatActivity {
    //웹뷰 객체 생성
    private WebView myWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview); //main화면 불러오기

        //웹뷰 객체
        myWebView = findViewById(R.id.webview);

        WebViewManager manager = new WebViewManager(WebViewActivity.this, myWebView);
        manager.setSettings();
        manager.setJavascriptInterface("DKITec");
//        manager.loadUrl("file:///android_asset/www/sample.html");
        manager.loadUrl("http://10.112.58.190/");
    }
}
