package com.example.basicmusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.example.basicmusic.api.BaseResponse;
import com.example.basicmusic.data.Music;
import com.example.basicmusic.repo.MusicRepository;

import java.io.File;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.Call;

public class MainActivityViewModel extends ViewModel {
    private MusicRepository mMusicRepository;
    private Pager<Integer, Music> mPager;
    public Flowable<PagingData<Music>> mFlowable;

    public MainActivityViewModel(){
        mMusicRepository = new MusicRepository();
        mPager = new Pager<Integer, Music>(
                new PagingConfig(20),
                () -> mMusicRepository.mMusicPagingSource
        );
        mFlowable = PagingRx.getFlowable(mPager);
        PagingRx.cachedIn(mFlowable,
                ViewModelKt.getViewModelScope(this));
    }

    public LiveData<List<Music>> getMusics(){
        return mMusicRepository.getMusics();
    }

}
