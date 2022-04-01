package com.example.mysnsaccount.model.recycerviewicon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.customwebview.WebViewActivity;
import com.example.mysnsaccount.dialogtest.AlertDialogActivity;
import com.example.mysnsaccount.permission.PermissionActivity;
import com.example.mysnsaccount.recyclerview.RecyclerViewActivity;
import com.example.mysnsaccount.retrofit.RetrofitActivity;
import com.example.mysnsaccount.util.GLog;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    private List<RecyclerViewItem> itemLists; // 어댑터에 들어갈 리스트
    Intent intent;

    //메인 액티비티와 연결
    public RecyclerViewAdapter(Context context, List<RecyclerViewItem> itemLists) {
        super();
        this.context = context;
        this.itemLists = itemLists;
    }

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }


    // 아이템 뷰를 위한 뷰 홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.icon_list, parent, false);
        return new ViewHolder(view);
    }

    // 생성된 뷰 홀더에 데이터 넣는 함수 (position에 해당하는 데이터를 뷰홀더 아이템뷰에 표시)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(itemLists.get(position).getIconDrawable());
        holder.title.setText(itemLists.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        if (itemLists != null && !itemLists.isEmpty())
            return itemLists.size();

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_text);

            itemView.setOnClickListener(view -> {

                int position = getBindingAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    try {
                        startActivity(position);

                    } catch (Exception e) {
                        GLog.d("실패다");

                    }
                }
            });
        }
    }

    public void addItem(ArrayList<RecyclerViewItem> itemList) {
        itemLists = itemList;
    }

    public void startActivity(int position) {
        switch (position) {
            case 0:
                context.startActivity(new Intent(context, WebViewActivity.class));
                break;
            case 1:
                context.startActivity(new Intent(context, RetrofitActivity.class));
                break;
            case 2:
                context.startActivity(new Intent(context, PermissionActivity.class));
                break;
            case 3:
                context.startActivity(new Intent(context, RecyclerViewActivity.class));
                break;
            case 4:
                context.startActivity(new Intent(context, AlertDialogActivity.class));
                break;
            default:
                GLog.d("모두 해당 안됨");
                break;
        }
    }
}