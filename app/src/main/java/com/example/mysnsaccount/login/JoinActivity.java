package com.example.mysnsaccount.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.dkiapi.CommonResponse;
import com.example.mysnsaccount.retrofit.RetrofitApiManager;
import com.example.mysnsaccount.retrofit.RetrofitInterface;
import com.example.mysnsaccount.retrofit.model.CommonRequest;
import com.example.mysnsaccount.util.AppUtil;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.HashUtils;

import retrofit2.Response;

public class JoinActivity extends Activity {
    private EditText etUserId;
    private EditText etUserPw;
    private EditText etUserPwCheck;
    private EditText etUserName;
    private EditText etUserPhone;
    private Button btnIdCheck;
    private boolean isIdCheck;
    private Button btnPwCheck;
    private Button btnJoin;
    private Intent intent;
    private Context context;
    private String userId;
    private String userPw;
    private String userPwCheck;
    private String userPhone;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        context = this;

        //기입 항목
        etUserId = findViewById(R.id.tv_id);
        etUserPw = findViewById(R.id.tv_pw);
        etUserPwCheck = findViewById(R.id.tv_pw_ok);
        etUserName = findViewById(R.id.tv_name);
        etUserPhone = findViewById(R.id.tv_phone);
        btnIdCheck = findViewById(R.id.btn_id);
        btnPwCheck = findViewById(R.id.pw_check);
        btnJoin = findViewById(R.id.sign_up_button);

        //아이디 중복 확인 버튼
        btnIdCheck.setOnClickListener(view -> {
            userId = etUserId.getText().toString();
            if (idCheckValidation(userId)) {
                requestIdCheck();
            }
        });

        etUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String inputId = etUserId.getText().toString();
                isIdCheck = TextUtils.equals(inputId, userId);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                GLog.d("editable :" + editable.toString());
            }
        });


        //비밀번호 확인 버튼
        btnPwCheck.setOnClickListener(view -> {
            userPw = etUserPw.getText().toString();
            userPwCheck = etUserPwCheck.getText().toString();
            if (pwCheckValidation(userPw, userPwCheck)) {
                btnPwCheck.setText("일치");
                AppUtil.showToast(context, getString(R.string.msg_pw));
            } else {
                btnPwCheck.setText("확인");
                etUserPw.setText("");
                etUserPwCheck.setText("");
                AppUtil.showToast(context, getString(R.string.msg_pw_check));
            }
        });

        etUserPw.addTextChangedListener(textWatcher);
        etUserPwCheck.addTextChangedListener(textWatcher);


        //전화번호 하이픈
        etUserPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        //회원가입 완료 버튼
        btnJoin.setOnClickListener(view -> {
            userId = etUserId.getText().toString();
            userPw = etUserPw.getText().toString();
            userName = etUserName.getText().toString();
            userPhone = etUserPhone.getText().toString();
            if (idCheckValidation(userId)) {
                if (isIdCheck) {
                    if (TextUtils.equals(btnPwCheck.getText().toString(), "일치")) {
                        userPw = HashUtils.getSHA256Encrypt(userPw);
                        if (userPhone.length() == 13) {
                            requestJoin();
                        } else {
                            AppUtil.showToast(context, getString(R.string.msg_phone_check));
                        }
                    } else {
                        AppUtil.showToast(context, getString(R.string.msg_pw_check));
                    }
                } else {
                    AppUtil.showToast(context, getString(R.string.msg_id_check));
                }
            } else {
                GLog.e("userId : " + userId);
            }
        });

    }


    private boolean pwCheckValidation(String userPw, String userPwCheck) {
        if (TextUtils.isEmpty(userPw) || TextUtils.isEmpty(userPwCheck)) {
            AppUtil.showToast(context, getString(R.string.msg_input_pw));
            return false;
        }

        if (!TextUtils.equals(userPw, userPwCheck)) {
            AppUtil.showToast(context, getString(R.string.msg_pw_check));
            return false;
        }
        return true;
    }

    private boolean idCheckValidation(String userId) {
        if (TextUtils.isEmpty(userId)) {
            AppUtil.showToast(context, getString(R.string.msg_input_id));
            return false;
        }
        return true;
    }

    private void joinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("회원가입 완료").setMessage("회원가입이 완료되었습니다.\n" + "로그인 페이지로 이동하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startIntent(true);
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startIntent(false);
                    }
                }).show();
    }

    private void startIntent(boolean isCheck) {

        intent = new Intent();
        if (isCheck) {
            setResult(RESULT_FIRST_USER, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }

    private void requestIdCheck() {
        CommonRequest model = new CommonRequest();
        model.setId(userId);
        RetrofitApiManager.getInstance().requestIdCheck(model, new RetrofitInterface() {
            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = (CommonResponse) response.body();
                    if (commonResponse != null) {
                        CommonResponse.ResultInfo resultInfo = commonResponse.getResultInfo();
                        if (resultInfo != null) {
                            String idErrorMsg = resultInfo.getErrorMsg();
                            if (resultInfo.isResult()) {
                                AppUtil.showToast(context, idErrorMsg);
                                GLog.d();
                                isIdCheck = resultInfo.isResult();

                            } else {
                                etUserId.setText("");
                                AppUtil.showToast(context, idErrorMsg);
                            }
                        } else {
                            GLog.e("errorMsg : " + resultInfo.getErrorMsg());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GLog.d("idCheckResponse :" + t.toString());
            }
        });
    }

    private void requestJoin() {
        CommonRequest model = new CommonRequest();
        model.setId(userId);
        model.setPw(userPw);
        model.setName(userName);
        model.setPhone(userPhone);
        RetrofitApiManager.getInstance().requestJoin(model, new RetrofitInterface() {
            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    CommonResponse commonResponse = (CommonResponse) response.body();
                    if (commonResponse != null) {
                        CommonResponse.ResultInfo resultInfo = commonResponse.getResultInfo();
                        if (resultInfo != null) {
                            String errMsg = resultInfo.getErrorMsg();
                            if (resultInfo.isResult()) {
                                joinDialog();
                                AppUtil.showToast(context, errMsg);
                            } else {
                                AppUtil.showToast(context, errMsg);
                            }
                        } else {
                            GLog.e("resultInfo is null");
                        }
                    } else {
                        GLog.e("commonResponse is null");
                    }
                } else {
                    GLog.e("Response is not Successful");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GLog.d("joinResponse :" + t.toString());
            }
        });
    }

    // 같은처리
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String inputPw = etUserPw.getText().toString();
            String inputPwCheck = etUserPwCheck.getText().toString();
            if (!TextUtils.equals(inputPw, inputPwCheck)) {
                btnPwCheck.setText("확인");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}
