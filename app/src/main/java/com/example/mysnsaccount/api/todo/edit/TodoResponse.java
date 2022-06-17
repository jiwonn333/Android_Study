package com.example.mysnsaccount.api.todo.edit;

import com.example.mysnsaccount.api.CommonResponse;
import com.google.gson.annotations.SerializedName;

public class TodoResponse extends CommonResponse {
    @SerializedName("todoInfo")
    public TodoInfo todoInfo;

    public TodoInfo getTodoInfo() {
        return this.todoInfo;
    }


    public static class TodoInfo {
        private int num;
        private String id;
        private String title;
        private String content;
        private boolean completed;
        private String createDate;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }

    @Override
    public String toString() {
        return "TodoResponse{" +
                "todoInfo=" + todoInfo +
                '}';
    }
}

