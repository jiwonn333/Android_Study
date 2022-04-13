package com.example.mysnsaccount;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
        add(new RecyclerViewItem(R.drawable.baseline_photo_camera_white_24, "Camera"));
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
        gridLayoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.addItem(itemLists);
        recyclerViewAdapter.notifyDataSetChanged();

        recyclerViewAdapter.setOnItemClickListener((view, position) -> {
            if (TextUtils.equals(itemLists.get(position).getTitle(), "Permission")) {
                if (checkPermission()) {
                    // 휴대폰 정보는 TelephonyManger를 이용
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    //안드로이드 버전에 따라권한 허가 받았는지 확인
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    } else {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        GLog.d("음성통화 상태 : [ getCallState ] >>> " + tm.getCallState());
                        GLog.d("데이터통신 상태 : [ getDataState ] >>> " + tm.getDataState());
                        GLog.d("통신사 ISO 국가코드 : [ getNetworkCountryIso ] >>> " + tm.getNetworkCountryIso());
                        GLog.d("통신사 ISO 국가코드 : [ getSimCountryIso ] >>> " + tm.getSimCountryIso());
                        GLog.d("SIM 카드 상태 : [ getSimState ] >>> " + tm.getSimState());
                    }
                    Toast.makeText(this, "권한 허용됨", Toast.LENGTH_SHORT).show();
                }
            } else if (TextUtils.equals(itemLists.get(position).getTitle(), "Camera")) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //권한이 없는 경우 requestPermissions() 메서드 호출하여 권한 요청
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, Constant.PERMISSIONS_REQUEST_CAMERA);
                    return;
                } else {
                    Toast.makeText(this, "카메라를 실행합니다", Toast.LENGTH_LONG).show();
                    callCamera();
                }
            }
        });

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
                                        UserPreference.setKakaoLoginSuccess(context, false);
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
                    }).setNegativeButton("CANCEL", (dialogInterface, i) -> {

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
        GLog.d("iskakaologinSuccess :" + iskakaoLoginSuccess);
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


    boolean checkPermission() {
        //위험 권한을 모두 승인했는지 여부
        boolean mPermissionsGranted = false;
        String[] mRequiredPermissions = new String[1];

        //안드로이드 버전에 따라 권한 다르게 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //11이상인 경우
            mRequiredPermissions[0] = Manifest.permission.READ_PHONE_NUMBERS;
        } else {
            //10이하인 경우
            mRequiredPermissions[0] = Manifest.permission.READ_PHONE_STATE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //필수 권한을 가지고 있는지 확인
            mPermissionsGranted = hasPermissions(mRequiredPermissions);
            GLog.d("grantResults mPermissionsGranted " + mPermissionsGranted);
            //필수 권한 중 한 개라도 없는 경우
            if (!mPermissionsGranted) {
                //권한을 요청한다.
                ActivityCompat.requestPermissions(this, mRequiredPermissions, Constant.PERMISSION_REQUEST_CODE);
            }
        } else {
            mPermissionsGranted = true;
        }
        return mPermissionsGranted;
    }


    private boolean hasPermissions(String[] permissions) {
        //필수 권한을 가지고 있는지 확인
        for (String permission : permissions) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //권한 요청에 대한 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constant.PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한이 허가되어 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    setPermissionDialog("전화 권한");
                }
                break;
            case Constant.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한이 허가되어 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    setPermissionDialog("카메라 권한");
                }
                break;
        }
    }


    public void setPermissionDialog(String title) {
        // 하나라도 거부한다면.
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title)
                .setMessage("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보>권한> 에서 모든 권한을 허용해 주십시오");

        // 권한설정 클릭시 이벤트 발생
        alertDialog.setPositiveButton("권한설정",
                (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + context.getPackageName()));
                    startActivity(intent);
                    dialog.cancel();
                });
        //취소
        alertDialog.setNegativeButton("취소",
                (dialog, which) -> {
                    dialog.cancel();
                });
        alertDialog.show();
    }

    private void callCamera() {
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, Constant.PERMISSIONS_REQUEST_CAMERA);
        }
    }

}


