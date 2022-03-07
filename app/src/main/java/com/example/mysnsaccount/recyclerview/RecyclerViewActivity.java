package com.example.mysnsaccount.recyclerview;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.CustomList;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.Data;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.RecyclerViewModel;
import com.example.mysnsaccount.retrofit.RetrofitApiManager;
import com.example.mysnsaccount.retrofit.RetrofitInterface;
import com.example.mysnsaccount.util.GLog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class RecyclerViewActivity extends AppCompatActivity {
    private Context context;
    List<CustomList> dataInfo;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activivty_recyclerview);

        context = this;

        dataInfo = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewAdapter = new RecyclerViewAdapter(context);
        recyclerView.setAdapter(recyclerViewAdapter);

        getRoots();
    }


    private void getRoots() {
        RetrofitApiManager.getInstance().requestRecyclerViewThumbnail(new RetrofitInterface() {
            @Override
            public void onResponse(Response response) {
                try {
                    if (response.isSuccessful()) {
                        RecyclerViewModel recyclerViewModel = (RecyclerViewModel) response.body();
                        if (recyclerViewModel != null) {
                            Data data = recyclerViewModel.getData();
                            if (data != null) {
                                List<CustomList> customLists = data.getList();
                                if (customLists != null && !customLists.isEmpty()) {
                                    recyclerViewAdapter.addItem(customLists);
                                    recyclerViewAdapter.notifyDataSetChanged();

                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    GLog.e("null");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GLog.e("결과 : 실패");
            }
        });
    }
}
