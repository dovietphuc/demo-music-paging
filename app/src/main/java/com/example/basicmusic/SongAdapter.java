package com.example.basicmusic;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmusic.data.Music;

import java.util.ArrayList;
import java.util.List;

import phucdv.android.musichelper.Song;

public class SongAdapter extends PagingDataAdapter<Music, SongAdapter.SongViewHolder> {
    private final Context mContext;
    private final MusicController mMusicController;

    public SongAdapter(Context context, @NonNull DiffUtil.ItemCallback<Music> diffCallback) {
        super(diffCallback);
        mContext = context;
        mMusicController = new MusicController(context, new MusicController.MusicInterface() {
            @Override
            public Music getMusicAt(int index) {
                return getItem(index);
            }

            @Override
            public int getSize() {
                return getItemCount();
            }
        });
    }

    public MusicController getMusicController() {
        return mMusicController;
    }

    public void start(){
        mMusicController.start();
        notifyItemChanged(mMusicController.getCurrentIndex());
    }

    public void pause(){
        mMusicController.pause();
        notifyItemChanged(mMusicController.getCurrentIndex());
    }

    public void playNext(){
        int lastIndex = mMusicController.getCurrentIndex();
        mMusicController.playNext();
        notifyItemChanged(lastIndex);
        notifyItemChanged(mMusicController.getCurrentIndex());
    }

    public void playPrev(){
        int lastIndex = mMusicController.getCurrentIndex();
        mMusicController.playPrev();
        notifyItemChanged(lastIndex);
        notifyItemChanged(mMusicController.getCurrentIndex());
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.bindView(getItem(position));
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView mAlbumArt;
        TextView mTitle;
        View mItemView;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            mAlbumArt = itemView.findViewById(R.id.album_art);
            mTitle = itemView.findViewById(R.id.txt_title);
            itemView.setOnClickListener(v -> {
                int lastIndex = mMusicController.getCurrentIndex();
                mMusicController.playSongAt(getLayoutPosition());
                notifyItemChanged(lastIndex);
                notifyItemChanged(mMusicController.getCurrentIndex());
                if (mItemClickListener != null) {
                    int pos = getLayoutPosition();
                    mItemClickListener.onClick(v, getItem(pos), pos);
                }

            });

        }

        public void bindView(Music song) {
            if(mMusicController.getCurrentIndex() == getLayoutPosition()){
                if(mMusicController.isPlaying() || mMusicController.isPreparing()) {
                    mItemView.setBackgroundColor(Color.GREEN);
                } else {
                    mItemView.setBackgroundColor(Color.YELLOW);
                }
            } else {
                mItemView.setBackgroundColor(Color.TRANSPARENT);
            }

            mTitle.setText(song.getTitle());
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.getId());
//                try {
//                    Bitmap bitmap = mContext.getContentResolver().loadThumbnail(trackUri, new Size(72, 72), null);
//                    mAlbumArt.setImageBitmap(bitmap);
//                } catch (Exception e) {
//                }
//            } else {
//                mAlbumArt.setImageURI(song.getAlbumUri());
//            }
        }
    }

    private OnItemClickListener mItemClickListener;

    public void setItemClickListener(OnItemClickListener Listener) {
        mItemClickListener = Listener;
    }

    public interface OnItemClickListener {
        void onClick(View v, Music song, int pos);
    }
}

class MusicDiffUtil extends DiffUtil.ItemCallback<Music> {

    @Override
    public boolean areItemsTheSame(@NonNull Music oldItem, @NonNull Music newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Music oldItem, @NonNull Music newItem) {
        return oldItem.getTitle().equals(newItem.getTitle())
                && oldItem.getData().equals(newItem.getData());
    }
}