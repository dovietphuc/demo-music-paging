package com.example.basicmusic.api;

import com.example.basicmusic.data.Music;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MusicApi {

    public class MusicResponse extends BaseResponse {
        @SerializedName("music")
        @Expose
        private List<Music> musics;

        @SerializedName("next_page")
        @Expose
        private int nextPage;

        public MusicResponse(boolean success, String message, List<Music> musics, int nextPage) {
            super(success, message);
            this.musics = musics;
            this.nextPage = nextPage;
        }

        public List<Music> getMusics() {
            return musics;
        }

        public void setMusics(List<Music> musics) {
            this.musics = musics;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }
    }

    @GET("api/musics")
    Call<MusicResponse> getMusic();

    @POST("api/upload")
    Call<BaseResponse> uploadMusic(@Body RequestBody body);

    @GET("api/musics")
    Single<MusicResponse> getMusic(@Query("page") int page, @Query("page_size") int pageSize);
}
