package com.example.nikhil.youtubeplayback.Rest;

public interface ResponseCallback<T> {

    void success(T t);

    void  failure(T t);


}
