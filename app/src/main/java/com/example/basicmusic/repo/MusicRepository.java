package com.example.basicmusic.repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.basicmusic.api.BaseResponse;
import com.example.basicmusic.api.MusicApi;
import com.example.basicmusic.api.MusicApiProvider;
import com.example.basicmusic.data.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicRepository {
    private MusicApi mMusicApi = MusicApiProvider.getMusicApi();
    public MusicPagingSource mMusicPagingSource;

    public MusicRepository(){
        mMusicPagingSource = new MusicPagingSource();
    }

    public LiveData<List<Music>> getMusics(){
        MutableLiveData<List<Music>> listUser =
                new MutableLiveData<>(new ArrayList<>());

        mMusicApi.getMusic().enqueue(new Callback<MusicApi.MusicResponse>() {
            @Override
            public void onResponse(Call<MusicApi.MusicResponse> call,
                                   Response<MusicApi.MusicResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().isSuccess()){
                        listUser.setValue(response.body().getMusics());
                        Log.d("MusicRepository", "onResponse: " + response.body().getMessage());
                    } else {
                        Log.e("MusicRepository", "onResponse: " + response.body().getMessage());
                    }
                } else {
                    Log.e("MusicRepository", "onResponse: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<MusicApi.MusicResponse> call, Throwable t) {
                Log.e("MusicRepository", "onFailure: ", t);
            }
        });

        return listUser;
    }

    public Call<BaseResponse> uploadMusic(File file, String title){
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", title);
        if(filePart != null){
            body.addPart(filePart);
        }
        return mMusicApi.uploadMusic(body.build());
    }
}
