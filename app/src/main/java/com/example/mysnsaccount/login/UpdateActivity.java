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
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.dkiapi.UpdateResponse;
import com.example.mysnsaccount.retrofit.RetrofitApiManager;
import com.example.mysnsaccount.retrofit.RetrofitInterface;
import com.example.mysnsaccount.retrofit.model.UpdateModel;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.HashUtils;
import com.example.mysnsaccount.util.UserPreference;

import retrofit2.Response;

public class UpdateActivity extends Activity {
    Context context;
    EditText id;
    EditText pw;
    EditText pwOk;
    EditText name;
    EditText phone;
    Button pwCheck;
    Button update;
    Button cancel;
    String userId;
    String userName;
    String userPhone;
    String userPw;
    String userPwOk;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        context = this;

        //update
        id = findViewById(R.id.tv_id);
        update = findViewById(R.id.update_btn);
        cancel = findViewById(R.id.update_cancel_btn);
        pw = findViewById(R.id.tv_pw);
        pwOk = findViewById(R.id.tv_pw_ok);
        name = findViewById(R.id.tv_name);
        phone = findViewById(R.id.tv_phone);
        pwCheck = findViewById(R.id.pw_check);

        userId = UserPreference.getUserId(context);
        id.setText(userId);


        pwCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPw = pw.getText().toString();
                userPwOk = pwOk.getText().toString();
                if (TextUtils.equals(userPw, userPwOk)) {
                    pwCheck.setText("일치");
                    Toast.makeText(context, "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    pwCheck.setText("확인");
                    pw.setText("");
                    pwOk.setText("");
                    Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //전화번호 하이픈
        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPw = pw.getText().toString();
                userPhone = phone.getText().toString();
                userName = name.getText().toString();
                if (TextUtils.equals(pwCheck.getText(), "일치")) {
                    userPw = HashUtils.getSHA256Encrypt(userPw);
                    if (userPhone.length() == 13) {
                        UpdateModel updateModel = new UpdateModel(userId, userPw, userName, userPhone);
                        RetrofitApiManager.getInstance().requestUpdateData(updateModel, new RetrofitInterface() {
                            @Override
                            public void onResponse(Response response) {
                                if (response.isSuccessful()) {
                                    UpdateResponse updateResponse = (UpdateResponse) response.body();
                                    String errMsg = updateResponse.getResultInfo().getErrorMsg();
                                    if (updateResponse != null) {
                                        if (updateResponse.getResultInfo().isResult()) {
                                            Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
                                            startIntent();
                                        } else {
                                            Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "전화번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "비밀번호 확인 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
