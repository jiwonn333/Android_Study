package com.example.mysnsaccount.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtils {


    public static String getUserPW(String str) {
        String hexUserPw = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            hexUserPw = String.format("%064x", new BigInteger(1, md.digest()));
            GLog.d("UserPw --> " + hexUserPw);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hexUserPw;
    }

}
