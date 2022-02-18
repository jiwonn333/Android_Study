package com.example.mysnsaccount.CustomWebView;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

//자바스크립트 코드 안드로이드에 결합
public class AndroidBridge {
    Context mContext;

    AndroidBridge(Context c) {
        mContext = c;
    }

    //버튼 클릭 시 토스트메시지 띄움
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
