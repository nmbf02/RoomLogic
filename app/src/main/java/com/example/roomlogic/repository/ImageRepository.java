package com.example.roomlogic.repository;

import android.net.Uri;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ImgBBApi;
import com.example.roomlogic.api.UnsplashApi;
import com.example.roomlogic.model.ImgBBResponse;
import com.example.roomlogic.model.UnsplashResponse;
import com.example.roomlogic.model.UnsplashResponse.UnsplashImage;
import java.io.File;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageRepository {
    private static final String UNSPLASH_API_KEY = "dHXvQR0XRpDvpFStnrXzMoz1G3ejMh5q2t1PrtMOaWA"; // Tu API Key de Unsplash
    private static final String IMGBB_API_KEY = "651e65dba258a282301201c931ab9411"; // Tu API Key de ImgBB

    private final UnsplashApi unsplashApi;
    private final ImgBBApi imgBBApi;

    public ImageRepository() {
        unsplashApi = ApiClient.getRetrofitInstance().create(UnsplashApi.class);
        imgBBApi = ApiClient.getRetrofitInstance().create(ImgBBApi.class);
    }

    // ✅ Método para obtener una imagen aleatoria desde Unsplash
    public MutableLiveData<String> getRandomImage() {
        MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();

        unsplashApi.searchImages("random", UNSPLASH_API_KEY, 1).enqueue(new Callback<UnsplashResponse>() {
            @Override
            public void onResponse(Call<UnsplashResponse> call, Response<UnsplashResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UnsplashImage> images = response.body().getResults();
                    if (images != null && !images.isEmpty()) {
                        imageUrlLiveData.setValue(images.get(0).getUrls().getRegular()); // ✅ Obtener la URL de la primera imagen
                    } else {
                        imageUrlLiveData.setValue(null);
                    }
                } else {
                    imageUrlLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<UnsplashResponse> call, Throwable t) {
                imageUrlLiveData.setValue(null);
            }
        });

        return imageUrlLiveData;
    }

    // ✅ Método para subir imágenes a ImgBB
    public MutableLiveData<String> uploadImage(Uri imageUri) {
        MutableLiveData<String> uploadResult = new MutableLiveData<>();

        File file = new File(imageUri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        imgBBApi.uploadImage(IMGBB_API_KEY, body).enqueue(new Callback<ImgBBResponse>() {
            @Override
            public void onResponse(Call<ImgBBResponse> call, Response<ImgBBResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    uploadResult.setValue(response.body().getData().getUrl()); // ✅ URL correcta
                } else {
                    uploadResult.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ImgBBResponse> call, Throwable t) {
                uploadResult.setValue(null);
            }
        });


        return uploadResult;
    }
}