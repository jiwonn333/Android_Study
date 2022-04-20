package com.example.mysnsaccount.retrofit.model;

import com.google.gson.annotations.SerializedName;

public class RequestLoginModel {
    public RequestLoginModel(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    @SerializedName("id")
    String id;
    @SerializedName("pw")
    String pw;


}
