package com.example.mysnsaccount.retrofit;

import com.example.mysnsaccount.dkiapi.IdCheckResponse;
import com.example.mysnsaccount.dkiapi.IdDeleteResponse;
import com.example.mysnsaccount.dkiapi.JoinResponse;
import com.example.mysnsaccount.dkiapi.LoginResponse;
import com.example.mysnsaccount.dkiapi.UpdateResponse;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.RecyclerViewModel;
import com.example.mysnsaccount.model.retrofitthumbnailmdoel.RetrofitModel;
import com.example.mysnsaccount.retrofit.model.DeleteModel;
import com.example.mysnsaccount.retrofit.model.IdCheckModel;
import com.example.mysnsaccount.retrofit.model.JoinModel;
import com.example.mysnsaccount.retrofit.model.LoginModel;
import com.example.mysnsaccount.retrofit.model.UpdateModel;
import com.example.mysnsaccount.userinfo.UserInfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApiService {
    //GET 방식으로 요청하고 baseURL 뒤에 붙을 주소 지정--retrofit
    @GET("4I1S")
    Call<RetrofitModel> getRetrofitThumbnail();

    //GET 방식으로 요청하고 baseURL 뒤에 붙을 주소 지정--recyclerview
    @GET("OR7Z")
    Call<RecyclerViewModel> getRecyclerViewThumbnail();

    @GET("user/{id}")
    Call<UserInfoResponse> getUserInfoCall(@Path(value = "id") String id);

    @GET("login")
    Call<LoginResponse> getDkiUserCall(@Query("id") String id,
                                       @Query("pw") String pw);


    @POST("login")
    Call<LoginResponse> getLoginDataCall(@Body String jsonBody);

    @POST("member/login")
    Call<LoginResponse> getLoginDataCall(@Body LoginModel loginModel);

    @POST("member")
    Call<JoinResponse> getJoinDataCall(@Body JoinModel model);

    @POST("member/idcheck")
    Call<IdCheckResponse> getIdCheckData(@Body IdCheckModel model);


    @HTTP(method = "DELETE", hasBody = true, path = "member")
    Call<IdDeleteResponse> getIdDeleteData(@Body DeleteModel model);

    @PUT("member")
    Call<UpdateResponse> getUpdateData(@Body UpdateModel model);

//    @DELETE("member")
//    Call<UserInfoDelete> getDeleteIdData1(@Body InfoDeleteModel model);


//    @GET("wearabledevice")
//    Call<WearableResponse> getWearableCall(@Query("operfation") String operation,
//                                           @Query("msisdn") String msisdn,
//                                           @Query("push_svr_type") String push_svr_type,
//                                           @Query("reg_id") String reg_id,
//                                           @Query("app_id") String app_id);
//
//    @GET("onenumber")
//    Call<OnenumberResponse> getOnenumberCall(@Query("operation") String operation,
//                                             @Query("msisdn") String msisdn,
//                                             @Query("dev_msisdn") String dev_msisdn);


}