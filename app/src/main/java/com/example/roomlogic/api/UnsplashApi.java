package com.example.roomlogic.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.example.roomlogic.model.UnsplashResponse;

public interface UnsplashApi {
    @GET("search/photos")
    Call<UnsplashResponse> searchImages(
            @Query("query") String query,
            @Query("client_id") String apiKey,
            @Query("per_page") int perPage
    );
}
