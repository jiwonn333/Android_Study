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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.dkiapi.IdCheckResponse;
import com.example.mysnsaccount.dkiapi.JoinResponse;
import com.example.mysnsaccount.retrofit.RetrofitApiManager;
import com.example.mysnsaccount.retrofit.RetrofitInterface;
import com.example.mysnsaccount.retrofit.model.IdCheckModel;
import com.example.mysnsaccount.retrofit.model.JoinModel;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.HashUtils;

import retrofit2.Response;

public class JoinActivity extends Activity {
    EditText id;
    EditText pw;
    EditText pwOk;
    EditText name;
    EditText phone;
    Button idCheck;
    boolean isIdCheck;
    Button pwCheck;
    Button submit;
    Intent intent;
    Context context;
    String userId;
    String userPw;
    String userPwOk;
    String userPhone;
    String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        context = this;

        //기입 항목
        id = findViewById(R.id.tv_id);
        pw = findViewById(R.id.tv_pw);
        pwOk = findViewById(R.id.tv_pw_ok);
        name = findViewById(R.id.tv_name);
        phone = findViewById(R.id.tv_phone);
        idCheck = findViewById(R.id.btn_id);
        pwCheck = findViewById(R.id.pw_check);
        submit = findViewById(R.id.sign_up_button);


        //아이디 중복 확인 버튼
        idCheck.setOnClickListener(view -> {
            userId = id.getText().toString();
            if (idCheckValidation(userId)) {
                IdCheckModel model = new IdCheckModel(userId);
                RetrofitApiManager.getInstance().requestIdCheckData(model, new RetrofitInterface() {
                    @Override
                    public void onResponse(Response response) {
                        if (response.isSuccessful()) {
                            IdCheckResponse idCheckResponse = (IdCheckResponse) response.body();
                            if (idCheckResponse != null) {
                                String idErrorMsg = idCheckResponse.getResultInfo().getErrorMsg();
                                if (idCheckResponse.getResultInfo().isResult()) {
                                    Toast.makeText(context, idErrorMsg, Toast.LENGTH_SHORT).show();
                                    isIdCheck = idCheckResponse.getResultInfo().isResult();
                                } else {
                                    id.setText("");
                                    Toast.makeText(context, idErrorMsg, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                GLog.e("errorMsg : " + idCheckResponse.getResultInfo().getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        GLog.d("idCheckResponse :" + t.toString());
                    }
                });
            }
        });


        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                GLog.d("start :" + start + " before : " + before + " count : " + count);
                String input_id = id.getText().toString();

                if (input_id != null) {
                    if (input_id.equals(userId)) {
                        isIdCheck = true;
                    } else {
                        isIdCheck = false;
                    }
                } else {
                    Toast.makeText(context, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                GLog.d("editable :" + editable.toString());
            }
        });

        //비밀번호 확인 버튼
        pwCheck.setOnClickListener(view -> {
            userPw = pw.getText().toString();
            userPwOk = pwOk.getText().toString();
            if (pwCheckValidation(userPw, userPwOk)) {
                pwCheck.setText("일치");
                Toast.makeText(context, "비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
            } else {
                pwCheck.setText("확인");
                pw.setText("");
                pwOk.setText("");
                Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //전화번호 하이픈
        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        //회원가입 완료 버튼
        submit.setOnClickListener(view -> {
            userId = id.getText().toString();
            userPw = pw.getText().toString();
            userPhone = phone.getText().toString();
            userName = name.getText().toString();

            idCheckValidation(userId);
            if (isIdCheck) {
                if (TextUtils.equals(pwCheck.getText().toString(), "일치")) {
                    userPw = HashUtils.getSHA256Encrypt(userPw);
                    if (userPhone.length() == 13) {
                        JoinModel model = new JoinModel(userId, userPw, userName, userPhone);
                        RetrofitApiManager.getInstance().requestJoinData(model, new RetrofitInterface() {
                            @Override
                            public void onResponse(Response response) {
                                if (response.isSuccessful()) {
                                    JoinResponse joinResponse = (JoinResponse) response.body();
                                    if (joinResponse != null) {
                                        if (joinResponse.getResultInfo().isResult()) {
                                            joinDialog();
                                            Toast.makeText(context, "회원가입을 완료했습니다.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, joinResponse.getResultInfo().getErrorMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        GLog.e("errorMsg : " + joinResponse.getResultInfo().getErrorMsg());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                GLog.d("joinResponse :" + t.toString());
                            }
                        });
                    } else {
                        Toast.makeText(context, "전화번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "비밀번호 일치 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "아이디 중복체크 해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean pwCheckValidation(String userPw, String userPwOk) {
        if (TextUtils.isEmpty(userPw) || TextUtils.isEmpty(userPwOk)) {
            Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TextUtils.equals(userPw, userPwOk)) {
            Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean idCheckValidation(String userId) {
        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(context, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
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
                        startIntent();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    private void startIntent() {
        intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
