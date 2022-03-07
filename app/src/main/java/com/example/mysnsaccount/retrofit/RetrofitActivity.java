package com.example.mysnsaccount.retrofit;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mysnsaccount.R;
import com.example.mysnsaccount.model.retrofitthumbnailmdoel.CustomList;
import com.example.mysnsaccount.model.retrofitthumbnailmdoel.Data;
import com.example.mysnsaccount.model.retrofitthumbnailmdoel.RetrofitModel;
import com.example.mysnsaccount.util.GLog;

import java.util.List;

import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {
    private TextView textViewResult;
    private ImageView imageViewResult;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit); //main 화면 불러오기
        context = this;

        textViewResult = findViewById(R.id.text_view_result);
        imageViewResult = findViewById(R.id.image_view_result);

        RetrofitApiManager.getInstance().requestRetrofitThumbnail(new RetrofitInterface() {
            @Override
            public void onResponse(Response response) {
                try {
                    if (response.isSuccessful()) {
                        RetrofitModel retrofitModel = (RetrofitModel) response.body();
                        if (retrofitModel != null) {
                            Data data = retrofitModel.getData();
                            if (data != null) {
                                List<CustomList> customLists = data.getCustomList();
                                if (customLists != null && !customLists.isEmpty()) {
                                    String url = customLists.get(0).getImageUrl();
                                    Glide.with(context).load(url).into(imageViewResult);
                                    textViewResult.setText("성공");
                                    GLog.d("결과 : 성공!");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    GLog.e("결과 : null");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GLog.d("결과 : 실패!");
            }
        });
    }
}