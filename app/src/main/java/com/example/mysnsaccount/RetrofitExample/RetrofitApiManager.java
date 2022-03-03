package com.example.mysnsaccount.RetrofitExample;

import android.annotation.SuppressLint;

import com.example.mysnsaccount.model.SingleThumbnailModel.SingleThumbnailModel;
import com.example.mysnsaccount.util.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("StaticFieldLeak")
public class RetrofitApiManager {
    private static RetrofitApiManager instance;

    public static RetrofitApiManager getInstance() {
        if (instance == null) {
            instance = new RetrofitApiManager();
        }

        return instance;
    }

    public static Retrofit Build() {

        return new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Gson 처리시
                .build();
    }

    public void requestSingleThumbnail(RetrofitInterface retrofitInterface) {
        Build().create(ApiService.class).getSingleThumbnail().enqueue(new Callback<SingleThumbnailModel>() {
            @Override
            public void onResponse(Call<SingleThumbnailModel> call, Response<SingleThumbnailModel> response) {
                retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<SingleThumbnailModel> call, Throwable t) {
                retrofitInterface.onFailure(t);
            }
        });
    }
}