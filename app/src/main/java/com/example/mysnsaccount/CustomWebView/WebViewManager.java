package com.example.mysnsaccount.CustomWebView;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mysnsaccount.util.Constant;

public class WebViewManager {

    private Context mContext;
    private WebView myWebView;
    private String bridgeName;

    public WebViewManager() {
    }

    //기본 세팅
    public WebViewManager(Context context, WebView webView) {
        this.mContext = context;
        this.myWebView = webView;

        myWebView.getSettings().setJavaScriptEnabled(true);
        //js 사용할 수 있도록 설정
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        //Client 웹뷰에 연결
        myWebView.setWebViewClient(new MyWebViewClient());
        //WebChromeClient 설정 - 브라우저 이벤트 구현을 위해 필요
        myWebView.setWebChromeClient(new MyWebChromeClient());
    }

    //웹페이지 호출
    public void loadUrl(String url) {
        myWebView.loadUrl(url);
    }

    //js 사용할 수 있도록 설정
    public void setJavascriptInterface(String bridgeName) {
        this.bridgeName = bridgeName;
        myWebView.addJavascriptInterface(new AndroidBridge(mContext), TextUtils.isEmpty(bridgeName) ? Constant.BRIDGE_DKI_TEC : bridgeName);
    }

    //js 활성화 여부
    public void setJavaScriptEnabled(boolean isEnable) {
        myWebView.getSettings().setJavaScriptEnabled(isEnable);
    }

    //자동으로 창을 열도록 js에 지시할건지
    public void setJavaScriptCanOpenWindowsAutomatically(boolean isEnable) {
        myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(isEnable);
    }

    //이미지 리소스 자동으로 로드 설정
    public void setLoadImagesAutomatically(boolean isEnable) {
        myWebView.getSettings().setLoadsImagesAutomatically(isEnable);
    }

    //확대축소 기능 사용 여부
    public void setSupportZoom(boolean isEnable) {
        myWebView.getSettings().setSupportZoom(isEnable);
    }

    //로컬스토리지 사용 여부
    public void setDomStorageEnabled(boolean isEnable) {
        myWebView.getSettings().setDomStorageEnabled(isEnable);
    }

    //앱 내부 캐시 사용 여부
    public void setAppCacheEnabled(boolean isEnable) {
        myWebView.getSettings().setAppCacheEnabled(isEnable);
    }

    //웹뷰 내에서 파일 엑세스 활성화 여부
    public void setAllowFileAccess(boolean isEnable) {
        myWebView.getSettings().setAllowFileAccess(isEnable);
    }

    //컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
    public void setLoadWithOverviewMode(boolean isEanable) {
        myWebView.getSettings().setLoadWithOverviewMode(isEanable);
    }

    private class MyWebViewClient extends WebViewClient {
        //로딩 시작될 때
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }
}
