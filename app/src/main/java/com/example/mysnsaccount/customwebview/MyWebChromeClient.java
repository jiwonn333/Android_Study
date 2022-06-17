package com.example.mysnsaccount.customwebview;

import android.os.Message;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.mysnsaccount.util.GLog;

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


    // 콘솔 로그 호출
    @Override
    public boolean onConsoleMessage(ConsoleMessage cm) {
        GLog.d(cm.message() + " -- From line "
                + cm.lineNumber() + " of "
                + cm.sourceId());
        return true;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String msg, JsResult result) {
        return super.onJsAlert(view, url, msg, result);
//        return true;
    }
}
