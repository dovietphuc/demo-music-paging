package com.example.basicmusic;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.basicmusic.api.BaseResponse;
import com.example.basicmusic.repo.MusicRepository;

import java.io.File;

import retrofit2.Call;

public class UploadMusicViewModel extends ViewModel {
    private MutableLiveData<File> mFile = new MutableLiveData<>();
    private MusicRepository mMusicRepository;

    public UploadMusicViewModel(){
        mMusicRepository = new MusicRepository();
    }

    public LiveData<File> getFile() {
        return mFile;
    }

    public void setFile(File file) {
        this.mFile.setValue(file);
    }

    public Call<BaseResponse> uploadMusic(@NonNull File file, @NonNull String title){
        return mMusicRepository.uploadMusic(file, title);
    }
}
