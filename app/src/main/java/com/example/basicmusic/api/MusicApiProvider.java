package com.example.basicmusic.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicApiProvider {
    public static String BASE_URL = "http://113.22.176.40:8888/";
    private static MusicApi mMusicApi;
    private static Retrofit mRetrofit;

    public static Retrofit getRetrofit(){
        if(mRetrofit == null){
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            mRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public static MusicApi getMusicApi(){
        if(mMusicApi == null){
            Retrofit retrofit = getRetrofit();
            mMusicApi = retrofit.create(MusicApi.class);
        }
        return mMusicApi;
    }
}
