package com.example.mysnsaccount.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.dkiapi.CommonResponse;
import com.example.mysnsaccount.retrofit.RetrofitApiManager;
import com.example.mysnsaccount.retrofit.RetrofitInterface;
import com.example.mysnsaccount.retrofit.model.CommonRequest;
import com.example.mysnsaccount.util.AppUtil;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.HashUtils;

import retrofit2.Response;

public class UpdateActivity extends Activity {
    private Context context;
    private EditText etUserId;
    private EditText etUserPw;
    private EditText etUserPwCheck;
    private EditText etUserName;
    private EditText etUserPhone;
    private Button btnPwCheck;
    private Button btnUpdate;
    private Button btnCancel;
    private String userId;
    private String userName;
    private String userPhone;
    private String userPw;
    private String userPwCheck;
    private Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        context = this;

        //update
        etUserId = findViewById(R.id.tv_id);
        btnUpdate = findViewById(R.id.update_btn);
        btnCancel = findViewById(R.id.update_cancel_btn);
        etUserPw = findViewById(R.id.tv_pw);
        etUserPwCheck = findViewById(R.id.tv_pw_ok);
        etUserName = findViewById(R.id.tv_name);
        etUserPhone = findViewById(R.id.tv_phone);
        btnPwCheck = findViewById(R.id.pw_check);

        intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            userName = intent.getStringExtra("userName");
            userPhone = intent.getStringExtra("userPhone");
        }
        etUserId.setText(userId);
        etUserName.setText(userName);
        etUserPhone.setText(userPhone);

        btnPwCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPw = etUserPw.getText().toString();
                userPwCheck = etUserPwCheck.getText().toString();
                if (TextUtils.equals(userPw, userPwCheck)) {
                    btnPwCheck.setText("일치");
                    AppUtil.showToast(context, getString(R.string.msg_pw));
                } else {
                    btnPwCheck.setText("확인");
                    etUserPw.setText("");
                    etUserPwCheck.setText("");
                    AppUtil.showToast(context, getString(R.string.msg_pw_check));
                }
            }
        });

        //전화번호 하이픈
        etUserPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPw = etUserPw.getText().toString();
                userPhone = etUserPhone.getText().toString();
                userName = etUserName.getText().toString();
                if (TextUtils.equals(btnPwCheck.getText(), "일치")) {
                    userPw = HashUtils.getSHA256Encrypt(userPw);
                    if (userPhone.length() == 13) {
                        CommonRequest model = new CommonRequest();
                        model.setId(userId);
                        model.setPw(userPw);
                        model.setName(userName);
                        model.setPhone(userPhone);
                        RetrofitApiManager.getInstance().requestUpdateUserInfo(model, new RetrofitInterface() {
                            @Override
                            public void onResponse(Response response) {
                                if (response.isSuccessful()) {
                                    CommonResponse commonResponse = (CommonResponse) response.body();
                                    if (commonResponse != null) {
                                        CommonResponse.ResultInfo resultInfo = commonResponse.getResultInfo();
                                        if (resultInfo != null) {
                                            String errMsg = resultInfo.getErrorMsg();
                                            if (resultInfo.isResult()) {
                                                AppUtil.showToast(context, errMsg);
                                                startIntent();
                                            } else {
                                                AppUtil.showToast(context, errMsg);
                                            }
                                        } else {
                                            GLog.e("resultInfo is null");
                                        }
                                    } else {
                                        GLog.e("updateResponse is null");
                                    }

                                } else {
                                    GLog.e("Response is not Success");
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                GLog.d("UpdateResponse : " + t.toString());
                            }
                        });
                    } else {
                        AppUtil.showToast(context, getString(R.string.msg_phone_check));
                    }
                } else {
                    AppUtil.showToast(context, getString(R.string.msg_pw_check));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == RESULT_OK) {
            userId = intent.getStringExtra("userId");
            userName = intent.getStringExtra("userName");
            userPhone = intent.getStringExtra("userPhone");

        } else {
            GLog.d("회원정보 수정 오류");
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
}
