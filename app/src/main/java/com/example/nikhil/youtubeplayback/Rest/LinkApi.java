package com.example.nikhil.youtubeplayback.Rest;

import com.example.nikhil.youtubeplayback.ApiResponseBody.LinkResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LinkApi {


    @GET("gwv1m")
    Call<LinkResponseBody> getLink();



}
