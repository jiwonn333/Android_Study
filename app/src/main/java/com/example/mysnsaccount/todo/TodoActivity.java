package com.example.mysnsaccount.todo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class TodoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton btnInsert;
    private TodoAdapter todoAdapter;
    private Context context;
    private EditText etTitle;
    private EditText etContent;
    private Button btnDialog;
    private String userId;
    private String title;
    private String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        context = this;
        btnInsert = findViewById(R.id.add_btn);
        todoAdapter = new TodoAdapter(context);
        recyclerView = findViewById(R.id.re_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(todoAdapter);

        // 데이터가 있으면 가져오기
        loadRecentDB();

        // 아이템 테두리
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 추가 버튼 클릭 시 // 통신..
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(TodoActivity.this);
                dialog.setContentView(R.layout.activity_tododialog);

                // 팝업창 정의
                etTitle = dialog.findViewById(R.id.et_title);
                etContent = dialog.findViewById(R.id.et_content);
                btnDialog = dialog.findViewById(R.id.dialog_btn);


                // insert 통신
                btnDialog.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onClick(View view) {
                        // 입력한 내용 가져오기
                        userId = UserPreference.getUserId(context);
                        title = etTitle.getText().toString();
                        content = etContent.getText().toString();

                        if (!TextUtils.isEmpty(title)) {
                            if (!TextUtils.isEmpty(content)) {
                                // request 모델에 저장
                                TodoRequest model = new TodoRequest();
                                model.setId(userId);
                                model.setTitle(title);
                                model.setContent(content);

                                // insert 통신
                                requestInsertTodo(model);
                            } else {
                                AppUtil.showToast(context, "내용을 입력해주세요.");
                            }
                        } else {
                            AppUtil.showToast(context, "제목을 입력해주세요.");
                        }

                        // 스크롤 부드럽게
                        recyclerView.smoothScrollToPosition(0);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }


    // 삽입 (insert)
    private void requestInsertTodo(TodoRequest model) {
        RetrofitApiManager.getInstance().requestInsertTodo(model, new RetrofitInterface() {
            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    TodoResponse todoResponse = (TodoResponse) response.body();
                    if (todoResponse != null) {
                        TodoResponse.TodoInfo todoInfo = todoResponse.getTodoInfo();
                        TodoResponse.ResultInfo resultInfo = todoResponse.getResultInfo();
                        GLog.e("todoInfo : " + todoInfo);
                        GLog.e("resultInfo : " + resultInfo);
                        if (resultInfo != null) {
                            if (todoInfo != null) {
                                if (resultInfo.isResult()) {
                                    AppUtil.showToast(context, resultInfo.getErrorMsg());
                                    loadRecentDB();
                                } else {
                                    AppUtil.showToast(context, resultInfo.getErrorMsg());
                                    GLog.e(resultInfo.getErrorMsg());
                                }
                            } else {
                                AppUtil.showToast(context, resultInfo.getErrorMsg());
                            }
                        } else {
                            GLog.e("resultInfo is null");
                        }
                    } else {
                        GLog.e("todoListResponse is null");
                    }
                } else {
                    GLog.e("insert Response is not successful");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GLog.e("insertTodoResponse : " + t.toString());
            }
        });
    }


    private void loadRecentDB() {
        TodoRequest model = new TodoRequest();
        userId = UserPreference.getUserId(context);
        model.setId(userId);
        requestListTodo(model);
    }

    // 조회(select)
    private void requestListTodo(TodoRequest model) {
        RetrofitApiManager.getInstance().requestListTodo(model, new RetrofitInterface() {
            @Override
            public void onResponse(Response response) {
                if (response.isSuccessful()) {
                    TodoListResponse todoListResponse = (TodoListResponse) response.body();
                    if (todoListResponse != null) {
                        List<TodoListResponse.TodoInfo> todoInfoList = todoListResponse.getTodoInfo();
                        TodoListResponse.ResultInfo resultInfo = todoListResponse.getResultInfo();
                        if (todoInfoList != null && resultInfo != null) {
                            if (resultInfo.isResult()) {
                                ArrayList<TodoResponse.TodoInfo> list = new ArrayList<>();
                                for (TodoListResponse.TodoInfo todoInfo : todoInfoList) {
                                    TodoResponse.TodoInfo items = new TodoResponse.TodoInfo();
                                    items.setTitle(todoInfo.getTitle());
                                    items.setContent(todoInfo.getContent());
                                    items.setCreateDate(todoInfo.getCreateDate());
                                    items.setNum(todoInfo.getNum());
                                    list.add(items);
                                }
                                todoAdapter.setTodoList(list);
                            } else {
                                AppUtil.showToast(context, resultInfo.getErrorMsg());
                            }
                        } else {
                            GLog.e("todoInfo is null");
                        }
                    } else {
                        GLog.e("todoResponse is null");
                    }
                } else {
                    GLog.e("select response is not Successful");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                GLog.d("TodoListResponse : " + t.toString());
            }
        });
    }
}
