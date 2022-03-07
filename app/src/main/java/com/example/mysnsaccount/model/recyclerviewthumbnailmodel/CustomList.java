package com.example.mysnsaccount.model.recyclerviewthumbnailmodel;

import android.content.Context;
import android.widget.Toast;

import com.example.mysnsaccount.R;
import com.example.mysnsaccount.util.GLog;

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

    //TextView(rating)
    public String getRatingText(Context context) {
        if (context == null) return "";

        String text_rating_warning = context.getString(R.string.text_rating_warning);
        String text_rating_all = context.getString(R.string.text_rating_all);

        if ("all".equals(getRating())) {
            return text_rating_all;
        } else {
            return String.format(text_rating_warning, getRating());
        }
    }


    //토스트메세지
    public String getRatingToastMessage(Context context) {
        if (context == null) return "";

        String msg_rating_warning = context.getString(R.string.msg_rating_warning);
        String msg_rating_all = context.getString(R.string.msg_rating_all);
        String msg_rating_R_rated = context.getString(R.string.msg_rating_R_rated);

        switch (getRating()) {
            case "12":
            case "15":
                Toast.makeText(context.getApplicationContext(), String.format(msg_rating_warning, getRating()), Toast.LENGTH_SHORT).show();
                break;
            case "19":
                Toast.makeText(context.getApplicationContext(), msg_rating_R_rated, Toast.LENGTH_SHORT).show();
                break;
            case "all":
                Toast.makeText(context.getApplicationContext(), msg_rating_all, Toast.LENGTH_SHORT).show();
                break;
            default:
                GLog.d("맞는조건 없음");
                break;
        }
        return null;
    }
}
