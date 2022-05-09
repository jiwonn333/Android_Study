package com.example.mysnsaccount;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.mysnsaccount.dkiapi.IdDeleteResponse;
import com.example.mysnsaccount.dkiapi.LoginResponse;
import com.example.mysnsaccount.login.JoinActivity;
import com.example.mysnsaccount.login.LoginActivity;
import com.example.mysnsaccount.login.UpdateActivity;
import com.example.mysnsaccount.model.recycerviewicon.RecyclerViewAdapter;
import com.example.mysnsaccount.model.recycerviewicon.RecyclerViewItem;
import com.example.mysnsaccount.retrofit.RetrofitApiManager;
import com.example.mysnsaccount.retrofit.RetrofitInterface;
import com.example.mysnsaccount.retrofit.model.DeleteModel;
import com.example.mysnsaccount.util.Constant;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.UserPreference;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Profile;

import java.util.ArrayList;

import retrofit2.Response;

public class MainActivity extends Activity {

    Context context;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    GridLayoutManager gridLayoutManager;
    TextView loginName;
    ImageView loginImage, loginProfile;
    ImageView joinImage;
    TextView loginText;
    TextView joinText;
    Intent intent;
    boolean iskakaoLoginSuccess;
    boolean isAutoChecked;
    Group loginSuccessUi;
    ConstraintLayout loginTextView;
    TextView loginUserName;
    TextView loginUserPhone;
    String name;
    String phone;
    String userId;

    ImageView updateImage;


    ArrayList<RecyclerViewItem> itemLists = new ArrayList<RecyclerViewItem>() {{
        add(new RecyclerViewItem(R.drawable.ic_baseline_web_24, "WebView"));
        add(new RecyclerViewItem(R.drawable.ic_baseline_broadcast_on_home_24, "Retrofit"));
        add(new RecyclerViewItem(R.drawable.ic_baseline_security_24, "Permission"));
        add(new RecyclerViewItem(R.drawable.ic_baseline_cached_24, "RecyclerView"));
        add(new RecyclerViewItem(R.drawable.ic_baseline_alert_24, "Dialog"));
        add(new RecyclerViewItem(R.drawable.baseline_photo_camera_white_24, "Camera"));
        add(new RecyclerViewItem(R.drawable.decode, "Encryption"));
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
        joinImage = findViewById(R.id.icon_join);
        loginText = findViewById(R.id.tv_login);
        joinText = findViewById(R.id.tv_join);
        loginSuccessUi = findViewById(R.id.login_success_group);
        loginTextView = findViewById(R.id.login_text);
        loginProfile = findViewById(R.id.login_image_view);
        loginName = findViewById(R.id.login_name_text_view);
        iskakaoLoginSuccess = UserPreference.getKakaoLoginSuccess(context);
        isAutoChecked = UserPreference.getAutoLoginCheck(context);
        loginUserName = findViewById(R.id.user_name);
        loginUserPhone = findViewById(R.id.user_phone);
        updateImage = findViewById(R.id.icon_update);


        if (iskakaoLoginSuccess || isAutoChecked) {
            setLoginUserInfo();
        } else {
            setShowLoginUi(false);
        }

        //join 클릭
        joinImage.setOnClickListener(view -> {
            intent = new Intent(context, JoinActivity.class);
            startActivityForResult(intent, Constant.REQUEST_JOIN_CODE);
        });

        //update 클릭
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(context, UpdateActivity.class);
                startActivityForResult(intent, Constant.REQUEST_LOGIN_CODE);
            }
        });


        //로그인 아이콘 클릭시
        loginImage.setOnClickListener(view -> {
            if (TextUtils.equals(loginText.getText(), "Login")) {
                intent = new Intent(context, LoginActivity.class);
                startActivityForResult(intent, Constant.REQUEST_LOGIN_CODE);

            } else if (TextUtils.equals(loginText.getText(), "Logout")) {
                logout();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constant.REQUEST_LOGIN_CODE) {
            if (resultCode == RESULT_OK) {
                userId = intent.getStringExtra("userId");
                name = intent.getStringExtra("userName");
                phone = intent.getStringExtra("userPhone");
                setShowLoginUi(true);
            } else {
                GLog.d("주고받기 오류");
            }
        } else if (requestCode == Constant.REQUEST_JOIN_CODE) {
            if (resultCode == RESULT_OK) {
                intent = new Intent(context, LoginActivity.class);
                startActivityForResult(intent, Constant.REQUEST_LOGIN_CODE);
//                finish();
            }
        } else {
            GLog.d("회원가입 오류");
        }
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
                            setShowLoginUi(true);
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
            userId = UserPreference.getUserId(context);
            String userPw = UserPreference.getUserPassword(context);
            RetrofitApiManager.getInstance().requestLoginDataCall(userId, userPw, new RetrofitInterface() {
                @Override
                public void onResponse(Response response) {
                    if (response.isSuccessful()) {
                        LoginResponse dkiUserData = (LoginResponse) response.body();
                        if (dkiUserData != null) {
                            if (dkiUserData.getResultInfo().getResult()) {
                                if (!TextUtils.isEmpty(userId)) {
                                    name = dkiUserData.getUserInfo().getName();
                                    phone = dkiUserData.getUserInfo().getPhone();
                                }
                                loginProfile.setImageResource(R.drawable.login_success);
                                setShowLoginUi(true);
                            }
                            Toast.makeText(context, dkiUserData.getResultInfo().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            GLog.d("서버 통신 원활하지 않습니다.");
                        }
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

    private void setShowLoginUi(boolean isLogin) {
        loginSuccessUi.setVisibility(isLogin ? View.VISIBLE : View.INVISIBLE);
        loginTextView.setVisibility(isLogin ? View.INVISIBLE : View.VISIBLE);
        loginImage.setImageResource(isLogin ? R.drawable.logout : R.drawable.ic_baseline_login_24);
        joinImage.setImageResource(isLogin ? View.GONE : R.drawable.ic_baseline_login_24);
        updateImage.setVisibility(isLogin ? View.VISIBLE : View.GONE);
        updateImage.setImageResource(isLogin ? R.drawable.update : View.GONE);
        loginProfile.setImageResource(isLogin ? R.drawable.login_success : View.INVISIBLE);
        loginText.setText(isLogin ? "Logout" : "Login");
        joinText.setText(isLogin ? "Update" : "Join");
        loginUserName.setText(name);
        loginUserPhone.setText(phone);
        loginName.setText(userId);
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
                    Toast.makeText(this, "권한이 허가되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    setPermissionDialog("전화 권한");
                }
                break;
            case Constant.PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한이 허가되었습니다..", Toast.LENGTH_SHORT).show();
                    callCamera();
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


    private void logout() {
        //팝업창 선택 (로그아웃, 탈퇴하기)
        userId = UserPreference.getUserId(context);

        final ArrayList<String> selectedItems = new ArrayList<String>();
        final String[] items = getResources().getStringArray(R.array.LOGOUT);
        selectedItems.add(items[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("로그아웃/탈퇴")
                .setSingleChoiceItems(R.array.LOGOUT, 0, (dialogInterface, position) -> {
                    selectedItems.clear();
                    selectedItems.add(items[position]);
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                switch (selectedItems.get(0)) {
                    case "로그아웃":
                        if (iskakaoLoginSuccess) {
                            UserApiClient.getInstance().logout(throwable -> {
                                if (throwable != null) {
                                    GLog.d("로그아웃 실패");
                                } else {
                                    UserPreference.setKakaoLoginSuccess(context, false);
                                    setShowLoginUi(false);
                                    Toast.makeText(context, "로그아웃 성공", Toast.LENGTH_SHORT).show();
                                }
                                return null;
                            });
                        } else {
                            UserPreference.setClear(context);
                            setShowLoginUi(false);
                            Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case "탈퇴하기":
                        if (iskakaoLoginSuccess) {
                            UserApiClient.getInstance().unlink(throwable -> {
                                if (throwable != null) {
                                    GLog.d("연결 끊기 실패");
                                } else {
                                    UserPreference.setKakaoLoginSuccess(context, false);
                                    setShowLoginUi(false);
                                    Toast.makeText(context, "연결 끊기 성공", Toast.LENGTH_SHORT).show();
                                }
                                return null;
                            });
                        } else {
                            //통신태우기??
                            DeleteModel model = new DeleteModel(userId);
                            RetrofitApiManager.getInstance().requestDeleteData(model, new RetrofitInterface() {
                                @Override
                                public void onResponse(Response response) {
                                    if (response.isSuccessful()) {
                                        IdDeleteResponse idDeleteResponse = (IdDeleteResponse) response.body();
                                        if (idDeleteResponse != null) {
                                            String errMsg = idDeleteResponse.getResultInfo().getErrorMsg();
                                            if (idDeleteResponse.getResultInfo().isResult()) {
                                                setShowLoginUi(false);
                                                Toast.makeText(context, errMsg, Toast.LENGTH_SHORT).show();
                                                GLog.d("errMsg : " + errMsg);
                                            } else {
                                                GLog.e("errMsg : " + errMsg);
                                            }
                                        } else {
                                            GLog.e("userInfoDelete is null");
                                        }
                                    } else {
                                        GLog.e("response is not Success");
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    GLog.d("idCheckResponse :" + t.toString());
                                }
                            });
                        }
                        break;
                    default:
                        GLog.d("선택한 값이 없음");
                        break;
                }
            }
        }).setNegativeButton("CANCEL", (dialogInterface, i) -> {
        }).show();
    }

}
