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

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.dkiapi.LoginResponse;
import com.example.mysnsaccount.retrofit.RetrofitApiManager;
import com.example.mysnsaccount.retrofit.RetrofitInterface;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.HashUtils;
import com.example.mysnsaccount.util.UserPreference;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.user.UserApiClient;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Response;

public class LoginActivity extends Activity {
    private static final boolean REQUEST_GET = false;
    Button btnKakaoLogin;
    Button btnLogin;
    EditText etUserId;
    EditText etUserPw;
    String userId;
    String userPw;
    String getUserId;
    String getUserPw;
    CheckBox checkSaveId;
    CheckBox checkAutoLogin;
    Intent intent;
    boolean validation;
    boolean saveIdCheck;
    boolean autoLoginCheck;
    boolean iskakaoLogin;
    Context context;
    String loginUserName;
    String loginUserPhone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

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
        iskakaoLogin = UserPreference.getKakaoLoginSuccess(context);


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
                requestService();
            }
        }


        btnLogin.setOnClickListener(view -> {
            getUserId = etUserId.getText().toString();
            getUserPw = etUserPw.getText().toString();

            if (!TextUtils.isEmpty(getUserPw)) {
                getUserPw = HashUtils.getSHA256Encrypt(getUserPw);
            }
            validation = loginCheckValidation(getUserId, getUserPw);
            if (validation) {
                requestService();
            } else {

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
                        UserPreference.setKakaoLoginSuccess(context, true);
                        startIntent();

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

            UserPreference.setKakaoLoginSuccess(context, true);
            startIntent();
            GLog.d("카카오계정으로 로그인 성공");
        }
        return null;
    };


    private boolean loginCheckValidation(String userId, String userPw) {

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(context, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(userPw)) {
            Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void requestService() {
        if (REQUEST_GET) {
            //GET
            RetrofitApiManager.getInstance().requestDkiUserCall(getUserId, getUserPw, new RetrofitInterface() {
                @Override
                public void onResponse(Response response) {
                    GLog.d("response : " + response);
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = (LoginResponse) response.body();
                        startLogin(loginResponse);
                    } else {
                        GLog.d("dkiUserResponse is not successful");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    GLog.d("dkiUserResponse :" + t.toString());
                }
            });
        } else {
            //POST
            RetrofitApiManager.getInstance().requestLoginDataCall(getUserId, getUserPw, new RetrofitInterface() {
                @Override
                public void onResponse(Response response) {
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = (LoginResponse) response.body();
                        startLogin(loginResponse);
                    } else {
                        GLog.d("dkiUserResponse is not successful");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    GLog.d("dkiUserResponse :" + t.toString());
                }
            });
        }
    }

    private void startLogin(LoginResponse loginResponse) {
        if (loginResponse != null) {
            if (loginResponse.getResultInfo().getResult()) {
                UserPreference.setUserId(context, getUserId);
                UserPreference.setUserPassword(context, getUserPw);
                UserPreference.setSaveIdCheck(context, checkSaveId.isChecked());
                UserPreference.setAutoLoginCheck(context, checkAutoLogin.isChecked());
                UserPreference.setKakaoLoginSuccess(context, false);
                userId = loginResponse.getUserInfo().getId();
                loginUserName = loginResponse.getUserInfo().getName();
                loginUserPhone = loginResponse.getUserInfo().getPhone();
                if (autoLoginCheck) Toast.makeText(context, "자동 로그인 성공", Toast.LENGTH_SHORT).show();
                startIntent();
            }
            Toast.makeText(context, loginResponse.getResultInfo().getErrorMsg(), Toast.LENGTH_SHORT).show();
        } else {
            GLog.d("서버 통신 원활하지 않습니다.");
        }
    }

    private void startIntent() {
        intent = new Intent();
        intent.putExtra("userId", userId);
        intent.putExtra("userName", loginUserName);
        intent.putExtra("userPhone", loginUserPhone);
        setResult(RESULT_OK, intent);
        finish();
    }
}

