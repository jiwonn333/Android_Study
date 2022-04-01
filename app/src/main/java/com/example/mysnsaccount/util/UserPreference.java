package com.example.mysnsaccount.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {

    private static final boolean DEFAULT_VALUE_BOOLEAN = false;

    // 로그인 관련 키
    public static final String PREF_USER_ID = "dki123";
    public static final String PREF_USER_PW = "73b04366c55e3815ef1ff3750c0b29afd866102926d52e09b4072c29dce0c50a";
    public static final String PREF_KEY_USER_ID = "userid";
    public static final String PREF_KEY_USER_PW = "userpw";
    public static final String PREF_KEY_KAKAO_USER_NAME = "kakaoname";
    public static final String PREF_KEY_KAKAO_USER_URL = "kakaourl";


    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("loginPref", MODE_PRIVATE);
    }

    public static String getUserId(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        String getUserId = pref.getString(PREF_KEY_USER_ID, "");

        return getUserId;
    }

    public static void setUserId(Context context, String userId) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(PREF_KEY_USER_ID, userId);
        editor.commit();
    }

    public static String getUserPassword(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        String getUserPassword = pref.getString(PREF_KEY_USER_PW, "");
        return getUserPassword;
    }

    public static void setUserPassword(Context context, String userPw) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(PREF_KEY_USER_PW, userPw);
        editor.commit();
    }


    public static boolean getSaveIdCheck(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        boolean getSaveIdCheck = pref.getBoolean("saveIdCheck", DEFAULT_VALUE_BOOLEAN);
        return getSaveIdCheck;
    }

    public static void setSaveIdCheck(Context context, boolean isChecked) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("saveIdCheck", isChecked);
        editor.commit();
    }

    public static boolean getAutoLoginCheck(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        boolean getAutoLoginCheck = pref.getBoolean("autoLoginCheck", DEFAULT_VALUE_BOOLEAN);
        return getAutoLoginCheck;
    }

    public static void setAutoLoginCheck(Context context, boolean isChecked) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("autoLoginCheck", isChecked);
        editor.commit();
    }

    public static void setClear(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.clear();
        editor.commit();
    }

    public static boolean getLoginSuccess(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        boolean getLoginSuccess = pref.getBoolean("loginSuccess", DEFAULT_VALUE_BOOLEAN);
        return getLoginSuccess;
    }

    public static void setLoginSuccess(Context context, boolean loginSuccess) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("loginSuccess", loginSuccess);
        editor.commit();
    }

    public static boolean getKakaoLoginSuccess(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        boolean getLoginSuccess = pref.getBoolean("kakaoLoginSuccess", DEFAULT_VALUE_BOOLEAN);
        return getLoginSuccess;
    }

    public static void setKakaoLoginSuccess(Context context, boolean loginSuccess) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("kakaoLoginSuccess", loginSuccess);
        editor.commit();
    }

    public static String getKakaoUserName(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        String getUserId = pref.getString(PREF_KEY_KAKAO_USER_NAME, "");

        return getUserId;
    }

    public static void setKakaoUserName(Context context, String userName) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(PREF_KEY_KAKAO_USER_NAME, userName);
        editor.commit();
    }

    public static String getKakaoUserUrl(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        String getUserId = pref.getString(PREF_KEY_KAKAO_USER_URL, "");

        return getUserId;
    }

    public static void setKakaoUserUrl(Context context, String userUrl) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(PREF_KEY_KAKAO_USER_URL, userUrl);
        editor.commit();
    }

}
