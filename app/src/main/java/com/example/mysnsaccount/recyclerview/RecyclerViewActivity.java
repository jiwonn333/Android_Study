package com.example.mysnsaccount.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activivty_recyclerview);

        context = this;

        dataInfo = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        textView = findViewById(R.id.text_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)); //가로로 스크롤
        recyclerViewAdapter = new RecyclerViewAdapter(context);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.setVisibility(View.VISIBLE);

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

                                } else {
                                    setErrorLayout("customLists값이 null 입니다.");
                                }
                            } else {
                                setErrorLayout("data값이 null 입니다.");
                            }
                        } else {
                            setErrorLayout("recyclerViewModel값이 null 입니다.");
                        }
                    } else {
                        setErrorLayout("통신오류.");
                    }
                } catch (Exception e) {
                    setErrorLayout(e.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                setErrorLayout("customLists값이 null 입니다.");
            }
        });
    }

    private void setErrorLayout(String msg) {
        recyclerView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(msg);
    }
}
