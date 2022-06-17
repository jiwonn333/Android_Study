package com.example.mysnsaccount.retrofit;

import com.example.mysnsaccount.api.CommonResponse;
import com.example.mysnsaccount.api.LoginResponse;
import com.example.mysnsaccount.api.todo.edit.TodoResponse;
import com.example.mysnsaccount.api.todo.get.TodoListResponse;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.RecyclerViewModel;
import com.example.mysnsaccount.model.retrofitthumbnailmdoel.RetrofitModel;
import com.example.mysnsaccount.retrofit.model.CommonRequest;
import com.example.mysnsaccount.retrofit.model.TodoRequest;

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

    @HTTP(method = "DELETE", path = "member")
    Call<CommonResponse> getIdDelete(@Query("id") String id);


    // TodoList
    // 할 일 목록 전체 조회
    @POST("todo/list")
    Call<TodoListResponse> getListTodo(@Body TodoRequest model);

    // insert
    @POST("todo")
    Call<TodoResponse> insertTodo(@Body TodoRequest model);


    // delete
    @HTTP(method = "DELETE", path = "todo")
    Call<TodoListResponse> todoDelete(@Query("num") int num,
                                      @Query("id") String id);

    // update
    @PUT("todo/detail")
    Call<TodoResponse> updateTodo(@Body TodoRequest model);


}