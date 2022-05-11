package com.example.mysnsaccount.retrofit;

import com.example.mysnsaccount.dkiapi.CommonResponse;
import com.example.mysnsaccount.dkiapi.LoginResponse;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.RecyclerViewModel;
import com.example.mysnsaccount.model.retrofitthumbnailmdoel.RetrofitModel;
import com.example.mysnsaccount.retrofit.model.CommonRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitApiService {
    //GET 방식으로 요청하고 baseURL 뒤에 붙을 주소 지정--retrofit
    @GET("4I1S")
    Call<RetrofitModel> getRetrofitThumbnail();

    //GET 방식으로 요청하고 baseURL 뒤에 붙을 주소 지정--recyclerview
    @GET("OR7Z")
    Call<RecyclerViewModel> getRecyclerViewThumbnail();

    @GET("login")
    Call<LoginResponse> getLoginUserInfo(@Query("id") String id,
                                         @Query("pw") String pw);

    @POST("member")
    Call<CommonResponse> getJoin(@Body CommonRequest model);

    @POST("member/idcheck")
    Call<CommonResponse> getIdCheck(@Body CommonRequest model);

    @POST("member/login")
    Call<LoginResponse> getLoginUserInfo(@Body CommonRequest model);

    @PUT("member")
    Call<CommonResponse> getUpdateUserInfo(@Body CommonRequest model);

    @HTTP(method = "DELETE", hasBody = true, path = "member")
    Call<CommonResponse> getIdDelete(@Body CommonRequest model);


}