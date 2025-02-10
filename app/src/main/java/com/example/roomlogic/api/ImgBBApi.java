package com.example.roomlogic.api;

import com.example.roomlogic.model.ImgBBResponse;
import com.example.roomlogic.util.ApiKeys;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ImgBBApi {
    @Multipart
    @POST("1/upload")
    Call<ImgBBResponse> uploadImage(
            @Query("key") String apiKey,
            @Part MultipartBody.Part image
    );

    // ✅ Método para subir imágenes sin pasar manualmente la API Key
    default Call<ImgBBResponse> uploadImage(MultipartBody.Part image) {  // ✅ Ahora usa ImgBBResponse correctamente
        return uploadImage(ApiKeys.IMGBB_API_KEY, image);
    }
}