package com.example.mysnsaccount.RetrofitExample;


import retrofit2.Response;

public interface RetrofitInterface {
    void onResponse(Response response);

    void onFailure(Throwable t);
}
