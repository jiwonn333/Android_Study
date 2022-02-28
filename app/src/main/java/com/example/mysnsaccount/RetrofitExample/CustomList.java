package com.example.mysnsaccount.RetrofitExample;

public class CustomList {
    private String imageUrl;

    private String gameTitle;

    private String rating;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public String getGameTitle() {
        return this.gameTitle;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return this.rating;
    }
}