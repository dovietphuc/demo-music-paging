package com.example.basicmusic;

import android.content.ContentUris;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;

import com.example.basicmusic.api.MusicApiProvider;
import com.example.basicmusic.data.Music;

import java.util.ArrayList;
import java.util.List;

import phucdv.android.musichelper.Song;

public class MusicController {
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private int mCurrentIndex;
    private boolean mIsPreparing;
    private MusicInterface mMusicInterface;

    public interface MusicInterface {
        Music getMusicAt(int index);
        int getSize();
    }

    public MusicController(Context context, @NonNull MusicInterface musicInterface) {
        mMusicInterface = musicInterface;
        mContext = context;

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mIsPreparing = false;
            }
        });

        mCurrentIndex = -1;
        mIsPreparing = false;
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public boolean isPlaying(){
        return mMediaPlayer.isPlaying();
    }

    public boolean isPreparing(){
        return mIsPreparing;
    }

    public void playNext() {
        if(mMusicInterface.getSize() != 0) {
            if (mCurrentIndex < mMusicInterface.getSize() - 1) {
                mCurrentIndex++;
            } else {
                mCurrentIndex = 0;
            }
            playSongAt(mCurrentIndex);
        }
    }

    public void playPrev() {
        if(mMusicInterface.getSize() != 0) {
            if (mCurrentIndex > 0) {
                mCurrentIndex--;
            } else {
                mCurrentIndex = mMusicInterface.getSize() - 1;
            }
            playSongAt(mCurrentIndex);
        }
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void start() {
        mMediaPlayer.start();
    }

    public void playSongAt(int index) {
        mMediaPlayer.reset();
        String url = MusicApiProvider.BASE_URL + mMusicInterface.getMusicAt(index).getData();
        try {
            mMediaPlayer.setDataSource(url);
            mCurrentIndex = index;
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error starting data source", e);
        }
        mMediaPlayer.prepareAsync();
        mIsPreparing = true;
    }
}
