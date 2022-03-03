package com.example.mysnsaccount.Permissions;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mysnsaccount.R;

public class PermissionActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 22;//권한변수
    private static final String TAG = "sjw";
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Button button = findViewById(R.id.perbtn);
        context = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    }
                    Log.d(TAG, "음성통화 상태 : [ getCallState ] >>> " + tm.getCallState());
                    Log.d(TAG, "데이터통신 상태 : [ getDataState ] >>> " + tm.getDataState());
                    Log.d(TAG, "통신사 ISO 국가코드 : [ getNetworkCountryIso ] >>> " + tm.getNetworkCountryIso());
                    Log.d(TAG, "통신사 ISO 국가코드 : [ getSimCountryIso ] >>> " + tm.getSimCountryIso());
                    Log.d(TAG, "SIM 카드 상태 : [ getSimState ] >>> " + tm.getSimState());
                }
            }
        });
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
            Log.d("sjw", "grantResults mPermissionsGranted " + mPermissionsGranted);
            //필수 권한 중 한 개라도 없는 경우
            if (!mPermissionsGranted) {
                //권한을 요청한다.
                ActivityCompat.requestPermissions(PermissionActivity.this, mRequiredPermissions, PERMISSION_REQUEST_CODE);
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

    private void logic() {
        Toast.makeText(this, "권한 허용", Toast.LENGTH_SHORT).show();
    }

    //권한 요청에 대한 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*권한이 있는경우 실행할 코드....*/
                    logic();
                } else {
                    // 하나라도 거부한다면.
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("앱 권한");
                    alertDialog.setMessage("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보>권한> 에서 모든 권한을 허용해 주십시오");
                    // 권한설정 클릭시 이벤트 발생
                    alertDialog.setPositiveButton("권한설정",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                    startActivity(intent);
                                    dialog.cancel();
                                }
                            });
                    //취소
                    alertDialog.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.show();
                }
                break;
        }
    }
}
