package com.example.mysnsaccount.retrofit.model;

import com.google.gson.annotations.SerializedName;

public class TodoRequest {
    @SerializedName("id")
    String id;
    @SerializedName("title")
    String title;
    @SerializedName("content")
    String content;
    @SerializedName("num")
    int num;
    @SerializedName("completed")
    boolean completed;
    @SerializedName("createDate")
    String createDate;

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    @Override
    public String toString() {
        return "TodoRequest{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", num=" + num +
                ", completed=" + completed +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
