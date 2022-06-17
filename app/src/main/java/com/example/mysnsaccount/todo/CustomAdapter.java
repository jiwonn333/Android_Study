//package com.example.mysnsaccount.todo;
//
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.Group;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.mysnsaccount.R;
//import com.example.mysnsaccount.api.todo.edit.TodoResponse;
//import com.example.mysnsaccount.api.todo.get.TodoListResponse;
//import com.example.mysnsaccount.retrofit.RetrofitApiManager;
//import com.example.mysnsaccount.retrofit.RetrofitInterface;
//import com.example.mysnsaccount.retrofit.model.TodoRequest;
//import com.example.mysnsaccount.util.AppUtil;
//import com.example.mysnsaccount.util.GLog;
//import com.example.mysnsaccount.util.UserPreference;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import retrofit2.Response;
//
//public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
//    private List<TodoRequest> mToDoItems;
//    private Context context;
//    private int curPos;
////    private List<TodoResponse> items;
//
//    public CustomAdapter(Context context) {
//        this.context = context;
//    }
//
//    public CustomAdapter(List<TodoRequest> mToDoItems, Context context) {
//        this.mToDoItems = mToDoItems;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View holder = LayoutInflater.from(context).inflate(R.layout.todoitem_list, parent, false);
//        return new ViewHolder(holder);
//    }
//
//    // 생성된 뷰에 데이터를 넣는 함수
//    @Override
//    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
//        holder.tvTitle.setText(mToDoItems.get(position).getTitle());
//        holder.tvContent.setText(mToDoItems.get(position).getContent());
//        holder.tvDate.setText(mToDoItems.get(position).getCreateDate());
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mToDoItems == null) {
//            return 0;
//        }
//
//        return mToDoItems.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        //todoitem_list
//        private TextView tvTitle;
//        private TextView tvContent;
//        private TextView tvDate;
//
//        private int num;
//        private String userId;
//        private ImageView btnUpdate;
//        private ImageView btnDelete;
//        private ImageView btnExpand;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            tvTitle = itemView.findViewById(R.id.tv_title);
//            tvContent = itemView.findViewById(R.id.tv_content);
//            tvDate = itemView.findViewById(R.id.tv_date);
//
//            btnUpdate = itemView.findViewById(R.id.btn_update);
//            btnDelete = itemView.findViewById(R.id.btn_delete);
//            btnExpand = itemView.findViewById(R.id.btn_expand);
//
//            Dialog dialog = new Dialog(context);
//            dialog.setContentView(R.layout.activity_tododialog);
//            EditText etTitle = dialog.findViewById(R.id.et_title);
//            EditText etContent = dialog.findViewById(R.id.et_content);
//            Button btnDialog = dialog.findViewById(R.id.dialog_btn);
//
//
//            btnDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    curPos = getBindingAdapterPosition();
//                    num = mToDoItems.get(curPos).getNum();
//                    userId = UserPreference.getUserId(context);
//
//                    requestDeleteTodo(num, userId);
//                    mToDoItems.remove(curPos);
//                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
//
//
//                }
//            });
//
//
//            btnUpdate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    curPos = getBindingAdapterPosition();
//                    etTitle.setText(mToDoItems.get(curPos).getTitle());
//                    etContent.setText(mToDoItems.get(curPos).getContent());
//
//                    btnDialog.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            userId = UserPreference.getUserId(context);
//                            num = mToDoItems.get(curPos).getNum();
//                            String title = etTitle.getText().toString();
//                            String content = etContent.getText().toString();
//                            @SuppressLint("SimpleDateFormat") String currentTime
//                                    = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//
//                            TodoRequest model = new TodoRequest();
//                            model.setId(userId);
//                            model.setNum(num);
//                            model.setTitle(title);
//                            model.setContent(content);
//
//                            requestUpdateTodo(model);
//                            dialog.dismiss();
//                            Toast.makeText(context, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    dialog.show();
//                }
//            });
//
//
//            btnExpand.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Group layoutGroup = itemView.findViewById(R.id.layout_group);
//                    int layoutGroupVisible = layoutGroup.getVisibility();
//                    switch (layoutGroupVisible) {
//                        case 0:
//                            layoutGroup.setVisibility(View.GONE);
//                            btnExpand.setImageResource(R.drawable.ic_baseline_expand_more_24);
//                            break;
//                        case 8:
//                            layoutGroup.setVisibility(View.VISIBLE);
//                            btnExpand.setImageResource(R.drawable.ic_baseline_expand_less_24);
//                            break;
//                    }
//                }
//            });
//        }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    public void setTodoList(ArrayList<TodoRequest> list) {
//        mToDoItems = list;
//        notifyDataSetChanged();
//    }
//
//    public void updateItem(TodoRequest _item) {
//        if (mToDoItems == null) {
//            mToDoItems = new ArrayList<>();
//        }
//        mToDoItems.set(curPos, _item);
//        notifyItemChanged(curPos);
//        notifyItemRangeChanged(curPos, getItemCount());
//    }
//
//
//    // 액티비티에서 호출되는 함수, 현재 어탭터에 새로운 게시글 아이템을 전달받아 추가하는 목적으로 구현
//    public void addItem(TodoRequest _item) {
//        if (mToDoItems == null) {
//            mToDoItems = new ArrayList<>();
//        }
//        mToDoItems.add(0, _item);
//        notifyItemChanged(0); // 새로고침
//    }
//
//    public void requestDeleteTodo(int num, String userId) {
//        RetrofitApiManager.getInstance().requestDeleteTodo(num, userId, new RetrofitInterface() {
//            @Override
//            public void onResponse(Response response) {
//                if (response.isSuccessful()) {
//                    TodoListResponse todoListResponse = (TodoListResponse) response.body();
//                    if (todoListResponse != null) {
//                        List<TodoListResponse.TodoInfo> todoInfoList = todoListResponse.getTodoInfo();
//                        TodoListResponse.ResultInfo resultInfo = todoListResponse.getResultInfo();
//                        if (resultInfo != null && todoInfoList != null) {
//                            if (resultInfo.isResult()) {
//                                AppUtil.showToast(context, resultInfo.getErrorMsg());
//                                notifyItemRemoved(curPos);
//                                notifyItemRangeChanged(curPos, getItemCount());
//
//                            }
//                            AppUtil.showToast(context, resultInfo.getErrorMsg());
//                        } else {
//                            GLog.e("todoInfo, resultInfo is null");
//                        }
//                    } else {
//                        GLog.e("todoResponse is null");
//                    }
//                } else {
//                    GLog.e("delete response is not successful");
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                GLog.e("delete onfailure" + t.getMessage());
//            }
//        });
//    }
//
//    public void requestUpdateTodo(TodoRequest model) {
//        RetrofitApiManager.getInstance().requestUpdateTodo(model, new RetrofitInterface() {
//            @Override
//            public void onResponse(Response response) {
//                if (response.isSuccessful()) {
//                    TodoResponse todoResponse = (TodoResponse) response.body();
//                    if (todoResponse != null) {
//                        TodoResponse.TodoInfo todoInfo = todoResponse.getTodoInfo();
//                        TodoResponse.ResultInfo resultInfo = todoResponse.getResultInfo();
//                        if (todoInfo != null && resultInfo != null) {
//                            if (resultInfo.isResult()) {
//                                TodoRequest model = new TodoRequest();
//                                model.setTitle(todoInfo.getTitle());
//                                model.setContent(todoInfo.getContent());
//                                model.setContent(todoInfo.getContent());
//                                model.setCreateDate(todoInfo.getCreateDate());
//                                updateItem(model);
//                            }
//                            AppUtil.showToast(context, resultInfo.getErrorMsg());
//                        } else {
//                            GLog.e("todoInfo, resultInfo is null");
//                        }
//                    } else {
//                        GLog.e("todoResponse is null");
//                    }
//                } else {
//                    GLog.e("update response is not successful");
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                GLog.e("update onfailure" + t.getMessage());
//            }
//        });
//
//    }
//
//}
