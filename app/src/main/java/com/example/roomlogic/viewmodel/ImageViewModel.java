package com.example.roomlogic.viewmodel;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.roomlogic.api.ApiClient;
import com.example.roomlogic.api.ImgBBApi;
import com.example.roomlogic.api.UnsplashApi;
import com.example.roomlogic.model.ImgBBResponse;
import com.example.roomlogic.model.UnsplashResponse;
import com.example.roomlogic.util.ApiKeys;
import java.io.File;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageViewModel extends ViewModel {
    private final UnsplashApi unsplashApi;
    private final ImgBBApi imgBBApi;
    private final MutableLiveData<List<UnsplashResponse.UnsplashImage>> imagesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> uploadedImageUrl = new MutableLiveData<>();

    public ImageViewModel() {
        unsplashApi = ApiClient.getUnsplashClient().create(UnsplashApi.class);
        imgBBApi = ApiClient.getImgBBClient().create(ImgBBApi.class);
    }

    // ✅ Obtener imágenes de Unsplash
    public LiveData<List<UnsplashResponse.UnsplashImage>> getImagesLiveData() {
        return imagesLiveData;
    }

    public void searchImages(String query) {
        unsplashApi.searchImages(query, ApiKeys.UNSPLASH_API_KEY, 10).enqueue(new Callback<UnsplashResponse>() {
            @Override
            public void onResponse(Call<UnsplashResponse> call, Response<UnsplashResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    imagesLiveData.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<UnsplashResponse> call, Throwable t) {
                imagesLiveData.setValue(null);
            }
        });
    }

    // ✅ Subir imágenes a ImgBB
    public LiveData<String> getUploadedImageUrl() {
        return uploadedImageUrl;
    }

    public void uploadImage(Uri imageUri) {
        File file = new File(imageUri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        imgBBApi.uploadImage(ApiKeys.IMGBB_API_KEY, body).enqueue(new Callback<ImgBBResponse>() {
            @Override
            public void onResponse(Call<ImgBBResponse> call, Response<ImgBBResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    uploadedImageUrl.setValue(response.body().getData().getUrl());
                } else {
                    uploadedImageUrl.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ImgBBResponse> call, Throwable t) {
                uploadedImageUrl.setValue(null);
            }
        });
    }
}