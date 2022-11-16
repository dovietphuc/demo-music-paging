package com.example.basicmusic.repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.example.basicmusic.api.MusicApi;
import com.example.basicmusic.api.MusicApiProvider;
import com.example.basicmusic.data.Music;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MusicPagingSource extends RxPagingSource<Integer, Music> {
    private MusicApi mMusicApi = MusicApiProvider.getMusicApi();

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Music> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Music> anchorPage =
                pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Music>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        Integer page = loadParams.getKey();
        if(page == null){
            page = 1;
        }
        return mMusicApi.getMusic(page, loadParams.getLoadSize())
                .subscribeOn(Schedulers.io())
                .map(this::toLoadResult);
    }

    private LoadResult<Integer, Music> toLoadResult(MusicApi.MusicResponse response){
        return new LoadResult.Page<>(
                response.getMusics(),
                null, // Only paging forward.
                response.getNextPage(),
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);
    }
}
