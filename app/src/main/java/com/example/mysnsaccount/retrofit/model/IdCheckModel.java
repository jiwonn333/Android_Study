package com.example.mysnsaccount.retrofit.model;

import com.google.gson.annotations.SerializedName;

public class IdCheckModel {
    public IdCheckModel(String id) {
        this.id = id;
    }

    @SerializedName("id")
    String id;
}
