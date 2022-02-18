package com.example.mysnsaccount.SNSLogin;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.util.helper.CommonProtocol;
import com.kakao.util.helper.Utility;

import java.util.ArrayList;
import java.util.List;

//SDK 초기화
public class GlobalHelper extends Application {
    private static volatile GlobalHelper mInstance = null;
    private static List<String> mGlobalUserLoginInfo = new ArrayList<String>();

    //전역변수에 값을 저장하고 반환하는 코드를 위해 추가
    public static List<String> getGlobalUserLoginInfo() {
        return mGlobalUserLoginInfo;
    }

    public static void setGlobalUserLoginInfo(List<String> userLoginInfo) {
        mGlobalUserLoginInfo = userLoginInfo;
    }

    public static GlobalHelper getGlobalApplicationContext() {
        if (mInstance == null) {
            throw new IllegalStateException("this application doesn't GlobalAuthHelper");
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Log.d("testLog", ", metaData : " + Utility.getMetadata(this, CommonProtocol.APP_KEY_PROPERTY));
        KakaoSDK.init(new KakaoSDKAdapter());

    }

    public class KakaoSDKAdapter extends KakaoAdapter {
        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL}; //모든 로그인 방식을 사용하고 싶을때 지정
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Nullable
                @Override
                public ApprovalType getApprovalType() {
                    return null;
                }

                @Override
                public boolean isSaveFormData() {
                    return false;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return new IApplicationConfig() {
                @Override
                public Context getApplicationContext() {
                    return GlobalHelper.getGlobalApplicationContext();
                }
            };
        }
    }
}
