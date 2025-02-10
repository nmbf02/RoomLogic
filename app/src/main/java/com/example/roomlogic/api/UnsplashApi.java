package com.example.roomlogic.api;

import com.example.roomlogic.model.UnsplashResponse;
import com.example.roomlogic.util.ApiKeys;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UnsplashApi {
    @GET("search/photos")
    Call<UnsplashResponse> searchImages(
            @Query("query") String query,
            @Query("client_id") String apiKey, // 🔹 Usa la API Key desde ApiKeys.java
            @Query("per_page") int perPage
    );

    // 🔹 Método para buscar imágenes con la API Key ya incluida
    default Call<UnsplashResponse> searchImages(String query, int perPage) {
        return searchImages(query, ApiKeys.UNSPLASH_API_KEY, perPage);
    }
}
