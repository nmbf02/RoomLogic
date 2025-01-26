package com.example.roomlogic.repository;

import com.example.roomlogic.model.LoginRequest;
import com.example.roomlogic.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
