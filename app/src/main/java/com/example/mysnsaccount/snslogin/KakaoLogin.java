package com.example.mysnsaccount.snslogin;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.example.mysnsaccount.MainActivity;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;
import java.util.List;

//계정인증 관련 내용 별도 kakaologin클래스로 구현
public class KakaoLogin extends Activity {
    //로그인 및 사용자 정보 요청
    public static class KakaoSessionCallback implements ISessionCallback {
        private Context mContext;
        private MainActivity mainActivity;

        public KakaoSessionCallback(Context context, MainActivity activity) {
            this.mContext = context;
            this.mainActivity = activity;
        }

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(mContext, "로그인 오류가 발생했습니다." + e.toString(), Toast.LENGTH_SHORT).show();
        }

        //requestMe()를 호출하여 계정정보를 전역변수에 저장하는 코드
        protected void requestMe() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    mainActivity.directToSecondActivity(false);
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    List userInfo = new ArrayList<>();
                    userInfo.add(String.valueOf(result.getId()));
                    userInfo.add(result.getKakaoAccount().getProfile().getNickname());
                    GlobalHelper mGlobalHelper = new GlobalHelper();
                    mGlobalHelper.setGlobalUserLoginInfo(userInfo);

                    mainActivity.directToSecondActivity(true);
                }
            });
        }
    }
}
