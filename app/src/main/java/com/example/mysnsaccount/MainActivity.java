package com.example.mysnsaccount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mysnsaccount.login.LoginActivity;
import com.example.mysnsaccount.model.recycerviewicon.RecyclerViewAdapter;
import com.example.mysnsaccount.model.recycerviewicon.RecyclerViewItem;
import com.example.mysnsaccount.util.Constant;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.UserPreference;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Profile;

import java.util.ArrayList;

public class MainActivity extends Activity {

    Context context;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    GridLayoutManager gridLayoutManager;
    TextView loginName;
    ImageView loginImage, loginProfile;
    TextView loginText;
    Intent intent;
    boolean iskakaoLoginSuccess;
    boolean isAutoChecked;
    Group loginSuccessUi;
    ConstraintLayout loginTextView;


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
        gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addItem(itemLists);
        recyclerViewAdapter.notifyDataSetChanged();

        //로그인 아이콘,텍스트 설정
        loginImage = findViewById(R.id.icon_login);
        loginText = findViewById(R.id.tv_login);
        loginSuccessUi = findViewById(R.id.login_success_group);
        loginTextView = findViewById(R.id.login_text);
        loginProfile = findViewById(R.id.login_image_view);
        loginName = findViewById(R.id.login_name_text_view);
        iskakaoLoginSuccess = UserPreference.getKakaoLoginSuccess(context);
        isAutoChecked = UserPreference.getAutoLoginCheck(context);

        if (iskakaoLoginSuccess || isAutoChecked) {
            setLoginUserInfo();
        } else {
            setLoginUi();
        }


        //로그인 아이콘 클릭시
        loginImage.setOnClickListener(view -> {
            if (TextUtils.equals(loginText.getText(), "Login")) {
                intent = new Intent(context, LoginActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE);

            } else if (TextUtils.equals(loginText.getText(), "Logout")) {
                if (iskakaoLoginSuccess) {
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
                                        setLoginUi();
                                        Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                                    }
                                    return null;
                                });
                                break;

                            case "탈퇴하기":
                                UserApiClient.getInstance().unlink(throwable -> {
                                    if (throwable != null) {
                                        GLog.d("연결 끊기 실패");
                                    } else {
                                        UserPreference.setKakaoLoginSuccess(context, false);
                                        setLoginUi();
                                        Toast.makeText(context, "연결 끊기 성공", Toast.LENGTH_SHORT).show();
                                    }
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
                        setLoginUi();
                        Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show();
                    }).setNeutralButton("취소", (dialogInterface, i) -> Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show()).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constant.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                setLoginUserInfo();
            } else {
                GLog.d("주고받기 오류");
            }
        }
    }

    private void setLoginUi() {
        loginImage.setImageResource(R.drawable.ic_baseline_login_24);
        loginText.setText("Login");
        loginTextView.setVisibility(View.VISIBLE);
        loginSuccessUi.setVisibility(View.INVISIBLE);
    }


    private void setLoginUserInfo() {
        iskakaoLoginSuccess = UserPreference.getKakaoLoginSuccess(context);
        if (iskakaoLoginSuccess) {
            UserApiClient.getInstance().me((user, throwable) -> {
                if (throwable != null) {
                    GLog.d("사용자 정보 요청 실패");
                } else if (user != null) {
                    Account account = user.getKakaoAccount();
                    if (account != null) {
                        Profile profile = account.getProfile();
                        if (profile != null) {
                            loginName.setText(user.getKakaoAccount().getProfile().getNickname());
                            Glide.with(context).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).into(loginProfile);
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
        } else {
            String userName = UserPreference.getUserId(context);
            if (!TextUtils.isEmpty(userName)) {
                loginName.setText(userName);
            }
            loginProfile.setImageResource(R.drawable.login_success);
        }
        loginSuccessUi.setVisibility(View.VISIBLE);
        loginTextView.setVisibility(View.INVISIBLE);
        loginImage.setImageResource(R.drawable.logout);
        loginText.setText("Logout");
    }


}


