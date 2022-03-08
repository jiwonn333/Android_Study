package com.example.mysnsaccount.recyclerview;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mysnsaccount.R;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.CustomList;
import com.example.mysnsaccount.util.GLog;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<CustomList> customLists; //어댑터에 들어갈 리스트


    //메인 액티비티와 연결
    public RecyclerViewAdapter(Context context, List<CustomList> customLists) {
        this.context = context;
        this.customLists = customLists;
    }

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    //뷰홀더 생성
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(view);
    }

    //생성된 뷰홀더에 데이터 넣는 함수
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        GLog.d("[테스트] gameTitle : " + customLists.get(position).getGameTitle());

        holder.getData();


    }

    @Override
    public int getItemCount() {
        GLog.d();
        if (customLists != null && !customLists.isEmpty())
            return customLists.size();

        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitle, rating;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gameTitle = itemView.findViewById(R.id.gameTitle);
            rating = itemView.findViewById(R.id.rating);
            imageView = itemView.findViewById(R.id.imageView);

            //클릭리스너 설정
            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();

                    GLog.d("adapter position " + position);
                    if (position != RecyclerView.NO_POSITION) {
//                         customLists.get(position).getRatingToastMessage(rating.getContext()); //클릭시 토스트메세지 띄우기
                        // 클릭시 rating값 1씩 증가
                        //pos = String.valueOf(pos);

                        try {
                            int item = Integer.parseInt(customLists.get(position).getRating());
                            item++;
                            String temp = String.valueOf(item);
                            customLists.get(position).setRating(temp);
                            notifyItemChanged(position);
                        } catch (Exception e) {
                            GLog.d("e : " + e.getMessage());
                            if (e instanceof NumberFormatException) {
                                Toast.makeText(context, "숫자로 변환 안됌", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }


        public void getData() {
            //gameTitle
            gameTitle.setText(customLists.get(getAdapterPosition()).getGameTitle());

            //rating
            rating.setText(customLists.get(getAdapterPosition()).getRatingText(context));

            //url
            Glide.with(context).load(customLists.get(getAdapterPosition()).getImageUrl()).into(imageView);
        }
    }

    public void addItem(List<CustomList> customList) {
        GLog.d("customList : " + customList.size());
        customLists = customList;
    }
}
