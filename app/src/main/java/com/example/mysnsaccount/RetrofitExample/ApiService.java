package com.example.mysnsaccount.RetrofitExample;

import com.example.mysnsaccount.model.SingleThumbnailModel.SingleThumbnailModel;
import com.example.mysnsaccount.model.ThumbnailModel.ThumbnailModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    //GET 방식으로 요청하고 baseURL 뒤에 붙을 주소 지정--retrofit
    @GET("4I1S")
    Call<SingleThumbnailModel> getSingleThumbnail();

    //GET 방식으로 요청하고 baseURL 뒤에 붙을 주소 지정--recyclerview
    @GET("OR7Z")
    Call<ThumbnailModel> getThumbnail();

}