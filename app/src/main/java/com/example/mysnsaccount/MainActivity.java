package com.example.mysnsaccount;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mysnsaccount.login.LoginActivity;
import com.example.mysnsaccount.model.recycerviewicon.RecyclerViewAdapter;
import com.example.mysnsaccount.model.recycerviewicon.RecyclerViewItem;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.UserPreference;
import com.kakao.sdk.user.UserApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    GridLayoutManager gridLayoutManager;
    TextView loginName;
    ImageView loginImage, loginProfile;
    TextView loginText;
    Intent intent;
    String kakaoLoginName;
    String kakaoLoginProfile;
    String loginUserName;
    boolean isKakaoLogin;
    boolean loginSuccess;
    boolean kakaoLoginSuccess;
    boolean saveIdCheck, autoLoginCheck;

    ArrayList<RecyclerViewItem> itemLists = new ArrayList<RecyclerViewItem>() {{
        add(new RecyclerViewItem(R.drawable.ic_baseline_web_24, "WebView"));
        add(new RecyclerViewItem(R.drawable.ic_baseline_broadcast_on_home_24, "Retrofit"));
        add(new RecyclerViewItem(R.drawable.ic_baseline_security_24, "Permission"));
        add(new RecyclerViewItem(R.drawable.ic_baseline_cached_24, "RecyclerView"));
        add(new RecyclerViewItem(R.drawable.ic_baseline_alert_24, "Dialog"));
    }};

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // RecyclerView 화면 아이콘 표시
        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(context, itemLists);
        gridLayoutManager = new GridLayoutManager(context, 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addItem(itemLists);
        recyclerViewAdapter.notifyDataSetChanged();

        //로그인 아이콘,텍스트 설정
        loginImage = findViewById(R.id.icon_login);
        loginText = findViewById(R.id.tv_login);
        loginImage.setImageResource(R.drawable.ic_baseline_login_24);
        loginText.setText("Login");

        loginSuccess = UserPreference.getLoginSuccess(context);
        kakaoLoginSuccess = UserPreference.getKakaoLoginSuccess(context);

        saveIdCheck = UserPreference.getSaveIdCheck(context);
        autoLoginCheck = UserPreference.getAutoLoginCheck(context);


        if (loginSuccess || kakaoLoginSuccess) {
            setLogin();

        }

        //로그인 아이콘 클릭시
        loginImage.setOnClickListener(view -> {
            if (TextUtils.equals(loginText.getText(), "Login")) {
                startLogin(LoginActivity.class); // 로그인 페이지로 이동

            } else if (TextUtils.equals(loginText.getText(), "Logout")) {
                if (kakaoLoginSuccess) {
                    //팝업창 선택 (로그아웃, 탈퇴하기)
                    final ArrayList<String> selectedItems = new ArrayList<String>();
                    final String[] items = getResources().getStringArray(R.array.LOGOUT);
                    selectedItems.add(items[0]);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("로그아웃/탈퇴")
                            .setSingleChoiceItems(R.array.LOGOUT, 0, (dialogInterface, position) -> {
                                selectedItems.clear();
                                selectedItems.add(items[position]);
                            }).setPositiveButton("OK", (dialogInterface, position) -> {
                        switch (selectedItems.get(0)) {
                            case "로그아웃":
                                UserApiClient.getInstance().logout(throwable -> {
                                    if (throwable != null) {
                                        GLog.d("로그아웃 실패");
                                    } else {
                                        UserPreference.setKakaoLoginSuccess(context, false);
                                        Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                                    }
                                    startLogin(context.getClass());

                                    return null;
                                });
                                break;

                            case "탈퇴하기":
                                UserApiClient.getInstance().unlink(throwable -> {
                                    if (throwable != null) {
                                        GLog.d("연결 끊기 실패");
                                    } else {
                                        UserPreference.setKakaoLoginSuccess(context, false);
                                        Toast.makeText(context, "연결 끊기 성공", Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                    return null;
                                });
                                break;
                            default:
                                GLog.d("선택한 값이 없음");
                                break;
                        }
                    }).setNegativeButton("CANCLE", (dialogInterface, i) -> {

                    }).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("로그아웃").setMessage("로그아웃 하면 정보가 삭제됩니다.").setPositiveButton("확인", (dialogInterface, i) -> {
                        UserPreference.setClear(context);
                        UserPreference.setLoginSuccess(context, false);
                        startLogin(context.getClass());
                        Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show();
                    }).setNeutralButton("취소", (dialogInterface, i) -> Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show()).show();
                }

            }
        });


    }

    private void startLogin(Class activity) {
        intent = new Intent(getApplicationContext(), activity);
        startActivity(intent);
        finish();
    }

    private void setLogin() {
        loginProfile = findViewById(R.id.login_image_view);
        loginName = findViewById(R.id.login_name_text_view);
        loginImage = findViewById(R.id.icon_login);
        loginText = findViewById(R.id.tv_login);


        if (kakaoLoginSuccess) {
            loginName.setText(UserPreference.getKakaoUserName(context));
            Glide.with(context).load(UserPreference.getKakaoUserUrl(context)).into(loginProfile);
        } else {
            loginName.setText(UserPreference.getUserId(context));
            loginProfile.setImageResource(R.drawable.login_success);

        }
        //공통(로그아웃)
        loginImage.setImageResource(R.drawable.logout);
        loginText.setText("Logout");
        GLog.d("isKakaoLogin :" + isKakaoLogin);
    }

}