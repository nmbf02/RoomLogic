package com.example.roomlogic.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.roomlogic.network.ImageUploader;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadImageViewModel extends AndroidViewModel {

    private final MutableLiveData<String> uploadResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorResult = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(2); // Dos hilos separados

    public UploadImageViewModel(Application application) {
        super(application);
    }

    public void uploadImage(File imageFile, String serverUrl) {
        executor.execute(() -> {
            try {
                String result = ImageUploader.uploadImage(imageFile, serverUrl);
                uploadResult.postValue(result);
            } catch (Exception e) {
                errorResult.postValue("Error al cargar la imagen: " + e.getMessage());
            }
        });
    }

    public LiveData<String> getUploadResult() {
        return uploadResult;
    }

    public LiveData<String> getErrorResult() {
        return errorResult;
    }
}
