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

        String textRatingWarning = context.getString(R.string.text_rating_warning);
        String textRatingAll = context.getString(R.string.text_rating_all);

        if ("all".equals(getRating())) {
            return textRatingAll;
        } else {
            return String.format(textRatingWarning, getRating());
        }
    }


    //토스트메세지
    public String getRatingToastMessage(Context context) {
        if (context == null) return "";

        String msgRatingWarning = context.getString(R.string.msg_rating_warning);
        String msgRatingAll = context.getString(R.string.msg_rating_all);
        String msgRatingR = context.getString(R.string.msg_rating_r_rated);

        switch (getRating()) {
            case "12":
            case "15":
                Toast.makeText(context.getApplicationContext(), String.format(msgRatingWarning, getRating()), Toast.LENGTH_SHORT).show();
                break;
            case "19":
                Toast.makeText(context.getApplicationContext(), msgRatingR, Toast.LENGTH_SHORT).show();
                break;
            case "all":
                Toast.makeText(context.getApplicationContext(), msgRatingAll, Toast.LENGTH_SHORT).show();
                break;
            default:
                GLog.d("맞는조건 없음");
                break;
        }
        return null;
    }
}
