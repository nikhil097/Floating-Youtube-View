package com.example.nikhil.youtubeplayback.ApiResponseBody;

import com.google.gson.annotations.SerializedName;

public class LinkResponseBody {


    @SerializedName("link")
    String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
