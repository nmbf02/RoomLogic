package com.example.roomlogic.api;

import com.example.roomlogic.model.Client;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("/clients") // Ajusta el endpoint según tu API
    Call<Void> createClient(@Body Client client);
}
