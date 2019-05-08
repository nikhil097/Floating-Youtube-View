package com.example.nikhil.youtubeplayback.Rest;

import com.example.nikhil.youtubeplayback.ApiResponseBody.LinkResponseBody;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {


    LinkApi linkApi;

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public ApiService()
    {
        linkApi=ApiService.getClient().create(LinkApi.class);
    }



    public void getLink(final ResponseCallback<LinkResponseBody> callback)
    {
        Call<LinkResponseBody> call=linkApi.getLink();

        call.enqueue(new Callback<LinkResponseBody>() {

            @Override
            public void onResponse(Call<LinkResponseBody> call, Response<LinkResponseBody> response) {
                if(response.isSuccessful())
                {
                    callback.success(response.body());
                }
                else{

                }

            }

            @Override
            public void onFailure(Call<LinkResponseBody> call, Throwable t) {

            }
        });
    }


}
