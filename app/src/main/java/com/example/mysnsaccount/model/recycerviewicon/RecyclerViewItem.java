package com.example.mysnsaccount.model.recycerviewicon;

public class RecyclerViewItem {
    private int iconDrawable;
    private String title;


    public RecyclerViewItem(int iconDrawable, String title) {
        this.iconDrawable = iconDrawable;
        this.title = title;
    }

    public void setIconDrawable(int iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconDrawable() {
        return iconDrawable;
    }

    public String getTitle() {
        return title;
    }
}
