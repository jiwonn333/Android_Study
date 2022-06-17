package com.example.mysnsaccount.retrofit;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.example.mysnsaccount.api.CommonResponse;
import com.example.mysnsaccount.api.LoginResponse;
import com.example.mysnsaccount.api.todo.edit.TodoResponse;
import com.example.mysnsaccount.api.todo.get.TodoListResponse;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.RecyclerViewModel;
import com.example.mysnsaccount.model.retrofitthumbnailmdoel.RetrofitModel;
import com.example.mysnsaccount.retrofit.model.CommonRequest;
import com.example.mysnsaccount.retrofit.model.TodoRequest;
import com.example.mysnsaccount.util.Constant;
import com.example.mysnsaccount.util.GLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("StaticFieldLeak")
public class RetrofitApiManager {
    private final static int RETROFIT_TIME_OUT = 10;
    private static RetrofitApiManager instance;
    public Intent intent;

    public static RetrofitApiManager getInstance() {
        if (instance == null) {
            instance = new RetrofitApiManager();
        }

        return instance;
    }

    public static Retrofit Build() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .client(getUnsafeOkHttpClient().build()) //OkHttp 사용해서 로그 보기
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Gson 처리시
                .build();
    }

    public static Retrofit UserInfoBuild1() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .client(getUnsafeOkHttpClient().build()) //OkHttp 사용해서 로그 보기
                .baseUrl(Constant.USER_INFO_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Gson 처리시
                .build();
    }

    public static Retrofit UserInfoBuild() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .client(getUnsafeOkHttpClient().build()) //OkHttp 사용해서 로그 보기
                .baseUrl(Constant.USER_INFO_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)) //Gson 처리시
                .build();
    }


//    public static Retrofit WearableCallBuild() {
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        return new Retrofit.Builder()
//                .client(getUnsafeOkHttpClient().build()) //OkHttp 사용해서 로그 보기
//                .baseUrl(Constant.WEARABLE_URL)
////                .addConverterFactory(GsonConverterFactory.create(gson)) //Gson 처리시
//                .addConverterFactory(TikXmlConverterFactory.create(new TikXml.Builder().exceptionOnUnreadXml(false).build()))       // XML 응답 처리시
//                .build();
//    }

    // 안전하지 않음으로 HTTPS를 통과합니다.
    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//                HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); //OkHttp 사용해서 로그 보기
            RetrofitLogInterceptor logging = new RetrofitLogInterceptor(new RetrofitLogInterceptor.Logger() {

                @Override
                public void log(String message) {
                    if (GLog.isRetrofitLog) {
                        try {
                            // JSON Format이 아닌경우 Exception
                            new JSONObject(message);
                            GLog.d("RESPONSE JSON ->\n" + GLog.getPretty(message));
                        } catch (JSONException e) {
                            GLog.d(message);
                        }
                    }
                }
            });
            logging.setLevel(RetrofitLogInterceptor.Level.BODY);
            builder.addInterceptor(logging);

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            //20200602 - 네트워크 연결 상태 TimeOut 추가 - 15초? 30초?
            builder.connectTimeout(RETROFIT_TIME_OUT, TimeUnit.SECONDS);
            builder.readTimeout(RETROFIT_TIME_OUT, TimeUnit.SECONDS);
            builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));

//            builder.addNetworkInterceptor(
//                    new Interceptor() {
//                        @Override
//                        public okhttp3.Response intercept(Chain chain) throws IOException {
//                            return null;
//                        }
////                        @Override
////                        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
////                            String fcmToken = PreferenceManager.getInstance(OllehApplication.getContext()).getFcmToken();
////                            if (TextUtils.isEmpty(fcmToken)) {
////                                getFcmToken();
////                                fcmToken = PreferenceManager.getInstance(OllehApplication.getContext()).getFcmToken();
////                            }
////
////                            String uuid = PreferenceManager.getInstance(OllehApplication.getContext()).getUUID();
////
////                            //20200904 - Transaction-id Unique 값으로 전달한다.
////                            //String txid = PreferenceManager.getInstance(OllehApplication.getContext()).getTXID();
////                            String txid = Util.getRandomUUID(OllehApplication.getContext());
////                            //KLog.d("HERE","","HERE HEADER iptv-tx-id =="+txid);
////
////                            String loginToken = OllehApplication.getLoginToken();
////                            String language = PreferenceManager.getInstance(OllehApplication.getContext()).getLanguageType().getLanguageCode();
//////							String authStr = STAGING_SERVER ? "Basic T1RBOjY0YzFhYWEzN2E=" : "Basic T1RBOm90YV9zeXN0ZW0=";
////
////                            okhttp3.Request request = null;
////                            okhttp3.Request original = chain.request();
////                            okhttp3.Request.Builder requestBuilder = original.newBuilder()
////                                    .addHeader(Constants.HEADER_ACCEPT, "application/json")
////                                    .addHeader(Constants.HEADER_CONTENT_TYPE, "application/json")
////                                    .addHeader(Constants.HEADER_AUTHORIZATION, AUTH_KEY)
////                                    .addHeader(Constants.HEADER_DEVICE_TOKEN, fcmToken)//FCM TOKEN 추가
////                                    .addHeader(Constants.HEADER_DEVICE_ID, uuid)//언인스톨해도 다시 변하지 않는 아이디값.
////                                    .addHeader(Constants.HEADER_IPTV_TX_ID, txid) //매번 transactionId 처럼 생성해서 할당.
////                                    .addHeader(Constants.HEADER_IPTV_LOGIN_TOKEN, loginToken)
////                                    .addHeader(Constants.HEADER_IPTV_LANG_TYPE, language);
////
//////                            GLog.d("[Header] Device-Token -> " + fcmToken);
//////                            GLog.d("[Header] authorization -> " + AUTH_KEY);
//////                            GLog.d("[Header] device-id -> " + uuid);
//////                            GLog.d("[Header] iptv-tx-id -> " + txid);
//////                            GLog.d("[Header] iptv-login-token -> " + loginToken);
//////                            GLog.d("[Header] iptv-lang-type -> " + language);
////
////                            request = requestBuilder.build();
////                            return chain.proceed(request);
////                        }
//                    });

            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void requestRetrofitThumbnail(RetrofitInterface retrofitInterface) {
        Build().create(RetrofitApiService.class).getRetrofitThumbnail().enqueue(new Callback<RetrofitModel>() {
            @Override
            public void onResponse(Call<RetrofitModel> call, Response<RetrofitModel> response) {
                retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<RetrofitModel> call, Throwable t) {
                retrofitInterface.onFailure(t);
            }
        });
    }

    public void requestRecyclerViewThumbnail(RetrofitInterface retrofitInterface) {
        Build().create(RetrofitApiService.class).getRecyclerViewThumbnail().enqueue(new Callback<RecyclerViewModel>() {
            @Override
            public void onResponse(Call<RecyclerViewModel> call, Response<RecyclerViewModel> response) {
                retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<RecyclerViewModel> call, Throwable t) {
                retrofitInterface.onFailure(t);
            }
        });
    }

    public void requestUserInfo(String id, String pw, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).getLoginUserInfo(id, pw).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                retrofitInterface.onFailure(t);
            }
        });
    }

    //login
    public void requestLoginUserInfo(CommonRequest model, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).getLoginUserInfo(model).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (retrofitInterface != null) retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (retrofitInterface != null) retrofitInterface.onFailure(t);
            }
        });
    }

    public void requestJoin(CommonRequest model, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).getJoin(model).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (retrofitInterface != null) retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                if (retrofitInterface != null) retrofitInterface.onFailure(t);
            }
        });
    }

    public void requestIdCheck(CommonRequest model, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).getIdCheck(model).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (retrofitInterface != null) retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                if (retrofitInterface != null) retrofitInterface.onFailure(t);
            }
        });
    }

    public void requestDeleteUserInfo(String id, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).getIdDelete(id).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (retrofitInterface != null) retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                if (retrofitInterface != null) retrofitInterface.onFailure(t);
            }
        });
    }

    public void requestUpdateUserInfo(CommonRequest model, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).getUpdateUserInfo(model).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (retrofitInterface != null) retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                if (retrofitInterface != null) retrofitInterface.onFailure(t);
            }
        });
    }


    public void requestListTodo(TodoRequest todoModel, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).getListTodo(todoModel).enqueue(new Callback<TodoListResponse>() {
            @Override
            public void onResponse(Call<TodoListResponse> call, Response<TodoListResponse> response) {
                if (retrofitInterface != null) retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<TodoListResponse> call, Throwable t) {
                if (retrofitInterface != null) retrofitInterface.onFailure(t);
            }
        });
    }


    public void requestInsertTodo(TodoRequest todoModel, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).insertTodo(todoModel).enqueue(new Callback<TodoResponse>() {
            @Override
            public void onResponse(Call<TodoResponse> call, Response<TodoResponse> response) {
                if (retrofitInterface != null) retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<TodoResponse> call, Throwable t) {
                if (retrofitInterface != null) retrofitInterface.onFailure(t);
            }
        });
    }

    public void requestDeleteTodo(int num, String id, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).todoDelete(num, id).enqueue(new Callback<TodoListResponse>() {
            @Override
            public void onResponse(Call<TodoListResponse> call, Response<TodoListResponse> response) {
                if (retrofitInterface != null) retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<TodoListResponse> call, Throwable t) {
                if (retrofitInterface != null) retrofitInterface.onFailure(t);
            }
        });
    }

    public void requestUpdateTodo(TodoRequest todoModel, RetrofitInterface retrofitInterface) {
        UserInfoBuild().create(RetrofitApiService.class).updateTodo(todoModel).enqueue(new Callback<TodoResponse>() {
            @Override
            public void onResponse(Call<TodoResponse> call, Response<TodoResponse> response) {
                if (retrofitInterface != null) retrofitInterface.onResponse(response);
            }

            @Override
            public void onFailure(Call<TodoResponse> call, Throwable t) {
                if (retrofitInterface != null) retrofitInterface.onFailure(t);
            }
        });
    }


}