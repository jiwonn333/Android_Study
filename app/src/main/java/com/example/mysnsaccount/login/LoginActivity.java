package com.example.mysnsaccount.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.dkiapi.CommonResponse;
import com.example.mysnsaccount.dkiapi.LoginResponse;
import com.example.mysnsaccount.retrofit.RetrofitApiManager;
import com.example.mysnsaccount.retrofit.RetrofitInterface;
import com.example.mysnsaccount.retrofit.model.CommonRequest;
import com.example.mysnsaccount.util.AppUtil;
import com.example.mysnsaccount.util.Constant;
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
    private Button btnKakaoLogin;
    private Button btnLogin;
    private Button btnJoin;
    private EditText etUserId;
    private EditText etUserPw;
    private CheckBox checkSaveId;
    private CheckBox checkAutoLogin;
    private Intent intent;
    private boolean validation;
    private boolean saveIdCheck;
    private boolean autoLoginCheck;
    private boolean iskakaoLogin;
    private Context context;
    private String userId;
    private String userPw;
    private String userName;
    private String userPhone;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;

        btnKakaoLogin = findViewById(R.id.btn_kakao_login);
        btnLogin = findViewById(R.id.btn_login);
        btnJoin = findViewById(R.id.btn_join);
        etUserId = findViewById(R.id.tv_id);
        etUserPw = findViewById(R.id.tv_pw);
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
            userId = etUserId.getText().toString();
            userPw = etUserPw.getText().toString();
            if (!TextUtils.isEmpty(userPw)) {
                userPw = HashUtils.getSHA256Encrypt(userPw);
            }
            validation = loginCheckValidation(userId, userPw);
            if (validation) {
                requestService();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, JoinActivity.class);
                startActivityForResult(intent, Constant.REQUEST_LOGIN_CODE);
//                finish();
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
            AppUtil.showToast(context, getString(R.string.msg_input_id));
            return false;
        }
        if (TextUtils.isEmpty(userPw)) {
            AppUtil.showToast(context, getString(R.string.msg_input_pw));
            return false;
        }
        return true;
    }

    private void requestService() {
        if (REQUEST_GET) {
            //GET
            RetrofitApiManager.getInstance().requestUserInfo(userId, userPw, new RetrofitInterface() {
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
                    GLog.d(t.getMessage());
                }
            });
        } else {
            //POST
            CommonRequest model = new CommonRequest();
            model.setId(userId);
            model.setPw(userPw);
            RetrofitApiManager.getInstance().requestLoginUserInfo(model, new RetrofitInterface() {
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
                    GLog.d("loginResponse :" + t.toString());
                }
            });
        }
    }

    private void startLogin(LoginResponse loginResponse) {
        if (loginResponse != null) {
            LoginResponse.UserInfo userInfo = loginResponse.getUserInfo();
            CommonResponse.ResultInfo resultInfo = loginResponse.getResultInfo();
            if (resultInfo != null) {
                String errMsg = resultInfo.getErrorMsg();
                if (resultInfo.isResult()) {
                    userId = userInfo.getId();
                    userName = userInfo.getName();
                    userPhone = userInfo.getPhone();
                    UserPreference.setUserId(context, userId);
                    UserPreference.setUserPassword(context, userPw);
                    UserPreference.setSaveIdCheck(context, checkSaveId.isChecked());
                    UserPreference.setAutoLoginCheck(context, checkAutoLogin.isChecked());
                    UserPreference.setKakaoLoginSuccess(context, false);
                    if (autoLoginCheck) {
                        AppUtil.showToast(context, getString(R.string.msg_auto_login_success));
                    }
                    startIntent();
                } else {
                    GLog.e("errMsg : " + errMsg);
                }
                AppUtil.showToast(context, errMsg);
            } else {
                GLog.e("ResultInfo is null");
            }
        } else {
            GLog.e("loginResponse is null");
        }
    }

    private void startIntent() {
        intent = new Intent();
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userPhone", userPhone);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GLog.d("resultCode : " + resultCode);
        if (resultCode == RESULT_FIRST_USER) {

        } else {
            finish();
        }
    }
}

