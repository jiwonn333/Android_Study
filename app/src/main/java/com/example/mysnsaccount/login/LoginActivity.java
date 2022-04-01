package com.example.mysnsaccount.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mysnsaccount.MainActivity;
import com.example.mysnsaccount.R;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.HashUtils;
import com.example.mysnsaccount.util.UserPreference;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Profile;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends Activity {
    Button btnKakaoLogin, btnLogin;
    EditText etUserId, etUserPw;
    String userId, userPw, getUserId, getUserPw, kakaoProfileImageUrl, kakaoProfileUserName;
    CheckBox checkSaveId, checkAutoLogin;
    Intent intent;
    boolean validation, saveIdCheck, autoLoginCheck;
    boolean loginSuccess;
    Context context = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnKakaoLogin = findViewById(R.id.btn_kakao_login);
        btnLogin = findViewById(R.id.btn_login);
        etUserId = findViewById(R.id.user_id);
        etUserPw = findViewById(R.id.user_pw);
        checkSaveId = findViewById(R.id.ck_id);
        checkAutoLogin = findViewById(R.id.ck_autologin);

        userId = UserPreference.getUserId(context);
        userPw = UserPreference.getUserPassword(context);

        saveIdCheck = UserPreference.getSaveIdCheck(context);
        autoLoginCheck = UserPreference.getAutoLoginCheck(context);

        loginSuccess = UserPreference.getLoginSuccess(context);

        // 아이디 저장 체크박스가 체크되어있으면 실행, 아니면 false
        checkSaveId.setChecked(saveIdCheck);
        if (saveIdCheck) {
            etUserId.setText(userId);
        } else {
            etUserId.setText("");
            etUserPw.setText("");
        }

        //자동로그인 체크박스 체크되어있으면 바로 로그인 실행
        checkAutoLogin.setChecked(autoLoginCheck);
        if (autoLoginCheck) {
            if (loginCheckValidation(userId, userPw)) {
                Toast.makeText(context, "자동 로그인 성공", Toast.LENGTH_SHORT).show();
                startIntent();
            }
        }


        btnLogin.setOnClickListener(view -> {
            getUserId = etUserId.getText().toString();
            getUserPw = etUserPw.getText().toString();

            if (!TextUtils.isEmpty(getUserPw)) {
                getUserPw = HashUtils.getEncryptData(getUserPw);
            }

            validation = loginCheckValidation(getUserId, getUserPw);

            if (validation) {
                UserPreference.setUserId(context, getUserId);
                UserPreference.setUserPassword(context, getUserPw);
                UserPreference.setSaveIdCheck(context, checkSaveId.isChecked());
                UserPreference.setAutoLoginCheck(context, checkAutoLogin.isChecked());
                UserPreference.setLoginSuccess(context, true);
                userId = getUserId;
                startIntent();
                Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show();
            } else {
                GLog.d("validation : " + validation);
            }
        });

        //카카오톡으로 로그인하기 버튼 클릭 시
        btnKakaoLogin.setOnClickListener(view -> {
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(context)) {
                UserApiClient.getInstance().loginWithKakaoTalk(context, (oAuthToken, throwable) -> {
                    if (throwable != null) {
                        GLog.d("카카오톡으로 로그인 실패");

                        if (throwable instanceof ClientError && (((ClientError) throwable).getReason()) == ClientErrorCause.Cancelled) {
                            return null;
                        }

                        UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
                    } else if (oAuthToken != null) {
                        GLog.d("카카오톡으로 로그인 성공");

                        startKakaoLoginActivity();

                    }
                    return null;
                });
            } else {
                UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
            }
        });
    }


    private Function2 callback = (Function2<OAuthToken, Throwable, Unit>) (oAuthToken, throwable) -> {
        if (throwable != null) {
            GLog.d("카카오계정으로 로그인 실패");
        } else if (oAuthToken != null) {
            GLog.d("카카오계정으로 로그인 성공");
            startKakaoLoginActivity();
        }
        return null;
    };

    private void startKakaoLoginActivity() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (throwable != null) {
                GLog.d("사용자 정보 요청 실패");
            } else if (user != null) {
                Account account = user.getKakaoAccount();
                if (account != null) {
                    Profile profile = account.getProfile();
                    if (profile != null) {
                        kakaoProfileImageUrl = profile.getProfileImageUrl();
                        kakaoProfileUserName = profile.getNickname();
                        UserPreference.setKakaoUserName(context, kakaoProfileUserName);
                        UserPreference.setKakaoUserUrl(context, kakaoProfileImageUrl);
                        UserPreference.setKakaoLoginSuccess(context, true);
                        startIntent();
                    } else {
                        GLog.d("profile이 null값 입니다.");
                    }
                } else {
                    GLog.d("account가 null값 입니다.");
                }
            } else {
                GLog.d("user가 null값 입니다.");
            }
            return null;
        });
    }


    private boolean loginCheckValidation(String userId, String userPw) {

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(context, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(userPw)) {
            Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TextUtils.equals(UserPreference.PREF_USER_ID, userId)) {
            Toast.makeText(context, "아이디가 틀림", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TextUtils.equals(UserPreference.PREF_USER_PW, userPw)) {
            Toast.makeText(context, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void startIntent() {
        intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

