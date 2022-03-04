package com.example.mysnsaccount.model.ThumbnailModel;

import java.util.List;

public class Data
{
    private String mainTitle;

    private List<CustomList> list;

    public void setMainTitle(String mainTitle){
        this.mainTitle = mainTitle;
    }
    public String getMainTitle(){
        return this.mainTitle;
    }
    public void setList(List<CustomList> list){
        this.list = list;
    }
    public List<CustomList> getList(){
        return this.list;
    }
}