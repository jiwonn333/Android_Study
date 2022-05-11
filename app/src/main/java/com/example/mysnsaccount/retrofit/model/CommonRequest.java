package com.example.mysnsaccount.retrofit.model;

import com.google.gson.annotations.SerializedName;

public class CommonRequest {
    @SerializedName("id")
    String id;
    @SerializedName("pw")
    String pw;
    @SerializedName("name")
    String name;
    @SerializedName("phone")
    String phone;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
