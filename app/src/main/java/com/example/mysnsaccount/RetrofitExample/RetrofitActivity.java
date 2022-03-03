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
import com.example.mysnsaccount.model.SingleThumbnailModel.CustomList;
import com.example.mysnsaccount.model.SingleThumbnailModel.Data;
import com.example.mysnsaccount.model.SingleThumbnailModel.SingleThumbnailModel;

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

        RetrofitApiManager.getInstance().requestSingleThumbnail(new RetrofitInterface() {
            @Override
            public void onResponse(Response response) {
                try {
                    if (response.isSuccessful()) {
                        SingleThumbnailModel singleThumbnailModel = (SingleThumbnailModel) response.body();
                        if (singleThumbnailModel != null) {
                            Data data = singleThumbnailModel.getData();
                            if (data != null) {
                                List<CustomList> customLists = data.getCustomList();
                                if (customLists != null && !customLists.isEmpty()) {
                                    String url = customLists.get(0).getImageUrl();
                                    Glide.with(context).load(url).into(imageViewResult);
                                    textViewResult.setText("성공");
                                    Log.d("sjw", "결과 : 성공!");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.d("sjw", "null");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("sjw", "결과 : 실패!");
            }
        });
    }
}