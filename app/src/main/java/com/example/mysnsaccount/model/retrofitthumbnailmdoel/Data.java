package com.example.mysnsaccount.model.retrofitthumbnailmdoel;

import java.util.List;

public class Data {

    private String mainTitle;

    private List<CustomList> customList;

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getMainTitle() {
        return this.mainTitle;
    }

    public void setCustomList(List<CustomList> customList) {
        this.customList = customList;
    }

    public List<CustomList> getCustomList() {
        return this.customList;
    }
}

