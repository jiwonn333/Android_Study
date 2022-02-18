package com.example.mysnsaccount.CustomWebView;

import android.os.Message;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class MyWebChromeClient extends WebChromeClient {
    //현재 로딩되는 페이지 상태 표시
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }

    //웹에서 새 창을 열 때 호출
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    //웹뷰가 창 닫을 때 호출
    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
    }
}
