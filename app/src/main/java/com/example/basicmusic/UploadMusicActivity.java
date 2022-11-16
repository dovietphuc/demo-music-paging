package com.example.basicmusic;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.CursorLoader;

import com.example.basicmusic.api.BaseResponse;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadMusicActivity extends AppCompatActivity {

    EditText mTitle;
    TextView mSelectSong;
    Button mUpload;
    UploadMusicViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_music);

        mViewModel = new ViewModelProvider(this).get(UploadMusicViewModel.class);

        mTitle = findViewById(R.id.edt_title);
        mSelectSong = findViewById(R.id.txt_select_song);
        mUpload = findViewById(R.id.btn_upload);

        mSelectSong.setOnClickListener(v -> {
            Intent audioIntent = new Intent();
            audioIntent.setType("audio/*");
            audioIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(audioIntent, 999);
        });

        mViewModel.getFile().observe(this, new Observer<File>() {
            @Override
            public void onChanged(File file) {
                if (file != null) {
                    mSelectSong.setText("Select song: " + file.getName());
                } else {
                    mSelectSong.setText("Select song: ");
                }
            }
        });

        mUpload.setOnClickListener(v -> {
            if (mViewModel.getFile().getValue() == null) {
                Toast.makeText(this, "Ban chua chon file", Toast.LENGTH_SHORT).show();
                return;
            }
            mViewModel.uploadMusic(mViewModel.getFile().getValue(), mTitle.getText().toString())
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().isSuccess()) {
                                    Toast.makeText(UploadMusicActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UploadMusicActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UploadMusicActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Toast.makeText(UploadMusicActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null || data.getData() == null) {
            // error
            return;
        }
        if (requestCode == 999) {
            try {
                Uri uri = data.getData();
                mViewModel.setFile(FileUtils.from(this, uri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}