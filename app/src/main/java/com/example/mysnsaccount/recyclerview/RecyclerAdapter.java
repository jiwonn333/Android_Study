package com.example.mysnsaccount.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.Data;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    //adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList<>();


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater를 이용해 전 단계에서 만들었던 item.xml 을 inflate 시킴
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        //return 인자는 ViewHolder임
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ItemViewHolder holder, int position) {
        //Item을 하나, 하나 보여주는 (bind되는) 함수
        holder.onBind(listData.get(position));

    }

    @Override
    public int getItemCount() {
        //RecyclerView의 총 개수
        return listData.size();
    }

    void addItem(Data data) {
        //외부에서 item을 추가시킬 함수
        listData.add(data);
    }

    //RecyclerView의 핵심인 ViewHoler이다.
    //여기서 subView를 setting 해주기
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView gameTitle, rating;
        private ImageView imageView;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            gameTitle = itemView.findViewById(R.id.gameTitle);
            rating = itemView.findViewById(R.id.rating);
        }

        void onBind(Data data) {
            gameTitle.setText(data.getMainTitle());
//            rating.setText(data.g);
        }
    }

//    private void getRoots() {
//        Call<Post> call = jsonPlaceHolderApi.getRoot();
//        call.enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//                if (response.isSuccessful()) {
//                    Post post = response.body();
////                    Log.d("sjw", "결과 : " + response.code());
//                    if (post != null) {
//                        textViewResult.setText(post.getErrMsg());
//                        String url = post.getData().getCustomList().get(0).getImageUrl();
//                        Glide.with(context).load(url).into(imageViewResult);
//                        Log.d("sjw", "결과 : 성공!");
//                        Log.d("sjw", "CustomList : " + post.getData().getCustomList()); //배열값이 있는지
//                        Log.d("sjw", "code : " + post.getCode());
//                        Log.d("sjw", "errMsg : " + post.getErrMsg());
//                        Log.d("sjw", "data : " + post.getData().getMainTitle());
//                        Log.d("sjw", "이미지url :" + post.getData());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//                Log.d("sjw", "실패ㅜㅜ");
//                t.printStackTrace();
//            }
//        });
//    }
}
