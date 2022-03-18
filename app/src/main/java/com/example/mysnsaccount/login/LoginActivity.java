package com.example.mysnsaccount.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.util.Constant;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.HashUtils;
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
    String UserId, UserPw;
    CheckBox saveId, autoLogin;
    SharedPreferences pref;
    SharedPreferences.Editor prefEdit;
    Intent intent;
    boolean idSave, autoLoginCheck, validation;

    Context context = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnKakaoLogin = findViewById(R.id.btn_kakao_login);
        btnLogin = findViewById(R.id.btn_login);
        etUserId = findViewById(R.id.user_id);
        etUserPw = findViewById(R.id.user_pw);
        saveId = findViewById(R.id.ck_id);
        autoLogin = findViewById(R.id.ck_autologin);


        pref = getSharedPreferences("loginPref", MODE_PRIVATE);
        prefEdit = pref.edit();

        UserId = pref.getString(Constant.PREF_KEY_USER_ID, "");
        UserPw = pref.getString(Constant.PREF_KEY_USER_PW, "");


        //아이디 저장 체크박스 체크되어있으면 실행, 체크 안되어있으면 false
        if (pref.getBoolean("idSave", false)) {
            GLog.d("pref.getBoolean(idSave, false) : " + pref.getBoolean("idSave", false));
            etUserId.setText(pref.getString(Constant.PREF_KEY_USER_ID, ""));
            saveId.setChecked(true);
            idSave = true;
        } else {
            etUserId.setText("");
            etUserPw.setText("");
            saveId.setChecked(false);
            idSave = false;
        }

        //자동로그인 체크박스 체크되어있으면 바로 로그인 실행
        if (pref.getBoolean("autoLoginCheck", false)) {
            GLog.d("pref.getBoolean(autoLoginCheck) : " + pref.getBoolean("autoLoginCheck", false));

            autoLogin.setChecked(true);
            autoLoginCheck = true;

            UserId = pref.getString(Constant.PREF_KEY_USER_ID, "");
            UserPw = pref.getString(Constant.PREF_KEY_USER_PW, "");

            if (!TextUtils.isEmpty(UserId) && !TextUtils.isEmpty(UserPw)) {
                if (TextUtils.equals(Constant.PREF_USER_ID, UserId) && TextUtils.equals(Constant.PREF_USER_PW, UserPw)) {
                    Toast.makeText(context, "자동 로그인 성공", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), LoginSuccessActivity.class);
                    intent.putExtra("tvId", UserId);
                    startActivity(intent);
                    finish();
                } else {
                    loginValidation(UserId, UserPw);
                    GLog.d("자동로그인 실패");
                }
            } else {
                loginValidation(UserId, UserPw);
                GLog.d("자동로그인 실패");
            }
        }


        btnLogin.setOnClickListener(view -> {
            UserId = etUserId.getText().toString();
            UserPw = etUserPw.getText().toString();

            UserPw = HashUtils.getUserPW(UserPw);

            if (TextUtils.equals(Constant.PREF_USER_PW, UserPw)) {

                validation = loginValidation(UserId, UserPw);
                GLog.d("validation" + validation);

                if (validation) {
                    prefEdit.putString(Constant.PREF_KEY_USER_ID, UserId);
                    prefEdit.putString(Constant.PREF_KEY_USER_PW, UserPw);
                    prefEdit.putBoolean("idSave", saveId.isChecked());
                    prefEdit.putBoolean("autoLoginCheck", autoLogin.isChecked());

                    prefEdit.commit();

                    Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), LoginSuccessActivity.class);
                    intent.putExtra("isKakaoLogin", false);
                    intent.putExtra("tvId", UserId);
                    startActivity(intent);
                    finish();

                } else {
                    GLog.d("validation" + validation);
                    Toast.makeText(context, "로그인 실패!!!", Toast.LENGTH_SHORT).show();
                }

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

                        userUI();

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
            userUI();
        }
        return null;
    };

    private void userUI() {
        UserApiClient.getInstance().me((user, throwable) -> {
            if (throwable != null) {
                GLog.d("사용자 정보 요청 실패");
            } else if (user != null) {
                Account account = user.getKakaoAccount();
                if (account != null) {
                    Profile profile = account.getProfile();
                    if (profile != null) {
                        Intent intent = new Intent(getApplicationContext(), LoginSuccessActivity.class);
                        intent.putExtra("nickName", profile.getNickname());
                        intent.putExtra("profileURL", profile.getProfileImageUrl());
                        intent.putExtra("isKakaoLogin", true);
                        startActivity(intent);
                        finish();
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


    private boolean loginValidation(String userId, String userPw) {
        GLog.d("userId " + userId);
        GLog.d("userPw " + userPw);
        GLog.d("pref.getString(Constant.PREF_KEY_USER_ID) : " + pref.getString(Constant.PREF_KEY_USER_ID, ""));
        GLog.d("pref.getString(Constant.PREF_KEY_USER_PW) :" + pref.getString(Constant.PREF_KEY_USER_PW, ""));

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(context, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(userPw)) {
            Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TextUtils.equals(Constant.PREF_USER_ID, userId)) {
            Toast.makeText(context, "아이디가 틀림", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TextUtils.equals(Constant.PREF_USER_PW, userPw)) {
            Toast.makeText(context, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}

