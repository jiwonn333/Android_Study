package com.example.mysnsaccount.todo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.api.todo.edit.TodoResponse;
import com.example.mysnsaccount.api.todo.get.TodoListResponse;
import com.example.mysnsaccount.retrofit.RetrofitApiManager;
import com.example.mysnsaccount.retrofit.RetrofitInterface;
import com.example.mysnsaccount.retrofit.model.TodoRequest;
import com.example.mysnsaccount.util.AppUtil;
import com.example.mysnsaccount.util.GLog;
import com.example.mysnsaccount.util.UserPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<TodoResponse.TodoInfo> items;
    private Context context;
    private int curPos;

    public TodoAdapter(Context context) {
        this.context = context;
    }

    public TodoAdapter(List<TodoResponse> mToDoItems, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(context).inflate(R.layout.todoitem_list, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        holder.tvTitle.setText(items.get(position).getTitle());
        holder.tvContent.setText(items.get(position).getContent());
        holder.tvDate.setText(items.get(position).getCreateDate());
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvDate;
        private int num;
        private String userId;
        private ImageView btnUpdate;
        private ImageView btnDelete;
        private ImageView btnExpand;


        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvDate = itemView.findViewById(R.id.tv_date);

            // 상세 페이지 전환시 결과값 intent 로 넘기기
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TodoDetailActivity.class);
                    intent.putExtra("title", tvTitle.getText().toString());
                    intent.putExtra("date", tvDate.getText().toString());
                    intent.putExtra("content", tvContent.getText().toString());
                    context.startActivity(intent);
                }
            });

            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.activity_tododialog);
            EditText etTitle = dialog.findViewById(R.id.et_title);
            EditText etContent = dialog.findViewById(R.id.et_content);
            Button btnDialog = dialog.findViewById(R.id.dialog_btn);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnExpand = itemView.findViewById(R.id.btn_expand);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    curPos = getBindingAdapterPosition();
                    num = items.get(curPos).getNum();
                    userId = UserPreference.getUserId(context);
                    requestDeleteTodo(num, userId);
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    curPos = getBindingAdapterPosition();
                    etTitle.setText(items.get(curPos).getTitle());
                    etContent.setText(items.get(curPos).getContent());

                    btnDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userId = UserPreference.getUserId(context);
                            num = items.get(curPos).getNum();
                            String title = etTitle.getText().toString();
                            String content = etContent.getText().toString();
                            @SuppressLint("SimpleDateFormat") String currentTime
                                    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                            TodoRequest model = new TodoRequest();
                            model.setId(userId);
                            model.setNum(num);
                            model.setTitle(title);
                            model.setContent(content);

                            requestUpdateTodo(model);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });


            btnExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Group layoutGroup = itemView.findViewById(R.id.layout_group);
                    int layoutGroupVisible = layoutGroup.getVisibility();

                    switch (layoutGroupVisible) {
                        case 0:
                            layoutGroup.setVisibility(View.GONE);
                            btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24);
                            break;
                        case 8:
                            layoutGroup.setVisibility(View.VISIBLE);
                            btnExpand.setImageResource(R.drawable.ic_baseline_expand_less_24);
                            break;
                    }
                }
            });


        }

    }


    @SuppressLint("NotifyDataSetChanged")
    public void setTodoList(ArrayList<TodoResponse.TodoInfo> list) {
        items = list;
        notifyDataSetChanged();
    }

    public void updateItem(TodoResponse.TodoInfo _item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.set(curPos, _item);
        notifyItemChanged(curPos);
        notifyItemRangeChanged(curPos, getItemCount());
    }

    public void addItem(TodoResponse.TodoInfo _item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(_item);
        notifyItemChanged(0); // 새로고침
    }


    public void requestDeleteTodo(int num, String userId) {
        RetrofitApiManager.getInstance().requestDeleteTodo(num, userId, new RetrofitInterface() {
            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    TodoListResponse todoListResponse = (TodoListResponse) response.body();
                    if (todoListResponse != null) {
                        List<TodoListResponse.TodoInfo> todoInfoList = todoListResponse.getTodoInfo();
                        TodoListResponse.ResultInfo resultInfo = todoListResponse.getResultInfo();
                        if (resultInfo != null && todoInfoList != null) {
                            if (resultInfo.isResult()) {
                                AppUtil.showToast(context, resultInfo.getErrorMsg());
                                notifyItemRemoved(curPos);
                                notifyItemRangeChanged(curPos, getItemCount());
                                items.remove(curPos);
                                AppUtil.showToast(context, resultInfo.getErrorMsg());
                            }
                            AppUtil.showToast(context, resultInfo.getErrorMsg());
                        } else {
                            GLog.e("todoInfo, resultInfo is null");
                        }
                    } else {
                        GLog.e("todoResponse is null");
                    }
                } else {
                    GLog.e("delete response is not successful");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GLog.e("delete onFailure" + t.getMessage());
            }
        });
    }

    public void requestUpdateTodo(TodoRequest model) {
        RetrofitApiManager.getInstance().requestUpdateTodo(model, new RetrofitInterface() {
            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    TodoResponse todoResponse = (TodoResponse) response.body();
                    if (todoResponse != null) {
                        TodoResponse.TodoInfo todoInfo = todoResponse.getTodoInfo();
                        TodoResponse.ResultInfo resultInfo = todoResponse.getResultInfo();
                        if (resultInfo != null) {
                            if (todoInfo != null) {
                                if (resultInfo.isResult()) {
                                    TodoResponse.TodoInfo model = new TodoResponse.TodoInfo();
                                    model.setTitle(todoInfo.getTitle());
                                    model.setContent(todoInfo.getContent());
                                    model.setContent(todoInfo.getContent());
                                    model.setCreateDate(todoInfo.getCreateDate());
                                    model.setNum(todoInfo.getNum());
                                    updateItem(model);
                                }
                                AppUtil.showToast(context, resultInfo.getErrorMsg());
                            } else {
                                AppUtil.showToast(context, resultInfo.getErrorMsg());
                                GLog.e(resultInfo.getErrorMsg());
                            }
                        } else {
                            GLog.e("resultInfo is null");
                        }
                    } else {
                        GLog.e("todoResponse is null");
                    }
                } else {
                    GLog.e("update response is not successful");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GLog.e("update onFailure" + t.getMessage());
            }
        });

    }
}
