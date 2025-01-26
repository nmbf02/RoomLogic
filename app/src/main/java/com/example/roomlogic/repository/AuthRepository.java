package com.example.roomlogic.repository;

import com.example.roomlogic.model.LoginResponse;
import com.example.roomlogic.model.user;
import com.example.roomlogic.model.LoginRequest;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthRepository {
    private final AuthApi authApi;

    public AuthRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authApi = RetrofitClient.getInstance().create(AuthApi.class);

    }

    public Call<LoginResponse> login(LoginRequest loginRequest) {
        return authApi.login(loginRequest);
    }
}
