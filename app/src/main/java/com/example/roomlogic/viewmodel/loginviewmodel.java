package com.example.roomlogic.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.roomlogic.model.LoginRequest;
import com.example.roomlogic.model.LoginResponse;
import com.example.roomlogic.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginviewmodel extends ViewModel {

    private final AuthRepository authRepository;

    private final MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();

    public loginviewmodel() {
        authRepository = new AuthRepository();
    }

    public LiveData<LoginResponse> login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);

        authRepository.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loginResult.setValue(response.body());
                } else {
                    loginResult.setValue(new LoginResponse(false, "Credenciales inválidas", null));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginResult.setValue(new LoginResponse(false, "Error de conexión: " + t.getMessage(), null));
            }
        });

        return loginResult;
    }
}
