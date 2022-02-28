package com.example.mysnsaccount.RetrofitExample;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    //GET 방식으로 요청하고 baseURL 뒤에 붙을 주소 지정
    @GET("4I1S")
    Call<Post> getPost();
}