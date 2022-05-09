package com.example.mysnsaccount.retrofit.model;

import com.google.gson.annotations.SerializedName;

public class UpdateModel {
    public UpdateModel(String id, String pw, String name, String phone) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.phone = phone;
    }

    @SerializedName("id")
    String id;
    @SerializedName("pw")
    String pw;
    @SerializedName("name")
    String name;
    @SerializedName("phone")
    String phone;
}
