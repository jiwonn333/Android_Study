package com.example.mysnsaccount.retrofit.model;

import com.google.gson.annotations.SerializedName;

public class DeleteModel {
    public DeleteModel(String id) {
        this.id = id;
    }

    @SerializedName("id")
    String id;
}
