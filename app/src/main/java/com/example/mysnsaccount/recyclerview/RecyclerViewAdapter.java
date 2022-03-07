package com.example.mysnsaccount.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mysnsaccount.R;
import com.example.mysnsaccount.model.recyclerviewthumbnailmodel.CustomList;
import com.example.mysnsaccount.util.GLog;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<CustomList> customLists;


    public RecyclerViewAdapter(Context context, List<CustomList> customLists) {
        this.context = context;
        this.customLists = customLists;
    }

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new MyViewHolder(view);
    }

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
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        customLists.get(position).getRatingToastMessage(rating.getContext());
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
