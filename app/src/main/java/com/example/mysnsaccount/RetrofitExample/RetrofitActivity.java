package com.example.mysnsaccount.RetrofitExample;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mysnsaccount.R;
import com.example.mysnsaccount.util.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {
    private TextView textViewResult;
    private ImageView imageViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit); //main 화면 불러오기
        context = this;

        textViewResult = findViewById(R.id.text_view_result);
        imageViewResult = findViewById(R.id.image_view_result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Gson 처리시
                //.addConverterFactory(ScalarsConverterFactory.create()) // String 등 처리시
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getPosts();
    }

    private void getPosts() {
        Call<Post> call = jsonPlaceHolderApi.getPost();
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Post post = response.body();
//                    Log.d("sjw", "결과 : " + response.code());
                    if (post != null) {
                        textViewResult.setText(post.getErrMsg());
                        String url = post.getData().getCustomList().get(0).getImageUrl();
                        Glide.with(context).load(url).into(imageViewResult);
                        Log.d("sjw", "결과 : 성공!");
                        Log.d("sjw", "code : " + post.getCode());
                        Log.d("sjw", "errMsg : " + post.getErrMsg());
                        Log.d("sjw", "data : " + post.getData().getMainTitle());
                        Log.d("sjw", "이미지url :" + post.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.d("sjw", "실패ㅜㅜ");
                t.printStackTrace();
            }
        });
    }
}
