package com.example.mysnsaccount.retrofit;

import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.RecyclerViewModel;
import com.example.mysnsaccount.model.retrofitthumbnailmdoel.RetrofitModel;
import com.example.mysnsaccount.wearable.WearableResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApiService {
    //GET 방식으로 요청하고 baseURL 뒤에 붙을 주소 지정--retrofit
    @GET("4I1S")
    Call<RetrofitModel> getRetrofitThumbnail();

    //GET 방식으로 요청하고 baseURL 뒤에 붙을 주소 지정--recyclerview
    @GET("OR7Z")
    Call<RecyclerViewModel> getRecyclerViewThumbnail();

    @GET("wearabledevice")
    Call<WearableResponse> getWearableCall(@Query("operation") String operation,
                                           @Query("msisdn") String msisdn,
                                           @Query("dev_msisdn") String dev_msisdn);

}