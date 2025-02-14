package com.example.roomlogic.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("execute_task")  // Asegúrate de que el endpoint sea correcto
    Call<Void> sendToken(@Body TokenRequest token);
}
